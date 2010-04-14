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

public class WinState extends BHGameState {

	private Image _winImage;
	private Music _winTheme;
	private int   _timer;
	
	@Override
	public int getID() {
		return Main.States.WinState.ordinal();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		_winImage = new Image("data/images/playerWins.png");
		_winTheme = new Music("data/sounds/America, Fuck Yeah.ogg");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		_timer = 2000;
		// _winTheme.play();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
		float centerX = container.getWidth() / 2.0f;
		float centerY = container.getHeight() / 2.0f;

		graphics.setBackground(Color.white);
		_winImage.drawCentered(centerX, centerY);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException {
		_timer -= millis;
		
		if (_timer < 0)
		{
			_winTheme.stop();
			game.enterState(Main.States.MainMenuState.ordinal(), 
					new FadeOutTransition(), new FadeInTransition());
		}
	}
}
