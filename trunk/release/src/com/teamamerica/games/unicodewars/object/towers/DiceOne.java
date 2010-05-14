package com.teamamerica.games.unicodewars.object.towers;

import java.util.Random;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class DiceOne extends TowerBase
{
	
	private static final int MAX_ATTACK = 20;
	private static final int MAX_SPEED = 20;

	public static int price = 5;
	
	public DiceOne(Location loc, Team team, String imgLoc)
	{
		super(TowerBase.Type.diceOne, 0, price, 0, 0, team, loc, imgLoc);
		
		//
		// Possible values for dice stats:
		// attack: 1-6
		// radius: 1-3
		// speed:: 1-6
		// Max values given various attack values
		// attack: 6 5 4 3 2 1
		// radius: 1 1 2 2 3 3
		// speed:: 1 2 2 3 3 4
		// Max total (attack=3 * radius=2 * speed=3) is 18

		Random gen = BB.inst().getRandom();
		this.attack = gen.nextInt(5) + 1; // 1-6
		this.radius = gen.nextInt(5 - (this.attack - 1)) / 2 + 1; // 1-3
		this.speed = 8 - (this.attack + this.radius); // 1-6
		if (this.speed < 1)
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
		
		int totalAtt = 0;
		int totalSpd = 0;
		Random gen = BB.inst().getRandom();
		
		if (level == 2)
		{
			totalAtt = 3;
			totalSpd = 2;
			this.getVisualComponent().updateImage("data/images/towers/Dice-2.png");
		}
		else if (level == 3)
		{
			totalAtt = 3;
			totalSpd = 2;
			this.radius++;
			this.getVisualComponent().updateImage("data/images/towers/Dice-3.png");
		}
		else if (level == 4)
		{
			totalAtt = 4;
			totalSpd = 3;
			this.getVisualComponent().updateImage("data/images/towers/Dice-4.png");
		}
		else if (level == 5)
		{
			totalAtt = 4;
			totalSpd = 3;
            this.getVisualComponent().updateImage("data/images/towers/Dice-5.png");
		}
		else if (level == 6)
		{
			totalAtt = 5;
			totalAtt = 4;
			this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
		}

		int attkAdd = (this.attack <= this.MAX_ATTACK) ? gen.nextInt(totalAtt) : 0; // 0-total
		this.attack += attkAdd;
		
		// int radiusAdd = (this.radius <= MAX_RADIUS) ? gen.nextInt(total -
		// attkAdd) : 0; // 0-total
		// this.radius += radiusAdd;
		
		int speedAdd = (this.speed <= MAX_SPEED) ? gen.nextInt(totalAtt - (attkAdd - 1)) + 1 : 0; // 0-total+1
		this.speed += speedAdd;

		//
		// Check to see if tower has been maxed out
		//
		if (level < 6 && this.attack >= MAX_ATTACK && this.speed >= MAX_SPEED)
		{
			level = 6;
			this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
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
			return 10;
		}
		else if (level == 2)
		{
			return 20;
		}
		else if (level == 3)
		{
			return 40;
		}
		else if (level == 4)
		{
			return 80;
		}
		else if (level == 5)
		{
			return 160;
		}
		return 0;
	}

}
