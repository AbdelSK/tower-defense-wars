package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class CyrillicChar extends MobObject
{
	public CyrillicChar(Location loc, Team side, int level, Type type)
	{
		super("Cyrillic Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.cyrillic);
	}
}
