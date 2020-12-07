package InfiniteFrogger;

import java.util.ArrayList;

import environment.Car;
import environment.Environment;
import environment.Lane;
import gameCommons.Game;
import util.Case;

@SuppressWarnings("unused")
public class LaneInf extends Lane {

	private int id;
	private boolean isEmpty;
	private boolean saved;

	//Constructeur(s)
	
	public LaneInf(Game game, Environment environment, int ord, int speed, boolean sens, double density, boolean isEmpty, int identity) {
		
		super(game,environment, ord, speed, sens, density);
		this.id = identity;
		this.isEmpty = isEmpty;
		this.saved = false;
		
		if (! this.isEmpty) {
			//Construction des voitures : 
			super.cars.add(new Car(super.game, getFirstCase(), super.leftToRight)); //1ere voiture
		}
	}

	
	/**
	 * Toutes les voitures se deplacent d'une case au bout d'un nombre "tic d'horloge" egal a leur vitesse
	 * <br/> Cette methode est appelee a chaque tic d'horloge.
	 * <br/> Les voitures sont ajoutees a l interface graphique meme quand elle ne bougent pas.
	 * <br/> A chaque tic d'horloge, une voiture peut etre ajoutee.
	 */
	public void update() {
		if(! this.isEmpty) {//Si la lane doit etre remplie
			updatePos();
			if(super.compteur < super.speed) {
				super.compteur++;
				if(super.cars.size() != 0) {  //Ne pas utiliser for each car moveCar peut potentiellement utiliser remove()
					for(int ca = super.cars.size() -1; ca >=0 ; ca--) { //Remonter la liste a l'envers pour eviter 
						Car car = super.cars.get(ca);					// les voitures qui se chevauchent (et probleme lorsque on les retire)
						moveCar(car, false, afficher());
						//this.cars.set(ca, car);
					}
				}
				
			} else {
				for(int ca = super.cars.size() -1; ca >=0 ; ca--) {
					Car car = super.cars.get(ca);
					moveCar(car, true, afficher());
					//this.cars.set(ca, car);
				}
				this.compteur = 0;
			
			}
			if (! this.isEmpty) {
				super.mayAddCar();
			}
		}
		

	}

	/**
	 * Met a jour l'ordonnee des voitures
	 */
	public void updatePos() {
		if( ! isEmpty) {
			for(Car car : super.cars) {
				car.setOrd(super.ord);
			}
		}
	}
	
	/**
	 * Si la lane est saved, on ne veut pas l'afficher
	 * @return
	 */
	private boolean afficher() {
		return !this.saved;
	}
	
	/**
	 * Recupere l'id de la LaneInf
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Recupere l'ord de la laneInf
	 * @return
	 */
	public int getOrd() {
		return this.ord;
	}
	
	/**
	 * Redefini l'ordonee de la lane
	 * @param ord
	 */
	public void setOrd(int ord) {
		super.ord = ord;
	}
	
	public boolean isEmpty() {
		return this.isEmpty;
	}
	
	/**
	 * Indique que la lane est sauvegardee
	 */
	public void saved() {
		this.saved = true;
	}
	
	/**
	 * Indique que la lane n'est pas sauvegardee
	 */
	public void loaded() {
		this.saved = false;
	}
	
	/**
	 * Vérifie si la case n'est pas située à l'interieur d'une voiture de la lane
	 * @param c La voiture
	 * @return
	 */
	public boolean isSafe(Case c) {
		if(this.isEmpty) {
			return true;
		}
		for (Car car : super.cars) {
			if(car.isInside(c)) {
				return false;
			}
		}
		
		return true;
	}
//		
//		/**
//		 *  Bouge la voiture de 1 si le compteur a atteint la variable speed, incremente le compteur sinon.
//		 * <br/> Prends en compte les bordures
//		 * <br/>Dans tout les cas, ajoute la voiture à l'interface graphique
//		 * @param a La voiture
//		 * @param move La voiture se déplace-elle ? Voir en fonction du compteur et de speed
//		 */
//		public void moveCar(Car a, boolean move) {
//			if(move == false) {
//				a.move(move, false);
//			} else {
//				if(a.getFrontPos() == getLastCase()) { //Vérifie les bordures
//					if (a.getLength() == 1) {
//						this.cars.remove(a);
//					} else {
//						a.move(move, true);
//					}
//				} else {
//					a.move(move, false);
//				}
//				
//			}
//		}

//	
//		/*
//		 * Fourni : mayAddCar(), getFirstCase() et getBeforeFirstCase() 
//		 */
//	
//		/**
//		 * Ajoute une voiture au début de la voie avec probabilité égale à la
//		 * densité, si la première case de la voie est vide
//		 */
//		private void mayAddCar() {
//			if (environment.isSafe(getFirstCase()) && environment.isSafe(getBeforeFirstCase())) {
//				if (game.randomGen.nextDouble() < density) {
//					cars.add(new Car(game, getBeforeFirstCase(), leftToRight));
//				}
//			}
//		}
//		
//		
//		
//		
//		/**
//		 * Cherche la premiere case de la lane affichée <b>en fonction du sens de celle ci</b>
//		 * @return La première case
//		 */
//		private Case getFirstCase() {
//			if (leftToRight) {
//				return new Case(0, ord);
//			} else
//				return new Case(game.width - 1, ord);
//		}
//		
//		/**
//		 * Cherche la dernière case de la lane affichée <b>en fonction du sens de celle ci</b>
//		 * @return La dernière case
//		 */
//		private Case getLastCase() {
//			if (leftToRight) {
//				return new Case(game.width - 1, ord);
//			} else
//				return new Case(0, ord);
//		}
//		
//		/**
//		 * Cherche la case cachée par l'interface graphique située devant la première case de la lane affichée 
//		 * <b>en fonction du sens de celle ci</b>
//		 * @return L'avant première case
//		 */
//		private Case getBeforeFirstCase() {
//			if (leftToRight) {
//				return new Case(-1, ord);
//			} else
//				return new Case(game.width, ord);
//		}

}
