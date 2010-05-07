package com.teamamerica.games.unicodewars.object.base;

import java.util.ArrayList;
import java.util.Collection;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
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
	public static final int totalHealth = 200;
	private int health;
	private ArrayList<Location> locations;
	private Sound hitSound;
	
	public BaseObject(String name, int id, Team team, int renderPriority, Location loc)
	{
		super(name, id, team, renderPriority, loc);
		this._size = size;
		this.health = totalHealth;
		locations = new ArrayList<Location>();
		for (int x = this.getPosition().x; x < this.getPosition().x + size; ++x)
		{
			for (int y = this.getPosition().y; y < this.getPosition().y + size; ++y)
			{
				locations.add(new Location(x, y));
			}
		}
		
		try
		{
			this.hitSound = new Sound("data/sounds/eating_short.wav");
		}
		catch (SlickException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public float getHealthPercent()
	{
		return ((float) health) / ((float) totalHealth);
	}

	private void hit(GameObject obj)
	{
		if (obj instanceof MobObject)
		{
			this.hitSound.play();
			MobObject temp = (MobObject) obj;
			this.health -= temp.getAttack();
			temp.deleteObject();
			if (this.health <= 0)
			{
				this.health = 0;
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

		Collection<GameObject> temp = BB.inst().getTeamObjectsAtLocations(opponent, locations);
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
