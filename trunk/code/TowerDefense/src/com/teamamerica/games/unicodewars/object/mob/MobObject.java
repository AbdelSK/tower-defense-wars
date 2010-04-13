package com.teamamerica.games.unicodewars.object.mob;

import java.io.File;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.util.pathfinding.Mover;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public abstract class MobObject extends GameObject implements Mover
{
	public static enum Type
	{
		chinese, latin, greek, cyrillic;
	}

	int attack = 0;
	int defense = 0;
	int speed = 0;
	int vitality = 0;
	int currentHP = 0;
	int totalHP = 0;
	int level = 0;
	int price = 0;
	int refund = 0;
	Type type;
	String imagePath;

	public MobObject(String name, int id, int renderPriority, Location loc, Team side, int level, Type type, String imgLoc)
	{
		super(name, id, side, renderPriority);
		
		setPosition(loc);
		this._size = 1;
		
		this.level = level;
		this.type = type;
		this.price = 20 * level;
		this.refund = this.price / 2;
		this.imagePath = imgLoc;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getCurrentHP()
	{
		return currentHP;
	}
	
	public int getTotalHP()
	{
		return totalHP;
	}
	
	public int getDefense()
	{
		return defense;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public int getVitality()
	{
		return vitality;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public String getImagePath()
	{
		return imagePath;
	}

	public int getRefundAmount()
	{
		return refund;
	}

	public boolean isAlive()
	{
		return currentHP > 0;
	}
	
	public boolean adjustHealth(int amount)
	{
		currentHP += amount;
		if (currentHP <= 0)
			return false;
		else
			return true;
	}
	
	// returns in format [Defense, Speed, Attack, Total HP, Price]
	public static int[] getStats(Type type, int level)
	{
		int[] stats = new int[5];
		
		switch (type)
		{
			case chinese:
				stats[0] = 4 * level;
				stats[1] = 1 * level;
				stats[2] = 1 * level;
				stats[3] = 2 * level * 20;
				break;
			case latin:
				stats[0] = 1 * level;
				stats[1] = 4 * level;
				stats[2] = 2 * level;
				stats[3] = 1 * level * 20;
				break;
			case greek:
				stats[0] = 2 * level;
				stats[1] = 1 * level;
				stats[2] = 4 * level;
				stats[3] = 1 * level * 20;
				break;
			case cyrillic:
				stats[0] = 1 * level;
				stats[1] = 2 * level;
				stats[2] = 1 * level;
				stats[3] = 4 * level * 20;
				break;
			default:
				stats[0] = 0;
				stats[1] = 0;
				stats[2] = 0;
				stats[3] = 0;
				break;
		}
		stats[4] = 20 * level;
		return stats;
	}

	@Override
	public void deleteObject()
	{
		super.deleteObject();
	}
	
	public void die()
	{
		ConfigurableEmitter emitter = null;
		try
		{
			File xmlFile = new File("src/data/effects/explode.xml");
			emitter = ParticleIO.loadEmitter(xmlFile);
		}
		catch (Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		emitter.setPosition(this._positionInPixels.x + 10, this._positionInPixels.y + 8);
		emitter.setImageName(imagePath);
		Event event = new Event(EventType.START_PARTICLE_EFFECT);
		event.addParameter("configurableEmitter", emitter);
		EventManager.inst().dispatch(event);
		deleteObject();
	}
}
