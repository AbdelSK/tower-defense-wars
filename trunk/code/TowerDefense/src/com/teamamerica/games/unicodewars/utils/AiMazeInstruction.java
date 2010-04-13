package com.teamamerica.games.unicodewars.utils;



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
	
	public AiMazeInstruction(Action action, Location towerLoc)
	{
		_action = action;
		_towerLoc = towerLoc;
	}
	
	public Action getAction()
	{
		return _action;
	}
	
	public Location getTowerLoc()
	{
		return _towerLoc;
	}
	
}
