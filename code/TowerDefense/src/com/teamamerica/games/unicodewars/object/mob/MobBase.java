package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;

public abstract class MobBase extends MobObject implements Mob
{
	int attack = 0;
	int defense = 0;
	int speed = 0;
	int vitality = 0;
	int currentHP = 0;
	int totalHP = 0;
	int level = 0;
	int price = 0;
	Location loc;
	MobFactory.type type;
	
	public MobBase(MobFactory.type type, int level)
	{
		super("Mob", BB.inst().getNextId(), 10);
		
		this.level = level;
		this.type = type;
		this.price = 5 * level;
		
		switch (this.type)
		{
			case chinese:
				this.defense = 4 * this.level;
				this.attack = this.speed = this.vitality = 3 * this.level;
			case latin:
				this.speed = 4 * this.level;
				this.defense = this.attack = this.vitality = 3 * this.level;
			case greek:
				this.attack = 4 * this.level;
				this.defense = this.speed = this.vitality = 3 * this.level;
			case cyrillic:
				this.vitality = 4 * this.level;
				this.defense = this.speed = this.attack = 3 * this.level;
		}
		this.currentHP = this.totalHP = this.vitality * 20;
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
	
	public MobFactory.type getType()
	{
		return type;
	}
	
	public boolean isAlive()
	{
		return currentHP > 0;
	}
	
	public Location getLocation()
	{
		return loc;
	}
}
