package gameCommons;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import InfiniteFrogger.EnvInf;
import graphicalElements.Element;
import graphicalElements.IFroggerGraphics;

public class Game {

	public final Random randomGen = new Random();

	// Caracteristique de la partie
	public final int width;
	public final int height;
	public final int minSpeedInTimerLoops;
	public final double defaultDensity;
	public int score;
	public Instant timerStart;
	
	public boolean endGame;

	// Lien aux objets utilisés
	private IEnvironment environment;
	private IFrog frog;
	private IFroggerGraphics graphic;

	/**
	 * 
	 * @param graphic
	 *            l'interface graphique
	 * @param width
	 *            largeur en cases
	 * @param height
	 *            hauteur en cases
	 * @param minSpeedInTimerLoop
	 *            Vitesse minimale, en nombre de tour de timer avant déplacement
	 * @param defaultDensity
	 *            densite de voiture utilisee par defaut pour les routes
	 */
	public Game(IFroggerGraphics graphic, int width, int height, int minSpeedInTimerLoop, double defaultDensity) {
		super();
		this.graphic = graphic;
		this.width = width;
		this.height = height;
		this.minSpeedInTimerLoops = minSpeedInTimerLoop;
		this.defaultDensity = defaultDensity;
		this.score = 0;
		this.endGame = false;
	}

	/**
	 * Lie l'objet frog à la partie
	 * 
	 * @param frog
	 */
	public void setFrog(IFrog frog) {
		this.frog = frog;
	}

	/**
	 * Lie l'objet environment a la partie
	 * 
	 * @param environment
	 */
	public void setEnvironment(IEnvironment environment) {
		this.environment = environment;
	}
	
	public void setTimer(Instant time) {
		this.timerStart = time;
	}

	/**
	 * 
	 * @return l'interface graphique
	 */
	public IFroggerGraphics getGraphic() {
		return graphic;
	}

	/**
	 * Teste si la partie est perdue et lance un ecran de fin approprie si tel
	 * est le cas
	 * 
	 * @return true si le partie est perdue
	 */
	public boolean testLose() {
		if ( this.environment.isSafe(this.frog.getPosition()) ){
			return false;
		} else {
			Instant timerStop = Instant.now();
			Duration d = Duration.between( timerStart , timerStop );
			
			String[] message = new String[3];
			message[0] = "Regardez devant vous !";
			
			message[1] = " Score:";
			message[1] += String.valueOf(this.score);
			
			message[2] = "You survived ";
			message[2] += toStringDuration(d);
			
			this.getGraphic().endGameScreen(message);
			return true;
		}
	}

	/**
	 * Teste si la partie est gagnee et lance un ecran de fin approprie si tel
	 * est le cas
	 * 
	 * @return true si la partie est gagnee
	 */
	public boolean testWin() {
		if ( this.environment.isWinningPosition(this.frog.getPosition()) ){
			String[] message = {"Grenouille sauvee !"};
			this.getGraphic().endGameScreen(message);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Si on est dans un cas d'environnnement infini, met a jour le score
	 */
	public void updateScore() {
		if (this.environment instanceof EnvInf) {
			EnvInf envInf = (EnvInf) this.environment; //Oblige à considerer this.environment comme un EnvInf 
														//sinon impossible acceder getLaneId()
			int ord = frog.getPosition().getOrd();
			int id = envInf.getLaneId(ord);
			if (id > this.score) {
				this.score = id;
			}
		}
	}
	
	public String toStringDuration(Duration d) {
		int h = d.toHoursPart();
		int m = d.toMinutesPart();
		int sec = d.toSecondsPart();
		String str = "";
		if( h!=0 ) {
			str += String.valueOf(h) + "h ";
		}
		if( m!=0 ) {
			str += String.valueOf(m) + "min ";
		}
		if( sec!=0 ) {
			str += String.valueOf(sec) + "sec ";
		}
		return str;
	}
	
	/**
	 * Actualise l'environnement, affiche la grenouille et verifie la fin de
	 * partie.
	 */
	public void update() {
		if(! this.endGame) {
			graphic.clear();
			environment.update();
			this.graphic.add(new Element(frog.getPosition(), Color.GREEN));
			updateScore();
			this.endGame = testLose();
			if(! this.endGame) {
				this.endGame = testWin();
			}
		}
	}

}
