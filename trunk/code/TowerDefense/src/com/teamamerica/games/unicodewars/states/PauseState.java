package com.teamamerica.games.unicodewars.states;

import java.awt.FontFormatException;
import java.io.IOException;
import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.StaticLayout;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.util.Point;
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
import com.teamamerica.games.unicodewars.system.BB;

public class PauseState extends BHGameState
{
	
	private Image _pauseImage;
	private Music _pauseMusic;
	private boolean unpause;
	private boolean quit;
	private boolean leaveToOptions;
	private int aiToggleButtonCount;
	
	private int _centerX;
	private int _centerY;
	
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
		_centerX = container.getWidth() / 2;
		_centerY = container.getHeight() / 2;
		_pauseMusic.loop(.75f, .75f);
		aiToggleButtonCount = 0;
		unpause = false;
		quit = false;
		leaveToOptions = false;
		
		// This will add all of the widgets for your GUI to the
		// Display for rendering and capturing input..
		try
		{
			layout(_feng.getDisplay(), container, game);
		}
		catch (FontFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		
		_feng.getDisplay().removeAllWidgets();
		_pauseMusic.stop();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException
	{
		float centerX = container.getWidth() / 2.0f;
		float centerY = container.getHeight() / 2.0f;
		
		graphics.setBackground(Color.white);
		_pauseImage.drawCentered(centerX, centerY);
		
		_feng.render(container, game, graphics);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		if (this.unpause)
		{
			this.unpause = false;
			game.enterState(Main.States.GameplayState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			return;
		}
		if (this.quit)
		{
			this.quit = false;
			GameplayState gameplayState = (GameplayState) game.getState(Main.States.GameplayState.ordinal());
			gameplayState.end();
			game.enterState(Main.States.MainMenuState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			return;
		}
		if (this.leaveToOptions)
		{
			game.enterState(Main.States.OptionsState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			return;
		}
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_R || key == Input.KEY_ESCAPE)
		{
			this.unpause = true;
		}
		else if (key == Input.KEY_O)
		{
			this.leaveToOptions = true;
		}
		else if (key == Input.KEY_1)
		{
			aiToggleButtonCount++;
			if (aiToggleButtonCount > 2)
			{
				BB.inst().setAiEnabled(!BB.inst().isAiEnabled());
			}
		}
	}
	
	private void layout(Display display, final GameContainer container, final StateBasedGame game) throws FontFormatException, IOException
	{
		try
		{
			FengGUI.setTheme(new XMLTheme("data/themes/QtCurve/QtCurve.xml"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (IXMLStreamableException e)
		{
			e.printStackTrace();
		}
		display.setLayoutManager(new StaticLayout());
		

		Button btn = FengGUI.createWidget(Button.class);
		btn.setText("Resume Game");
		btn.setPosition(new Point(_centerX - 300, _centerY - 100));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				unpause = false;
				game.enterState(Main.States.GameplayState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Options Menu");
		btn.setPosition(new Point(_centerX - 100, _centerY - 100));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				game.enterState(Main.States.OptionsState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Quit Game");
		btn.setPosition(new Point(_centerX + 100, _centerY - 100));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				GameplayState gameplayState = (GameplayState) game.getState(Main.States.GameplayState.ordinal());
				gameplayState.end();
				game.enterState(Main.States.MainMenuState.ordinal(), new FadeOutTransition(), new FadeInTransition());
			}
		});
		display.addWidget(btn);
		
	}
}
