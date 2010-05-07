package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CyrillicChar extends MobObject
{
	public CyrillicChar(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Cyrillic Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.cyrillic, imgLoc);
		
		this.speed = 2 * (int) Math.pow(2, this.level - 1);
		this.attack = 1 * (int) Math.pow(2, this.level - 1);
		this.defense = this.attack * 2;
		
		this.currentHP = this.totalHP = this.defense * 150;
	}
}
