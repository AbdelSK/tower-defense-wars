package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CardOne extends TowerBase
{
	
	public static int price = 30;
	public static int BASE_ATTACK = 2;
	public static int BASE_SPEED = 2;
	public static int BASE_RADIUS = 5;
	
	public CardOne(Location loc, Team team)
	{
		super(TowerBase.Type.cardOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc);
	}
	
	public boolean canUpgrade()
	{
		return this.level < 4;
	}
	
	public void doUpgrade()
	{
		this.level++;
		
		if (level == 2)
		{
			this.attack = 4;
			this.speed = 4;
			this.radius = 10;
		}
		else if (level == 3)
		{
			this.attack = 8;
			this.speed = 8;
			this.radius = 15;
		}
		else if (level == 4)
		{
			this.attack = 16;
			this.speed = 16;
			this.radius = 20;
		}
	}
	
	@Override
	public String getInfoString()
	{
		return "Card Tower Level " + this.level;
	}

}