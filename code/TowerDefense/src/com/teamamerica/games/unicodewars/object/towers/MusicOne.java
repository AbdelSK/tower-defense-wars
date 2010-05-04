package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class MusicOne extends TowerBase
{
	
	public static int price = 500;
	public static int BASE_ATTACK = 2;
	public static int BASE_SPEED = 5;
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
			this.attack = 4;
			this.speed = 10;
			this.radius = 3;
			this.getVisualComponent().updateImage("data/images/towers/Music-2.png");
		}
		else if (level == 3)
		{
			this.attack = 8;
			this.speed = 15;
			this.radius = 4;
			this.getVisualComponent().updateImage("data/images/towers/Music-3.png");
		}
		else if (level == 4)
		{
			this.attack = 16;
			this.speed = 20;
			this.radius = 5;
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
			return 1000;
		}
		else if (level == 2)
		{
			return 2000;
		}
		else if (level == 3)
		{
			return 4000;
		}
		return 0;
	}

}
