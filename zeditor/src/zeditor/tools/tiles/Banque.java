package zeditor.tools.tiles;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


//Regexps � appliquer:
///////////////////////
//1) commentaires
//find:    \{(.*)\}
//replace: /*$1*/
//2) les points
//find:     \(([0-9]*),([0-9]*)\)
//replace: new Point($1, $2)

public abstract class Banque {

	// Donn�es d'entr�e
	protected Point[] coords;
	
	protected List<GraphChange> pkmChanges;
	// Donn�es construites par {@link GenereCorrespondanceDec#doTheJob()}
	Map<Point, Integer> motifParPoint;
	Map<Integer, Point> pointParMotif;
	
	public static final String PKM_PATH = "F:\\Code\\Projets Pascal\\Zildo\\Developpement\\Graph\\";
	
	// Ensemble des points correspondant � la position haute-gauche de chaque
	// tile
	public Point[] getCoords() {
		if (coords == null) {
			int[][] coordsInt = getCoordsInt();
			coords = new Point[coordsInt.length];
			int p=0;
			for (int[] crds : coordsInt) {
				coords[p++] = new Point(crds[0], crds[1]);
			}
		}
		return coords;
	}

	// List des num�ros de tile o� on change de PKM
	// Dans point on a: x=num�ro de tile / y=offset Y pour la page suivante
	public List<GraphChange> getPkmChanges() {
		return pkmChanges;
	}

	public void setMapCorrespondance(Map<Point, Integer> map, Map<Integer, Point> map2) {
		motifParPoint = map;
		pointParMotif = map2;
	}

	/**
	 * Renvoie le num�ro de la tile � la position donn�e. Renvoie -1 si il n'y a
	 * pas de tile � cet endroit.
	 * 
	 * @param x
	 * @param y
	 * @return int
	 */
	public int getNumTile(int x, int y) {
		Point p = new Point(x, y);
		Integer i = motifParPoint.get(p);
		return i == null ? -1 : i.intValue();
	}
	
	public Point getCoordsTile(int i) {
		return pointParMotif.get(i);
	}
	
	/**
	 * Save this bank in DEC format.
	 */
    public void save() {
    	TileBankEdit bankEdit=new TileBankEdit(this);
    	
    	System.out.println("Processing "+bankEdit.getName()+"...");
    	Iterator<GraphChange> it=getPkmChanges().iterator();
    	GraphChange current=it.next();
    	int nTile=0;
    	for (Point p : getCoords()) {
    		if (current != null && current.nTile == nTile) {
    			System.out.println("Loading "+current.imageName);
    			bankEdit.loadImage(current.imageName, 255);	// 0 as transparency color
    			if (it.hasNext()) {
    				current=it.next();
    			} else {
    				current=null;
    			}
    		}
    		
    		bankEdit.addSprFromImage(nTile++, p.x, p.y);

    	}
    	
    	bankEdit.saveBank();
    	
    	System.out.println("Ok");
    }
    
    protected int[][] getCoordsInt() {
    	return new int[][] {};
    }
}
