package com.teamamerica.games.unicodewars.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;

public class OptionsMenuState extends BHGameState
{
	private Main.States stateToLeaveTo;
	private boolean leaving;
	GameContainer container;

	@Override
	public int getID()
	{
		return Main.States.OptionsState.ordinal();
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		if (game instanceof Main)
		{
			Main realGame = (Main) game;
			stateToLeaveTo = realGame.LastState;
		}
		leaving = false;
		
		this.container = container;
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		leaving = false;
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException
	{
		arg2.setColor(Color.white);
		arg2.fillRect(0, 0, arg0.getWidth(), arg0.getHeight());
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException
	{
		if (leaving)
		{
			arg1.enterState(stateToLeaveTo.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
		
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_ESCAPE)
		{
			leaving = true;
		}
	}

	private void setVolumeOnOff(boolean on)
	{
		this.container.setMusicOn(on);
	}

}
