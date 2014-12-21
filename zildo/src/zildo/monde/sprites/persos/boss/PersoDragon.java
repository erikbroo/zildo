/**
 * The Land of Alembrum
 * Copyright (C) 2006-2013 Evariste Boussaton
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

package zildo.monde.sprites.persos.boss;

import zildo.client.sound.BankSound;
import zildo.monde.Bezier3;
import zildo.monde.Trigo;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.desc.ElementDescription;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.monde.sprites.desc.SpriteAnimation;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.elements.ElementProjectile;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoNJ;
import zildo.monde.sprites.utils.CompositeElement;
import zildo.monde.util.Point;
import zildo.monde.util.Pointf;
import zildo.monde.util.Vector2f;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class PersoDragon extends PersoNJ {

	CompositeElement neck;
	
	double gamma;
	
	int cnt = 0;
	
	int[] seq = {3, 3, 2, 2, 1, 0, 4, 4};
	
	Element redSphere;
	
	public PersoDragon(int x, int y) {
		//this.x = x;
		//this.y = y;
		pv = 5;
		setInfo(PersoInfo.ENEMY);
		
		desc = PersoDescription.DRAGON;
		neck = new CompositeElement(this);
		neck.lineShape(seq.length);
	}
	
	@Override
	public void animate(int compteur_animation) {
		super.animate(compteur_animation);
		
		int nth=0;
		double beta = gamma;
		double iota = 0;
		float xx=x, yy=y, zz=z; 
		
		
		Pointf neckPoint = new Pointf((float) (x + 10*Math.cos(gamma/5)), 
										(float) ( 60 + 4*Math.sin(gamma*2)) );
		Pointf headPoint = new Pointf((float) (neckPoint.x + 10*Math.sin(gamma)),
									(float) (neckPoint.y + 5 + 5*Math.cos(gamma)) );
									
		Bezier3 bz = new Bezier3(new Pointf(x, 0), neckPoint, headPoint);
		
		for (int i=0;i<neck.elems.size()-1;i++) {
			Element e = neck.elems.get(i+1);
			if (i >= 6) {	// Wings
				e.x = neck.elems.get(3).x - 40;
				e.y = neck.elems.get(3).y+100;// + 100;
				e.z = neck.elems.get(3).z + 60;// + 100; // - 40;
				if (i == 7) {
					e.x = e.x +80;
					e.reverse = Reverse.HORIZONTAL;
				}
			} else {
				Pointf interpolated = bz.interpol(i / 5f);
				e.x = interpolated.x;
				e.z = interpolated.y;
				//e.x = xx + (float) (3 * Math.cos(beta * 0.7) + 12 * Math.sin(iota));
				//e.z = zz + 20 - (float) (2 * Math.sin(beta) + 2 * Math.cos(iota));
				if (i == 5) {	// Head
					/*
					e.z -= 30;
					e.x -= 10;
					*/
					//e.setForeground(true);
					e.reverse = neckPoint.x < headPoint.x ? Reverse.HORIZONTAL : Reverse.NOTHING;
				}
				e.y = yy;
				//e.z = zz + 6;
			}
			e.setAddSpr(seq[i]);
			xx = e.x;
			yy = e.y;
			zz = e.z;
			beta += 0.01;
			iota += 0.001;
			nth++;
		}
		neck.elems.get(1).visible = false;
		//refElement.setAddSpr(1);
		visible = false;
		
		if (cnt++ % 150 == 0) {
			Element h = neck.elems.get(6);
			Element elem = EngineZildo.spriteManagement.spawnSpriteGeneric(SpriteAnimation.SEWER_SMOKE,
					(int) h.x, (int) h.y,
					2, 2, this, null);
			elem.z = h.z + 10;
			//elem.floor = 2;
			elem.setForeground(true);
			
			Perso zildo = EngineZildo.persoManagement.lookFor(this, 10, PersoInfo.ZILDO);

			
			// Target hero
			if (zildo != null) {
				double zDirection = Trigo.getAngleRadian(headPoint.x, headPoint.y-2, zildo.x, zildo.y);
				Vector2f speedVect = Trigo.vect(zDirection, 1.8f);
				redSphere = new ElementProjectile(ElementDescription.BROWNSPHERE1, 
						headPoint.x, headPoint.y-2+30, z,
						speedVect.x, speedVect.y, this);
				EngineZildo.spriteManagement.spawnSprite(redSphere);
				EngineZildo.soundManagement.broadcastSound(BankSound.SerpentSpit, new Point(x, y));
				
				redSphere.setForeground(true);
				redSphere.addFire();
				//count = 200;
			} else {
				//count = 50;
			}
		}
		//visible = true;
		gamma += 0.08;

		
	}
}
