package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class MusicOne extends TowerBase
{
	
	public static int price = 500;
	public static int BASE_ATTACK = 6;
	public static int BASE_SPEED = 6;
	public static int BASE_RADIUS = 2;
	
	public MusicOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.musicOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc, imgLoc);
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
			this.attack = 8;
			this.speed = 9;
			this.radius = 2;
			this.getVisualComponent().updateImage("data/images/towers/Music-2.png");
		}
		else if (level == 3)
		{
			this.attack = 10;
			this.speed = 12;
			this.radius = 3;
			this.getVisualComponent().updateImage("data/images/towers/Music-3.png");
		}
		else if (level == 4)
		{
			this.attack = 14;
			this.speed = 15;
			this.radius = 3;
			this.getVisualComponent().updateImage("data/images/towers/Music-4.png");
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Music Tower Level " + this.level;
	}

	public int getUpgradePrice()
	{
		if (level == 1)
		{
			return 750;
		}
		else if (level == 2)
		{
			return 1000;
		}
		else if (level == 3)
		{
			return 1500;
		}
		return 0;
	}

}
