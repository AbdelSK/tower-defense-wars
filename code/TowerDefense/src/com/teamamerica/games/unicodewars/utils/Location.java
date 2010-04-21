package com.teamamerica.games.unicodewars.utils;

import java.util.ArrayList;
import java.util.List;
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
	
	public List<Location> getLocsWithinDistance(int distance)
	{
		return getLocsWithinDistance(this, distance);
	}

	public static List<Location> getLocsWithinDistance(Location loc, int distance)
	{
		ArrayList<Location> ret = new ArrayList<Location>();
		
		for (int x = loc.x - distance; x <= loc.x + distance; x++)
		{
			for (int y = loc.y - distance; y <= loc.y + distance; y++)
			{
				Location temp = new Location(x, y);
				if (loc.distance(temp) <= distance)
					ret.add(temp);
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
