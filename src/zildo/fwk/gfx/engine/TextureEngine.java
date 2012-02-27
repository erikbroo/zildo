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

package zildo.fwk.gfx.engine;

import java.nio.ByteBuffer;

import zildo.fwk.gfx.GFXBasics;
import zildo.resource.Constantes;

/**
 * Abstract class which provides management of a texture set.<p/>
 * 
 * The first phase is the texture creation. It must be done like this:<ul>
 * <li>call prepareSurfaceForTexture()</li>
 * <li>draw things on the GFXBasics returned</li>
 * <li>call generateTexture() for adding texture to the texture set</li>
 * </ul>
 * To tell the openGL engine that it must draw with the right texture, just call:<br/>
 *  <code>GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureTab[i]);</code><br/>
 * where 'i' is the number of the generated texture
 *                   
 * @author tchegito
 *
 */
public abstract class TextureEngine {

    protected int n_Texture;

    protected int[] textureTab;
    protected boolean textureFormat;	// Current texture's format (TRUE=RGBA / FALSE=RGB)
    protected ByteBuffer scratch;

    
    public TextureEngine() {
    	
		// Initialize number of textures
		n_Texture=0;
	
		textureTab=new int[Constantes.NB_MOTIFBANK + Constantes.NB_SPRITEBANK];
    }
    
    @Override
	public void finalize() {
		// Free the allocated textures
		for (int i=0;i<n_Texture;i++) {
			//SafeRelease(ppTexture[i]);
		}
    }
    
    public GFXBasics prepareSurfaceForTexture(boolean p_alpha) {
        // Create image
    	if (p_alpha) {
    		scratch = ByteBuffer.allocateDirect(256 * 256 * 4);
    	} else {
    		scratch = ByteBuffer.allocateDirect(256 * 256 * 3);
    	}
		GFXBasics surface=new GFXBasics(true);
		surface.SetBackBuffer(scratch, 256, 256);
    	
		return surface;
    }
    
    /**
     * The real call to OpenGL methods
     * @return int texture ID
     */
    public abstract int doGenerateTexture();

    public void generateTexture() { 
    	
    	int idTexture = doGenerateTexture();
    	
        // Reset bytebuffer scratch
        scratch.clear();
        
        // Store texture id
        textureTab[n_Texture]=idTexture;

        // Ready for next one
        n_Texture++;
    }
    
    public abstract void getTextureImage(int p_texId);
/*    
    public void saveScreen(int p_texId) {

		// Draw texture with depth
    	GLUtils.copyScreenToTexture(p_texId, 1024, 512);
    }
    */
}
