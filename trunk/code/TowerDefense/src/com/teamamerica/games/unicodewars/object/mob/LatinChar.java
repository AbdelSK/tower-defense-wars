package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class LatinChar extends MobObject
{
	public LatinChar(Location loc, Team side, int level, Type type)
	{
		super("Latin Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.latin);

		this.speed = 4 * (int) Math.pow(this.level, 3);
		this.attack = 2 * (int) Math.pow(this.level, 2);
		this.defense = this.vitality = 1 * (int) Math.pow(this.level, 3);
		
		this.currentHP = this.totalHP = this.vitality * 80;
	}
}
