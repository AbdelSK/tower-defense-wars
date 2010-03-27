package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class ChineseChar extends MobObject
{
	public ChineseChar(Location loc, Team side, int level, Type type)
	{
		super("Chinese Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.chinese);
		
		this.defense = 4 * this.level;
		this.vitality = 2 * this.level;
		this.speed = this.attack = 1 * this.level;
		
		this.currentHP = this.totalHP = this.vitality * 20;
	}
}
