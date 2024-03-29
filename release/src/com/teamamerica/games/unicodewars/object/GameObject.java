package com.teamamerica.games.unicodewars.object;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.component.Component;
import com.teamamerica.games.unicodewars.component.VisualComponent;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class GameObject
{
	private static Logger logger = Logger.getLogger(GameObject.class);

	/**
	 * A comparator that sorts the components by the individual render
	 * priorities Lower priorities will be first.
	 */
	public static Comparator<GameObject> render = new Comparator<GameObject>() {
		public int compare(GameObject c1, GameObject c2)
		{
			return new Integer(c1._renderPriority).compareTo(c2._renderPriority);
		}
	};
	
	public static Comparator<GameObject> birthTime = new Comparator<GameObject>() {
		public int compare(GameObject c1, GameObject c2)
		{
			return new Long(c1._createdTime).compareTo(c2._createdTime);
		}
	};

	protected int _id;
	protected String _name;
	
	/** Location of this entity */
	protected Location _position;
	/** Pixel location of this entity which is used for visual effects only. */
	/** The object is considered to be occupying the tile specified by */
	/** _position */
	protected Location _positionInPixels;
	/** Size (in tiles) of this entity */
	protected short _size;
	
	protected Team _team;
	
	private int _renderPriority;
	
	private List<Component> _updateQueue;
	private List<Component> _renderQueue;
	
	private Map<String, Object> _userData;
	protected HashSet<Location> _covering;
	
	private long _createdTime;

	public GameObject(String name, int id, Team team, int renderPriority, Location loc)
	{
		_name = name;
		_id = id;
		
		_position = loc;
		_positionInPixels = GameMap.inst().getLocationInPixels(loc);
		_team = team;
		
		_updateQueue = new LinkedList<Component>();
		_renderQueue = new LinkedList<Component>();
		
		_renderPriority = renderPriority;
		
		_userData = new HashMap<String, Object>();
		_createdTime = System.currentTimeMillis();
	}
	
	/**
	 * Return the id of this GameObject
	 * 
	 * @return
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Return the name of this GameObject
	 * 
	 * @return
	 */
	public String getName()
	{
		return _name;
	}
	
	public Team getTeam()
	{
		return _team;
	}
	
	public long getCreatedTime()
	{
		return _createdTime;
	}

	/**
	 * Add the component to the different queues
	 * 
	 * @param c
	 */
	public void addComponent(Component c)
	{
		_updateQueue.add(c);
		_renderQueue.add(c);
		
		Collections.sort(_updateQueue, Component.update);
		Collections.sort(_renderQueue, Component.render);
	}
	
	/**
	 * Called every update cycle. Allows us to update all of the components
	 * attached to this entity.
	 * 
	 * @param elapsed
	 */
	public void update(int elapsed)
	{
		for (Component c : _updateQueue)
		{
			c.update(elapsed);
		}
	}
	
	/**
	 * Called every frame in order to render this Object and its components.
	 * 
	 * @param g
	 */
	public void render(Graphics g)
	{
		g.pushTransform();
		for (Component c : _renderQueue)
		{
			c.render(g);
		}
		g.popTransform();
	}
	
	/**
	 * Return the position of this GameObject
	 * 
	 * @return
	 */
	public Location getPosition()
	{
		return _position;
	}
	
	/**
	 * Sets the position of this GameObject
	 * 
	 * @param v
	 */
	public void setPosition(Location x)
	{
		Location old = _position.copy();
		locationsCovered().remove(old);
		_position = x;
		_positionInPixels = GameMap.inst().getLocationInPixels(x);
		BB.inst().updateObjectLocation(this, old, _position);
	}
	
	/**
	 * Return the pixel location of this GameObject. This is used for visual
	 * effects only. The object is considered to be occupying whatever tile
	 * location is returned by getPosition()
	 * 
	 * @return
	 */
	public Location getPositionInPixels()
	{
		return _positionInPixels;
	}
	

	/**
	 * Sets the pixel location of this GameObject. This is used for visual
	 * effects only. The object is considered to be occupying whatever tile
	 * location is returned by getPosition()
	 */
	public void setPositionInPixels(Location x)
	{
		_positionInPixels = x;
	}
	
	/**
	 * Sets the pixel location of this GameObject. This is used for visual
	 * effects only. The object is considered to be occupying whatever tile
	 * location is returned by getPosition()
	 * 
	 * @param v
	 */
	public void setPositionInPixel(int x, int y)
	{
		_positionInPixels.x = x;
		_positionInPixels.y = y;
	}
	
	/**
	 * Returns true if the specified location is being used by the specified
	 * GameObject, false otherwise
	 * 
	 * @return boolean
	 */
	public boolean coversLocation(Location loc)
	{
		return locationsCovered().contains(loc);
	}
	
	public Collection<Location> locationsCovered()
	{
		if (_covering == null)
		{
			_covering = new HashSet<Location>();
		}
		if (_covering.size() == 0)
		{
			for (int x = _position.x; x < _position.x + _size; ++x)
			{
				for (int y = _position.y; y < _position.y + _size; ++y)
				{
					_covering.add(new Location(x, y));
				}
			}
		}
		
		return _covering;
	}

	public short getSize()
	{
		return this._size;
	}

	/**
	 * Set some user data specific to this GameObject
	 * 
	 * @param name
	 * @param data
	 */
	public void setUserData(String name, Object data)
	{
		if (_userData.containsKey(name))
			logger.error("User Data Key Collision: " + _id + " key: " + name + "");
		_userData.put(name, data);
	}
	
	/**
	 * Call this when removing/killing an object to clean things up.
	 */
	public void deleteObject()
	{
		if (BB.inst().removeTeamObject(this))
		{
			
			for (Component c : _updateQueue)
			{
				c.deleteComponent();
			}
			
			_updateQueue.clear();
			_renderQueue.clear();
		}
		else
			System.out.println("PROBLEM! Failed to remove object.");
	}
	
	protected VisualComponent getVisualComponent()
	{
		for (Component c : _renderQueue)
		{
			if (c instanceof VisualComponent)
				return (VisualComponent) c;
		}
		return null;
	}
	
}
