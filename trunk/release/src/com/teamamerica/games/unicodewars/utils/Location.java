package com.teamamerica.games.unicodewars.utils;

import java.util.HashSet;
import java.util.Set;
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
	
	/**
	 * Return the manhanttan distance
	 * 
	 * @param loc
	 * @return
	 */
	public int distance(Location loc)
	{
		return distance(this, loc);
	}
	
	public static int distance(Location l1, Location l2)
	{
		return Math.abs(l1.x - l2.x) + Math.abs(l1.y - l2.y);
	}
	
	public Set<Location> getLocsWithinDistance(int distance)
	{
		return getLocsWithinDistance(this, distance);
	}

	public static Set<Location> getLocsWithinDistance(Location loc, int distance)
	{
		HashSet<Location> ret = new HashSet<Location>();
		
		for (int i = distance; i > 0; i--)
		{
			for (int x = i, y = 0; x >= 0 && y <= i; x--, y++)
			{
				ret.add(new Location(loc.x - x, loc.y - y));
				ret.add(new Location(loc.x - x, loc.y + y));
				ret.add(new Location(loc.x + x, loc.y - y));
				ret.add(new Location(loc.x + x, loc.y + y));
			}
		}
		
		return ret;
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
