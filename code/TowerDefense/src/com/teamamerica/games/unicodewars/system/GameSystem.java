package com.teamamerica.games.unicodewars.system;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.object.GameObject;

public class GameSystem
{
	private static Logger logger = Logger.getLogger(GameSystem.class);
	
	public enum Systems
	{
		SpawnSubsystem
	};
	
	public long _frameCount;
	private Map<Systems, Subsystem> _systems;
	
	public GameSystem(int width, int height)
	{
		
		BB.inst().setScreen(width, height);
		
		_systems = new TreeMap<Systems, Subsystem>();

	}

	/**
	 * Add a subsystem to our map.
	 * 
	 * @param id
	 * @param s
	 */
	public void addSubsystem(Systems id, Subsystem s)
	{
		_systems.put(id, s);
	}
	
	public void loadLevel(String levelName)
	{
		BB.inst();
		EventManager.inst();
		GameMap.inst();
	}
	
	public void update(int elapsed)
	{
		for (Subsystem s : _systems.values())
		{
			s.update(elapsed);
		}
		
		EventManager.inst().update(elapsed);
		GameMap.inst().update(elapsed);
		
		for (List<GameObject> team : BB.inst().getAll())
		{
			for (GameObject obj : team)
			{
				obj.update(elapsed);
			}
		}
	}
	
	public void render(Graphics g)
	{
		// GameObject controlled = (GameObject)
		// BB.inst().getVariableObject(Variable.controlledObject);
		
		g.pushTransform();
		g.resetTransform();
		// g.translate(-controlled.getPosition().x + centerX,
		// -controlled.getPosition().y + centerY);

		GameMap.inst().render(g);
		for (List<GameObject> team : BB.inst().getAll())
		{
			for (GameObject obj : team)
			{
				obj.render(g);
			}
		}
		
		g.popTransform();
	}
}
