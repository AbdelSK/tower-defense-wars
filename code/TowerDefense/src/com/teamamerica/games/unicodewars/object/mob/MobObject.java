package com.teamamerica.games.unicodewars.object.mob;

import org.newdawn.slick.util.pathfinding.Mover;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class MobObject extends GameObject implements Mover
{
	
	public MobObject(String name, int id, int renderPriority)
	{
		super(name, id, Team.Player1, renderPriority);
		// TODO Auto-generated constructor stub
	}
	
	public MobObject(String name, int id, int renderPriority, Location loc)
	{
		super(name, id, Team.Player1, renderPriority);
		
		this._position = loc;
	}

}
