/**
 * Legend of Zildo
 * Copyright (C) 2006-2011 Evariste Boussaton
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

package zeditor.tools.sprites;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.LogManager;

import zeditor.tools.banque.Grotte;
import zeditor.tools.tiles.MotifBankEdit;
import zildo.fwk.bank.SpriteBank;
import zildo.monde.Game;
import zildo.monde.map.Zone;
import zildo.monde.sprites.SpriteModel;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.prefs.Constantes;
import zildo.server.EngineZildo;
import zildo.server.MapManagement;
import zildo.server.Server;

/**
 * Test class, doesn't apart to real project.<br/>
 * 
 * We can find here a bunch of methods dealing some connex issues like :<ul>
 * <li>saving a sprite/motif bank</li>
 * <li>fix a bank</li>
 * <li>generate an image</li>
 * <li>modify all maps</li>
 * <li>and so on</li>
 * </ul>
 * @author Tchegito
 *
 */
public class Modifier {
	
	public final static int COLOR_BLUE = 255;
	
     public static void main(String[] args) {
         // Intialize game engine
        Game g=new Game(null, true);
        new EngineZildo(g);
       

        //new Modifier().fixPnj2();
        //new Modifier().saveElements2();
        //new Modifier().saveFontes2();
        //new Modifier().saveBanque();
        //new Modifier().saveGears();
        //new Modifier().saveAllMaps();
        //new Modifier().generateImg();
    }
     
     public void generateImg() {
    	 MotifBankEdit bankEdit=new MotifBankEdit(new Grotte());
    	 bankEdit.charge_motifs(bankEdit.getName()+".dec");
    	 bankEdit.generateImg();
     }
     
     public void saveBanque() {
    	 new Grotte().save();
     }
     
     public void saveElements2() {
         SpriteBankEdit bankElem=new SpriteBankEdit(EngineZildo.spriteManagement.getSpriteBank(SpriteBank.BANK_ELEMENTS));
         bankElem.loadImage("objets", COLOR_BLUE);
         int nSpr=bankElem.getNSprite();
         Zone[] elements=new ElementsPlus().getZones();
         for (Zone z : elements) {
         	bankElem.addSprFromImage(nSpr, z.x1, z.y1, z.x2, z.y2);
         	nSpr++;
         }
         bankElem.setName("elements2.spr");
         bankElem.saveBank();
     }

     public void saveFontes2() {
         SpriteBankEdit bankElem=new SpriteBankEdit(EngineZildo.spriteManagement.getSpriteBank(SpriteBank.BANK_FONTES));
         bankElem.loadImage("fontes", COLOR_BLUE);
         int nSpr=bankElem.getNSprite();
         Zone[] elements=new Fontes().getZones();
         for (Zone z : elements) {
         	bankElem.addSprFromImage(nSpr, z.x1, z.y1, z.x2, z.y2);
         	nSpr++;
         }
         bankElem.setName("font2.spr");
         bankElem.saveBank();
     }
     
     public void saveGears() {
         SpriteBankEdit bankElem=new SpriteBankEdit(EngineZildo.spriteManagement.getSpriteBank(SpriteBank.BANK_ELEMENTS));
         
         bankElem.loadImage("interia2", COLOR_BLUE);
         int nSpr=bankElem.getNSprite();
    	 for (int i=0;i<nSpr;i++) {
    		 bankElem.removeSpr(0);
    	 }
    	 // Add doors
    	 Zone[] elements=new Gears().getZones();
    	 nSpr=0;
         for (Zone z : elements) {
          	bankElem.addSprFromImage(nSpr, z.x1, z.y1, z.x2, z.y2);
          	nSpr++;
          }
    	 bankElem.setName("gear.spr");
    	 bankElem.saveBank();
     }
     
     /** Not useful anymore. It remains here as an example. **/
    public void fixPnj2() {
        EngineZildo.spriteManagement.charge_sprites("PNJ3.SPR");
       
        // Remove spector
        SpriteBankEdit bankIn=new SpriteBankEdit(EngineZildo.spriteManagement.getSpriteBank(SpriteBank.BANK_PNJ));
        SpriteBankEdit bankOut=new SpriteBankEdit(EngineZildo.spriteManagement.getSpriteBank(6));
        int fin=bankOut.getNSprite();
        for (int i=0;i<6;i++) {
            int nSprOriginal=PersoDescription.VOLANT_BLEU.getNSpr() + i;
            SpriteModel model=bankIn.get_sprite(nSprOriginal);
            System.out.println("On copie le sprite no"+nSprOriginal);
            bankOut.addSpr(fin+i, model.getTaille_x(), model.getTaille_y(), bankIn.getSpriteGfx(nSprOriginal));
        }
        bankIn.removeSpr(124);
        bankIn.removeSpr(124);
        bankIn.removeSpr(124);
        bankIn.removeSpr(124);
        bankIn.removeSpr(124);
        bankIn.removeSpr(124);
        bankIn.saveBank();
        bankOut.saveBank();
    }

    public void saveAllMaps() {
    	
		String path=Constantes.DATA_PATH;
		File directory=new File(path);
		
		File[] maps = directory.listFiles(new FilenameFilter() {
    		public boolean accept(File dir, String name) {
    			return name.toLowerCase().endsWith(".map");
    		}
		});
		LogManager.getLogManager().reset();
		
        Game game = new Game(null, true);
        Server server = new Server(game, true);
		for (File f : maps) {
			String name=f.getName();
			System.out.println("Processing "+name+"...");
			EngineZildo.mapManagement.charge_map(name);
		        
	        // Save the map into a temporary file
			MapManagement mapManagement=EngineZildo.mapManagement;
			mapManagement.saveMapFile(name);
		}
    }
}