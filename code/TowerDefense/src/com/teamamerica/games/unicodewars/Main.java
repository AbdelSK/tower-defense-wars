package com.teamamerica.games.unicodewars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import com.teamamerica.games.unicodewars.gui.FengWrapper;
import com.teamamerica.games.unicodewars.states.BHGameState;
import com.teamamerica.games.unicodewars.states.GameplayState;
import com.teamamerica.games.unicodewars.states.LoseState;
import com.teamamerica.games.unicodewars.states.MainMenuState;
import com.teamamerica.games.unicodewars.states.SplashState;
import com.teamamerica.games.unicodewars.states.WinState;

public class Main extends StateBasedGame
{

	private FengWrapper _fengWrapper;
	
	public enum States
	{
		SplashState, MainMenuState, GameplayState, LoseState, WinState
	};
	
	public Main()
	{
		super("Unicode Wars - Prototype");
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
		for (States s : States.values())
		{
			GameState state = getState(s.ordinal());
			((BHGameState) state).setFengWrapper(_fengWrapper);
		}
		enterState(States.SplashState.ordinal());
	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new Main());
			app.setDisplayMode(1024, 768, true);
			app.setMaximumLogicUpdateInterval(20);
			app.setMinimumLogicUpdateInterval(16);
			app.setTargetFrameRate(80);
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
