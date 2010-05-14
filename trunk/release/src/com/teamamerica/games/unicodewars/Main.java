package com.teamamerica.games.unicodewars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;
import com.teamamerica.games.unicodewars.gui.FengWrapper;
import com.teamamerica.games.unicodewars.states.BHGameState;
import com.teamamerica.games.unicodewars.states.CreditsState;
import com.teamamerica.games.unicodewars.states.GameplayState;
import com.teamamerica.games.unicodewars.states.LoseState;
import com.teamamerica.games.unicodewars.states.MainMenuState;
import com.teamamerica.games.unicodewars.states.OptionsMenuState;
import com.teamamerica.games.unicodewars.states.PauseState;
import com.teamamerica.games.unicodewars.states.SplashState;
import com.teamamerica.games.unicodewars.states.WinState;

public class Main extends StateBasedGame
{

	private FengWrapper _fengWrapper;
	
	public enum States
	{
		SplashState, MainMenuState, GameplayState, LoseState, WinState, PauseState, OptionsState, CreditsState
	};
	
	public States LastState;

	public Main()
	{
		super("Unicode Wars");
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException
	{
		_fengWrapper = new FengWrapper(container);

		addState(new SplashState());
		addState(new MainMenuState());
		addState(new GameplayState());
		addState(new LoseState());
		addState(new WinState());
		addState(new PauseState());
		addState(new OptionsMenuState());
		addState(new CreditsState());
		for (States s : States.values())
		{
			GameState state = getState(s.ordinal());
			((BHGameState) state).setFengWrapper(_fengWrapper);
		}
		enterState(States.SplashState.ordinal());
	}
	
	@Override
	public void enterState(int id, Transition enter, Transition leave)
	{
		int lastId = this.getCurrentStateID();
		for (States s : States.values())
		{
			if (lastId == s.ordinal())
				LastState = s;
		}
		super.enterState(id, enter, leave);
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new Main());
			app.setDisplayMode(1024, 768, false);
			app.setMaximumLogicUpdateInterval(20);
			app.setMinimumLogicUpdateInterval(16);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
