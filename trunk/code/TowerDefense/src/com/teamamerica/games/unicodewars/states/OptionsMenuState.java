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
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;

public class OptionsMenuState extends BHGameState
{
	private Main.States stateToLeaveTo;
	private boolean leaving;
	private int _centerX;
	private int _centerY;
	private Music _optionsMusic;
	private Sound _optionSoundFX;


	@Override
	public int getID()
	{
		return Main.States.OptionsState.ordinal();
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException
	{
		_optionsMusic = new Music("src/data/sounds/Options Music.ogg");
		_optionSoundFX = new Sound("src/data/sounds/click.wav");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		_centerX = container.getWidth() / 2;
		_centerY = container.getHeight() / 2;
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

		if (game instanceof Main)
		{
			Main realGame = (Main) game;
			stateToLeaveTo = realGame.LastState;
		}
		_optionsMusic.play();
		leaving = false;
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		_feng.getDisplay().removeAllWidgets();

		leaving = false;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		g.setBackground(Color.white);
		_feng.render(container, game, g);
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException
	{
		if (leaving)
		{
			arg1.enterState(stateToLeaveTo.ordinal(), new FadeOutTransition(), new FadeInTransition());
			_optionsMusic.fade(300, .1f, true);
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
		btn.setText("Toggle Fullscreen");
		btn.setPosition(new Point(_centerX - 100, _centerY + 75));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				try
				{
					_optionSoundFX.play();
					container.setFullscreen(!container.isFullscreen());
				}
				catch (SlickException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Toggle Music");
		btn.setPosition(new Point(_centerX - 100, _centerY + 25));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				_optionSoundFX.play();
				container.setMusicOn(!container.isMusicOn());
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Toggle Sound");
		btn.setPosition(new Point(_centerX - 100, _centerY - 25));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				container.setSoundOn(!container.isSoundOn());
				_optionSoundFX.play();
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Exit Menu");
		btn.setPosition(new Point(_centerX - 100, _centerY - 75));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				leaving = true;
			}
		});
		display.addWidget(btn);
		
	}

}
