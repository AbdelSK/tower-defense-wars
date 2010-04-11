package com.teamamerica.games.unicodewars.object.towers;

import java.util.Random;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class DiceOne extends TowerBase
{
	
	public static int price = 10;
	
	
	public DiceOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.diceOne, 0, price, 0, 0, team, loc, imgLoc);
	}

	@Override
	public boolean canUpgrade()
	{
		return this.level < 6;
	}

	@Override
	public void doUpgrade()
	{
		this.level++;
		
		int total = 0;
		Random gen = BB.inst().getRandom();
		
		if (level == 2)
		{
			total = 12;
			this.getVisualComponent().updateImage("data/images/towers/Dice-2.png");
		}
		else if (level == 3)
		{
			total = 17;
			this.getVisualComponent().updateImage("data/images/towers/Dice-3.png");
		}
		else if (level == 4)
		{
			total = 24;
			this.getVisualComponent().updateImage("data/images/towers/Dice-4.png");
		}
		else if (level == 5)
		{
			total = 29;
			this.getVisualComponent().updateImage("data/images/towers/Dice-5.png");
		}
		else if (level == 6)
		{
			total = 35;
			this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
		}

		this.attack = gen.nextInt(total);
		if (this.attack == 0)
		{
			this.attack = 1;
		}
		this.radius = gen.nextInt(total - this.attack);
		if (this.radius == 0)
		{
			this.radius = 1;
		}
		this.speed = total - (this.attack + this.radius);
		if (this.speed == 0)
		{
			this.speed = 1;
		}
		super.doUpgrade();
	}
	
	@Override
	public String getInfoString()
	{
		return "Dice Tower Level " + this.level;
	}
	
	@Override
	public String getStatusString()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getUpgradePrice()
	{
		if (level == 1)
		{
			return 15;
		}
		else if (level == 2)
		{
			return 20;
		}
		else if (level == 3)
		{
			return 30;
		}
		else if (level == 4)
		{
			return 40;
		}
		else if (level == 5)
		{
			return 60;
		}
		return 0;
	}
		
}
