package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CyrillicChar extends MobObject
{
	public CyrillicChar(Location loc, Team side, int level, Type type)
	{
		super("Cyrillic Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.cyrillic);
		
		this.vitality = 4 * this.level;
		this.speed = 2 * this.level;
		this.defense = this.attack = 1 * this.level;
		
		this.currentHP = this.totalHP = this.vitality * 20;
	}
}
