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

public class SplashState extends BHGameState {

	private Image _splashImage;
	private Music _splashTheme;
	private int   _timer;
	
	@Override
	public int getID() {
		return Main.States.SplashState.ordinal();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		_splashImage = new Image("data/images/teamamerica_1024.png");
		_splashTheme = new Music("data/sounds/Splash.ogg");
		_timer = 160;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		_splashTheme.play();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		float centerX = container.getWidth() / 2.0f;
		float centerY = container.getHeight() / 2.0f;

		graphics.setBackground(Color.white);
		_splashImage.drawCentered(centerX, centerY);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException {
		_timer -= millis;

		if (true)
		{
			// _splashTheme.stop();
			try
			{
				Thread.currentThread();
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// sleep for 1000 ms
			
			_splashTheme.stop();
			game.enterState(Main.States.MainMenuState.ordinal(), 
					new FadeOutTransition(), new FadeInTransition());
		}
		
		// if (_timer < 0) {
		// game.enterState(Main.States.MainMenuState.ordinal(),
		// new FadeOutTransition(), new FadeInTransition());
		// }
	}
}
