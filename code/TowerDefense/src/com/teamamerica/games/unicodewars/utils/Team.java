package com.teamamerica.games.unicodewars.utils;

public enum Team
{
	Player1(0), Player2(1);
	
	private final int index;

	Team(int index)
	{
		this.index = index;
	}
	
	public int index()
	{
		return this.index;
	}
}
