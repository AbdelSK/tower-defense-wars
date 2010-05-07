package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BossMob extends MobObject
{
	public BossMob(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Boss Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.boss, imgLoc);
		
		this.defense = 25;
		this.speed = 1;
		this.attack = 1000;
		
		this.currentHP = this.totalHP = 250000;
		this.price = 10;
		this.refund = 5;
	}
}

