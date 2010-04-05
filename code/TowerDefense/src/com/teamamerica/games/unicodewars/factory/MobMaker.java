package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.MoverComponent;
import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.mob.ChineseChar;
import com.teamamerica.games.unicodewars.object.mob.CyrillicChar;
import com.teamamerica.games.unicodewars.object.mob.GreekChar;
import com.teamamerica.games.unicodewars.object.mob.LatinChar;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class MobMaker
{
	private MobMaker()
	{
	}
	
	public static void MakeMob(MobObject.Type type, int level, Team team)
	{
		Location loc = GameMap.inst().getTeamSpawnPoint(team);
		MobObject temp;
		String imgLoc = null;
		switch (type)
		{
			case chinese:
				temp = new ChineseChar(loc, team, level, type);
				imgLoc = "data/images/mobs/Chinese";
				break;
			case cyrillic:
				temp = new CyrillicChar(loc, team, level, type);
				imgLoc = "data/images/mobs/Cyrillic";
				break;
			case greek:
				temp = new GreekChar(loc, team, level, type);
				imgLoc = "data/images/mobs/Greek";
				break;
			case latin:
				temp = new LatinChar(loc, team, level, type);
				imgLoc = "data/images/mobs/Latin";
				break;
			default:
				temp = new LatinChar(loc, team, level, type);
				imgLoc = "data/images/mobs/Latin";
				break;
		}
		
		switch (level)
		{
			case 1:
				imgLoc += "-1.png";
				break;
			case 2:
				imgLoc += "-2.png";
				break;
			case 3:
				imgLoc += "-3.png";
				break;
			case 4:
				imgLoc += "-4.png";
				break;
			case 5:
				imgLoc += "-5.png";
				break;
			default:
				imgLoc += "-1.png";
				break;
		}

		MoverComponent pathPart = new MoverComponent(temp);
		temp.addComponent(pathPart);
		pathPart.setPath(GameMap.inst().getSpawnPath(team));
		VisualComponent visualPart = new VisualComponent(temp, imgLoc);
		temp.addComponent(visualPart);
		BB.inst().addTeamObject(temp, team);
	}
}
