package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class MusicOne extends TowerBase
{
	
	public static int price = 40;
	public static int BASE_ATTACK = 2;
	public static int BASE_SPEED = 5;
	public static int BASE_RADIUS = 2;
	
	public MusicOne(Location loc, Team team)
	{
		super(TowerBase.Type.musicOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc);
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
			this.speed = 10;
			this.radius = 4;
		}
		else if (level == 3)
		{
			this.attack = 8;
			this.speed = 15;
			this.radius = 8;
		}
		else if (level == 4)
		{
			this.attack = 16;
			this.speed = 20;
			this.radius = 16;
		}
	}
	
	@Override
	public String getInfoString()
	{
		return "Music Tower Level " + this.level;
	}

}
