package com.teamamerica.games.unicodewars.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.utils.Timer;

public class SplashState extends BHGameState
{

	private Image _splashImage;
	private Music _splashTheme;
	private Timer _timer;
	private boolean leave;
	private boolean firstTimeUpdate;
	
	@Override
	public int getID()
	{
		return Main.States.SplashState.ordinal();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException
	{
		_splashImage = new Image("data/images/teamamerica_1024.png");
		_splashTheme = new Music("data/sounds/Splash.ogg");

	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		this.leave = false;
		_timer = new Timer();
		this.firstTimeUpdate = true;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException
	{
		float centerX = container.getWidth() / 2.0f;
		float centerY = container.getHeight() / 2.0f;

		graphics.setBackground(Color.white);
		_splashImage.drawCentered(centerX, centerY);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		if (_timer.xMilisecondsPassed(1000))
		{
			if (!_splashTheme.playing() || this.leave)
			{
				game.enterState(Main.States.MainMenuState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			}
		}
		
		if (this.firstTimeUpdate)
		{
			_splashTheme.play();
			this.firstTimeUpdate = false;
		}
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		this.leave = true;
	}
}
