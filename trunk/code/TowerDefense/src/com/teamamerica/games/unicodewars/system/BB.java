package com.teamamerica.games.unicodewars.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.utils.KeyListener;
import com.teamamerica.games.unicodewars.utils.Variable;

/**
 * This class is a shared memory between all of
 * the different subsystems and Objects in the
 * game world.
 * 
 * @author wkerr
 *
 */
public class BB {
    private static Logger logger = Logger.getLogger( BB.class );

    private static BB _blackboard;
    
    private Random _random;
    private int _nextId;
    
    private Map<Variable,Object> _variableMap;
    
	private List<GameObject>      _objects;	
	
	private List<KeyListener>     _keysPressed;
		
	private BB() { 
		_random = new Random(System.currentTimeMillis());
		_nextId = 0;
		_variableMap = new HashMap<Variable,Object>();
		
		_objects = new LinkedList<GameObject>();
		
		_keysPressed = new ArrayList<KeyListener>();
		
		_variableMap.put(Variable.maxAnglularAcceleration, 2.0f);
	}
		
	public static BB inst() { 
		if (_blackboard == null) { 
			_blackboard = new BB();
		}
		return _blackboard;
	}
	
	public void doneLoading() { 
		Collections.sort(_objects, GameObject.render);
	}
	
	public Random getRandom() { 
		return _random;
	}
	
	/**
	 * Get the next available ID.
	 * @return
	 */
	public int getNextId() { 
		return _nextId++;
	}
	
	/**
	 * Save the screen settings.
	 * @param width
	 * @param height
	 */
	public void setScreen(int width, int height) { 
		_variableMap.put(Variable.screenWidth, width);
		_variableMap.put(Variable.screenHeight, height);
		
		_variableMap.put(Variable.centerX, width/2);
		_variableMap.put(Variable.centerY, height/2);
	}
	
	/**
	 * Get one of the variables that don't change after 
	 * initialization.
	 * @param name
	 * @return
	 */
	public Object getVariableObject(Variable name) { 
		return _variableMap.get(name);
	}
	
	public void setVariableObject(Variable name, Object value) { 
		_variableMap.remove(name);
		_variableMap.put(name, value);
	}
	
	public void add(GameObject obj) { 
		_objects.add(obj);
	}
	
	public Collection<GameObject> getAll() { 
		return _objects;
	}
	
	
	public void keyPressed(int key) { 
		for (KeyListener c : _keysPressed) 
			c.keyPressed(key);
	}
	
	public void keyReleased(int key) { 
		for (KeyListener c : _keysPressed) { 
			c.keyReleased(key);
		}
	}
	
	public void addKeyListener(KeyListener c) { 
		_keysPressed.add(c);
	}
	
	public void removeKeyListener(KeyListener c) { 
		_keysPressed.remove(c);
	}
}
