package InfiniteFrogger;

import java.util.ArrayList;
import java.util.Random;

import environment.Environment;
import util.Case;
import util.Direction;
import gameCommons.Game;

public class EnvInf extends Environment {
	
	private ArrayList<LaneInf> lanes = new ArrayList<>();
	private ArrayList<LaneInf> lanesSafe = new ArrayList<>(); //Array de sauvegarde des lanes
	private int limitClean = 10;  //Combien de lanes sont sauvegardee au dessus et en dessous le l'interface graphique
	private int nbLanes;
	private double trottoire = 0.2;
	
	public EnvInf(Game game) {
		super(game); //reprend le constructeur de la classe Environment
		//Il y a 26 lanes :  1 cachee ( mais 0 compense)
		nbLanes = super.game.height;
		
		for (int i = 0; i < nbLanes; i++) {
			boolean isEmpty;
			if (i == 0 || i == 1) {
				isEmpty = true;
			} else {
				isEmpty = false;
			}
			int randomSense = rangedRandomInt(0,1);
			//Sens de la lane random
			boolean sense;
			if(randomSense == 0) {
				sense = false;
			} else {
				sense = true;
			}
			//Vitesse random
			int speed = rangedRandomInt(super.game.minSpeedInTimerLoops, super.game.minSpeedInTimerLoops + 2);
			this.lanes.add(new LaneInf(super.game, this, i, speed , sense , super.game.defaultDensity, isEmpty, i)); //Crée lane d'id i et 1ere lane vide
		}
	}
	
	
	@Override
	public void update() {
		for(LaneInf lane : this.lanes) {
			if(! lane.isEmpty() ) {
				lane.update();
			}
		}
		for(LaneInf lane : this.lanesSafe) {
			if(! lane.isEmpty() ) {
				lane.update();
			}
		}
		
	}
	
	
	//Random
	
	
	/**
	 * Crée un double random compris dans un certain intervalle [rangeMin, rangeMax]
	 * @param rangeMin
	 * @param rangeMax
	 * @return la valeur du double
	 */
	public double rangedRandomDouble(int rangeMin, int rangeMax) {
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return randomValue;
	}
	
	/**
	 * Crée un int random compris dans un certain intervalle [rangeMin, rangeMax]
	 * @param rangeMin
	 * @param rangeMax La borne maximale du random (doit être positive !!)
	 * @return la valeur du int
	 */
	public int rangedRandomInt(int rangeMin, int rangeMax) {
		Random r = new Random();
		int randomValue = rangeMin + r.nextInt(rangeMax +1);
		return randomValue;
	}
	
	
	
	//Operations sur this.lanes
	
	
	
	/**
	 * Ajoute une nouvelle lane aleatoire dans l'arrayList lanes
	 * @param ord l'ordonnee de la lane a ajouter
	 * @param id l'id de la lane a ajouter
	 */
	private void addNewLane(int ord, int id) {
		int randomSense = rangedRandomInt(0,1);
		//Sens de la lane random
		boolean sense;
		if(randomSense == 0) {
			sense = false;
		} else {
			sense = true;
		}
		//Lane remplie random
		boolean isEmpty = false;
		if (game.randomGen.nextDouble() < trottoire) {
			isEmpty = true;
		}
		//Vitesse random
		int speed = rangedRandomInt(super.game.minSpeedInTimerLoops, super.game.minSpeedInTimerLoops + 2);
		
		this.lanes.add(new LaneInf(super.game, this, ord, speed , sense , super.game.defaultDensity, isEmpty, id));
	}
	
	
	/**
	 * Recupere une lane dans lanes
	 * @param ord l'ordonnee de la lane a recuperer 
	 * <br/>/!\ prendre l'ord dans les limites de l'interface graphique !!
	 * @return une lane d'id et ord -1 si lane non trouvée
	 */
	public LaneInf getLane(int ord) {
		for (LaneInf lane : this.lanes) {
			if(lane.getOrd() == ord) {
				return lane;
			}
		}
		return new LaneInf(super.game, this, -1, 0 , true , super.game.defaultDensity, true, -1);
	}
	
	/**
	 * Cherche l'id d'une lane a partir de son ordonee parmi les lanes affichees
	 * @param ord ordonnee de la lane
	 * @return
	 */
	public int getLaneId(int ord) {
		return getLane(ord).getId();
	}
	
	/**
	 * Recupere la premiere Lane affichee
	 * @return
	 */
	public LaneInf getFirstLane() {
		return getLane(0);
	}
	
	/**
	 * Recupere la derniere Lane affichee
	 * @return
	 */
	public LaneInf getLastLane() {
		return getLane(this.nbLanes-1);
	}
	
	
	
