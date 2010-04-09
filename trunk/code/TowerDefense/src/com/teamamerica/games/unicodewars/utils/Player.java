package com.teamamerica.games.unicodewars.utils;

public class Player
{
	private int gold;
	private int income;
	private int score;
	
	public Player()
	{
		this.gold = 30;
		this.income = 0;
		this.score = 0;
	}
	
	public void setGold(int gold)
	{
		this.gold = gold;
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public void addIncome(int income)
	{
		this.income += income;
	}
	
	public int getIncome()
	{
		return income;
	}
	
	public void addScore(int score)
	{
		this.score += score;
	}
	
	public int getScore()
	{
		return score;
	}

}
