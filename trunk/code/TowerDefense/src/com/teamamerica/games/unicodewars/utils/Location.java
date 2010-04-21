package com.teamamerica.games.unicodewars.utils;

import com.teamamerica.games.unicodewars.system.GameMap;

public class Location implements Comparable<Object>
{
	public int x;
	public int y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int distance(Location loc)
	{
		return Math.abs(this.x - loc.x) + Math.abs(this.y - loc.y);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (o instanceof Location)
		{
			Location temp = (Location) o;
			return (temp.x == this.x && temp.y == this.y);
		}
		return false;
	}
	
	@Override
	public int compareTo(Object o)
	{
		if (o == null)
		{
			return 1;
		}
		else if (o instanceof Location)
		{
			Location temp = (Location) o;
			if (this.y < temp.y)
			{
				return -1;
			}
			else if (this.y > temp.y)
			{
				return 1;
			}
			else
			{
				if (this.x < temp.x)
				{
					return -1;
				}
				else if (this.x > temp.x)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
		}
		return 1;
	}
	
	@Override
	public int hashCode()
	{
		return (GameMap.inst().rows * this.y) + this.x;
	}

	public Location copy()
	{
		return new Location(x, y);
	}
}
