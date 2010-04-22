package com.teamamerica.games.unicodewars.object.towers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
	
	Type type;

	int level = 1;
	int radius = 0;
	int attack = 0;
	int speed = 0;
	int price = 0;
	private Timer stopWatch;
	private HashSet<Location> locsInRange;
	private String imagePath;
	private MobObject target = null;
	
	public TowerBase(Type type, int attack, int price, int radius, int speed, Team team, Location loc, String imgLoc)
	{
		super("Tower", BB.inst().getNextId(), team, 100, loc);

		this.price = price;
		this.type = type;
		this._size = size;
		this.imagePath = imgLoc;
		stopWatch = BB.inst().getNewTimer();
		locsInRange = new HashSet<Location>();
		
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
		if (team == Team.Player1)
			BB.inst().getUsersPlayer().purchase(this.price);
		System.out.println(this.type + "," + loc.x + "," + loc.y);
	}
	
	@Override
	public void update(int elapsed)
	{
		if (stopWatch.xMilisecondsPassed(2000 / this.speed))
		{
			MobObject toAttack = null;
			Location attackLoc = null;
			
			if (target != null)
			{
				if (!locsInRange.contains(target.getPosition()) || !target.isAlive())
					target = null;
			}

			if (target == null)
			{
				
				Team attacking;
				switch (this.getTeam())
				{
					case Player1:
						attacking = Team.Player2;
						break;
					case Player2:
						attacking = Team.Player1;
						break;
					default:
						attacking = Team.Player2;
				}
				
				ArrayList<Location> temp = new ArrayList<Location>(locsInRange);
				List<GameObject> objs = new ArrayList<GameObject>(BB.inst().getTeamObjectsAtLocations(attacking, temp));
				
				Collections.sort(objs, GameObject.birthTime);

				for (GameObject obj : objs)
				{
					if (obj instanceof MobObject)
					{
						MobObject mob = (MobObject) obj;
						toAttack = mob;
						attackLoc = mob.getPosition();
						break;
					}
				}
			}
			else
			{
				toAttack = target;
				attackLoc = target.getPosition();
			}
			
			this.attack(attackLoc, toAttack);
		}
	}
	
	public void attack(Location loc, MobObject mob)
	{
		target = mob;
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
					emitter[i].setPosition(this.getPositionInPixels().x, this.getPositionInPixels().y);
					System.out.println("setting emitter " + i + " position to " + this.getPositionInPixels().x + "," + this.getPositionInPixels().x);
					Event event = new Event(EventType.START_PARTICLE_EFFECT);
					event.addParameter("configurableEmitter", emitter[i]);
					// EventManager.inst().dispatch(event);
				}
			}
			if (!mob.adjustHealth(-this.attack))
			{
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
				target = null;
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
		Event buildEvent = new Event(buildType, this.getPosition(), this);
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
	
	private void registerTower()
	{
		registerNewSpaces();
		
		for (int x = this.getPosition().x; x < this.getPosition().x + this._size; x++)
		{
			for (int y = this.getPosition().y; y < this.getPosition().y + this._size; y++)
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
		for (int x = this.getPosition().x; x < this.getPosition().x + this._size; x++)
		{
			for (int y = this.getPosition().y; y < this.getPosition().y + this._size; y++)
			{
				Location loc = new Location(x, y);
				locsInRange.addAll(loc.getLocsWithinDistance(this.radius));
			}
		}
		ArrayList<Location> retain = new ArrayList<Location>();
		for (Location loc : locsInRange)
		{
			if (loc.x >= 0 && loc.x < GameMap.inst().columns && loc.y >= 0 && loc.y < GameMap.inst().rows && (GameMap.inst().getTilesTeam(loc) == getTeam()))
				retain.add(loc);
		}
		locsInRange.retainAll(retain);
	}
	
	public List<Location> getLocationsInRange()
	{
		return new ArrayList<Location>(locsInRange);
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
	
	@Override
	public void deleteObject()
	{
		if (BB.inst().getHUD() == this)
			BB.inst().setHUD(null);

		for (int x = this.getPosition().x; x < this.getPosition().x + this._size; x++)
		{
			for (int y = this.getPosition().y; y < this.getPosition().y + this._size; y++)
			{
				BB.inst().removeMouseListenerForLocation(new Location(x, y));
			}
		}
		GameMap.inst().removeTower(this);
		super.deleteObject();
	}
}
