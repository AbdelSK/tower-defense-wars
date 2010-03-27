package com.teamamerica.games.unicodewars.utils;

public enum Team
{
	Player1(0), Player2(1);
	
	private final int index;
	private int income;
	private int score;
	private int gold;

	Team(int index)
	{
		this.income = 10;
		this.score = 0;
		this.gold = 10;
		this.index = index;
	}
	
	public int index()
	{
		return this.index;
	}
	
	public void addIncome(int x)
	{
		this.income += x;
	}
	
	public void addScore(int x)
	{
		this.score += x;
	}

	public void removeGold(int x)
	{
		this.gold -= x;
	}
	
	public void addGold(int x)
	{
		this.gold += x;
	}
	
	public int getIncome()
	{
		return this.income;
	}
	
	public int getGold()
	{
		return this.gold;
	}
}
