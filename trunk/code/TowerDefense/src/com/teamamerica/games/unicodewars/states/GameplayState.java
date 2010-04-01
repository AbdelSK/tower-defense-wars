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
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.CardOne;
import com.teamamerica.games.unicodewars.object.towers.ChessOne;
import com.teamamerica.games.unicodewars.object.towers.CurrencyOne;
import com.teamamerica.games.unicodewars.object.towers.DiceOne;
import com.teamamerica.games.unicodewars.object.towers.MusicOne;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.object.towers.TowerButton;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.system.GameSystem;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Team;

public class GameplayState extends BHGameState
{
	private static enum Winner
	{
		nobody, player1, player2;
	}
	
	private static Logger logger = Logger.getLogger(GameplayState.class);
	private GameSystem _gameSystem;
	private Container mobInterface;
	private Container towerInterface;
	private EventListener el;
	private boolean bLayoutComplete;
	private Winner winner;
	
	public GameplayState()
	{
		mobInterface = new Container(new GridLayout(4, 5));
		towerInterface = new Container(new GridLayout(2, 3));
		bLayoutComplete = false;
		winner = Winner.nobody;
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
		el = new EventListener() {
			
			@Override
			public void onEvent(Event e)
			{
				Team teamDestroyed = (Team) e.getValue("teamDestroyed");
				
				if (teamDestroyed == Team.Player1)
				{
					winner = Winner.player2;
				}
				else if (teamDestroyed == Team.Player2)
				{
					winner = Winner.player1;
				}
			}
		};
		EventManager.inst().registerForAll(EventType.BASE_DESTROYED, el);
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
		container.setShowFPS(false);
		_gameSystem.render(g);
		_feng.render(container, game, g);
		
		g.setAntiAlias(true);
		g.setBackground(Color.black);
		g.setColor(Color.black);
		g.drawString(container.getFPS() + "", 2, 2);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		_gameSystem.update(millis);
		if (winner == Winner.player1)
		{
			winner = Winner.nobody;
			game.enterState(Main.States.WinState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
		else if (winner == Winner.player2)
		{
			winner = Winner.nobody;
			game.enterState(Main.States.LoseState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
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
		if (!bLayoutComplete)
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
			bLayoutComplete = true;
		}
	}
	
	private void layoutTowerButtons(Display display)
	{
		final TowerButton tb[] = new TowerButton[6];
		String[][] text = new String[6][2];
		TowerBase.Type types[] = new TowerBase.Type[6];
		text[0][0] = "Dice";
		text[1][0] = "Chess Pieces";
		text[2][0] = "Currency";
		text[3][0] = "Card Suits";
		text[4][0] = "Musical Notes";
		text[5][0] = "Special";
		text[0][1] = "Dice Tower\nDamage: ??\nSpeed: ??\nRange: ??\nPrice: " + DiceOne.price;
		text[1][1] = "Chess Piece\nDamage: " + ChessOne.BASE_ATTACK + "\nSpeed: " + ChessOne.BASE_SPEED + "\nRange: " + ChessOne.BASE_RADIUS + "\nPrice: " + ChessOne.price;
		text[2][1] = "Currency\nDamage: " + CurrencyOne.BASE_ATTACK + "\nSpeed: " + CurrencyOne.BASE_SPEED + "\nRange: " + CurrencyOne.BASE_RADIUS + "\nPrice: " + CurrencyOne.price;
		text[3][1] = "Card Suit\nDamage: " + CardOne.BASE_ATTACK + "\nSpeed: " + CardOne.BASE_SPEED + "\nRange: " + CardOne.BASE_RADIUS + "\nPrice: " + CardOne.price;
		text[4][1] = "Musical Note\nDamage: " + MusicOne.BASE_ATTACK + "\nSpeed: " + MusicOne.BASE_SPEED + "\nRange: " + MusicOne.BASE_RADIUS + "\nPrice: " + MusicOne.price;
		text[5][1] = "Special\nDamage: ??\nSpeed: ??\nRange: ??\nPrice: " + MusicOne.price;
		types[0] = TowerBase.Type.diceOne;
		types[0] = TowerBase.Type.chessOne;
		types[0] = TowerBase.Type.currencyOne;
		types[0] = TowerBase.Type.cardOne;
		types[0] = TowerBase.Type.musicOne;
		types[0] = TowerBase.Type.diceOne;
		
		towerInterface.setPosition(new Point(640, 0));
		towerInterface.setHeight(256);
		towerInterface.setWidth(384);

		for (int i = 0; i < 6; i++)
		{
			tb[i] = FengGUI.createWidget(TowerButton.class);
			// tb[i] = new TowerButton(text[i][0], text[i][1], 128, 128,
			// types[i]);
			tb[i].setSize(128, 128);
			tb[i].setMultiline(true);
			tb[i].setShrinkable(false);
			tb[i].setText("shit");
			towerInterface.addWidget(tb[i]);
		}
	}
	
	private void layoutMobButtons(Display display)
	{
		int buttonSize = 64;
		final Button buttons[] = new Button[20];
		
		for (int i = 0; i < 20; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setSize(64, 64);
			buttons[i].setMultiline(true);
			buttons[i].setShrinkable(false);
		}
		
		mobInterface.setPosition(new Point(0, 0));
		mobInterface.setHeight(64);
		mobInterface.setWidth(320);

		for (int i = 0; i < 5; i++)
		{
			buttons[i].setText("Chinese " + (i + 1));
			buttons[i].setPosition(new Point(i * buttonSize, 0));
			buttons[i].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobChinese(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.chinese);
					BB.inst().setMobLevelSelection(level);
				}
			});
		}

		for (int i = 0; i < 5; i++)
		{
			buttons[i + 5].setText("Latin " + (i + 1));
			buttons[i + 5].setPosition(new Point(i * buttonSize, buttonSize));
			buttons[i + 5].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobLatin(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.latin);
					BB.inst().setMobLevelSelection(level);
				}
			});
		}
		
		for (int i = 0; i < 5; i++)
		{
			buttons[i + 10].setText("Greek " + (i + 1));
			buttons[i + 10].setPosition(new Point(i * buttonSize, 2 * buttonSize));
			buttons[i + 10].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobGreek(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.greek);
					BB.inst().setMobLevelSelection(level);
				}
			});
		}
		
		for (int i = 0; i < 5; i++)
		{
			buttons[i + 15].setText("Cyrillic " + (i + 1));
			buttons[i + 15].setPosition(new Point(i * buttonSize, 3 * buttonSize));
			buttons[i + 15].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
					MobMaker.MakeMobCyrillic(level, Team.Player1);
					BB.inst().setMobTypeSelection(MobObject.Type.cyrillic);
					BB.inst().setMobLevelSelection(level);
				}
			});
		}
		
		for (int i = 0; i < 20; i++)
			mobInterface.addWidget(buttons[i]);
	}
}
