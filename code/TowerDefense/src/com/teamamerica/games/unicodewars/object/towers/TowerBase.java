package com.teamamerica.games.unicodewars.object.towers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.Input;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
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
	ConfigurableEmitter emitter[] = null;
	private ParticleSystem part_sys = null;
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

		this.boardX = GameMap.inst().getLocationInPixels(loc).x;
		this.boardY = GameMap.inst().getLocationInPixels(loc).y;
		this.price = price;
		this.type = type;
		this._size = size;
		this.imagePath = imgLoc;
		setPosition(loc);
		stopWatch = BB.inst().getNewTimer();
		listeners = new HashMap<Location, EventListener>();
		attackMap = new HashMap<Location, ArrayList<MobObject>>();
		sortedLocs = new ArrayList<Location>();
		
		this.radius = radius;
		this.attack = attack;
		this.speed = speed;
		
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
			if (emitter == null)
			{
				try
				{
					File xml = new File("src/data/effects/blue_beam.xml");
					this.part_sys = ParticleIO.loadConfiguredSystem(xml);
					emitter = new ConfigurableEmitter[this.part_sys.getEmitterCount()];
				}
				catch (IOException e)
				{
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
					System.exit(0);
				}
				
				for (int i = 0; i < this.part_sys.getEmitterCount(); i++)
				{
					emitter[i] = (ConfigurableEmitter) this.part_sys.getEmitter(i);
					emitter[i].setPosition(this.boardX, this.boardY);
					System.out.println("setting emitter " + i + " position to " + this.boardX + "," + this.boardY);
					Event event = new Event(EventType.START_PARTICLE_EFFECT);
					event.addParameter("configurableEmitter", emitter[i]);
					// EventManager.inst().dispatch(event);
				}
			}

			if (!mob.adjustHealth(-this.attack))
			{
				attackMap.get(loc).remove(mob);
				
				if (mob.getTeam() == Team.Player2)
				{
					BB.inst().getUsersPlayer().addGold(2 * mob.getLevel());
				}
				// for (int i = 0; i < emitter.length; i++)
				// {
				// emitter[i].reset();
				// emitter[i].completed();
				// emitter[i].wrapUp();
				// emitter[i] = null;
				// }
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
	
	public int getLevel()
	{
		return this.level;
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
	}
	
	public void sellTower()
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
		BB.inst().getUsersPlayer().addGold(this.getSellPrice());
		this.deleteObject();
	}

	private void handleMobInRange(GameObject obj, Location loc)
	{
		ArrayList<MobObject> inRange = attackMap.get(loc);
		inRange.add((MobObject) obj);
		
		// System.out.println("Mob " + obj.getId() +
		// " is in range. Mobs in Range: " + inRange.size());
	}
	
	private void handleMobLeavingRange(GameObject obj, Location loc)
	{
		if (enemy != null && obj.getId() == enemy.getId())
		{
			enemy = null;
		}
		
		ArrayList<MobObject> inRange = attackMap.get(loc);
		inRange.remove((MobObject) obj);
		
		// System.out.println("Mob " + obj.getId() +
		// " is leaving range. Mobs in Range: " + inRange.size());
	}
	
	@Override
	public void deleteObject()
	{
		super.deleteObject();
		
		if (BB.inst().getHUD() == this)
			BB.inst().setHUD(null);

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
