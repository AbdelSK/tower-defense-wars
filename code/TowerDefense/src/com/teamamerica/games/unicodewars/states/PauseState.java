package com.teamamerica.games.unicodewars.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;

public class PauseState extends BHGameState
{
	
	private Image _pauseImage;
	private Music _pauseMusic;
	private boolean unpause;
	
	@Override
	public int getID()
	{
		return Main.States.PauseState.ordinal();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException
	{
		_pauseImage = new Image("data/images/paused.png");
		_pauseMusic = new Music("data/sounds/Pause Music.ogg");
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		_pauseMusic.loop(.75f, .25f);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		_pauseMusic.stop();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException
	{
		float centerX = container.getWidth() / 2.0f;
		float centerY = container.getHeight() / 2.0f;
		
		graphics.setBackground(Color.white);
		_pauseImage.drawCentered(centerX, centerY);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		if (this.unpause)
		{
			game.enterState(Main.States.GameplayState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_R || key == Input.KEY_ESCAPE)
		{
			this.unpause = true;
		}
	}
}
