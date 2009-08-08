package zildo.monde.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import zildo.fwk.bank.SpriteBank;
import zildo.fwk.file.EasyBuffering;
import zildo.monde.decors.Element;
import zildo.monde.decors.SpriteEntity;
import zildo.monde.dialog.Behavior;
import zildo.monde.dialog.DialogManagement;
import zildo.monde.persos.Perso;
import zildo.monde.persos.PersoGarde;
import zildo.monde.persos.PersoGardeVert;
import zildo.monde.persos.PersoNJ;
import zildo.monde.persos.PersoVolant;
import zildo.monde.persos.utils.MouvementPerso;
import zildo.monde.persos.utils.MouvementZildo;
import zildo.monde.persos.utils.PersoDescription;
import zildo.server.EngineZildo;
import zildo.server.SpriteManagement;


public class Area {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static public int M_MOTIF_MASQUE=128;
	static private int M_MOTIF_ANIME=64;

	static private int SCROLL_LEFT=0;           // Pour les changements de map
	static private int SCROLL_RIGHT=1;
	static private int SCROLL_UP=2;
	static private int SCROLL_DOWN=3;

	static private int MOTIFS_EXTERIEUR=1;
	static private int MOTIFS_INTERIEUR=0;	

	// For roundAndRange
	static public int ROUND_X=0;
	static public int ROUND_Y=0;
	
	private int dim_x,dim_y;
	private String name;
	private Map<Integer,Case> mapdata;
	private int n_persos,n_sprites,n_pe;
	private List<ChainingPoint> listPointsEnchainement;

	private Collection<Point> changes;
	