	//Operations sur this.lanesSafe
	
	
	
	/**
	 * Retire la lane de l'arrayList lanes et la met dans lanesSafe
	 * </br>Une lane sauvegardee ne s'affiche plus
	 * @param lane La lane a sauvegarder
	 */
	private void saveLane(LaneInf lane) {
		lane.saved();
		this.lanesSafe.add(lane);
		this.lanes.remove(lane);
	}
	

	/**
	 * Recupere la lane de l'arrayList lanes et la depose dans lanes
	 * @param newOrd La nouvelle ordonnee de la lane
	 * @param id L'id de la lane a recuperer
	 * @return Si l'operation a reussi, return true. Sinon, return false
	 */
	private boolean restoreLane(int newOrd, int id) {
		if( findSavedLane(id) ) {
			LaneInf newLane = getSavedLane(id);
			this.lanesSafe.remove(newLane);
			
			newLane.setOrd(newOrd);
			newLane.loaded();
			this.lanes.add(newLane);
			return true;
		}
		return false;
	}
	
	/**
	 * Recupere une lane dans lanesSafe. 
	 * </br>A utiliser avec findSavedLane(int id)
	 * @param id l'id de la lane a recuperer 
	 * @return une lane d'id et ord -1 si lane non trouvée
	 */
	public LaneInf getSavedLane(int id) {
		for (LaneInf lane : this.lanesSafe) {
			if(lane.getId() == id) {
				return lane;
			}
		}
		return new LaneInf(game, this, -1, 0 , true , game.defaultDensity, true, -1);
	}
	
	/**
	 * Verifie si la lane cherchee existe dans lanesSafe
	 * @param id l'id de la lane
	 * @return
	 */
	public boolean findSavedLane(int id) {
		for (LaneInf lane : this.lanesSafe) {
			if (lane.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Libere de l'espace dans lanesSafe en supprimant les lanes trop eloignees.
	 * </br>La limite a partir de laquelle les lanes sont supprimees est this.limitClean
	 */
	public void cleanSavedLanes() {
		int firstId = getFirstLane().getId();
		int lastId = getLastLane().getId();
		for (int i = 0; i < this.lanesSafe.size(); i ++) {
			LaneInf lane = this.lanesSafe.get(i);
			if (   (lane.getId() > lastId + this.limitClean)   ||   (lane.getId() < firstId - this.limitClean)   ) {
				this.lanesSafe.remove(lane);
			}
		}
	}
	
	
	
	//Verifications lanes
	
	
	@Override
	public boolean isSafe(Case c) {
		int ord = c.getOrd(); //Idx de la lane dans l'arrayList
		LaneInf lane = getLane(ord);
		return lane.isSafe(c);
	}

	@Override
	public boolean isWinningPosition(Case c) {
//		if(c.getOrd() == game.height - 1) { 
//			return true;
//		}
		return false;
	}


	

	
	/**
	 * Si on monte, diminue tous les ord des lanes contenues dans l'arrayList lanes de 1. 
	 * Si on descend, les augmente de 1.
	 * @param dir
	 */
	private void shiftOrdLane(Direction dir) {
		if(dir == Direction.up) {
			for(LaneInf lane : this.lanes) {
				int ord = lane.getOrd();
				lane.setOrd(ord -1);
			}
		} else if(dir == Direction.down) {
			for(LaneInf lane : this.lanes) {
				int ord = lane.getOrd();
				lane.setOrd(ord +1);
			}
		}
	}
	
	
	/**
	 * En fonction de la direction, place les lignes aux extremites dans lanesSafe
	 * @param key
	 */
	public void moveLane(Direction key) {
		LaneInf firstLane = getFirstLane();
		LaneInf lastLane = getLastLane();
		
		if (key == Direction.up) {
			//Sauvegarde la premiere lane et l'enleve de this.lanes
			saveLane(firstLane);
			
			int newId = lastLane.getId() + 1; //Recupere l'id a chercher
			shiftOrdLane(key); //Decale toutes les lanes
			
			if(! restoreLane(nbLanes -1, newId)) {
				addNewLane(nbLanes -1, newId);
			}
			
			
		} else if (key == Direction.down) {
			if (  ! (firstLane.getId() == 0)  ||  ! (firstLane.getId() == 1)  ) {  //Verifier bordure : id == 0 id == 1
				saveLane(lastLane);
				
				int newId = firstLane.getId() - 1; //Recupere l'id a chercher
				shiftOrdLane(key); //Decale toutes les lanes
				
				if(! restoreLane(0, newId)) {
					addNewLane(0, newId);
				}
				
			}
		}
		cleanSavedLanes();
	}
	
	

}
