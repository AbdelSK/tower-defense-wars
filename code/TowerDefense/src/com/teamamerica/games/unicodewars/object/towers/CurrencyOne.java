package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CurrencyOne extends TowerBase
{
	
	public static int price = 60;
	public static int BASE_ATTACK = 5;
	public static int BASE_SPEED = 5;
	public static int BASE_RADIUS = 3;
	
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
			this.attack = 10;
			this.speed = 9;
			this.radius = 4;
			this.getVisualComponent().updateImage("data/images/towers/Currency-2.png");
		}
		else if (level == 3)
		{
			this.attack = 15;
			this.speed = 13;
			this.radius = 6;
			this.getVisualComponent().updateImage("data/images/towers/Currency-3.png");
		}
		else if (level == 4)
		{
			this.attack = 20;
			this.speed = 17;
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
			return 90;
		}
		else if (level == 2)
		{
			return 130;
		}
		else if (level == 3)
		{
			return 150;
		}
		return 0;
	}

}
