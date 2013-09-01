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

package junit.area;

import junit.framework.Assert;

import org.junit.Test;

import zeditor.tools.builder.AllMapProcessor;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.Perso.PersoInfo;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class CheckConsistentMap {

	@Test
	public void testEnemies() {
		new AllMapProcessor() {
			
			@Override
			public boolean run() {
				for (Perso p : EngineZildo.persoManagement.tab_perso) {
					boolean realEnemy = p.getInfo() == PersoInfo.ENEMY;
					boolean shouldBeEnemy = false;
					
					switch (p.getDesc()) {
					case ABEILLE:
						continue;	// Both are tolerated
					case BAS_GARDEVERT:
					case CHAUVESOURIS:
					case CORBEAU:
					case CRABE:
					case CREATURE:
					case ECTOPLASME:
					case GREEN_BLOB:
					case FIRETHING:
					case GARDE_CANARD:
					case RABBIT:
					case RAT:
					case SPECTRE:
					case SQUELETTE:
					case VAUTOUR:
					case VOLANT_BLEU:
						shouldBeEnemy = true;
					}
					Assert.assertTrue("Aaaaie ! "+p.getName()+" ("+p.getDesc()+") at ("+(int) p.getX()+","+(int) p.getY()+") should be enemy !", realEnemy == shouldBeEnemy);
				}
				return false;
			}
		}.modifyAllMaps();
	}
}
