package com.teamamerica.games.unicodewars.utils;

public class Timer
{
	private long _start;
	private long _pauseStart;
	private long _pauseEnd;
	private long _pausedTime;
	private boolean _paused;
	
	public Timer()
	{
		_start = System.currentTimeMillis();
		this._pauseEnd = 0;
		this._pauseStart = 0;
		this._paused = false;
	}
	
	public void pause()
	{
		if (!_paused)
		{
			_pauseStart = System.currentTimeMillis();
			_paused = true;
		}
	}
	
	public void unpause()
	{
		if (_paused)
		{
			_pauseEnd = System.currentTimeMillis();
			_pausedTime += _pauseEnd - _pauseStart;
			_paused = false;
		}
	}

	public boolean oneSecondPassed()
	{
		return xMilisecondsPassed(1000);
	}
	
	public boolean xMilisecondsPassed(long interval)
	{
		if (_paused)
			return false;
		if (getElapsedTime() >= interval)
		{
			this._start = System.currentTimeMillis();
			this._pauseEnd = 0;
			this._pauseStart = 0;
			_pausedTime = 0;
			return true;
		}
		return false;
	}
	
	public long getElapsedTime()
	{
		if (!_paused)
		{
			long totalElapsedTime = System.currentTimeMillis() - this._start;
			return totalElapsedTime - _pausedTime;
		}
		else
		{
			return _pauseStart - _start;
		}
	}
	
	public long timeUntilXMilisecondsPass(long interval)
	{
		return interval - getElapsedTime();
	}
	
	public boolean paused()
	{
		return _paused;
	}
}
