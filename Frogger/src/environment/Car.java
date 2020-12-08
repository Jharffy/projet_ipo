package environment;

import java.awt.Color;
import java.util.Random;

import util.Case;
import gameCommons.Game;
import graphicalElements.Element;

public class Car {
	private Game game;
	private Case leftPosition;
	private boolean leftToRight;
	private int length;
	private final Color colorLtR = Color.ORANGE;
	private final Color colorRtL = Color.BLUE;

	//Constructeur(s)
	
	/**
	 * Ajoute une voiture
	 * @param game Les elements de jeu
	 * @param pos La case sur laquelle est la position avant de la voiture
	 * @param direction true si la voiture se déplace de gauche à droite, false sinon
	 */
	public Car(Game game, Case Frontpos, Boolean direction) {
		this.game = game;
		this.leftToRight = direction;
		
		this.length = rangedRandomInt(1,3);
		Case pos;
		if(direction) {
			pos = new Case(Frontpos.getAbsc() - this.length -1, Frontpos.getOrd());
		} else {
			pos = Frontpos;
		}
		
		this.leftPosition = pos;
	}
	
	
	
	
	/**
	 * CrÃ©e un int random compris dans un certain intervalle [rangeMin, rangeMax]
	 * @param rangeMin La borne minimale du random
	 * @param rangeMax La borne maximale du random (doit etre positive !!)
	 * @return la valeur du int
	 */
	public int rangedRandomInt(int rangeMin, int rangeMax) {
		Random r = new Random();
		int randomValue = rangeMin + r.nextInt(rangeMax +1); //Car r.nextInt(rangeMax) est compris entre [0, rangeMax[
		return randomValue;									 //rangeMax est exclus
	}
	
	
	/**
	 * Retourne la case de l'arriere de la voiture en fonction de sa direction
	 * @return
	 */
	public Case getRearPos() {
		if (leftToRight) {
			return leftPosition;
		} else {
			int absc = leftPosition.getAbsc() + length -1;
			return new Case(absc, leftPosition.getOrd());
			
		}
	}
	
	/**
	 * Retourne la case du devant de la voiture en fonction de sa direction
	 * @return
	 */
	public Case getFrontPos() {
		if (leftToRight) {
			int absc = leftPosition.getAbsc() + length -1;
			return new Case(absc, leftPosition.getOrd());
		} else {
			return leftPosition;
		}
	}
	
	/**
	 * Recupere la longueur de la voiture
	 * @return
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Modifie la longueur de la voiture
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Modifie l'ordonnee de la position de la voiture
	 * @param ord
	 */
	public void setOrd(int ord) {
		int absc = this.leftPosition.getAbsc();
		this.leftPosition = new Case(absc, ord);
	}
	
	/**
	 * Vérifie si la case est positionnée à l'interieur de la voiture
	 * @param c
	 * @return
	 */
	public boolean isInside(Case c) {
		int abs = c.getAbsc();
		int rear = this.getRearPos().getAbsc();
		int front = this.getFrontPos().getAbsc();
		if(abs <= rear && abs >= front) {  //En fonction des sens
			return true;
		} else if (abs >= rear && abs <= front) {
			return true;
		} else {
			return false;
		}
	}
	
	
	

	/**
	 * Bouge la voiture de 1 case et l'ajoute à l'élément graphique
	 * @param move La voiture bouge-elle ?
	 * @param border La voiture est-elle sur une bordure ? 
	 * Si oui, déplace la voiture en diminuant sa taille de 1, à condition qu'elle soit différente de 1
	 * @param affiche Doit-on afficher la Car ?
	 */
	public void move(boolean move, boolean border, boolean affiche) {
		if(move) {
			if(this.leftToRight) {
				if (border == false) {
				int absc = leftPosition.getAbsc() +1;
				Case c = new Case(absc, leftPosition.getOrd());
				this.leftPosition = c;
				
				} else {
					int absc = leftPosition.getAbsc() +1;
					Case c = new Case(absc, leftPosition.getOrd());
					this.leftPosition = c;
					int length = getLength(); 
					if(length > 1) {
						setLength(length -1);
					}
				}
				
			} else {
				if (border == false) {
					int absc = leftPosition.getAbsc() -1;
					Case c = new Case(absc, leftPosition.getOrd());
					this.leftPosition = c;
				} else {
					int length = getLength(); 
					if(length > 1) {
						setLength(length -1);
					}
				}
			}
		}
		if(affiche) {
			this.addToGraphics();
		}
	}

	
	
	/* Fourni : addToGraphics() permettant d'ajouter un element graphique correspondant a la voiture*/
	/**
	 * Ajouter un element graphique correspondant a la voiture
	 * </br>C'est ici que les lanes sont ajoutees a l'interface graphique
	 */
	private void addToGraphics() {
		for (int i = 0; i < length; i++) {
			Color color = colorRtL;
			if (this.leftToRight){
				color = colorLtR;
			}
			game.getGraphic()
					.add(new Element(leftPosition.absc + i, leftPosition.ord, color));
		}
	}

}
