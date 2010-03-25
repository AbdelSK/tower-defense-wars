package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.MoverComponent;
import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.mob.ChineseChar;
import com.teamamerica.games.unicodewars.object.mob.CyrillicChar;
import com.teamamerica.games.unicodewars.object.mob.GreekChar;
import com.teamamerica.games.unicodewars.object.mob.LatinChar;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

// import com.teamamerica.games.unicodewars.system.BB;

public class MobMaker
{
	private MobMaker()
	{
	}
	
	
	public static MobObject MakeMob(MobObject.Type type, int level, Team team)
	{
		Location loc = GameMap.inst().getTeamSpawnPoint(team);
		MobObject temp;
		switch(type)
		{
			case chinese:
				temp = new ChineseChar(loc, team, level, type);
				break;
			case cyrillic:
				temp = new CyrillicChar(loc, team, level, type);
				break;
			case greek:
				temp = new GreekChar(loc, team, level, type);
				break;
			case latin:
				temp = new LatinChar(loc, team, level, type);
				break;
			default:
				temp = new LatinChar(loc, team, level, type);
				break;
		}

		MoverComponent pathPart = new MoverComponent(temp);
		temp.addComponent(pathPart);
		VisualComponent visualPart = new VisualComponent(temp);
		temp.addComponent(visualPart);
		return temp;
	}
	
	public static MobObject MakeMobChinese1(Team team)
	{
		return MakeMob(MobObject.Type.chinese, 1, team);
	}
	
	public static MobObject MakeMobChinese2(Team team)
	{
		return MakeMob(MobObject.Type.chinese, 2, team);
	}
	
	public static MobObject MakeMobChinese3(Team team)
	{
		return MakeMob(MobObject.Type.chinese, 3, team);
	}
	
	public static MobObject MakeMobChinese4(Team team)
	{
		return MakeMob(MobObject.Type.chinese, 4, team);
	}
	
	public static MobObject MakeMobChinese5(Team team)
	{
		return MakeMob(MobObject.Type.chinese, 5, team);
	}
}