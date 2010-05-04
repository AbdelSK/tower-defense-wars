package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CardOne extends TowerBase
{
	
	public static int price = 250;
	public static int BASE_ATTACK = 2;
	public static int BASE_SPEED = 2;
	public static int BASE_RADIUS = 4;
	
	public CardOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.cardOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc, imgLoc);
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
			this.attack = 4;
			this.speed = 4;
			this.radius = 5;
			this.getVisualComponent().updateImage("data/images/towers/Card-2.png");
		}
		else if (level == 3)
		{
			this.attack = 8;
			this.speed = 8;
			this.radius = 7;
			this.getVisualComponent().updateImage("data/images/towers/Card-3.png");
		}
		else if (level == 4)
		{
			this.attack = 16;
			this.speed = 16;
			this.radius = 8;
			this.getVisualComponent().updateImage("data/images/towers/Card-4.png");
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Card Tower Level " + this.level;
	}
	
	public int getUpgradePrice()
	{
		if (level == 1)
		{
			return 500;
		}
		else if (level == 2)
		{
			return 1000;
		}
		else if (level == 3)
		{
			return 2000;
		}
		return 0;
	}

}