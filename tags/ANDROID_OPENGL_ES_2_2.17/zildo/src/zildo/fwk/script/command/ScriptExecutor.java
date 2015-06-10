/**
 * The Land of Alembrum
 * Copyright (C) 2006-2013 Evariste Boussaton
 * 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package zildo.fwk.script.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import zildo.Zildo;
import zildo.client.ClientEngineZildo;
import zildo.client.ClientEvent;
import zildo.client.ClientEventNature;
import zildo.fwk.script.context.IEvaluationContext;
import zildo.fwk.script.context.SpriteEntityContext;
import zildo.fwk.script.xml.element.action.runtime.RuntimeAction;
import zildo.fwk.script.xml.element.action.runtime.RuntimeScene;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.persos.Perso;
import zildo.resource.Constantes;
import zildo.server.EngineZildo;

public class ScriptExecutor {

	// Stack of current scripts
	List<ScriptProcess> scripts = new ArrayList<ScriptProcess>();
	
	Set<Perso> involved=new HashSet<Perso>();	// All characters involved in script
	public boolean userEndedAction;				// TRUE if user has ended last action with ACTION keypressed
	
	List<ScriptProcess> subScriptsEnded = new ArrayList<ScriptProcess>();
	List<ScriptProcess> toTerminate = new ArrayList<ScriptProcess>();
	List<ScriptProcess> toExecute = new ArrayList<ScriptProcess>();
	
	/**
	 * Ask for engine to execute the given script, with/without a NOEVENT signal at the end (bad for map scripts).
	 * @param p_script
	 * @param p_finalEvent
	 * @param p_topPriority TRUE=this script will be executed before all others
	 * @param p_context context (optional)
	 * @param p_caller process calling a new one
	 */
	public void execute(RuntimeScene p_script, boolean p_finalEvent, boolean p_topPriority, IEvaluationContext p_context, ScriptProcess p_caller) {
		// TODO: attempt to duplicate context, need to refactor cleanly
		IEvaluationContext ctx = p_context;
		if (p_context != null) {
			ctx = ctx.clone();
		}
		ScriptProcess sp = new ScriptProcess(p_script, this, p_finalEvent, p_topPriority, ctx, p_caller);
		if (p_caller != null) {
			p_caller.setSubProcess(sp);
		} else {
			if (scripts.size() == 0) {
				scripts.add(sp);
			} else {
				// Scripts are actually processing, so keep it ready to be added during the #render method.
				toExecute.add(sp);
			}
		}
	}
	
	/**
	 * Execute a frame inside the current locking script, and all non locking ones.
	 */
	public void render() {
		if (!scripts.isEmpty()) {
			if (Zildo.infoDebugScript) {
				System.out.println(scripts.size() + " scripts running");
			}
			// 0) Terminate scripts asked by external methods (#stopFromContext for example)
			for (ScriptProcess process : toTerminate) {
				terminate(process);
			}
			toTerminate.clear();
			
			// 1) Render current scripts
			for (ScriptProcess process : scripts) {
				
				// Does this process have a sub process ? Check recursively
				while (process.subProcess != null) {
					if (subScriptsEnded.contains(process.subProcess)) {
						// This sub process has recently ended, so we cut the link from the parent
						process.subProcess = null;
						subScriptsEnded.remove(process.subProcess);
						break;
					}
					process = process.subProcess;
				}
				
				RuntimeAction currentNode=process.getCurrentNode();
				if (currentNode == null) {
					// We reach the end of the script
					toTerminate.add(process);
				} else {
					renderElement(process, currentNode, true);
					
					// Render current actions too
					for (Iterator<RuntimeAction> it=process.currentActions.iterator();it.hasNext();) {
						RuntimeAction action=it.next();
						if (action.done) {	// It's done, so remove the action
							it.remove();
						} else {
							renderElement(process, action, false);
						}
					}
					
					// Did the last action finished ? So we'll avoid GUI blinking with 1-frame long script. (issue 28)
					if (process.getCurrentNode() == null) {
						toTerminate.add(process);
					}
				}
				
				// end condition
				if (process.scene.locked) {
					break;
				}
			}
			
			// 2) Terminate those who are waiting
			for (ScriptProcess process : toTerminate) {
				terminate(process);
			}
			toTerminate.clear();
			
			// 3) Create the awaiting one
			for (ScriptProcess process : toExecute) {
				int i=0;
				for (;i<scripts.size();i++) {
					if (!scripts.get(i).topPriority) {
						break;
					}
				}
				scripts.add(i, process);
			}
			toExecute.clear();

		}
	}

	/**
	 * Script just terminated.
	 * @param process
	 */
	private void terminate(ScriptProcess process) {
		boolean rootScript = scripts.remove(process);
		if (!rootScript) {
			// This script isn't in the global list, that means it's a subscript launched by another one
			subScriptsEnded.add(process);
		}
		process.terminate();
		if (!isScripting()) {
			// Get back to life the involved characters
			for (Perso p : involved) {
				p.setGhost(p.getFollowing() != null);	// Cancel 'ghost' except if character is following someone
				if (p.isZildo()) {
					p.setOpen(true);
					p.setSpeed(Constantes.ZILDO_SPEED);
				}
				p.setUnstoppable(false);	// Reset this status
			}
			involved.clear();
			// Focus on Zildo
			SpriteEntity zildo=EngineZildo.persoManagement.getZildo();
			if (zildo != null) {
				ClientEngineZildo.mapDisplay.setFocusedEntity(zildo);
			}
			// Stop forced music
			EngineZildo.soundManagement.setForceMusic(false);

			if (process.finalEvent) {
				EngineZildo.askEvent(new ClientEvent(ClientEventNature.NOEVENT));
			}
		}
	}
	
	private void renderAction(ScriptProcess p_process, RuntimeAction p_action, boolean p_moveCursor) {
		boolean achieved=false;
		if (p_action.isMultiple()) {
			// Actions list
			achieved=true;
			for (RuntimeAction action : p_action.actions) {
				if (!action.done) {
					renderElement(p_process, action, false);
					achieved=achieved & action.done;
				}
			}
		} else {
			achieved=p_process.actionExec.render(p_action);
		}
		if (p_moveCursor) {
			if (p_action.unblock) {	// Action is unblocking, so go next, but keep it in a list
				p_process.currentActions.add(p_action);
				achieved=true;
			}
			if (achieved) {
				p_process.cursor++;
			}
		}
	}
	
	private void renderVariable(ScriptProcess process, RuntimeAction p_elem) {
		boolean achieved = process.varExec.render(p_elem);
		if (achieved) {
			process.cursor++;
		}
	}
	
	private void renderElement(ScriptProcess process, RuntimeAction currentNode, boolean moveCursor) {
		// TODO: We can differentiate Action and Var in RuntimeAction with 2 fields
		// So as we don't need to check class, and cast
		if (!currentNode.var) {
			renderAction(process, currentNode, moveCursor);
		} else {
			renderVariable(process, currentNode);
		}		
	}
	
	public boolean isScripting() {
		return isScripting(false);
	}
	
	/**
	 * Returns TRUE if any blocking script is going.
	 * @param onlyTopPriority TRUE=focus only on topPriority script (=mapScript)
	 * @return
	 */
	public boolean isScripting(boolean onlyTopPriority) {
		if (scripts.isEmpty()) {
			return false;
		}
		for (ScriptProcess process : scripts) {
			if (onlyTopPriority) {
				// Because topPriority is set to TRUE only for mapScript. These are the scripts triggered automatically on a new map.
				// See (<mapScript><condition>... in XML scripts)
				if (process.topPriority) {
					return true;
				}
			} else {
				// Is this script unblocking ?
				if (process.scene.locked) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns TRUE if given name is in the processing queue (i.e. the quest are unfinished)
	 * @param p_name
	 * @return boolean
	 */
	public boolean isProcessing(String p_name) {
		for (ScriptProcess process : scripts) {
			if (process.isNameProcessing(p_name)) {
				return true;
			}
		}
		return false;
	}
	
	public void clearUnlockingScripts() {
		for (Iterator<ScriptProcess> it = scripts.iterator();it.hasNext();) {
			ScriptProcess process = it.next();
			if (!process.scene.locked) {
				it.remove();
			}
		}
	}
	
	public void userEndAction() {
		userEndedAction=true;
	}
	
	/**
	 * Stop any scripts whose context is linked to this entity.
	 * @param entity
	 */
	public void stopFromContext(SpriteEntity entity) {
		for (ScriptProcess sp : scripts) {
			IEvaluationContext ctx = sp.actionExec.context;
			if (ctx != null && ctx instanceof SpriteEntityContext) {
				Perso perso = (Perso) ctx.getActor();
				if ( perso != null && perso.getId() == entity.getId()) {
					toTerminate.add(sp);
				}
			}
		}
	}
}