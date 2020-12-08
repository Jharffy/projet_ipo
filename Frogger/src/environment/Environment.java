package environment;

import java.util.ArrayList;
import java.util.Random;

import util.Case;
import gameCommons.Game;
import gameCommons.IEnvironment;

public class Environment implements IEnvironment {
	
	protected Game game;
	private ArrayList<Lane> lanes = new ArrayList<>();
	
	public Environment(Game game) {
		this.game = game;
		//Il y a 24 lanes : 1 depart, 1 arrivée, 1 cachee ( mais 0 compense)
		for (int i = 0; i < game.height-2; i++) {
			int randomSense = rangedRandomInt(0,1);
			//Sens de la lane random
			boolean sense;
			if(randomSense == 0) {
				sense = false;
			} else {
				sense = true;
			}
			//Vitesse random
			int speed = rangedRandomInt(game.minSpeedInTimerLoops, game.minSpeedInTimerLoops + 2);
			lanes.add(new Lane(game, this, i+1, speed , sense , game.defaultDensity ));
		}
	}
	
	
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
	 * @param rangeMax La borne maximale du random (doit etre positive !!)
	 * @return la valeur du int
	 */
	public int rangedRandomInt(int rangeMin, int rangeMax) {
		Random r = new Random();
		int randomValue = rangeMin + r.nextInt(rangeMax +1);
		return randomValue;
	}
	
	
	
	/**
	 * Récupère une lane
	 * @param ord l'ordonnée de la lane à récupérer
	 * @return
	 */
	public Lane getLane(int ord) {
		return this.lanes.get(ord-1);
	}
	
	
	@Override
	public boolean isSafe(Case c) {
		int ord = c.getOrd(); //Idx de la lane dans l'arrayList
		if(ord == 0 | isWinningPosition(c) ) {
			return true; //Bordures !!
		}
		Lane lane = getLane(ord);
		
		return lane.isSafe(c);
	}

	@Override
	public boolean isWinningPosition(Case c) {
		if(c.getOrd() == game.height - 1) { 
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		for(Lane lane : this.lanes) {
			lane.update();
		}
		
	}
		

}
