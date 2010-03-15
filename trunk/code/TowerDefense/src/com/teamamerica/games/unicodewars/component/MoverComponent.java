package com.teamamerica.games.unicodewars.component;

import org.newdawn.slick.util.pathfinding.Path;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.GameMap;

public class MoverComponent extends Component
{
	private Path path;
	
	public MoverComponent(GameObject owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void update(int elapsed)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void updatePath()
	{
		GameMap.inst().getPathFinder().findPath((MobObject) this._parent, this._parent.getPosition().x, this._parent.getPosition().y, 1, 1);
	}
	
	public Path getPath()
	{
		return path;
	}

}
