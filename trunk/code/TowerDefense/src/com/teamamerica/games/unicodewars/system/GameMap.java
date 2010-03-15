package com.teamamerica.games.unicodewars.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class GameMap implements TileBasedMap
{
	private enum TileType
	{
		Base, Tower, Spawn, Blocked, Free
	}
	
	private static GameMap _instance;
	
	private static Logger logger = Logger.getLogger(GameMap.class);
	private TileType[][] map;
	private Team[][] teamMap;
	private Map<Location, List<EventListener>> listeners;
	private Queue<Event> eventQueue;
	private PathFinder pathFinder;
	public final int rows = 40; // height
	public final int columns = 64; // width
	public final int tileSize = 16;

	private GameMap()
	{
		this.map = new TileType[columns][rows];
		eventQueue = new LinkedList<Event>();
		this.listeners = new HashMap<Location, List<EventListener>>();
		for (int y = 0; y < rows; y++)
		{
			for (int x = 0; x < columns; x++)
			{
				this.listeners.put(new Location(x, y), new ArrayList<EventListener>());
				this.map[x][y] = TileType.Free; // This will change
				if (x < columns / 2)
				{
					this.teamMap[x][y] = Team.Player1;
				}
				else
				{
					this.teamMap[x][y] = Team.Player2;
				}
			}
		}
		this.pathFinder = new AStarPathFinder(this, 10000, false);
	}
	
	public static GameMap inst()
	{
		if (_instance == null)
		{
			_instance = new GameMap();
		}
		return _instance;
	}
	
	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty)
	{
		if (context.getMover().getClass() != MobObject.class)
		{
			return false;
		}
		MobObject temp = (MobObject) context.getMover();
		if (teamMap[tx][ty] != temp.getTeam())
		{
			return false;
		}

		if (map[tx][ty] == TileType.Base)
		{
			return false;
		}
		else if (map[tx][ty] == TileType.Tower)
		{
			return true;
		}
		else if (map[tx][ty] == TileType.Blocked)
		{
			return true;
		}

		return false;
	}
	
	@Override
	public float getCost(PathFindingContext context, int tx, int ty)
	{
		if (context.getMover().getClass() == MobObject.class)
			return 1;
		else
			return 0;
	}
	
	@Override
	public int getHeightInTiles()
	{
		return rows;
	}
	
	@Override
	public int getWidthInTiles()
	{
		return columns;
	}
	
	@Override
	public void pathFinderVisited(int x, int y)
	{
	}
	
	public PathFinder getPathFinder()
	{
		return this.pathFinder;
	}

	public void buildTower(GameObject obj)
	{
		for (int i = obj.getPosition().x; i < (obj.getPosition().x + obj.getSize()); i++)
		{
			for (int j = obj.getPosition().y; j < (obj.getPosition().y + obj.getSize()); j++)
			{
				this.map[i][j] = TileType.Tower;
			}
		}
	}
	
	public void registerSpace(GameObject obj, EventListener callback)
	{
		listeners.get(obj.getPosition()).add(callback);
	}
	
	public void unregisterSpace(GameObject obj, EventListener callback)
	{
		listeners.get(obj.getPosition()).remove(callback);
	}

	public void removeTower(GameObject obj)
	{
		for (int i = obj.getPosition().x; i < (obj.getPosition().x + obj.getSize()); i++)
		{
			for (int j = obj.getPosition().y; j < (obj.getPosition().y + obj.getSize()); j++)
			{
				if (this.map[i][j] == TileType.Tower)
					this.map[i][j] = TileType.Free;
			}
		}
	}

	public boolean canBuildTower(Location loc, short size)
	{
		for (int i = loc.x; i < (loc.x + size); i++)
		{
			for (int j = loc.y; j < (loc.y + size); j++)
			{
				if (this.map[i][j] == TileType.Tower)
					return false;
				if (this.map[i][j] == TileType.Base)
					return false;
				if (this.map[i][j] == TileType.Spawn)
					return false;
				if (this.map[i][j] == TileType.Blocked)
					return false;
			}
		}
		return true;
	}

	public void visitSpace(GameObject obj)
	{
		Event event = new Event(EventType.ENTER_SPACE, obj.getPosition());
		event.addParameter("id", obj.getId());
		dispatch(event);
	}
	
	public void leaveSpace(GameObject obj)
	{
		Event event = new Event(EventType.LEAVE_SPACE, obj.getPosition());
		event.addParameter("id", obj.getId());
		dispatch(event);
	}

	private void dispatch(Event e)
	{
		eventQueue.add(e);
	}

	public void update(int elapsed)
	{
		List<Event> copy = new LinkedList<Event>(eventQueue);
		
		// remove everything from the queue in case new events are generated.
		eventQueue.clear();
		
		List<Event> keep = new LinkedList<Event>();
		for (Event event : copy)
		{
			// if we need to delay this event add it to the keep list.
			logger.debug("Dispatching: " + event.getId() + " at location " + event.getLocation());
			
			for (EventListener callback : listeners.get(event.getLocation()))
			{
				callback.onEvent(event);
			}
			
		}
		
		eventQueue.addAll(keep);
	}
	
	public void render(Graphics g)
	{
		// Draw borders;
		g.setColor(Color.white);
		g.drawLine(0, 0, columns * tileSize, 0); // Top
		g.drawLine(0, rows * tileSize, columns * tileSize, rows * tileSize); // Bottom
		g.drawLine(0, 0, 0, rows * tileSize); // Left
		g.drawLine(columns * tileSize, 0, columns * tileSize, rows * tileSize); // Right

		g.setColor(Color.cyan);
		for (int i = 1; i < rows; i++)
		{
			g.drawLine(0, i * tileSize, columns * tileSize, i * tileSize);
		}
		
		for (int i = 1; i < columns; i++)
		{
			g.drawLine(i * tileSize, 0, i * tileSize, rows * tileSize);
		}

	}

}
