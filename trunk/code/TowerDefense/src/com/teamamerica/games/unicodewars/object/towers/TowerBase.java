package com.teamamerica.games.unicodewars.object.towers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import org.fenggui.Button;
import org.fenggui.FengGUI;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.util.Point;
import org.newdawn.slick.Input;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.MouseListener;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Timer;

public abstract class TowerBase extends GameObject
{
	public static enum Type
	{
		diceOne(DiceOne.price), chessOne(ChessOne.price), cardOne(CardOne.price), musicOne(MusicOne.price), currencyOne(CurrencyOne.price);
		
		public final int price;
		
		Type(int price)
		{
			this.price = price;
		}
	}
	
	public static final short size = 2;

	int boardX;
	int boardY;
	
	Type type;

	int level = 1;
	int radius = 0;
	int attack = 0;
	int speed = 0;
	int price = 0;
	private Timer stopWatch;
	private HashMap<Location, EventListener> listeners;
	private GameObject enemy = null;
	private ArrayList<Location> sortedLocs;
	private String imagePath;
	
	private HashMap<Location, ArrayList<MobObject>> attackMap;

	public TowerBase(Type type, int attack, int price, int radius, int speed, Team team, Location loc, String imgLoc)
	{
		super("Tower", BB.inst().getNextId(), team, 100);

		this.price = price;
		this.type = type;
		this._size = size;
		this.imagePath = imgLoc;
		setPosition(loc);
		Random gen = BB.inst().getRandom();
		stopWatch = BB.inst().getNewTimer();
		listeners = new HashMap<Location, EventListener>();
		attackMap = new HashMap<Location, ArrayList<MobObject>>();
		sortedLocs = new ArrayList<Location>();
		
		if (type.equals(Type.diceOne))
		{
			this.attack = gen.nextInt(7);
			if (this.attack == 0)
			{
				this.attack = 1;
			}
			this.radius = gen.nextInt(7 - this.attack);
			if (this.radius == 0)
			{
				this.radius = 1;
			}
			this.speed = 7 - (this.attack + this.radius);
			if (this.speed == 0)
			{
				this.speed = 1;
			}
		}
		else
		{
			this.radius = radius;
			this.attack = attack;
			this.speed = speed;
		}
		
		EventType buildType;
		switch (team)
		{
			case Player1:
				buildType = EventType.P1_TOWER_BUILT;
				break;
			case Player2:
				buildType = EventType.P2_TOWER_BUILT;
				break;
			default:
				buildType = EventType.P2_TOWER_BUILT;
		}
		Event buildEvent = new Event(buildType, loc, this);
		EventManager.inst().dispatch(buildEvent);
		registerTower();
		System.out.println(loc.x + "," + loc.y);
	}
	
	@Override
	public void update(int elapsed)
	{
		if (stopWatch.xMilisecondsPassed(2000 / this.speed))
		{
			
			MobObject toAttack = null;
			Location attackLoc = null;

			for (Location loc : sortedLocs)
			{
				if (attackMap.get(loc).size() > 0)
				{
					toAttack = attackMap.get(loc).get(0);
					attackLoc = loc;
					for (MobObject mob : attackMap.get(loc))
					{
						if (mob.getCurrentHP() < toAttack.getCurrentHP())
						{
							toAttack = mob;
						}
					}
					break;
				}
			}

			this.attack(attackLoc, toAttack);
		}
	}
	
	public void attack(Location loc, MobObject mob)
	{
		if (mob != null)
		{
			if (!mob.adjustHealth(-this.attack))
			{
				attackMap.get(loc).remove(mob);
				
				// need to add this once AI is implemented
				// if (mob.getTeam() == Team.Player2)
				BB.inst().getPlayer().addGold(2 * mob.getLevel());

				mob.die();
			}
		}
	}

	public Type getType()
	{
		return type;
	}

	public int getRadius()
	{
		return radius;
	}

