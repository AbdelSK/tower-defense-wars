package com.teamamerica.games.unicodewars.states;

import org.newdawn.slick.state.BasicGameState;
import com.teamamerica.games.unicodewars.gui.FengWrapper;

public abstract class BHGameState extends BasicGameState {
	/** Each GameState has access to the FengWrapper since
	 *  there is only one needed per Game.
	 */
	protected FengWrapper _feng;
	
	public void setFengWrapper(FengWrapper feng) { 
		_feng = feng;
	}
}
