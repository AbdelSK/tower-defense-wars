package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.MoverComponent;
import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.mob.BossMob;
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
	
	public static MobObject MakeMob(MobObject.Type type, int level, Team team)
	{
		Location loc = GameMap.inst().getTeamSpawnPoint(team);
		MobObject temp;
		String imgLoc = null;
		switch (type)
		{
			case chinese:
				imgLoc = "data/images/mobs/Chinese" + getImageSuffix(level);
				temp = new ChineseChar(loc, team, level, type, imgLoc);
				break;
			case cyrillic:
				imgLoc = "data/images/mobs/Cyrillic" + getImageSuffix(level);
				temp = new CyrillicChar(loc, team, level, type, imgLoc);
				break;
			case greek:
				imgLoc = "data/images/mobs/Greek" + getImageSuffix(level);
				temp = new GreekChar(loc, team, level, type, imgLoc);
				break;
			case latin:
				imgLoc = "data/images/mobs/Latin" + getImageSuffix(level);
				temp = new LatinChar(loc, team, level, type, imgLoc);
				break;
			case boss:
				// imgLoc = "data/images/mobs/Boss.png";
				imgLoc = "data/images/towers/Special-3.png";
				temp = new BossMob(loc, team, level, type, imgLoc);
				break;
			default:
				imgLoc = "data/images/mobs/Latin" + getImageSuffix(level);
				temp = new LatinChar(loc, team, level, type, imgLoc);
				break;
		}
		
		MoverComponent pathPart = new MoverComponent(temp);
		temp.addComponent(pathPart);
		pathPart.setPath(GameMap.inst().getSpawnPath(team));
		VisualComponent visualPart = new VisualComponent(temp, imgLoc);
		temp.addComponent(visualPart);
		BB.inst().addTeamObject(temp, team);
		
		return temp;
	}
	
	private static String getImageSuffix(int level)
	{
		String suffix;

		switch (level)
		{
			case 1:
				suffix = "-1.png";
				break;
			case 2:
				suffix = "-2.png";
				break;
			case 3:
				suffix = "-3.png";
				break;
			case 4:
				suffix = "-4.png";
				break;
			case 5:
				suffix = "-5.png";
				break;
			default:
				suffix = "-1.png";
				break;
		}
		
		return suffix;
	}
}
