package com.teamamerica.games.unicodewars.object.towers;

import java.util.HashMap;
import java.util.Random;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public abstract class TowerBase extends GameObject
{
	public static enum Type
	{
		diceOne(DiceOne.price), ;
		
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
	private HashMap<Location, EventListener> listeners;
	private GameObject enemy = null;
	
	public TowerBase(Type type, int attack, int price, int radius, int speed, Team team, Location loc)
	{
		super("Tower", BB.inst().getNextId(), team, 100);

		this.price = price;
		this.type = type;
		this._size = size;
		this._position = loc;
		Random gen = BB.inst().getRandom();
		listeners = new HashMap<Location, EventListener>();
		
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
	}
	
	@Override
	public void update(int elapsed)
	{
		
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
		// TODO Auto-generated method stub
		
	}

	public String getInfoString()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getStatusString()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void registerTower()
	{
		// TODO Auto-generated method stub
		for (int x = this._position.x; x < this._position.x + this._size; x++)
		{
			for (int y = this._position.y; y < this._position.y + this._size; y++)
			{
				EventListener temp = new EventListener() {
					
					@Override
					public void onEvent(Event e)
					{
						// TODO Auto-generated method stub
						switch (e.getId())
						{
							case ENTER_SPACE:
								handleMobInRange(e.sender);
								break;
							case LEAVE_SPACE:
								handleMobLeavingRange(e.sender);
								break;
						}

					}
				};
				Location loc = new Location(x, y);
				GameMap.inst().registerSpace(loc, temp);
				listeners.put(loc, temp);
			}
		}
	}
	
	private void handleMobInRange(GameObject obj)
	{
		
	}
	
	private void handleMobLeavingRange(GameObject obj)
	{
		if (enemy != null && obj.getId() == enemy.getId())
		{
			enemy = null;
		}
	}
	
	@Override
	public void deleteObject()
	{
		super.deleteObject();
		for (Location key : listeners.keySet())
		{
			GameMap.inst().unregisterSpace(key, listeners.get(key));
		}
		GameMap.inst().removeTower(this);
	}

}
