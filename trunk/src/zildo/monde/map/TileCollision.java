/**
 *
 */
package zildo.monde.map;

import zildo.fwk.IntSet;
import zildo.fwk.gfx.engine.TileEngine;
import zildo.monde.map.Angle;

/**
 * @author tchegito
 */
public class TileCollision {

    private final TileInfo[] tileInfos = new TileInfo[TileEngine.tileBankNames.length * 256];

    final IntSet walkable = new IntSet(1, 6, 19, 23, 27, 35, 40, 41, 42, 43, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
            64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 89, 90, 91, 99, 139, 140, 141, 142, 143, 144, 145, 146, 166, 168, 170, 171,
            172, 174, 175, 176, 177, 178, 183);

    final IntSet walkable2 = new IntSet(22, 23, 25, 34, 35, 36, 37, 58, 59, 61, 67, 68, 71, 72,
            73, // +256
            77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 94, 107, 108, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 126,
            127, 128, 130, 139, 140, 141, 142, 143, 144, 145, 146, 147, 168, 169, 173, 174, 175, 176, 177, 178);

    final IntSet walkable3 = new IntSet(0, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 94, 95, 96, // +512
            97, 98, 99, 100, 101, 102, 217, 218, 219, 220, 221, 222, 240);

    final IntSet walkable4 = new IntSet(9, 37, 38, 39, 41, 42, 43, 44, 45, 46, 50, 51, 52, 53, 78, 79, 80, 81, // +768
            82, 83, 84, 155, 156, 157, 158, 159, 160);

    final IntSet walkable5 = new IntSet(45, 81, 82, 135, 137, 147, 173, 178, 210, 212, 213, 227, 228, 229, // +1024
            230, 231, 239, 240, 241);

    public TileCollision() {
        buildTileInfos();
    }

    public boolean collide(int p_posX, int p_posY, int p_nTile) {
        return tileInfos[p_nTile].collide(p_posX, p_posY);
    }

    public boolean collide(int p_nTile) {
    	return !tileInfos[p_nTile].walkable;
    }
    
    private void buildTileInfos() {
        // Build the entire tile infos
        for (int i = 0; i < tileInfos.length; i++) {
            TileInfo tileInfo = new TileInfo();
            // Les portes sont toujours sauv�es selon le m�me sch�ma, donc un simple
            // d�calage dans le num�ro de motif suffit pour toutes les traiter
            int onmap=i;
            if (i >= 590 && i <= 605)
                onmap -= 16;

            switch (onmap) {
                // Mi diagonal mi horizontal : collines bord du haut
                case 2:
                    tileInfo.cornerDiagonal = true;
                    tileInfo.blockAngle = Angle.SUDEST;
                    break;
                case 4:
                    tileInfo.cornerDiagonal = true;
                    tileInfo.blockAngle = Angle.SUDOUEST;
                    break;
                case 47:
                    tileInfo.cornerDiagonal = true;
                    tileInfo.blockAngle = Angle.NORDOUEST;
                    break;
                case 48:
                    tileInfo.cornerDiagonal = true;
                    tileInfo.inverse=true;
                    tileInfo.blockAngle = Angle.NORDEST;    // TODO: check this
                    break;
                // Les bords en diagonal (ex:collines)
                case 5:
                case 8:
                case 35:
                case 79:
                case 106:
                case 723:
                case 787:
                case 917:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.SUDOUEST;
                    break;
                case 7:
                case 0:
                case 27:
                case 77:
                case 104:
                case 721:
                case 788:
                case 915:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.SUDEST;
                    break;
                case 13:
                case 23:
                case 84:
                case 102:
                case 156:
                case 719:
                case 786:
                case 913:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.NORDEST;
                    break;
                case 11:
                case 19:
                case 85:
                case 100:
                case 157:
                case 717:
                case 785:
                case 911:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.NORDOUEST;
                    break;

                // Les parties verticales
                case 101:
                case 323:
                case 324:
                case 327:
                case 328:
                case 363:
                case 364:
                case 385:
                case 405:
                case 408:
                case 578:
                case 579:
                case 586:
                case 587:
                case 663:
                case 664:
                case 665:
                case 670:
                case 671:
                case 676:
                case 677:
                case 865:
                case 870:
                case 912:
                case 1037:
                case 1102:
                case 1103:
                case 1107:
                case 1108:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.NORD;
                    break;

                case 3:
                case 105:
                case 329:
                case 580:
                case 581:
                case 588:
                case 589:
                case 867:
                case 872:
                case 916:
                case 1162:
                case 1163:
                case 1164:
                case 1175:
                case 1192:
                case 1201:
                case 1203:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.SUD;
                    break;

                // Les parties horizontales
                case 39:
                case 107:
                case 179:
                case 185:
                case 194:
                case 278:
                case 314:
                case 424:
                case 574:
                case 576:
                case 582:
                case 584:
                case 749: /* case 751: */
                case 753:
                case 755:
                case 667:
                case 669:
                case 857:
                case 859:
                case 861:
                case 863:
                case 873:
                case 879:
                case 918:
                case 1027:
                case 1029:
                case 1031:
                case 1063:
                case 1097:
                case 1213:
                case 1275:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.OUEST;
                    break;

                case 46:
                case 103:
                case 195:
                case 279:
                case 315:
                case 425:
                case 575:
                case 577:
                case 583:
                case 585:
                case 750: /* case 752: */
                case 754:
                case 756:
                case 858:
                case 860:
                case 862:
                case 864:
                case 874:
                case 880:
                case 914:
                case 1064:
                case 1098:
                case 1214:
                case 1276:
                    tileInfo.half=true;
                    tileInfo.blockAngle=Angle.EST;
                    break;

                // Coins

                case 155:
                case 946:
                case 954:
                case 958:
                case 920:
                    tileInfo.corner=true;
                    tileInfo.blockAngle=Angle.NORDEST;
                    break;
                case 947:
                    tileInfo.corner=true;
                    tileInfo.blockAngle=Angle.SUDOUEST;
                    break;
                case 948:
                    tileInfo.corner=true;
                    tileInfo.blockAngle=Angle.SUDEST;
                    break;
                case 158:
                case 945:
                case 953:
                case 957:
                case 919:
                    tileInfo.corner=true;
                    tileInfo.blockAngle=Angle.NORDOUEST;
                    break;
                case 921:
                case 1171:
                	tileInfo.corner=true;
                	tileInfo.inverse=true;
                	tileInfo.blockAngle=Angle.SUDOUEST;
                    break;
                case 922:
                case 1172:
                	tileInfo.corner=true;
                	tileInfo.inverse=true;
                	tileInfo.blockAngle=Angle.SUDEST;
                    break;

                default:
                	tileInfo.walkable=isWalkable(onmap);

            }

            tileInfos[i] = tileInfo;

        }
    }
    
	///////////////////////////////////////////////////////////////////////////////////////
	// isWalkable
	///////////////////////////////////////////////////////////////////////////////////////
	// IN:motif de map (motif+256*banque)
	// OUT:True si c'est un motif franchissable par les persos
	//     False si c'est un obstacle
	///////////////////////////////////////////////////////////////////////////////////////
	private boolean isWalkable(int on_map)
	{
		if (on_map<256)
			return walkable.contains(on_map);
		else if (on_map<512)
			return walkable2.contains(on_map-256);
		else if (on_map<768)
			return walkable3.contains(on_map-512);
		else if (on_map<1024)
			return walkable4.contains(on_map-768);
		else
			return walkable5.contains(on_map-1024);
	}
}