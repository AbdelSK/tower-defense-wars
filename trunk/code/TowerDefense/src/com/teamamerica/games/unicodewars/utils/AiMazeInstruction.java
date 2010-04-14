package com.teamamerica.games.unicodewars.utils;

import com.teamamerica.games.unicodewars.object.towers.TowerBase;



public class AiMazeInstruction
{
	public enum Action
	{
		create(0), upgrade(1);
		
		private final int index;
		
		Action(int index)
		{
			this.index = index;
		}
		
		public int index()
		{
			return this.index;
		}
	}
	
	private Action _action;
	private Location _towerLoc;
	private TowerBase.Type _towerType;
	
	public AiMazeInstruction(Action action, TowerBase.Type towerType, Location towerLoc)
	{
		_action = action;
		_towerLoc = towerLoc;
		_towerType = towerType;
	}
	
	public Action getAction()
	{
		return _action;
	}
	
	public Location getTowerLoc()
	{
		return _towerLoc;
	}
	
	public TowerBase.Type getTowerType()
	{
		return _towerType;
	}
}
