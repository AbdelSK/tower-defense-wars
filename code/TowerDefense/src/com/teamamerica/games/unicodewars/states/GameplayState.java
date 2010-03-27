package com.teamamerica.games.unicodewars.states;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameSystem;
import com.teamamerica.games.unicodewars.utils.Team;

public class GameplayState extends BHGameState
{
	private static Logger logger = Logger.getLogger(GameplayState.class);
	private int i;
	private GameSystem _gameSystem;
	
	public GameplayState()
	{
		
	}
	
	@Override
	public int getID()
	{
		return Main.States.GameplayState.ordinal();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException
	{
		_gameSystem = new GameSystem(container.getWidth(), container.getHeight());
		_gameSystem.loadLevel("Hello world");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		layout(_feng.getDisplay());
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		_feng.getDisplay().removeAllWidgets();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		g.setBackground(Color.black);
		
		_gameSystem.render(g);
		_feng.render(container, game, g);
		container.setShowFPS(false);
		g.setColor(Color.black);
		g.drawString(container.getFPS() + "", 2, 2);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		_gameSystem.update(millis);
	}

	@Override
	public void keyPressed(int key, char c)
	{
		BB.inst().keyPressed(key);
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		BB.inst().keyReleased(key);
	}
	
	@Override
	public void mousePressed(int button, int x, int y)
	{
		BB.inst().mouseClicked(button, x, y);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y)
	{
		BB.inst().mouseReleased(button, x, y);
	}

	/**
	 * Layout the GUI for this game state. We definitely need some sliders to
	 * modify values, etc.
	 */
	private void layout(Display display)
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
		
		layoutTowerButtons(display);
		layoutMobButtons(display);
	}
	
	private void layoutTowerButtons(Display display)
	{
		int buttonSize = 128;
		Button buttons[] = new Button[6];

		buttons[0] = FengGUI.createWidget(Button.class);
		buttons[0].setText("Dice");
		buttons[0].setPosition(new Point(1024 - buttonSize, 0));
		buttons[0].setSize(buttonSize, buttonSize);
		buttons[0].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Dice pressed.");
			}
		});
		display.addWidget(buttons[0]);
		
		buttons[1] = FengGUI.createWidget(Button.class);
		buttons[1].setText("Chess Pieces");
		buttons[1].setPosition(new Point(1024 - buttonSize, buttonSize - 1));
		buttons[1].setSize(buttonSize, buttonSize);
		buttons[1].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Chess Pieces pressed.");
			}
		});
		display.addWidget(buttons[1]);
		
		buttons[2] = FengGUI.createWidget(Button.class);
		buttons[2].setText("Currency");
		buttons[2].setPosition(new Point(1024 - 2 * buttonSize, 0));
		buttons[2].setSize(buttonSize, buttonSize);
		buttons[2].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Currency pressed.");
			}
		});
		display.addWidget(buttons[2]);
		
		buttons[3] = FengGUI.createWidget(Button.class);
		buttons[3].setText("Card Suits");
		buttons[3].setPosition(new Point(1024 - 2 * buttonSize, buttonSize - 1));
		buttons[3].setSize(buttonSize, buttonSize);
		buttons[3].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Card Suits pressed.");
			}
		});
		display.addWidget(buttons[3]);
		
		buttons[4] = FengGUI.createWidget(Button.class);
		buttons[4].setText("Musical Notes");
		buttons[4].setPosition(new Point(1024 - 3 * buttonSize, 0));
		buttons[4].setSize(buttonSize, buttonSize);
		buttons[4].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Musical Notes pressed.");
			}
		});
		display.addWidget(buttons[4]);
		
		buttons[5] = FengGUI.createWidget(Button.class);
		buttons[5].setText("Special");
		buttons[5].setPosition(new Point(1024 - 3 * buttonSize, buttonSize - 1));
		buttons[5].setSize(buttonSize, buttonSize);
		buttons[5].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Special pressed.");
			}
		});
		display.addWidget(buttons[5]);
	}
	
	private void layoutMobButtons(Display display)
	{
		int buttonSize = 64;
		Button buttons[] = new Button[20];

		for (i = 0; i < 5; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setText("Chinese " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, 1));
			buttons[i].setSize(buttonSize, buttonSize);
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					MobMaker.MakeMobChinese(i, Team.Player1);
				}
			});
			display.addWidget(buttons[i]);
		}
		
		for (i = 0; i < 5; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setText("Latin " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, buttonSize + 1));
			buttons[i].setSize(buttonSize, buttonSize);
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					MobMaker.MakeMobLatin(i, Team.Player1);
				}
			});
			display.addWidget(buttons[i]);
		}
		
		for (i = 0; i < 5; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setText("Greek " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, 2 * buttonSize + 1));
			buttons[i].setSize(buttonSize, buttonSize);
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					MobMaker.MakeMobGreek(i, Team.Player1);
				}
			});
			display.addWidget(buttons[i]);
		}
		
		for (i = 0; i < 5; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setText("Cyrillic " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, 3 * buttonSize));
			buttons[i].setSize(buttonSize, buttonSize);
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					MobMaker.MakeMobCyrillic(i, Team.Player1);
				}
			});
			display.addWidget(buttons[i]);
		}
	}
}
