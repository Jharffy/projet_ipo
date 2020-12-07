package frog;

import gameCommons.Game;
import gameCommons.IFrog;
import util.Case;
import util.Direction;

public class Frog implements IFrog {
	
	private Game game;
	private Case pos;
	private Direction dir;
	
	public Frog(Game game) {
		this.game = game;
		this.pos = new Case(game.width/2, 0);
	}

	@Override
	public Case getPosition() {
		return this.pos;
	}

	@Override
	public Direction getDirection() {
		return this.dir;
	}

	@Override
	public void move(Direction key) {
		Case originCase = this.getPosition();
		if(key == Direction.up) {
			if(originCase.getOrd() < game.height -1) { //Vérifie les limites
				Case newCase = new Case(originCase.getAbsc(), originCase.getOrd() +1);
				this.pos = newCase;
			}
			
		} else if (key == Direction.down) {
			if(originCase.getOrd() != 0) {
				Case newCase = new Case(originCase.getAbsc(), originCase.getOrd() -1);
				this.pos = newCase;
			}
		} else if (key == Direction.left) {
			if(originCase.getAbsc() != 0) {
				Case newCase = new Case(originCase.getAbsc() -1, originCase.getOrd());
				this.pos = newCase;
			}
		} else if (key == Direction.right) {
			if(originCase.getAbsc() < game.width-1) {
				Case newCase = new Case(originCase.getAbsc() +1, originCase.getOrd() );
				this.pos = newCase;
			}
		}
		
		
		this.dir = key;
	}


}
