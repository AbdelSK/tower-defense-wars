package com.teamamerica.games.unicodewars.object.towers;

import com.teamamerica.games.unicodewars.utils.Team;

public class TowerFactory
{
	
	public static enum type
	{
		diceOne(DiceOne.price), ;
		
		public final int price;
		
		type(int price)
		{
			this.price = price;
		}
	}
	
	public static Tower createTower(type t, int gridX, int gridY, Team team)
	{
		switch (t)
		{
			case diceOne:
				return new DiceOne(gridX, gridY, team);
		}
		return null;
	}
}
