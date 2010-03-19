package com.teamamerica.games.unicodewars.object.mob;

public class MobFactory
{
	public static enum type
	{
		chinese, latin, greek, cyrillic;
	}
	
	public static Mob createMob(type t, int level)
	{
		switch (t)
		{
			case chinese:
				return new ChineseChar(t, level);
			case latin:
				return new LatinChar(t, level);
			case cyrillic:
				return new CyrillicChar(t, level);
			case greek:
				return new GreekChar(t, level);
		}
		return null;
	}
}
