package environment;

import java.util.ArrayList;

import util.Case;
import gameCommons.Game;

public class Lane {
	protected Game game;
	protected int ord;
	protected int speed;
	protected ArrayList<Car> cars = new ArrayList<>();
	protected boolean leftToRight;
	protected double density;
	protected Environment environment;
	
	protected int compteur;

	//Constructeur(s)
	
	public Lane(Game game, Environment environment, int ord, int speed, boolean sens, double density) {
		this.game = game;
		this.environment = environment;
		this.ord = ord;
		this.speed = speed;
		this.leftToRight = sens;
		this.density = density;
		
		this.compteur = 0;
		
		//Construction des voitures : 
		cars.add(new Car(game, getFirstCase(), leftToRight)); //1ere voiture
	}

	
	/**
	 * Toutes les voitures se déplacent d'une case au bout d'un nombre "tic d'horloge" égal à leur vitesse
	 * <br/> Cette méthode est appelée à chaque tic d'horloge.
	 * <br/> Les voitures sont ajoutees a l interface graphique meme quand elle ne bougent pas.
	 * <br/> A chaque tic d'horloge, une voiture peut être ajoutée.
	 */
	public void update() {
		
		if(this.compteur < this.speed) {
			this.compteur++;
			if(this.cars.size() != 0) {  //Ne pas utiliser for each car moveCar peut potentiellement utiliser remove()
				for(int ca = this.cars.size() -1; ca >= 0 ; ca--) { //Remonter la liste à l'envers pour éviter 
					Car car = this.cars.get(ca);					// les voitures qui se chevauchent (et problème lorsque on les retire)
					moveCar(car, false, true);
					//this.cars.set(ca, car);
				}
			}
			
		} else {
			for(int ca = this.cars.size() -1; ca >= 0 ; ca--) {
				Car car = this.cars.get(ca);
				moveCar(car, true, true);
				//this.cars.set(ca, car);
			}
			this.compteur = 0;
		
		}
		
		mayAddCar();
	}

	




	/**
	 * Bouge la voiture de 1 si le compteur a atteint la variable speed, incremente le compteur sinon.
	 * <br/> Prends en compte les bordures
	 * <br/>Dans tout les cas, ajoute la voiture à l'interface graphique
	 * @param a La voiture
	 * @param move La voiture se deplace-elle ? Voir en fonction du compteur et de speed
	 * @param affiche Doit on afficher la Car ?
	 */
	public void moveCar(Car a, boolean move, boolean affiche) {
		if(move == false) {
			a.move(move, false, affiche);
		} else {
			if(a.getFrontPos() == getLastCase()) { //Vérifie les bordures
				if (a.getLength() == 1) {
					this.cars.remove(a);
				} else {
					a.move(move, true, affiche);
				}
			} else {
				a.move(move, false, affiche);
			}
			
		}
	}
	
	/**
	 * Vérifie si la case n'est pas située à l'interieur d'une voiture de la lane
	 * @param c La voiture
	 * @return
	 */
	public boolean isSafe(Case c) {
		// TODO Auto-generated method stub
		for (Car car : this.cars) {
			if(car.isInside(c)) {
				return false;
			}
		}
		
		return true;
	}

	/*
	 * Fourni : mayAddCar(), getFirstCase() et getBeforeFirstCase() 
	 */

	/**
	 * Ajoute une voiture au début de la voie avec probabilité égale à la
	 * densité, si la première case de la voie est vide
	 */
	protected void mayAddCar() {
		if (environment.isSafe(getFirstCase()) && environment.isSafe(getBeforeFirstCase())) {
			if (game.randomGen.nextDouble() < density) {
				cars.add(new Car(game, getBeforeFirstCase(), leftToRight));
			}
		}
	}
	
	
	
	
	/**
	 * Cherche la premiere case de la lane affichée <b>en fonction du sens de celle ci</b>
	 * @return La première case
	 */
	protected Case getFirstCase() {
		if (leftToRight) {
			return new Case(0, ord);
		} else
			return new Case(game.width - 1, ord);
	}
	
	/**
	 * Cherche la dernière case de la lane affichée <b>en fonction du sens de celle ci</b>
	 * @return La dernière case
	 */
	protected Case getLastCase() {
		if (leftToRight) {
			return new Case(game.width - 1, ord);
		} else
			return new Case(0, ord);
	}
	
	/**
	 * Cherche la case cachée par l'interface graphique située devant la première case de la lane affichée 
	 * <b>en fonction du sens de celle ci</b>
	 * @return L'avant première case
	 */
	protected Case getBeforeFirstCase() {
		if (leftToRight) {
			return new Case(-1, ord);
		} else
			return new Case(game.width, ord);
	}

}
