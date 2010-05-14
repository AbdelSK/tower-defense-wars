package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CurrencyOne extends TowerBase
{
	
	public static int price = 1000;
	public static int BASE_ATTACK = 8;
	public static int BASE_SPEED = 6;
	public static int BASE_RADIUS = 4;
	
	public CurrencyOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.currencyOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc, imgLoc);
	}
	
	public boolean canUpgrade()
	{
		return this.level < 4;
	}
	
	public void doUpgrade()
	{
		if (_team == Team.Player1)
			BB.inst().getUsersPlayer().purchase(this.getUpgradePrice());
		this.level++;
		
		if (level == 2)
		{
			this.attack = 13;
			this.speed = 12;
			this.radius = 5;
			this.getVisualComponent().updateImage("data/images/towers/Currency-2.png");
		}
		else if (level == 3)
		{
			this.attack = 16;
			this.speed = 15;
			this.radius = 7;
			this.getVisualComponent().updateImage("data/images/towers/Currency-3.png");
		}
		else if (level == 4)
		{
			this.attack = 20;
			this.speed = 20;
			this.radius = 7;
			this.getVisualComponent().updateImage("data/images/towers/Currency-4.png");
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Currency Tower Level " + this.level;
	}
	
	public int getUpgradePrice()
	{
		if (level == 1)
		{
			return 2000;
		}
		else if (level == 2)
		{
			return 4000;
		}
		else if (level == 3)
		{
			return 10000;
		}
		return 0;
	}

}
