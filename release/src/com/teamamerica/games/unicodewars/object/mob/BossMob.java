package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BossMob extends MobObject
{
	public BossMob(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Boss Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.boss, imgLoc);
		
		this.defense = 20;
		this.speed = 1;
		this.attack = 1000;
		
		this.currentHP = this.totalHP = 500000;
		this.price = 0;
		this.refund = 0;
	}
	
	public void setSpeed(int locSpeed)
	{
		speed = locSpeed;
	}
}

