package com.teamamerica.games.unicodewars.utils;

public class Location implements Comparable
{
	public int x;
	public int y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (o.getClass() == Location.class)
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
		else if (o.getClass() == Location.class)
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
}
