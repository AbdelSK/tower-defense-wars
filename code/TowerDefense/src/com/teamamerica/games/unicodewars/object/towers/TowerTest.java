package com.teamamerica.games.unicodewars.object.towers;

import java.util.ArrayList;

public class TowerTest
{
	
	public static void main(String args[])
	{
		ArrayList<Tower> TowerList = new ArrayList<Tower>();
		TowerList.add(TowerFactory.createTower(TowerFactory.type.diceOne, 5, 6));
		
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