	public int getPrice()
	{
		return price;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public int getUpgradePrice()
	{
		return Math.round(.5f * price);
	}
	
	public int getSellPrice()
	{
		return Math.round(.75f * price);
	}
	
	public boolean canUpgrade()
	{
		return level < 4;
	}

	public void doUpgrade()
	{
		this.registerNewSpaces();
		EventType buildType;
		switch (this._team)
		{
			case Player1:
				buildType = EventType.P1_TOWER_BUILT;
				GameMap.inst().updateDefaultMobPath(Team.Player2);
				break;
			case Player2:
				buildType = EventType.P2_TOWER_BUILT;
				GameMap.inst().updateDefaultMobPath(Team.Player1);
				break;
			default:
				buildType = EventType.P2_TOWER_BUILT;
		}
		Event buildEvent = new Event(buildType, this._position, this);
		EventManager.inst().dispatch(buildEvent);
		
		price += this.getUpgradePrice();
	}

	public String getInfoString()
	{
		return null;
	}
	
	public String getImagePath()
	{
		return imagePath;
	}

	public String getStatusString()
	{

		return null;
	}
	
	protected void registerTower()
	{
		for (int x = this._position.x - this.radius; x < this._position.x + this._size + this.radius; x++)
		{
			for (int y = this._position.y - this.radius; y < this._position.y + this._size + this.radius; y++)
			{
				EventListener temp = new EventListener() {
					
					@Override
					public void onEvent(Event e)
					{

						switch (e.getId())
						{
							case ENTER_SPACE:
								handleMobInRange(e.sender, e.getLocation());
								break;
							case LEAVE_SPACE:
								handleMobLeavingRange(e.sender, e.getLocation());
								break;
						}

					}
				};
				Location loc = new Location(x, y);
				GameMap.inst().registerSpace(this, loc, temp);
				listeners.put(loc, temp);
				attackMap.put(loc, new ArrayList<MobObject>());
				sortedLocs.add(loc);
				Collections.sort(sortedLocs);
			}
		}
		
		for (int x = this._position.x; x < this._position.x + this._size; x++)
		{
			for (int y = this._position.y; y < this._position.y + this._size; y++)
			{
				MouseListener temp = new MouseListener() {
					
					@Override
					public void MouseReleased(int button, int x, int y)
					{
						switch (button)
						{
							case Input.MOUSE_LEFT_BUTTON:
								handleTowerClick();
								break;
							case Input.MOUSE_RIGHT_BUTTON:
								sellTower();

						}
					}
					
					@Override
					public void MouseClicked(int button, int x, int y)
					{
						
					}
				};
				
				BB.inst().addMouseListenerForLocation(temp, new Location(x, y));
			}
		}
	}
	
	protected void registerNewSpaces()
	{
		for (int x = this._position.x - this.radius; x < this._position.x + this._size + this.radius; x++)
		{
			for (int y = this._position.y - this.radius; y < this._position.y + this._size + this.radius; y++)
			{
				Location loc = new Location(x, y);
				if (this.listeners.get(loc) == null)
				{
					EventListener temp = new EventListener() {
						
						@Override
						public void onEvent(Event e)
						{
							
							switch (e.getId())
							{
								case ENTER_SPACE:
									handleMobInRange(e.sender, e.getLocation());
									break;
								case LEAVE_SPACE:
									handleMobLeavingRange(e.sender, e.getLocation());
									break;
							}
							
						}
					};
					
					GameMap.inst().registerSpace(this, loc, temp);
					listeners.put(loc, temp);
					attackMap.put(loc, new ArrayList<MobObject>());
					sortedLocs.add(loc);
					Collections.sort(sortedLocs);
				}
			}
		}
	}

	private void handleTowerClick()
	{
		BB.inst().setHUD(this);
		GameMap.inst().setSelectedTower(this);
		Button buttons[] = new Button[3];
		
		for (int i = 0; i < 3; i++)
		{
			buttons[i] = FengGUI.createWidget(Button.class);
			buttons[i].setShrinkable(false);
			buttons[i].setMultiline(true);
			buttons[i].setSize(256, 64);
		}
		
		if (BB.inst().getHUD().canUpgrade())
		{
			buttons[0].setPosition(new Point(382, 0));
			buttons[0].setText("Upgrade " + this.type + " to level " + (this.level + 1) + "\nCost: " + this.getUpgradePrice());
			buttons[0].addButtonPressedListener(new IButtonPressedListener() {
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					if (BB.inst().getHUD().canUpgrade())
						BB.inst().getHUD().doUpgrade();
					else
						BB.inst().getCurrentHUD()[0].setEnabled(false);
					System.out.println("Upgraded " + BB.inst().getHUD().type + "(" + BB.inst().getHUD()._id + ") to level " + BB.inst().getHUD().level);
					BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[0]);
					BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[1]);
					BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[2]);
					BB.inst().setCurrentHUD(null);
					GameMap.inst().clearSelectedTower();
				}
			});
		}

		buttons[1].setPosition(new Point(382, 64));
		buttons[1].setText("Sell " + this.type + " for " + this.getSellPrice() + "g.");
		buttons[1].addButtonPressedListener(new IButtonPressedListener() {
			public void buttonPressed(ButtonPressedEvent arg0)
			{
				System.out.println("Sold " + BB.inst().getHUD().type + "(" + BB.inst().getHUD()._id + ")");
				BB.inst().getHUD().sellTower();
				BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[0]);
				BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[1]);
				BB.inst().getDisplay().removeWidget(BB.inst().getCurrentHUD()[2]);
				BB.inst().setCurrentHUD(null);
				GameMap.inst().clearSelectedTower();
			}
		});
		
		buttons[2].setPosition(new Point(382, 128));
		buttons[2].setSize(128, 96);
		buttons[2].setEnabled(false);
		buttons[2].setText(BB.inst().getHUD().type + " Level " + BB.inst().getHUD().level + "\nAttack: " + BB.inst().getHUD().attack + "\nRange: " + BB.inst().getHUD().radius + "\nSpeed: " + BB.inst().getHUD().speed);

		BB.inst().setCurrentHUD(buttons);

		if (BB.inst().getHUD().canUpgrade())
			BB.inst().getDisplay().addWidget(buttons[0]);

		BB.inst().getDisplay().addWidget(buttons[1]);
		BB.inst().getDisplay().addWidget(buttons[2]);
	}
	
	private void sellTower()
	{
		EventType sellType;
		switch (this._team)
		{
			case Player1:
				sellType = EventType.P1_TOWER_SOLD;
				break;
			case Player2:
				sellType = EventType.P2_TOWER_SOLD;
				break;
			default:
				sellType = EventType.P2_TOWER_SOLD;
				break;
		}
		Event sellEvent = new Event(sellType);
		EventManager.inst().dispatch(sellEvent);
		this.deleteObject();
	}

	private void handleMobInRange(GameObject obj, Location loc)
	{
		ArrayList<MobObject> inRange = attackMap.get(loc);
		inRange.add((MobObject) obj);
		
		System.out.println("Mob " + obj.getId() + " is in range. Mobs in Range: " + inRange.size());
	}
	
	private void handleMobLeavingRange(GameObject obj, Location loc)
	{
		if (enemy != null && obj.getId() == enemy.getId())
		{
			enemy = null;
		}
		
		ArrayList<MobObject> inRange = attackMap.get(loc);
		inRange.remove((MobObject) obj);
		
		System.out.println("Mob " + obj.getId() + " is leaving range. Mobs in Range: " + inRange.size());
	}
	
	@Override
	public void deleteObject()
	{
		super.deleteObject();
		for (Location key : listeners.keySet())
		{
			GameMap.inst().unregisterSpace(this, key, listeners.get(key));
		}
		for (int x = this._position.x; x < this._position.x + this._size; x++)
		{
			for (int y = this._position.y; y < this._position.y + this._size; y++)
			{
				BB.inst().removeMouseListenerForLocation(new Location(x, y));
			}
		}
		GameMap.inst().removeTower(this);
	}

}
