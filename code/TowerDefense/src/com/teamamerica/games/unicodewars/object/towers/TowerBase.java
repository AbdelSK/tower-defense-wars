package com.teamamerica.games.unicodewars.object.towers;

import java.util.Random;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Team;

public abstract class TowerBase extends GameObject implements Tower
{
	int boardX;
	int boardY;
	
	TowerFactory.type type;

	int level = 1;
	int radius = 0;
	int attack = 0;
	int speed = 0;
	int price = 0;
	
	public TowerBase(TowerFactory.type type, int attack, int price, int radius, int speed)
	{
		super("Tower", BB.inst().getNextId(), Team.Player1, 100); // We'll need
																	// to update
																	// the team
																	// bit

		this.price = price;
		this.type = type;
		Random gen = new Random();
		
		if (type.equals(TowerFactory.type.diceOne))
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
	}
	
	public TowerFactory.type getType()
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

}
