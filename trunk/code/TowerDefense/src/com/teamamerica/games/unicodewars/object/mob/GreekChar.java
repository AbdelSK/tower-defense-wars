package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class GreekChar extends MobObject
{
	public GreekChar(Location loc, Team side, int level, Type type)
	{
		super("Greek Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.greek);
		
		this.attack = 4 * this.level;
		this.defense = 2 * this.level;
		this.speed = this.vitality = 1 * this.level;
		
		this.currentHP = this.totalHP = this.vitality * 20;
	}
}
