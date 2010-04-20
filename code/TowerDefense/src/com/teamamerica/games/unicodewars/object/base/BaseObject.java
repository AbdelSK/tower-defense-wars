package com.teamamerica.games.unicodewars.object.base;

import java.util.ArrayList;
import java.util.List;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BaseObject extends GameObject
{
	public static final short size = 4;
	private int health;
	private ArrayList<Location> locations;
	
	public BaseObject(String name, int id, Team team, int renderPriority, Location loc)
	{
		super(name, id, team, renderPriority, loc);
		this._size = size;
		this.health = 200;
		locations = new ArrayList<Location>();
		for (int x = this.getPosition().x; x < this.getPosition().x + size; ++x)
		{
			for (int y = this.getPosition().y; y < this.getPosition().y + size; ++y)
			{
				locations.add(new Location(x, y));
			}
		}
	}
	
	public int getHealth()
	{
		return health;
	}

	private void hit(GameObject obj)
	{
		if (obj instanceof MobObject)
		{
			MobObject temp = (MobObject) obj;
			this.health -= temp.getAttack();
			temp.deleteObject();
			if (this.health <= 0)
			{
				Event e = new Event(EventType.BASE_DESTROYED);
				e.addParameter("teamDestroyed", this.getTeam());
				EventManager.inst().dispatch(e);
			}
		}
	}
	
	@Override
	public void update(int elapsed)
	{
		Team opponent = Team.Player1;
		switch (this._team)
		{
			case Player1:
				opponent = Team.Player2;
				break;
			case Player2:
				opponent = Team.Player1;
				break;
		}

		List<GameObject> temp = BB.inst().getTeamObjectsAtLocations(opponent, locations);
		for (GameObject obj : temp)
		{
			hit(obj);
		}
	}
	
	@Override
	public void deleteObject()
	{
		super.deleteObject();
	}

}
