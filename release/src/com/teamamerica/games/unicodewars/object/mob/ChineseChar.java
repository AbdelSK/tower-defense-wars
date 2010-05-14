package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class ChineseChar extends MobObject
{
	public ChineseChar(Location loc, Team side, int level, Type type, String imgLoc)
	{
		super("Chinese Mob", BB.inst().getNextId(), 10, loc, side, level, MobObject.Type.chinese, imgLoc);
	}
}
