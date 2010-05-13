package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class ChessOne extends TowerBase
{
	
	public static int price = 50;
	public static int BASE_ATTACK = 7;
	public static int BASE_SPEED = 2;
	public static int BASE_RADIUS = 1;
	
	public ChessOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.chessOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc, imgLoc);
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
			this.speed = 4;
			this.radius = 1;
			this.getVisualComponent().updateImage("data/images/towers/Chess-2.png");
		}
		else if (level == 3)
		{
			this.attack = 14;
			this.speed = 7;
			this.radius = 2;
			this.getVisualComponent().updateImage("data/images/towers/Chess-3.png");
		}
		else if (level == 4)
		{
			this.attack = 20;
			this.speed = 12;
			this.radius = 2;
			this.getVisualComponent().updateImage("data/images/towers/Chess-4.png");
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Chess Tower Level " + this.level;
	}

	public int getUpgradePrice()
	{
		if (level == 1)
		{
			return 100;
		}
		else if (level == 2)
		{
			return 250;
		}
		else if (level == 3)
		{
			return 500;
		}
		return 0;
	}
}
