/**
 * Legend of Zildo
 * Copyright (C) 2006-2012 Evariste Boussaton
 * Based on original Zelda : link to the past (C) Nintendo 1992
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

package zildo.platform.engine;

import org.lwjgl.opengl.GL11;

import zildo.client.ClientEngineZildo;
import zildo.fwk.gfx.engine.TextureEngine;
import zildo.fwk.gfx.engine.TileEngine;
import zildo.fwk.gfx.primitive.TileGroupPrimitive.ActionNthRunner;
import zildo.monde.util.Point;
import zildo.monde.util.Vector3f;

public class LwjglTileEngine extends TileEngine {

	public LwjglTileEngine(TextureEngine texEngine) {
		super(texEngine);
	}
	
	private TextureBinder texBinder = new TextureBinder();
	
	@Override
	public void render(boolean backGround) {

		if (initialized) {
			Vector3f ambient = ClientEngineZildo.ortho.getAmbientColor();
			if (ambient != null) {
				GL11.glColor3f(ambient.x, ambient.y, ambient.z);
			}
			
			Point p = ClientEngineZildo.mapDisplay.getCamera();
			GL11.glPushMatrix();
			GL11.glTranslatef(-p.x, -p.y, 0f);

			if (backGround) {
				// Display BACKGROUND
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glEnable(GL11.GL_BLEND);
				meshBACK.render(texBinder);
				meshBACK2.render(texBinder);
				GL11.glDisable(GL11.GL_BLEND);
			}
			else {
				// Display FOREGROUND
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glEnable(GL11.GL_BLEND);

				meshFORE.render(texBinder);
				
				GL11.glDisable(GL11.GL_BLEND);
			}

			// GL11.glColor3f(1f, 1f, 1f);
			GL11.glPopMatrix();
		}
	}
	
	private class TextureBinder implements ActionNthRunner {
		public void execute(final int i) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureEngine.getNthTexture(i));
		}
	}
}