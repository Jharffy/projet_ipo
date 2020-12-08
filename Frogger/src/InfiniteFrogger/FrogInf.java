package InfiniteFrogger;

import frog.Frog;
import gameCommons.Game;
import util.Case;

public class FrogInf extends Frog{
	
	//attributs
	
	
	
	//constructeurs
	public FrogInf(Game game) {
		super(game);
		super.pos = new Case(super.game.width/2, 1);
	}
	

}
