package com.teamamerica.games.unicodewars.states;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.layout.GridLayout;
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
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.gui.MobButton;
import com.teamamerica.games.unicodewars.gui.TowerButton;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.CardOne;
import com.teamamerica.games.unicodewars.object.towers.ChessOne;
import com.teamamerica.games.unicodewars.object.towers.CurrencyOne;
import com.teamamerica.games.unicodewars.object.towers.DiceOne;
import com.teamamerica.games.unicodewars.object.towers.MusicOne;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
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
	
	private final int EMITTER_REFRESH_COUNT = 300;
	
	private ParticleSystem particleSystem;

	private static Logger logger = Logger.getLogger(GameplayState.class);
	private GameSystem _gameSystem;
	private Container mobInterface;
	private Container towerInterface;
	private EventListener baseDestroyedListener;
	private EventListener emitterListener;
	private boolean bLayoutComplete;
	private Winner winner;
	private boolean paused;
	private Music _gameTheme;
	private float _themePosition = -1;
	private final MobButton _mobButtons[][] = new MobButton[4][5];
	final TowerButton _towerButtons[] = new TowerButton[5];
	
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
		Image image = new Image("data/effects/fireball.png", false);
		particleSystem = new ParticleSystem(image);
		particleSystem.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
		particleSystem.setUsePoints(false);
		
		_gameTheme = new Music("data/sounds/Game1.ogg");
		_gameSystem = new GameSystem(container, container.getWidth(), container.getHeight());
		_gameSystem.pause();

	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		this.paused = false;
		layout(_feng.getDisplay());
		_gameSystem.unpause();
		
		_gameTheme.loop(1, .75f);
		if (_themePosition != -1)
			_gameTheme.setPosition(_themePosition);

		baseDestroyedListener = new EventListener() {
			
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
		EventManager.inst().registerForAll(EventType.BASE_DESTROYED, baseDestroyedListener);
		emitterListener = new EventListener() {
			
			@Override
			public void onEvent(Event e)
			{
				ConfigurableEmitter ce = (ConfigurableEmitter) e.getValue("configurableEmitter");
				particleSystem.addEmitter(ce);
				if (particleSystem.getEmitterCount() % EMITTER_REFRESH_COUNT == 0)
				{
					particleSystem.setRemoveCompletedEmitters(true);
				}
				else
				{
					particleSystem.setRemoveCompletedEmitters(false);
				}
			}
		};
		EventManager.inst().registerForAll(EventType.START_PARTICLE_EFFECT, emitterListener);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		_feng.getDisplay().removeAllWidgets();
		_gameSystem.pause();
		_themePosition = _gameTheme.getPosition();
		_gameTheme.stop();
		particleSystem.removeAllEmitters();
		// particleSystem.reset();
		// particleSystem = particleSystem.duplicate();
	}
	
	public void start()
	{
		_gameSystem.start();
		BB.inst().setDisplay(_feng.getDisplay());
		particleSystem.removeAllEmitters();
		_themePosition = 0;
	}
	
	public void end()
	{
		_gameSystem.end();
		_themePosition = -1;
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
		
		try
		{
			particleSystem.render();
		}
		catch (Exception e)
		{
			System.out.println("Warning: particle system rendering");
			e.printStackTrace(System.err);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int millis) throws SlickException
	{
		_gameSystem.update(millis);
		
		if (this.paused)
		{
			particleSystem.removeAllEmitters();
			game.enterState(Main.States.PauseState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
		else
		{
			particleSystem.update(millis);
		}
		if (winner == Winner.player1)
		{
			winner = Winner.nobody;
			EventManager.inst().unregisterForAll(EventType.BASE_DESTROYED, baseDestroyedListener);
			EventManager.inst().unregisterForAll(EventType.START_PARTICLE_EFFECT, baseDestroyedListener);
			end();
			game.enterState(Main.States.WinState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
		else if (winner == Winner.player2)
		{
			winner = Winner.nobody;
			EventManager.inst().unregisterForAll(EventType.BASE_DESTROYED, baseDestroyedListener);
			EventManager.inst().unregisterForAll(EventType.START_PARTICLE_EFFECT, baseDestroyedListener);
			end();
			game.enterState(Main.States.LoseState.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}

		for (int i = 0; i < 5; i++)
		{
			if (_towerButtons[i].getType().price > BB.inst().getUsersPlayer().getGold())
			{
				_towerButtons[i].setEnabled(false);
			}
			else
			{
				_towerButtons[i].setVisible(true);
			}
		}

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				if (_mobButtons[i][j].getPrice() > BB.inst().getUsersPlayer().getGold())
				{
					_mobButtons[i][j].setEnabled(false);
				}
				else
				{
					_mobButtons[i][j].setEnabled(true);
				}
			}
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
		MobObject.Type type = null;
		int level = 0;
		
		switch (key)
		{
			case Input.KEY_P:
				_gameSystem.pause();
				this.paused = true;
				break;
			case Input.KEY_1:
				type = MobObject.Type.chinese;
				level = 1;
				break;
			case Input.KEY_2:
				type = MobObject.Type.chinese;
				level = 2;
				break;
			case Input.KEY_3:
				type = MobObject.Type.chinese;
				level = 3;
				break;
			case Input.KEY_4:
				type = MobObject.Type.chinese;
				level = 4;
				break;
			case Input.KEY_5:
				type = MobObject.Type.chinese;
				level = 5;
				break;
			case Input.KEY_Q:
				type = MobObject.Type.latin;
				level = 1;
				break;
			case Input.KEY_W:
				type = MobObject.Type.latin;
				level = 2;
				break;
			case Input.KEY_E:
				type = MobObject.Type.latin;
				level = 3;
				break;
			case Input.KEY_R:
				type = MobObject.Type.latin;
				level = 4;
				break;
			case Input.KEY_T:
				type = MobObject.Type.latin;
				level = 5;
				break;
			case Input.KEY_A:
				type = MobObject.Type.greek;
				level = 1;
				break;
			case Input.KEY_S:
				type = MobObject.Type.greek;
				level = 2;
				break;
			case Input.KEY_D:
				type = MobObject.Type.greek;
				level = 3;
				break;
			case Input.KEY_F:
				type = MobObject.Type.greek;
				level = 4;
				break;
			case Input.KEY_G:
				type = MobObject.Type.greek;
				level = 5;
				break;
			case Input.KEY_Z:
				type = MobObject.Type.cyrillic;
				level = 1;
				break;
			case Input.KEY_X:
				type = MobObject.Type.cyrillic;
				level = 2;
				break;
			case Input.KEY_C:
				type = MobObject.Type.cyrillic;
				level = 3;
				break;
			case Input.KEY_V:
				type = MobObject.Type.cyrillic;
				level = 4;
				break;
			case Input.KEY_B:
				type = MobObject.Type.cyrillic;
				level = 5;
				break;
			default:
				BB.inst().keyReleased(key);
				break;
		}
		
		if (type != null)
		{
			BB.inst().spawnUsersMob(type, level);
		}
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
		else
		{
			display.addWidget(mobInterface);
			display.addWidget(towerInterface);
		}
	}
	
	private void layoutTowerButtons(Display display)
	{
		final String[][] text = new String[5][2];
		final TowerBase.Type types[] = new TowerBase.Type[5];
		text[0][0] = "Dice";
		text[1][0] = "Chess Pieces";
		text[2][0] = "Card Suits";
		text[3][0] = "Musical Notes";
		text[4][0] = "Currency";
		text[0][1] = "Dice Tower\nDamage: ??\nSpeed: ??\nRange: ??\nPrice: " + DiceOne.price;
		text[1][1] = "Chess Piece\nDamage: " + ChessOne.BASE_ATTACK + "\nSpeed: " + ChessOne.BASE_SPEED + "\nRange: " + ChessOne.BASE_RADIUS + "\nPrice: " + ChessOne.price;
		text[2][1] = "Card Suit\nDamage: " + CardOne.BASE_ATTACK + "\nSpeed: " + CardOne.BASE_SPEED + "\nRange: " + CardOne.BASE_RADIUS + "\nPrice: " + CardOne.price;
		text[3][1] = "Musical Note\nDamage: " + MusicOne.BASE_ATTACK + "\nSpeed: " + MusicOne.BASE_SPEED + "\nRange: " + MusicOne.BASE_RADIUS + "\nPrice: " + MusicOne.price;
		text[4][1] = "Currency\nDamage: " + CurrencyOne.BASE_ATTACK + "\nSpeed: " + CurrencyOne.BASE_SPEED + "\nRange: " + CurrencyOne.BASE_RADIUS + "\nPrice: " + CurrencyOne.price;
		types[0] = TowerBase.Type.diceOne;
		types[1] = TowerBase.Type.chessOne;
		types[2] = TowerBase.Type.cardOne;
		types[3] = TowerBase.Type.musicOne;
		types[4] = TowerBase.Type.currencyOne;
		
		towerInterface.setPosition(new Point(640, 0));
		towerInterface.setHeight(256);
		towerInterface.setWidth(384);

		for (int i = 0; i < 5; i++)
		{
			Container buttonContainer = new Container(new GridLayout(1, 1));
			_towerButtons[i] = FengGUI.createWidget(TowerButton.class);
			_towerButtons[i].init(text[i][0] + "\n$" + types[i].price, text[i][1], 128, 128, types[i]);
			_towerButtons[i].setPosition(new Point(0, 0));
			_towerButtons[i].addButtonPressedListener(_towerButtons[i]);
			buttonContainer.addWidget(_towerButtons[i]);
			towerInterface.addWidget(buttonContainer);
		}
	}
	
	private void layoutMobButtons(Display display)
	{
		String text[] = new String[4];
		MobObject.Type type[] = new MobObject.Type[4];
		text[0] = "Chn";
		text[1] = "Lat";
		text[2] = "Grk";
		text[3] = "Cyr";
		type[0] = MobObject.Type.chinese;
		type[1] = MobObject.Type.latin;
		type[2] = MobObject.Type.greek;
		type[3] = MobObject.Type.cyrillic;
		
		mobInterface.setPosition(new Point(0, 0));
		mobInterface.setHeight(64);
		mobInterface.setWidth(320);

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 5; j++)
			{
				String label2 = "HP: " + MobObject.getMobTotalHp(MobObject.Type.values()[i], j + 1) + "\nDef: " + MobObject.getMobDefense(MobObject.Type.values()[i], j + 1) + "\nAtk: " + MobObject.getMobAttack(MobObject.Type.values()[i], j + 1)
						+ "\nSpd: " + MobObject.getMobSpeed(MobObject.Type.values()[i], j + 1);
				Container buttonContainer = new Container(new GridLayout(1, 1));
				_mobButtons[i][j] = FengGUI.createWidget(MobButton.class);
				_mobButtons[i][j].setSize(64, 64);
				_mobButtons[i][j].setMultiline(true);
				_mobButtons[i][j].setShrinkable(false);
				_mobButtons[i][j].setPosition(new Point(j * 64, i * 64));
				_mobButtons[i][j].init(text[i] + " " + (j + 1) + "\n$" + MobObject.getMobPrice(type[i], j + 1), label2, 64, 64, type[i], (j + 1));
				_mobButtons[i][j].addButtonPressedListener(_mobButtons[i][j]);
				buttonContainer.addWidget(_mobButtons[i][j]);
				mobInterface.addWidget(buttonContainer);
			}
	}
}
