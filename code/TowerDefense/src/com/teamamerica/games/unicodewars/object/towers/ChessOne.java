package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class ChessOne extends TowerBase
{
	
	public static int price = 20;
	public static int BASE_ATTACK = 5;
	public static int BASE_SPEED = 2;
	public static int BASE_RADIUS = 2;
	
	public ChessOne(Location loc, Team team)
	{
		super(TowerBase.Type.chessOne, BASE_ATTACK, price, BASE_RADIUS, BASE_SPEED, team, loc);
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
			this.attack = 10;
			this.speed = 4;
			this.radius = 4;
			this.getVisualComponent().updateImage("data/images/towers/Chess-2.png");
		}
		else if (level == 3)
		{
			this.attack = 15;
			this.speed = 8;
			this.radius = 8;
			this.getVisualComponent().updateImage("data/images/towers/Chess-3.png");
		}
		else if (level == 4)
		{
			this.attack = 20;
			this.speed = 16;
			this.radius = 16;
			this.getVisualComponent().updateImage("data/images/towers/Chess-4.png");
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Chess Tower Level " + this.level;
	}

}
