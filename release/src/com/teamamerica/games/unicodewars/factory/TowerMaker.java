package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.object.towers.CardOne;
import com.teamamerica.games.unicodewars.object.towers.ChessOne;
import com.teamamerica.games.unicodewars.object.towers.CurrencyOne;
import com.teamamerica.games.unicodewars.object.towers.DiceOne;
import com.teamamerica.games.unicodewars.object.towers.MusicOne;
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
		
		TowerBase temp = null;
		String imgPath = null;
		switch (t)
		{
			case diceOne:
				imgPath = "data/images/towers/Dice-1.png";
				temp = new DiceOne(loc, team, imgPath);
				break;
			case chessOne:
				imgPath = "data/images/towers/Chess-1.png";
				temp = new ChessOne(loc, team, imgPath);
				break;
			case cardOne:
				imgPath = "data/images/towers/Card-1.png";
				temp = new CardOne(loc, team, imgPath);
				break;
			case musicOne:
				imgPath = "data/images/towers/Music-1.png";
				temp = new MusicOne(loc, team, imgPath);
				break;
			case currencyOne:
				imgPath = "data/images/towers/Currency-1.png";
				temp = new CurrencyOne(loc, team, imgPath);
				break;
		}
		VisualComponent looks = new VisualComponent(temp, imgPath);
		temp.addComponent(looks);
		BB.inst().addTeamObject(temp, team);
		if (GameMap.inst().buildTower(temp))
		{
			return temp;
		}
		else
		{
			temp.deleteObject();
			return null;
		}
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
