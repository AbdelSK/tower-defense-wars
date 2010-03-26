package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.towers.DiceOne;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;


public class TowerMaker
{
	private TowerMaker()
	{
	}
	
	
	public static TowerBase createTower(TowerBase.Type t, Location loc, Team team)
	{
		switch (t)
		{
			case diceOne:
				TowerBase temp = new DiceOne(loc, team);
				VisualComponent looks = new VisualComponent(temp);
				temp.addComponent(looks);
				BB.inst().addTeamObject(temp, team);
				GameMap.inst().buildTower(temp);
				return temp;
		}
		return null;
	}

	// Example Method
	public static TowerBase MakeTower()
	{
		TowerBase temp = null;
		return temp;
	}
	
	public static TowerBase MakeDiceTower(Location loc, Team team)
	{
		return createTower(TowerBase.Type.diceOne, loc, team);
	}
}
