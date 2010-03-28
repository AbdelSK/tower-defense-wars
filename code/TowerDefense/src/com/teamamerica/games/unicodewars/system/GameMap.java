package com.teamamerica.games.unicodewars.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.heuristics.ManhattanHeuristic;
import com.teamamerica.games.unicodewars.factory.BaseMaker;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.base.BaseObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
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
	private HashMap<Location, List<EventListener>> listeners;
	private HashMap<Location, TowerBase> hmTowers;
	private Queue<Event> eventQueue;
	private PathFinder pathFinder;
	private ArrayList<Location> spawnPoints;
	private ArrayList<Path> spawnPaths;
	private ArrayList<Location> baseLocations;
	private Color gridColor;
	private Timer colorTimer;
	private int colorStage;
	private int alphaStage;

	public final int rows = 32; // height
	public final int columns = 64; // width
	public final int tileSize = 16;

	private GameMap()
	{
		this.map = new TileType[columns][rows];
		this.teamMap = new Team[columns][rows];
		this.spawnPoints = new ArrayList<Location>();
		this.baseLocations = new ArrayList<Location>();
		this.spawnPaths = new ArrayList<Path>();

		for (Team team : Team.values())
		{
			this.spawnPoints.add(new Location(-1, -1));
			this.baseLocations.add(new Location(-1, -1));
			this.spawnPaths.add(null);
		}

		eventQueue = new LinkedList<Event>();
		this.listeners = new HashMap<Location, List<EventListener>>();
		this.hmTowers = new HashMap<Location, TowerBase>();
		
		this.pathFinder = new AStarPathFinder(this, 10000, false, new ManhattanHeuristic(0));
		
		this.gridColor = new Color(1.0f, 0, 0);
		this.colorTimer = new Timer();
		this.colorStage = 0;
		this.alphaStage = 0;
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
		Location b1Loc = new Location(0, (rows / 2) - (BaseObject.size / 2));
		BaseObject b1 = BaseMaker.MakeBase(Team.Player1, b1Loc);
		buildBase(b1);
		this.baseLocations.set(Team.Player1.index(), new Location(b1Loc.x + 2, b1Loc.y + 2));
		Location b2Loc = new Location(columns - 4, (rows / 2) - (BaseObject.size / 2));
		BaseObject b2 = BaseMaker.MakeBase(Team.Player2, b2Loc);
		buildBase(b2);
		this.baseLocations.set(Team.Player2.index(), new Location(b2Loc.x + 2, b2Loc.y + 2));
		
		// Set up the spawn paths
		for (Team team : Team.values())
		{
			Location spawn;
			Location base;
			
			switch (team)
			{
				case Player1:
					spawn = this.spawnPoints.get(Team.Player1.index());
					base = this.baseLocations.get(Team.Player2.index());
					break;
				case Player2:
					spawn = this.spawnPoints.get(Team.Player2.index());
					base = this.baseLocations.get(Team.Player1.index());
					break;
				default:
					spawn = this.spawnPoints.get(Team.Player1.index());
					base = this.baseLocations.get(Team.Player2.index());
					break;
			}
			Path path = this.pathFinder.findPath(null, spawn.x, spawn.y, base.x, base.y);
			this.spawnPaths.set(team.index(), path);
		}

	}
	
	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty)
	{
		if (tx < 0 && tx >= columns && ty < 0 && ty >= rows)
			return true;

		if ((context.getMover() instanceof MobObject))
		{
			MobObject temp = (MobObject) context.getMover();
			if (teamMap[tx][ty] == temp.getTeam())
			{
				return true;
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
		}

		return false;
	}
	
	@Override
	public float getCost(PathFindingContext context, int tx, int ty)
	{
		if (context.getMover() instanceof MobObject)
			return 1;
		else if (context.getMover() == null)
			return 1;
		else
			return 0;
	}
	
	@Override
	public int getHeightInTiles()
	{
		return rows;
	}
	
	public TowerBase getTowerAtLoc(Location loc)
	{
		return hmTowers.get(loc);
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
	
	public Path getSpawnPath(Team team)
	{
		return this.spawnPaths.get(team.index());
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
		
		if (updateDefaultMobPath(obj.getTeam()) == null)
		{
			obj.deleteObject();
		}
		else
		{
			hmTowers.put(obj.getPosition(), (TowerBase) obj);
		}
	}
	
	private Path updateDefaultMobPath(Team team)
	{
		Team self, enemy;
		switch (team)
		{
			case Player1:
				self = Team.Player1;
				enemy = Team.Player2;
				break;
			case Player2:
				self = Team.Player2;
				enemy = Team.Player1;
				break;
			default:
				self = Team.Player1;
				enemy = Team.Player2;
		}
		MobObject dummy = new MobObject(null, colorStage, colorStage, null, enemy, colorStage, MobObject.Type.chinese) {};
		Location spawn = this.getTeamSpawnPoint(enemy);
		Location base = this.getTeamBaseLocation(self);
		Path path = this.pathFinder.findPath(dummy, spawn.x, spawn.y, base.x, base.y);

		if (path != null)
		{
			this.spawnPaths.set(enemy.index(), path);
		}
		dummy = null;
		
		return path;
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
		hmTowers.remove(obj.getPosition());
		updateDefaultMobPath(obj.getTeam());
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
				if (i >= columns || j >= rows)
					return false;
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
		Location temp;
		
		if (loc == null)
		{
			temp = null;
		}
		else
		{
			temp = new Location(loc.x, loc.y);
			temp.x *= this.tileSize;
			temp.y *= this.tileSize;
		}
		return temp;
	}
	
	/**
	 * Returns a location object, with the location relative to pixels instead
	 * of rows and columns.
	 * 
	 * @param step
	 *            Step object which specifies a tile location
	 * @return the location in pixel format
	 */
	public Location getLocationInPixels(Step step)
	{
		Location temp = new Location(step.getX(), step.getY());
		temp.x *= this.tileSize;
		temp.y *= this.tileSize;
		return temp;
	}

	/**
	 * Returns a new location object with the location relative to the grid
	 * location from a pixel based location.
	 * 
	 * @param tx
	 *            the pixel location x component
	 * @param ty
	 *            the pixel location y component
	 * @return the new grid based location
	 */
	public Location getGridLocationFromPixels(int tx, int ty)
	{
		if (tx > 0 && tx < (columns * tileSize) && ty > 0 && ty < (rows * tileSize))
		{
			int x = tx / tileSize;
			int y = ty / tileSize;
			return new Location(x, y);
		}
		return null;
	}

	/**
	 * Gets the spawn point for that team's location
	 * 
	 * @param team
	 *            the team to get the spawn point for
	 * @return the spawn point location
	 */
	public Location getTeamSpawnPoint(Team team)
	{
		return this.spawnPoints.get(team.index());
	}
	
	/**
	 * Gets the location of the base for a team.
	 * 
	 * @param team
	 *            the team to find the base for
	 * @return the base's location
	 */
	public Location getTeamBaseLocation(Team team)
	{
		Location temp = this.baseLocations.get(team.index()).copy();
		return temp;
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
			
			if (listeners.get(event.getLocation()) == null)
			{
				listeners.put(event.getLocation(), new ArrayList<EventListener>());
			}
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
			
			switch (this.alphaStage)
			{
				case 0:
					this.gridColor.a -= 0.1f;
					if (this.gridColor.a < 0.1f)
						this.alphaStage++;
					break;
				case 1:
					this.gridColor.a += 0.1f;
					if (this.gridColor.a > 0.9f)
						this.alphaStage--;
					break;
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
		for (Location loc : this.spawnPoints)
		{
			g.setColor(Color.green);
			g.fillRect(loc.x * tileSize, loc.y * tileSize, tileSize, tileSize);
		}
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