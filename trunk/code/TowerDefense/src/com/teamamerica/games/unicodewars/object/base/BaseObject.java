package com.teamamerica.games.unicodewars.object.base;

import java.util.ArrayList;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BaseObject extends GameObject
{
	public static final short size = 4;
	private ArrayList<EventListener> listeners;
	private int health;
	private boolean registered;
	
	public BaseObject(String name, int id, Team team, int renderPriority, Location loc)
	{
		super(name, id, team, renderPriority);
		// TODO Auto-generated constructor stub
		this._position = loc;
		this._size = size;
		this.health = 10000;
		this.registered = false;
		this.listeners = new ArrayList<EventListener>();
		RegisterMapListeners();
	}
	
	private void RegisterMapListeners()
	{
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
								hit(e.sender);
								break;
						}

					}
				};
				GameMap.inst().registerSpace(new Location(x, y), temp);
				listeners.add(temp);
			}
		}
	}
	
	private void hit(GameObject obj)
	{
		if (obj instanceof MobObject)
		{
			MobObject temp = (MobObject) obj;
			this.health -= temp.getAttack();
			temp.deleteObject();
		}
	}

}