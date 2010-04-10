package com.teamamerica.games.unicodewars.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import org.fenggui.Button;
import org.fenggui.Display;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.KeyListener;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.MouseListener;
import com.teamamerica.games.unicodewars.utils.Player;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Variable;

/**
 * This class is a shared memory between all of the different subsystems and
 * Objects in the game world.
 * 
 * @author wkerr
 * @author coby
 * 
 */
public class BB {
    private static Logger logger = Logger.getLogger( BB.class );
    private static BB _blackboard;
    private Random _random;
    private int _nextId;
    private Map<Variable,Object> _variableMap;
	private List<List<GameObject>> _objects;
	private List<KeyListener>     _keysPressed;
	private List<MouseListener> _mouseClicked;
	private HashMap<Location, MouseListener> _mouseClickedAtLocation;
	private TowerBase.Type towerSelection;
	private MobObject.Type mobTypeSelection;
	private int mobLevelSelection;
	private Display display;
	private TowerBase HUD;
	private Button currentHUD[];
	private Player players[];
		
	private BB()
	{
		_random = new Random(System.currentTimeMillis());
		_nextId = 0;
		_variableMap = new HashMap<Variable,Object>();
		
		_objects = new ArrayList<List<GameObject>>();
		_objects.add(new LinkedList<GameObject>()); // Player 1's objects
		_objects.add(new LinkedList<GameObject>()); // Player 2's objects
		
		_keysPressed = new ArrayList<KeyListener>();
		_mouseClicked = new ArrayList<MouseListener>();
		_mouseClickedAtLocation = new HashMap<Location, MouseListener>();
		towerSelection = TowerBase.Type.diceOne;
		mobTypeSelection = MobObject.Type.chinese;
		mobLevelSelection = 1;
		players = new Player[2];
		players[0] = new Player();
		players[1] = new Player();
	}
		
	public static BB inst() { 
		if (_blackboard == null)
			_blackboard = new BB();

		return _blackboard;
	}
	
	public void doneLoading() { 
		for (List<GameObject> team : this._objects)
			Collections.sort(team, GameObject.render);
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
	
	public void addTeamObject(GameObject obj, Team player)
	{
		_objects.get(player.index()).add(obj);
	}
	
	public List<List<GameObject>> getAll()
	{
		return _objects;
	}
	
	public TowerBase.Type getTowerSelection()
	{
		return towerSelection;
	}
	
	public void setTowerSelection(TowerBase.Type tmpTowerSelection)
	{
		towerSelection = tmpTowerSelection;
	}

	public MobObject.Type getMobTypeSelection()
	{
		return mobTypeSelection;
	}
	
	public void setMobTypeSelection(MobObject.Type tmpMobTypeSelection)
	{
		mobTypeSelection = tmpMobTypeSelection;
	}

	public int getMobLevelSelection()
	{
		return mobLevelSelection;
	}
	
	public void setMobLevelSelection(int tmpMobLevelSelection)
	{
		mobLevelSelection = tmpMobLevelSelection;
	}

	public boolean removeTeamObject(GameObject obj)
	{
		List<GameObject> teamList = _objects.get(obj.getTeam().index());
		
		if (teamList.remove(obj))
		{
			return true;
		}

		return false;
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
	
	public void mouseClicked(int button, int x, int y)
	{
		Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
		MouseListener c = _mouseClickedAtLocation.get(loc);
		if (c != null)
			c.MouseClicked(button, x, y);
		else
		{
			for (MouseListener d : _mouseClicked)
				d.MouseClicked(button, x, y);
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
		MouseListener c = _mouseClickedAtLocation.get(loc);
		if (c != null)
			c.MouseReleased(button, x, y);
		else
		{
			for (MouseListener d : _mouseClicked)
				d.MouseReleased(button, x, y);
		}
	}
	
	public void addMouseListenerListener(MouseListener c)
	{
		_mouseClicked.add(c);
	}
	
	public void addMouseListenerForLocation(MouseListener c, Location loc)
	{
		_mouseClickedAtLocation.put(loc, c);
	}

	public void removeMouseListener(MouseListener c)
	{
		_mouseClicked.remove(c);
	}
	
	public void removeMouseListenerForLocation(Location loc)
	{
		_mouseClickedAtLocation.remove(loc);
	}
	
	public void setDisplay(Display disp)
	{
		this.display = disp;
	}

	public Display getDisplay()
	{
		return this.display;
	}
	
	public TowerBase getHUD()
	{
		return this.HUD;
	}
	
	public void setHUD(TowerBase towerBase)
	{
		this.HUD = towerBase;
	}
	
	public void setCurrentHUD(Button buttons[])
	{
		this.currentHUD = buttons;
	}
	
	public Button[] getCurrentHUD()
	{
		return this.currentHUD;
	}
	
	public Player getPlayer()
	{
		return players[0];
	}
}
