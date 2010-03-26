package com.teamamerica.games.unicodewars.object;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.component.Component;
import com.teamamerica.games.unicodewars.system.BB;
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
	
	protected int _id;
	protected String _name;
	
	/** Location of this entity */
	protected Location _position;
	/** Size (in tiles) of this entity */
	protected short _size;
	
	protected Team _team;
	
	private int _renderPriority;
	
	private List<Component> _updateQueue;
	private List<Component> _renderQueue;
	
	private Map<String, Object> _userData;
	
	public GameObject(String name, int id, Team team, int renderPriority)
	{
		_name = name;
		_id = id;
		
		_position = new Location(-1, -1);
		_team = team;
		
		_updateQueue = new LinkedList<Component>();
		_renderQueue = new LinkedList<Component>();
		
		_renderPriority = renderPriority;
		
		_userData = new HashMap<String, Object>();
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
		_position = x;
	}
	
	/**
	 * Sets the position of this GameObject
	 * 
	 * @param v
	 */
	public void setPosition(int row, int column)
	{
		_position.x = row;
		_position.y = column;
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
		BB.inst().removeTeamObject(this);
		
		for (Component c : _updateQueue)
		{
			c.deleteComponent();
		}
	}
}