	public Area()
	{
		mapdata=new HashMap<Integer, Case>();
		listPointsEnchainement=new ArrayList<ChainingPoint>();
		
		changes=new HashSet<Point>();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// get_Areacase
	///////////////////////////////////////////////////////////////////////////////////////
	// IN : coordinates
	// OUT: Case object at the given coordinates
	///////////////////////////////////////////////////////////////////////////////////////
	public Case get_mapcase(int x, int y)
	{
		return mapdata.get(new Integer(y*this.dim_x + x));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// set_Areacase
	///////////////////////////////////////////////////////////////////////////////////////
	// IN:coordinates, Case object
	///////////////////////////////////////////////////////////////////////////////////////
	public void set_mapcase(int x,int y,Case c)
	{
		mapdata.put(new Integer(y*this.dim_x + x),c);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// get_animatedAreacase
	///////////////////////////////////////////////////////////////////////////////////////
	// IN:coordinates, frame index
	///////////////////////////////////////////////////////////////////////////////////////
	Case get_animatedAreacase(int x, int y,int compteur_animation)
	{
		Case temp=this.get_mapcase(x,y);
		temp.setN_motif(temp.getAnimatedMotif(compteur_animation));
		return temp;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// readArea
	///////////////////////////////////////////////////////////////////////////////////////
	// IN : coordinates on Area
	// OUT: return motif + bank*256
	///////////////////////////////////////////////////////////////////////////////////////
	// Return n_motif + n_banque*256 from a given position on the Area
	public int readmap(int x,int y)
	{
		Case temp=this.get_mapcase(x,(int) (y+4));
		int a=temp.getN_banque() & 31;
		int b=temp.getN_motif();
		/*
		if (a==2 && b==0)
		{
			a=temp.n_banque_masque & 31;
			b=temp.n_motif_masque;
		}
		*/
		a=a << 8;
		return a + b;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// writeArea
	///////////////////////////////////////////////////////////////////////////////////////
	// IN:x,y (coordinates on Area), quoi =motif + bank*256
	///////////////////////////////////////////////////////////////////////////////////////
	public void writemap(int x,int y,int quoi)
	{
	 Case temp=this.get_mapcase(x,y+4);
	 temp.setN_motif(quoi & 255);
	 temp.setN_banque(quoi >> 8);
	 this.set_mapcase(x,y+4,temp);
	 
	 changes.add(new Point(x,y+4));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// roundAndRange
	///////////////////////////////////////////////////////////////////////////////////////
	// IN:float to round and range, indicator on which coordinate to compute
	//    ROUND_X(default)  -. x , ROUND_Y -. y
	///////////////////////////////////////////////////////////////////////////////////////
	// Trunc a float, and get it into the Area, with limits considerations.
	///////////////////////////////////////////////////////////////////////////////////////
	public int roundAndRange(float x, int whatToRound)
	{
		int result=(int)x;
		if (x<0)
			x=0;
		int max=dim_x;
		if (whatToRound==ROUND_Y)
			max=dim_y;
		if (x > (max*16 - 16) )
			x=max*16 - 16;
	
		return result;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// isAlongBorder
	///////////////////////////////////////////////////////////////////////////////////////
	public boolean isAlongBorder(int x, int y)
	{
		return (x<4 || x>dim_x*16-8 ||
				y<4 || y>dim_y*16-4);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	// isChangingArea
	///////////////////////////////////////////////////////////////////////////////////////
	// IN : x,y (pixel coordinates for perso location)
	///////////////////////////////////////////////////////////////////////////////////////
	// Return ChainingPoint if Zildo's crossing one (door, or Area's border)
	///////////////////////////////////////////////////////////////////////////////////////
	public ChainingPoint isChangingMap(float x, float y) {
		// On parcourt les points d'enchainements
		int ax=(int) (x / 16);
		int ay=(int) (y / 16);
		boolean border;
		if (this.n_pe!=0) {
			for (ChainingPoint chPoint : listPointsEnchainement) {
				// Area's borders
				border=isAlongBorder((int) x,(int) y);
				if (chPoint.isCollide(ax,ay,border)) {
					addChainingContextInfos(chPoint);
					return chPoint;
				}
			}
		}
		return null;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// addContextInfos
	///////////////////////////////////////////////////////////////////////////////////////
	// Fill the given ChainingPoint with two extra infos: 'orderX' and 'orderY'
	///////////////////////////////////////////////////////////////////////////////////////
	void addChainingContextInfos(ChainingPoint chPoint) {
		int orderX=0;
		int orderY=0;
		// We're gonna get a sort number in each coordinate for all chaining point
		// referring to the same Area.
		for (ChainingPoint chP : listPointsEnchainement) {
			if (chP.getMapname().equals(chPoint.getMapname())) {
				if (chP.getPx() <= chPoint.getPx()) {
					orderX++;
				}
				if (chP.getPy() <= chPoint.getPy()) {
					orderY++;
				}
			}
		}
		chPoint.setOrderX(orderX);
		chPoint.setOrderY(orderY);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// getTarget
	///////////////////////////////////////////////////////////////////////////////////////
	// IN : comingArea -. Area's name
	///////////////////////////////////////////////////////////////////////////////////////
	public ChainingPoint getTarget(String comingArea, int orderX, int orderY) {
		if (this.n_pe!=0) {
			for (ChainingPoint chPoint : listPointsEnchainement) {
				if (chPoint.getMapname().equals(comingArea)) {
					if (orderX == 0 && orderY == 0) {
						return chPoint;
					} else {
						// Get the right one, because there is several connections between
						// the two Areas.
						addChainingContextInfos(chPoint);
						if (chPoint.getOrderX() == orderX &&
							chPoint.getOrderY() == orderY) {
							return chPoint;
						}
					}
				}
			}
		}
		return null;
	}
	
		/*
	
				// On place Zildo sur son bon angle si c'est pas le cas}
				int angle
	
	       {On change de Area}
	       temp:=name;              {On sauve l'ancien nom}
	       fade(FALSE);
	       charger_aventure_Area(Area1,tab_pe[i].Areaname);
	       {On cherche le point de r�apparition de Zildo}
	       if n_pe<>0 then                   {Ce nombre ne PEUT pas �tre nul}
	        for j:=0 to n_pe-1 do
	         if tab_pe[j].Areaname=temp then begin
	          x:=(tab_pe[j].px and 127)*16+16;
	          y:=(tab_pe[j].py and 127)*16+8;
	          if (tab_pe[j].px and 128) <> 0 then begin
	           x:=x-8;y:=y+8;
	          end;
	          coming_Area:=1;
	          {On met Zildo un peu en avant}
	          case angle of
	           0:y:=y-16;
	           1:x:=x+16;
	           2:y:=y+16;
	           3:x:=x-16;
	          end;
	          camerax:=round(x)-16*10;
	          cameray:=round(y)-16*6;
	          if camerax>(16*dim_x-16*20) then camerax:=16*dim_x-16*20;
	          if cameray>(16*dim_y-16*13+8)  then cameray:=16*dim_y-16*13+8;
	          if camerax<0 then camerax:=0;
	          if cameray<0 then cameray:=0;
	          exit;
	         end;
	}
	*/
	
	///////////////////////////////////////////////////////////////////////////////////////
	// attackTile
	///////////////////////////////////////////////////////////////////////////////////////
	public void attackTile(Point tileLocation) {
		// On teste si Zildo d�truit un buisson
		int on_Area=this.readmap(tileLocation.getX(),tileLocation.getY());
		if (on_Area==165) {
			Point spriteLocation=new Point(tileLocation.getX()*16+8, tileLocation.getY()*16+8);
			EngineZildo.spriteManagement.spawnSpriteGeneric(Element.SPR_BUISSON,
															spriteLocation.getX(),
															spriteLocation.getY(),0, null);
			EngineZildo.broadcastSound("CasseBuisson", spriteLocation);
	
			this.writemap(tileLocation.getX(),tileLocation.getY(),166);
		}
	
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// translatePoints
	///////////////////////////////////////////////////////////////////////////////////////
	// Shift every Area's point by this vector (shiftX, shiftY) to another Area
	///////////////////////////////////////////////////////////////////////////////////////
	public void translatePoints(int shiftX, int shiftY, Area targetArea) {
		Case tempCase;
		for (int i=0;i<dim_y;i++) {
			for (int j=0;j<dim_x;j++) {
				tempCase=get_mapcase(j,i);
				targetArea.set_mapcase(j+shiftX,i+shiftY,tempCase);
			}
		}
	}

	public void addPointEnchainement(ChainingPoint ch) {
		listPointsEnchainement.add(ch);
	}
	
	public int getDim_x() {
		return dim_x;
	}

	public void setDim_x(int dim_x) {
		this.dim_x = dim_x;
	}

	public int getDim_y() {
		return dim_y;
	}

	public void setDim_y(int dim_y) {
		this.dim_y = dim_y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getN_persos() {
		return n_persos;
	}

	public void setN_persos(int n_persos) {
		this.n_persos = n_persos;
	}

	public int getN_sprites() {
		return n_sprites;
	}

	public void setN_sprites(int n_sprites) {
		this.n_sprites = n_sprites;
	}

	public int getN_pe() {
		return n_pe;
	}

	public void setN_pe(int n_pe) {
		this.n_pe = n_pe;
	}

	public List<ChainingPoint> getListPointsEnchainement() {
		return listPointsEnchainement;
	}

	public void setListPointsEnchainement(List<ChainingPoint> listPointsEnchainement) {
		this.listPointsEnchainement = listPointsEnchainement;
	}
	
	public boolean isModified() {
		return !changes.isEmpty();
	}
	
	public Collection<Point> getChanges() {
		return changes;
	}
	
	public void resetChanges() {
		changes.clear();
	}
	
	/**
	 * Serialize the map into an EasyWritingFile object.
	 * @return EasyWritingFile
	 */
    public EasyBuffering serialize() {

    	EasyBuffering file=new EasyBuffering();
    	
        // 1) Header
    	file.put((byte) this.getDim_x());
    	file.put((byte) this.getDim_y());
    	file.put((byte) this.getN_persos());
    	file.put((byte) this.getN_sprites());
    	file.put((byte) this.getN_pe());

        // 2) Save the map cases
        for (int i = 0; i < this.getDim_y(); i++) {
            for (int j = 0; j < this.getDim_x(); j++) {
                Case temp = this.get_mapcase(j, i + 4);

                file.put((byte) temp.getN_motif());
                file.put((byte) temp.getN_banque());
                file.put((byte) temp.getN_motif_masque());
                file.put((byte) temp.getN_banque_masque());
            }
        }

        // 3) Chaining points
        if (this.getN_pe() != 0) {
            for (ChainingPoint ch : this.getListPointsEnchainement()) {
            	file.put((byte) ch.getPx());
            	file.put((byte) ch.getPy());
            	file.put(ch.getMapname(),9);
            }
        }

        // 4) Sprites
		if (this.getN_sprites()!=0) {
			List<SpriteEntity> spriteEntities=EngineZildo.spriteManagement.getSpriteEntities();
			int nSprites=0;
			for (SpriteEntity entity : spriteEntities) {
				int type=entity.getEntityType();
				boolean ok=true;
				if (entity.getEntityType() == SpriteEntity.ENTITYTYPE_ELEMENT) {
					Element elem=(Element) entity;
					if (elem.getLinkedPerso() != null) {
						ok=false;
					}
				}
				if (entity.isVisible() && ok && (type == SpriteEntity.ENTITYTYPE_ELEMENT || type == SpriteEntity.ENTITYTYPE_ENTITY)) {
					// Only element
					file.put((int) entity.x);
					file.put((int) entity.y);
					file.put((byte) entity.getNSpr());
					nSprites++;
					if (nSprites > this.getN_sprites()) {
						System.out.println("Too much sprites ! ("+nSprites+")");
					}
				}

			}
		}
        
		// 5) Persos (characters)
		if (this.getN_persos()!=0) {
			List<Perso> persos=EngineZildo.persoManagement.tab_perso;
			for (Perso perso : persos) {
				if (!perso.isZildo()) {
					file.put((int) perso.x);
					file.put((int) perso.y);
					file.put((int) perso.z);
					file.put((byte) perso.getQuel_spr().first());
					file.put((byte) perso.getInfo());
					file.put((byte) perso.getEn_bras());
					file.put((byte) perso.getQuel_deplacement().ordinal());
					file.put((byte) perso.getAngle().ordinal());
					file.put(perso.getNom(),9);
				}
			}
		}

		// 6) Sentences
		DialogManagement dialogManagement=EngineZildo.dialogManagement;
		int nPhrases=dialogManagement.getN_phrases();
		file.put((byte) nPhrases);
		if (nPhrases > 0) {
			// On lit les phrases
			String[] dialogs=dialogManagement.getDialogs();
			for (int i=0;i<nPhrases;i++) {
				file.put(dialogs[i]);
			}
			// On lit le nom
			Map<String, Behavior> behaviors=dialogManagement.getBehaviors();
			for (Entry<String, Behavior> entry : behaviors.entrySet()) {
				file.put(entry.getKey(), 9);
				Behavior behav=entry.getValue();
				for (int i : behav.replique) {
					file.put((byte) i);
				}
			}
		}
        return file;
    }
	
	/**
	 * 
	 * @param p_buffer
	 * @return Area
	 */
	public static Area deserialize(EasyBuffering p_buffer, boolean p_spawn) {
		
		Area map=new Area();
		SpriteManagement spriteManagement=EngineZildo.spriteManagement;

		map.setDim_x(p_buffer.readUnsignedByte());
		map.setDim_y(p_buffer.readUnsignedByte());
		map.setN_persos(p_buffer.readUnsignedByte());
		map.setN_sprites(p_buffer.readUnsignedByte());
		map.setN_pe(p_buffer.readUnsignedByte());
		
		// La map
		for (int i=0;i<map.getDim_y();i++)
			for (int j=0;j<map.getDim_x();j++)
			{
				//System.out.println("x="+j+"  y="+i);
				Case temp=new Case();
				temp.setN_motif(p_buffer.readUnsignedByte());
				temp.setN_banque(p_buffer.readUnsignedByte());
				temp.setN_motif_masque(p_buffer.readUnsignedByte());
				temp.setN_banque_masque(p_buffer.readUnsignedByte());
			
				map.set_mapcase(j,i+4,temp);
		
				if (temp.getN_motif()==99 && temp.getN_banque()==1 && p_spawn) {
					// Fum�e de chemin�e
					spriteManagement.spawnSpriteGeneric(Element.SPR_FUMEE,j*16,i*16,0, null);
				}
			}
		 /*
	     with spr do begin
	      x:=j*16+16;
	      y:=i*16+28;
	      z:=16;
	      vx:=0.3+random(5)*0.01;vy:=0;vz:=0;
	      ax:=-0.01;ay:=0;az:=0.01+random(5)*0.001;
	      quelspr:=6;
	     end;
	     spawn_sprite(spr);
	    end;*/
	
		// Les P.E
		ChainingPoint pe;
		if (map.getN_pe()!=0) {
			for (int i=0;i<map.getN_pe();i++) {
				pe=new ChainingPoint();
				pe.setPx(p_buffer.readUnsignedByte());
				pe.setPy(p_buffer.readUnsignedByte());
				pe.setMapname(p_buffer.readString(9));
				map.addPointEnchainement(pe);
			}
		}
	
		// Les sprites
		if (map.getN_sprites()!=0) {
			for (int i=0;i<map.getN_sprites();i++) {
				int x=((int)(p_buffer.readUnsignedByte()) << 8) + p_buffer.readUnsignedByte();
				int y=((int)p_buffer.readUnsignedByte() << 8) + p_buffer.readUnsignedByte();
				short nSpr;
				nSpr=p_buffer.readUnsignedByte();
				if (p_spawn) {
					spriteManagement.spawnSprite(SpriteBank.BANK_ELEMENTS,nSpr,x,y);
				}
			}
		}
	
		// Les persos
		if (map.getN_persos()!=0) {
			for (int i=0;i<map.getN_persos();i++) {
				PersoNJ perso;
				int x=((int)p_buffer.readUnsignedByte() << 8) + p_buffer.readUnsignedByte();
				int y=((int)p_buffer.readUnsignedByte() << 8) + p_buffer.readUnsignedByte();
				int z=((int)p_buffer.readUnsignedByte() << 8) + p_buffer.readUnsignedByte();
	
				PersoDescription desc=PersoDescription.fromNSpr(p_buffer.readUnsignedByte());
				
				switch (desc) {
				case BAS_GARDEVERT:
					perso=new PersoGardeVert();break;
				case GARDE_CANARD:
					perso=new PersoGarde();break;
				case CORBEAU:
				case SPECTRE:
					perso=new PersoVolant();break;
				default:
					perso=new PersoNJ();break;
				}
				perso.setX((float)x);
				perso.setY((float)y);
				perso.setZ((float)z);
				perso.setQuel_spr(desc);
				perso.setInfo(p_buffer.readUnsignedByte());
				perso.setEn_bras(p_buffer.readUnsignedByte());
				perso.setQuel_deplacement(MouvementPerso.fromInt((int) p_buffer.readUnsignedByte()));
				perso.setAngle(Angle.fromInt(p_buffer.readUnsignedByte()));
				
				perso.setNBank(SpriteBank.BANK_PNJ);
				perso.setNom(p_buffer.readString(9));
				Zone zo=new Zone();
				zo.setX1(map.roundAndRange(perso.getX()-16*5, Area.ROUND_X));
				zo.setY1(map.roundAndRange(perso.getY()-16*5, Area.ROUND_Y));
				zo.setX2(map.roundAndRange(perso.getX()+16*5, Area.ROUND_X));
				zo.setY2(map.roundAndRange(perso.getY()+16*5, Area.ROUND_Y));
				perso.setZone_deplacement(zo);
				perso.setPv(3);
				perso.setDx(-1);
				perso.setMouvement(MouvementZildo.MOUVEMENT_VIDE);
	
				if (perso.getQuel_spr().first() >= 128) {
					perso.setNBank(SpriteBank.BANK_PNJ2);
				}

				perso.initPersoFX();
	
				if (p_spawn) {
					spriteManagement.spawnPerso(perso);
				}
			}
		}
	
		// Les Phrases
		int n_phrases=0;
		if (!p_buffer.eof()) {
			n_phrases=p_buffer.readUnsignedByte();
			if (n_phrases > 0) {
				DialogManagement dialogManagement=EngineZildo.dialogManagement;
				// On lit les phrases
				for (int i=0;i<n_phrases;i++) {
		
					String phrase=p_buffer.readString();
					//dialogManagement.addSentence(phrase);
				}
				if (!p_buffer.eof()) {
					while (!p_buffer.eof()) {
						// On lit le nom
						String nomPerso=p_buffer.readString(9);
						// On lit le comportement
						short[] comportement=new short[10];
						p_buffer.readUnsignedBytes(comportement, 0, 10);
						//dialogManagement.addBehavior(nomPerso,comportement);
					}
				}
			}
		}
		
		return map;
	}

}