package com.teamamerica.games.unicodewars.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.teamamerica.games.unicodewars.object.GameObject;

/**
 * This is the base class for all events. These will be passed around when
 * different things occur.
 * 
 * @author wkerr
 * 
 */
public class Event
{
	
	private EventType _id;
	private Location _loc;
	private List<GameObject> _recipients;
	public final GameObject sender;
	
	// TODO We may want to dispacth an event in the future.
	// private long _dispatchTime;
	
	private Map<String, Object> _parameters;
	
	public Event(EventType id, Location loc, GameObject sender)
	{
		_id = id;
		_loc = loc;
		_recipients = new ArrayList<GameObject>();
		_parameters = new HashMap<String, Object>();
		this.sender = sender;
	}
	
	public Event(EventType id)
	{
		_id = id;
		_loc = null;
		_recipients = new ArrayList<GameObject>();
		_parameters = new HashMap<String, Object>();
		this.sender = null;
	}

	/**
	 * Return the ID of this event.
	 * 
	 * @return
	 */
	public EventType getId()
	{
		return _id;
	}
	
	public Location getLocation()
	{
		if (_loc != null)
		{
			return _loc;
		}
		return new Location(-1, -1);
	}

	/**
	 * Add a recipient to the list.s
	 * 
	 * @param obj
	 */
	public void addRecipient(GameObject obj)
	{
		_recipients.add(obj);
	}
	
	public List<GameObject> getRecipients()
	{
		return _recipients;
	}
	
	/**
	 * Add the parameter to the list of parameters for this event.
	 * 
	 * @param name
	 * @param obj
	 */
	public void addParameter(String name, Object obj)
	{
		_parameters.put(name, obj);
	}
	
	/**
	 * Get the value associated with given parameter.
	 * 
	 * @param name
	 * @return
	 */
	public Object getValue(String name)
	{
		return _parameters.get(name);
	}
}
