package com.teamamerica.games.unicodewars.states;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.GridLayout;
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
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameSystem;
import com.teamamerica.games.unicodewars.utils.Team;

public class GameplayState extends BHGameState
{
	private static Logger logger = Logger.getLogger(GameplayState.class);
	private int i;
	private GameSystem _gameSystem;
	private Container mobInterface;
	private Container towerInterface;
	
	public GameplayState()
	{
		mobInterface = new Container(new GridLayout(4, 5));
		towerInterface = new Container(new GridLayout(2, 3));
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
		display.addWidget(mobInterface);
		display.addWidget(towerInterface);
	}
	
	private void layoutTowerButtons(Display display)
	{
		int buttonSize = 128;
		Button buttons[] = new Button[6];
		
		towerInterface.setPosition(new Point(1024 - 384, 0));
		towerInterface.setLayoutManager(null);
		towerInterface.setHeight(256);
		towerInterface.setWidth(384);

		buttons[0] = FengGUI.createWidget(Button.class);
		buttons[0].setText("Dice");
		buttons[0].setSize(buttonSize, buttonSize);
		buttons[0].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Dice pressed.");
				BB.inst().setTowerSelection(TowerBase.Type.diceOne);
			}
		});
		towerInterface.addWidget(buttons[0]);
		
		buttons[1] = FengGUI.createWidget(Button.class);
		buttons[1].setText("Chess Pieces");
		buttons[1].setSize(buttonSize, buttonSize);
		buttons[1].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Chess Pieces pressed.");
				BB.inst().setTowerSelection(TowerBase.Type.chessOne);
			}
		});
		towerInterface.addWidget(buttons[1]);
		
		buttons[2] = FengGUI.createWidget(Button.class);
		buttons[2].setText("Currency");
		buttons[2].setSize(buttonSize, buttonSize);
		buttons[2].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Currency pressed.");
				BB.inst().setTowerSelection(TowerBase.Type.currencyOne);
			}
		});
		towerInterface.addWidget(buttons[2]);
		
		buttons[3] = FengGUI.createWidget(Button.class);
		buttons[3].setText("Card Suits");
		buttons[3].setSize(buttonSize, buttonSize);
		buttons[3].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Card Suits pressed.");
				BB.inst().setTowerSelection(TowerBase.Type.cardOne);
			}
		});
		towerInterface.addWidget(buttons[3]);
		
		buttons[4] = FengGUI.createWidget(Button.class);
		buttons[4].setText("Musical Notes");
		buttons[4].setSize(buttonSize, buttonSize);
		buttons[4].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Musical Notes pressed.");
				BB.inst().setTowerSelection(TowerBase.Type.musicOne);
			}
		});
		towerInterface.addWidget(buttons[4]);
		
		buttons[5] = FengGUI.createWidget(Button.class);
		buttons[5].setText("Special");
		buttons[5].setSize(buttonSize, buttonSize);
		buttons[5].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Special pressed.");
			}
		});
		towerInterface.addWidget(buttons[5]);
	}
	
	private void layoutMobButtons(Display display)
	{
		int buttonSize = 64;
		Button buttons[] = new Button[20];
		
		mobInterface.setPosition(new Point(0, 0));
		mobInterface.setLayoutManager(null);
		mobInterface.setHeight(256);
		mobInterface.setWidth(320);

		for (i = 0; i < 5; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setText("Chinese " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, 1));
			buttons[i].setSize(buttonSize, buttonSize);
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobChinese(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.chinese);
					BB.inst().setMobLevelSelection(level);
				}
			});
			mobInterface.addWidget(buttons[i]);
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
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobLatin(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.latin);
					BB.inst().setMobLevelSelection(level);
				}
			});
			mobInterface.addWidget(buttons[i]);
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
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobGreek(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.greek);
					BB.inst().setMobLevelSelection(level);
				}
			});
			mobInterface.addWidget(buttons[i]);
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
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobCyrillic(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.cyrillic);
					BB.inst().setMobLevelSelection(level);
				}
			});
			mobInterface.addWidget(buttons[i]);
		}
	}
}
