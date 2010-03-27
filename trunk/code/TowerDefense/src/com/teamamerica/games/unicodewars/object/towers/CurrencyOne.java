package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CurrencyOne extends TowerBase
{
	
	public static int price = 60;
	private static int BASE_ATTACK = 5;
	private static int BASE_SPEED = 5;
	private static int BASE_RADIUS = 5;
	
	public CurrencyOne(Location loc, Team team)
	{
		super(TowerBase.Type.currencyOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc);
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
			this.attack = 9;
			this.speed = 9;
			this.radius = 9;
		}
		else if (level == 3)
		{
			this.attack = 13;
			this.speed = 13;
			this.radius = 13;
		}
		else if (level == 4)
		{
			this.attack = 17;
			this.speed = 17;
			this.radius = 17;
		}
	}
	
	@Override
	public String getInfoString()
	{
		return "Currency Tower Level " + this.level;
	}

}
