package com.teamamerica.games.unicodewars.utils;

public class Timer
{
	private long _start;
	
	public Timer()
	{
		_start = System.currentTimeMillis();
	}
	
	public boolean oneSecondPassed()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime - _start > 1000)
		{
			_start = currentTime;
			return true;
		}
		return false;
	}
	
	public boolean xMilisecondsPassed(long interval)
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime - this._start > interval)
		{
			this._start = currentTime;
			return true;
		}
		return false;
	}
	
	public long getStartTime()
	{
		return this._start;
	}
	
	public long timeUntilXMilisecondsPass(long interval)
	{
		long currentTime = System.currentTimeMillis();
		return interval - (currentTime - this._start);
	}
}
