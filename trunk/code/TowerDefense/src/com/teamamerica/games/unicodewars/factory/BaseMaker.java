package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.base.BaseObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BaseMaker
{
	private BaseMaker()
	{
	};
	
	public static BaseObject MakeBase(Team team, Location loc)
	{
		BaseObject temp = new BaseObject(team.toString() + " base", BB.inst().getNextId(), team, 20, loc);

		VisualComponent view = new VisualComponent(temp);
		temp.addComponent(view);
		
		BB.inst().addTeamObject(temp, team);
		return temp;
	}
}
