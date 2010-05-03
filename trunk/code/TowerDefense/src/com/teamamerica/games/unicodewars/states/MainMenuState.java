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
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameMap;

public class MainMenuState extends BHGameState {

	private int _centerX;
	private int _centerY;
	
	private StateBasedGame _game;
	private Image _backgroundImage;
	private Music _menuTheme;
	private boolean _bFirstGame;
	
	public MainMenuState()
	{
		_bFirstGame = true;
	}
	
	@Override
	public int getID() {
		return Main.States.MainMenuState.ordinal();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		_game = game;
		_backgroundImage = new Image("data/images/main-menu.png");
		_menuTheme = new Music("data/sounds/Main.ogg");
		
		_centerX = container.getWidth() / 2;
		_centerY = container.getHeight() / 2;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

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
		
		_menuTheme.loop(1, .75f);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		
		// Don't forget to remove all of the widgets before moving
		// to the next state.  If you forget then the widgets (although not rendered)
		// could actually receive button presses.
		_feng.getDisplay().removeAllWidgets();
		_menuTheme.stop();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// other rendering would go here (for instance if we wanted to put a 
		// pretty background we would render it here.
		g.setBackground(Color.white);

		_backgroundImage.draw(0,0);
		_feng.render(container, game, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int elapsed)
			throws SlickException {
		// If we had something to update, it would go here.
	}

	private void layout(Display display, final GameContainer container, final StateBasedGame game) throws FontFormatException, IOException
	{
		try {
			FengGUI.setTheme(new XMLTheme("data/themes/QtCurve/QtCurve.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IXMLStreamableException e) {
			e.printStackTrace();
		}
		display.setLayoutManager(new StaticLayout());

		Button btn = FengGUI.createWidget(Button.class);
		btn.setText("Play Game");
		btn.setPosition(new Point(800, _centerY));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0) {
				if (!_bFirstGame)
				{
					GameMap.$delete();
					BB.$delete();
				}
				_bFirstGame = false;
				((GameplayState) _game.getState(Main.States.GameplayState.ordinal())).start();
				_game.enterState(Main.States.GameplayState.ordinal());
			} 
		});	
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Exit Game");
		btn.setPosition(new Point(800, _centerY - 50));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0) {
				container.exit();
			} 
		});	
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Options");
		btn.setPosition(new Point(800, _centerY - 100));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				_game.enterState(Main.States.OptionsState.ordinal());
			}
		});
		display.addWidget(btn);
		
		btn = FengGUI.createWidget(Button.class);
		btn.setText("Credits");
		btn.setPosition(new Point(800, _centerY - 150));
		btn.setSize(200, 50);
		btn.addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				_game.enterState(Main.States.CreditsState.ordinal());
			}
		});
		display.addWidget(btn);
	}
}
