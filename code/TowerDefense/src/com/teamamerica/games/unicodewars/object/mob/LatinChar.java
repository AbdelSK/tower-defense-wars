package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class LatinChar extends MobObject
{
	public LatinChar(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Latin Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.latin, imgLoc);

		this.speed = 4 * (int) Math.pow(2, this.level - 1);
		this.attack = 2 * (int) Math.pow(2, this.level - 1);
		this.defense = this.vitality = 1 * (int) Math.pow(2, this.level - 1);
		
		this.currentHP = this.totalHP = this.vitality * 25;
	}
}
