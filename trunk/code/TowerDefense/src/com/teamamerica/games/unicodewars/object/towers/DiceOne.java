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
		
		Random gen = BB.inst().getRandom();
		this.attack = gen.nextInt(7);
		if (this.attack == 0)
		{
			this.attack = 1;
		}
		this.radius = gen.nextInt(7 - this.attack);
		if (this.radius == 0)
		{
			this.radius = 1;
		}
		this.speed = 7 - (this.attack + this.radius);
		if (this.speed == 0)
		{
			this.speed = 1;
		}
		this.registerNewSpaces();
	}

	@Override
	public boolean canUpgrade()
	{
		return this.level < 6;
	}

	@Override
	public void doUpgrade()
	{
		if (_team == Team.Player1)
			BB.inst().getUsersPlayer().purchase(this.getUpgradePrice());
		this.level++;
		
		int total = 0;
		Random gen = BB.inst().getRandom();
		
		if (level == 2)
		{
			total = 5;// 12;
			this.getVisualComponent().updateImage("data/images/towers/Dice-2.png");
		}
		else if (level == 3)
		{
			total = 5;// 17;
			this.getVisualComponent().updateImage("data/images/towers/Dice-3.png");
		}
		else if (level == 4)
		{
			total = 7;// 24;
			this.getVisualComponent().updateImage("data/images/towers/Dice-4.png");
		}
		else if (level == 5)
		{
			total = 5;// 29;
			this.getVisualComponent().updateImage("data/images/towers/Dice-5.png");
		}
		else if (level == 6)
		{
			total = 6;// 35;
			this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
		}

		int attkAdd = gen.nextInt(total);
		this.attack += attkAdd;
		
		int radiusAdd = gen.nextInt(total - attkAdd);
		this.radius += radiusAdd;
		
		int speedAdd = total - (attkAdd + radiusAdd);
		this.speed += speedAdd;

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
			return 20;
		}
		else if (level == 2)
		{
			return 40;
		}
		else if (level == 3)
		{
			return 80;
		}
		else if (level == 4)
		{
			return 160;
		}
		else if (level == 5)
		{
			return 320;
		}
		return 0;
	}

}
