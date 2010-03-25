package com.teamamerica.games.unicodewars.object.base;

import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BaseObject extends GameObject
{
	
	public BaseObject(String name, int id, Team team, int renderPriority, Location loc)
	{
		super(name, id, team, renderPriority);
		// TODO Auto-generated constructor stub
		this._position = loc;
	}
	
}
