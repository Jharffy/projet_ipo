package gameCommons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

import javax.swing.Timer;

import InfiniteFrogger.EnvInf;
import InfiniteFrogger.FrogInf;
import environment.Environment;
import frog.Frog;
import givenEnvironment.GivenEnvironment;
import graphicalElements.FroggerGraphic;
import graphicalElements.IFroggerGraphics;

public class Main {

	public static void main(String[] args) {

		//Caractéristiques du jeu  Taille d'un tableau de cases (pas des pixels !!)
		int width = 26;
		int height = 20;
		int tempo = 100;
		int minSpeedInTimerLoops = 3;
		double defaultDensity = 0.07;
		
		//Création de l'interface graphique
		IFroggerGraphics graphic = new FroggerGraphic(width, height);
		//Création de la partie
		Game game = new Game(graphic, width, height, minSpeedInTimerLoops, defaultDensity);
		//Création et liason de la grenouille
		IFrog frog = new FrogInf(game);
		game.setFrog(frog);
		graphic.setFrog(frog);
		//Création et liaison de l'environnement
		IEnvironment env = new EnvInf(game);
		game.setEnvironment(env);
		((FroggerGraphic) graphic).setEnv(env);
		
		//Enregistrement du debut du timer
		Instant start = Instant.now();
		game.setTimer(start);
				
		//Boucle principale : l'environnement s'acturalise tous les tempo milisecondes
		Timer timer = new Timer(tempo, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.update();
				graphic.repaint();
			}
		});
		timer.setInitialDelay(0);
		timer.start();
	}
}
