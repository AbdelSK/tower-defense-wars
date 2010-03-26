package com.teamamerica.games.unicodewars.object.towers;

import java.util.ArrayList;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class TowerTest
{
	
	public static void main(String args[])
	{
		ArrayList<TowerBase> TowerList = new ArrayList<TowerBase>();
		TowerList.add(TowerMaker.createTower(TowerBase.Type.diceOne, new Location(5, 6), Team.Player1));
		
		while (TowerList.get(0).canUpgrade())
		{
			TowerList.get(0).doUpgrade();
			System.out.println(TowerList.get(0).getInfoString());
			System.out.println("Tower Attack: " + TowerList.get(0).getAttack());
			System.out.println("Tower Radius: " + TowerList.get(0).getRadius());
			System.out.println("Tower Speed: " + TowerList.get(0).getSpeed());
			System.out.println("----------------");
		}
	}
}
