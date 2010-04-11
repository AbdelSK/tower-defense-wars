package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class GreekChar extends MobObject
{
	public GreekChar(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Greek Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.greek, imgLoc);
		
		this.attack = 4 * (int) Math.pow(this.level, 2);
		this.defense = 2 * (int) Math.pow(this.level, 3);
		this.speed = this.vitality = 1 * (int) Math.pow(this.level, 3);
		
		this.currentHP = this.totalHP = this.vitality * 80;
	}
}
