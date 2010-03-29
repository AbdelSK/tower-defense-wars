package com.teamamerica.games.unicodewars.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;

/**
 * @author wkerr
 * 
 */
public class EventManager
{
	private static Logger logger = Logger.getLogger(EventManager.class);
	
	private static EventManager _mgr = null;
	
	private Queue<Event> _eventQueue;
	
	/** Listen for events of a given type routed to a specific game object */
	private Map<EventType, Map<Integer, List<EventListener>>> _unique;
	
	/** Listen for any events of a given type. */
	private Map<EventType, List<EventListener>> _all;
	
	private EventManager()
	{
		_eventQueue = new LinkedList<Event>();
		_unique = new HashMap<EventType, Map<Integer, List<EventListener>>>();
		_all = new HashMap<EventType, List<EventListener>>();
		
		for (EventType type : EventType.values())
		{
			_unique.put(type, new TreeMap<Integer, List<EventListener>>());
			_all.put(type, new ArrayList<EventListener>());
		}
	}
	
	public static EventManager inst()
	{
		if (_mgr == null)
			_mgr = new EventManager();
		return _mgr;
	}
	
	/**
	 * Register for all events of a given type.
	 * 
	 * @param event
	 * @param callback
	 */
	public void registerForAll(EventType event, EventListener callback)
	{
		_all.get(event).add(callback);
	}
	
	public void unregisterForAll(EventType event, EventListener callback)
	{
		_all.get(event).remove(callback);
	}

	/**
	 * Register for a subset of events for a given type. The subset includes any
	 * events that should be forwarded to the specific GameObject
	 * 
	 * @param event
	 * @param obj
	 * @param callback
	 */
	public void register(EventType event, GameObject obj, EventListener callback)
	{
		List<EventListener> listeners = _unique.get(event).get(obj.getId());
		if (listeners == null)
		{
			listeners = new ArrayList<EventListener>();
			_unique.get(event).put(obj.getId(), listeners);
			logger.debug("Registering: " + obj.getName() + " " + event);
		}
		listeners.add(callback);
	}
	
	public void dispatch(Event e)
	{
		_eventQueue.add(e);
	}
	
	/**
	 * Dispatch all of the messages that we've received ... that should go out.
	 * 
	 * @param millis
	 */
	public void update(int millis)
	{
		
		List<Event> copy = new LinkedList<Event>(_eventQueue);
		
		// remove everything from the queue in case new events are generated.
		_eventQueue.clear();
		
		List<Event> keep = new LinkedList<Event>();
		for (Event event : copy)
		{
			// if we need to delay this event add it to the keep list.
			logger.debug("Dispatching: " + event.getId());
			
			for (EventListener callback : _all.get(event.getId()))
			{
				callback.onEvent(event);
			}
			
			// Now dispatch to all of the individuals only interested in
			// this message if it belongs to them.
			List<GameObject> recipients = event.getRecipients();
			Map<Integer, List<EventListener>> map = _unique.get(event.getId());
			for (GameObject obj : recipients)
			{
				if (obj != null)
				{
					logger.debug("\tRecipient: " + obj.getName());
					List<EventListener> listeners = map.get(obj.getId());
					if (listeners != null)
					{
						// Send it out to all the callbacks listening to this
						// id.
						for (EventListener callback : listeners)
						{
							callback.onEvent(event);
						}
					}
				}
			}
		}
		
		_eventQueue.addAll(keep);
	}
}
