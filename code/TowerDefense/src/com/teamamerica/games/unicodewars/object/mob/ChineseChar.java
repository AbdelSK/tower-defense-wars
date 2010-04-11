package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class ChineseChar extends MobObject
{
	public ChineseChar(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Chinese Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.chinese, imgLoc);
		
		this.defense = 4 * (int) Math.pow(this.level, 3);
		this.vitality = 2 * (int) Math.pow(this.level, 3);
		this.speed = this.attack = 1 * (int) Math.pow(this.level, 2);
		
		this.currentHP = this.totalHP = this.vitality * 80;
	}
}
