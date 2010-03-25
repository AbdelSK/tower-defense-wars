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
import com.teamamerica.games.unicodewars.factory.BaseMaker;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.base.BaseObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Timer;

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
	private ArrayList<Location> spawnPoints;
	private ArrayList<Location> baseLocations;
	private Color gridColor;
	private Timer colorTimer;
	private int colorStage;

	public final int rows = 32; // height
	public final int columns = 64; // width
	public final int tileSize = 16;

	private GameMap()
	{
		this.map = new TileType[columns][rows];
		this.teamMap = new Team[columns][rows];
		this.spawnPoints = new ArrayList<Location>();
		this.baseLocations = new ArrayList<Location>();

		for (Team team : Team.values())
		{
			this.spawnPoints.add(new Location(-1, -1));
			this.baseLocations.add(new Location(-1, -1));
		}

		eventQueue = new LinkedList<Event>();
		this.listeners = new HashMap<Location, List<EventListener>>();
		
		this.pathFinder = new AStarPathFinder(this, 10000, false);
		
		this.gridColor = new Color(1.0f, 0, 0);
		this.colorTimer = new Timer();
		this.colorStage = 0;
	}
	
	/**
	 * Returns the single instance of GameMap
	 * 
	 * @return the GameMap instance
	 */
	public static GameMap inst()
	{
		if (_instance == null)
		{
			_instance = new GameMap();
		}
		return _instance;
	}
	
	public void LoadMap()
	{
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
		// Set up the spawn points
		this.map[(columns / 2) - 1][(rows / 2)] = TileType.Spawn;
		this.spawnPoints.set(Team.Player2.index(), new Location((columns / 2) - 1, rows / 2));
		this.map[(columns / 2)][(rows / 2)] = TileType.Spawn;
		this.spawnPoints.set(Team.Player1.index(), new Location(columns / 2, rows / 2));
		
		// Set up the bases
		BaseObject b1 = BaseMaker.MakeBase(Team.Player1, new Location(0, (rows / 2) - 2));
		b1.RegisterMapListeners();
		BaseObject b2 = BaseMaker.MakeBase(Team.Player2, new Location(columns - 4, (rows / 2) - 2));
		b2.RegisterMapListeners();
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
	
	/**
	 * Returns the path finder for the map
	 * 
	 * @return
	 */
	public PathFinder getPathFinder()
	{
		return this.pathFinder;
	}
	
	private void buildBase(BaseObject obj)
	{
		for (int i = obj.getPosition().x; i < (obj.getPosition().x + obj.getSize()); i++)
		{
			for (int j = obj.getPosition().y; j < (obj.getPosition().y + obj.getSize()); j++)
			{
				this.map[i][j] = TileType.Base;
			}
		}
	}

	/**
	 * Sets the tiles covered by a towers location to having a tower
	 * 
	 * @param obj
	 *            the tower just built. Must have location set.
	 */
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
	
	/**
	 * Register to listen for any event to happen in that location
	 * 
	 * @param loc
	 *            the location to listen to
	 * @param callback
	 *            the EventListener to call
	 */
	public void registerSpace(Location loc, EventListener callback)
	{
		if (listeners.get(loc) == null)
		{
			listeners.put(loc, new ArrayList<EventListener>());
		}
		listeners.get(loc).add(callback);
	}
	
	/**
	 * Unregister listening to that location.
	 * 
	 * @param loc
	 *            the location being listened to
	 * @param callback
	 *            the EventListener registered with this space to unregister
	 */
	public void unregisterSpace(Location loc, EventListener callback)
	{
		listeners.get(loc).remove(callback);
	}
	
	/**
	 * Sets the area once occupied by a tower to free space
	 * 
	 * @param obj
	 *            the tower being removed
	 */
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
	
	/**
	 * Checks to see whether or not it is possible to build a tower at that
	 * location
	 * 
	 * @param loc
	 *            the location to see if the tower can be built. (The upper left
	 *            tile)
	 * @param size
	 *            the size, in tiles, of one side of a tower
	 * @return true: If the tower can be built. false: otherwise
	 */
	public boolean canBuildTower(Location loc, short size, Team team)
	{
		for (int i = loc.x; i < (loc.x + size); i++)
		{
			for (int j = loc.y; j < (loc.y + size); j++)
			{
				if (this.teamMap[i][j] != team)
					return false;
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
	
	/**
	 * Meant to be called by Mobs or other moving objects. Notifies listeners of
	 * that space that that object is entering that space.
	 * 
	 * @param obj
	 *            the object entering the location
	 * @param loc
	 *            the location being visited
	 */
	public void visitSpace(GameObject obj, Location loc)
	{
		Event event = new Event(EventType.ENTER_SPACE, loc, obj);
		event.addParameter("id", obj.getId());
		dispatch(event);
	}
	
	/**
	 * Meant to be called by Mobs or other moving objects. Notifies listeners of
	 * that space that that object is leaving that space.
	 * 
	 * @param obj
	 *            the object entering the location
	 * @param loc
	 *            the location being left
	 */
	public void leaveSpace(GameObject obj, Location loc)
	{
		Event event = new Event(EventType.LEAVE_SPACE, loc, obj);
		event.addParameter("id", obj.getId());
		dispatch(event);
	}
	
	/**
	 * Returns a location object, with the location relative to pixels instead
	 * of rows and columns.
	 * 
	 * @param loc
	 *            initial location in rows and columns
	 * @return the location in pixel format
	 */
	public Location getLocationInPixels(Location loc)
	{
		Location temp = new Location(loc.x, loc.y);
		temp.x *= this.tileSize;
		temp.y *= this.tileSize;
		return temp;
	}
	
	public Location getTeamSpawnPoint(Team team)
	{
		return this.spawnPoints.get(team.index());
	}

	private void dispatch(Event e)
	{
		eventQueue.add(e);
	}
	
	/**
	 * The update method for the map, handles firing the queued events.
	 * 
	 * @param elapsed
	 *            the milliseconds elapsed from the last update
	 */
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
		
		if (this.colorTimer.xMilisecondsPassed(99))
		{
			if (this.colorStage == 0)
			{
				this.gridColor.g += 0.05f;
				if (this.gridColor.g > 0.95f)
					this.colorStage++;
			}
			else if (this.colorStage == 1)
			{
				this.gridColor.r -= 0.1f;
				if (this.gridColor.r < 0.05f)
					this.colorStage++;
			}
			else if (this.colorStage == 2)
			{
				this.gridColor.b += 0.1f;
				if (this.gridColor.b > 0.95f)
					this.colorStage++;
			}
			else if (this.colorStage == 3)
			{
				this.gridColor.g -= 0.1f;
				if (this.gridColor.g < 0.05f)
					this.colorStage++;
			}
			else if (this.colorStage == 4)
			{
				this.gridColor.r += 0.1f;
				if (this.gridColor.r > 0.95f)
					this.colorStage++;
			}
			else if (this.colorStage == 5)
			{
				this.gridColor.b -= 0.1f;
				if (this.gridColor.b < 0.05f)
					this.colorStage = 0;
			}
		}
	}
	
	/**
	 * The render method for the map, handles drawing the grid.
	 * 
	 * @param g
	 *            the graphics object to draw to
	 */
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, (columns * tileSize) / 2, rows * tileSize);
		g.setColor(Color.darkGray);
		g.fillRect((columns * tileSize) / 2, 0, (columns * tileSize) / 2, rows * tileSize);
		for (int i = 1; i < rows; i++)
		{
			g.setColor(this.gridColor);
			g.drawLine(0, i * tileSize, columns * tileSize, i * tileSize);
		}
		
		for (int i = 1; i < columns; i++)
		{
			g.setColor(this.gridColor);
			g.drawLine(i * tileSize, 0, i * tileSize, rows * tileSize);
		}
		
		// Draw borders;
		g.setColor(Color.gray);
		g.drawLine(0, 0, columns * tileSize, 0); // Top
		g.drawLine(0, rows * tileSize, columns * tileSize, rows * tileSize); // Bottom
		g.drawLine(0, 0, 0, rows * tileSize); // Left
		g.drawLine(columns * tileSize, 0, columns * tileSize, rows * tileSize); // Right
	}

}
