package com.teamamerica.games.unicodewars.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;
import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.util.Point;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.Constants;
import com.teamamerica.games.unicodewars.utils.KeyListener;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.MouseListener;
import com.teamamerica.games.unicodewars.utils.Player;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Timer;
import com.teamamerica.games.unicodewars.utils.Variable;

/**
 * This class is a shared memory between all of the different subsystems and
 * Objects in the game world.
 * 
 * @author wkerr
 * @author coby
 * 
 */
public class BB
{
	private final String MAZE_LIST_FILE_DELIMITER = ",";
	private final String MAZE_LIST_FILE_NAME = "/data/levels/MazeList.txt";
	private final String MAZE_FILE_DIR_NAME = "/data/levels/";

	private static Logger logger = Logger.getLogger(BB.class);
	private static BB _blackboard;
	private Random _random;
	private int _nextId;
	private List<Timer> _timers;
	private boolean _paused;
	private Map<Variable, Object> _variableMap;
	private List<HashMap<Location, Set<GameObject>>> _objects;
	private List<KeyListener> _keysPressed;
	private List<MouseListener> _mouseClicked;
	private HashMap<Location, MouseListener> _mouseClickedAtLocation;
	private TowerBase.Type towerSelection;
	private MobObject.Type mobTypeSelection;
	private int mobLevelSelection;
	private Display display;
	private TowerBase HUD;
	private Button currentHUD[];
	private IButtonPressedListener buttonPressedListeners[];
	private boolean HUDLayedOut;
	private Player players[];
	private boolean bAiEnabled;
	private int _gameLevel;
	private int _numGameLevels;
	private HashMap<Integer, ArrayList<String>> _listFileNames;
	private int _aNumMobsSpawned[];

	private BB()
	{
		initVarsForLevel();
		_random = new Random(System.currentTimeMillis());
		_timers = new ArrayList<Timer>();
		_gameLevel = 0;
		_listFileNames = null;
		bAiEnabled = true;
	}
	
	public static void $delete()
	{
		_blackboard = null;
	}

	public static BB inst()
	{
		if (_blackboard == null)
		{
			_blackboard = new BB();
		}
		return _blackboard;
	}
	
	public void doneLoading()
	{
		// for (List<GameObject> team : this._objects)
		// Collections.sort(team, GameObject.render);
	}
	
	public Random getRandom()
	{
		return _random;
	}
	
	/**
	 * Get the next available ID.
	 * 
	 * @return
	 */
	public int getNextId()
	{
		return _nextId++;
	}
	
	/**
	 * Returns the current level of the game
	 * 
	 * @return
	 */
	public int getGameLevel()
	{
		return _gameLevel;
	}
	
	/**
	 * Increments the game level
	 */
	public void incrementGameLevel()
	{
		_gameLevel++;
		initVarsForLevel();
	}
	
	/**
	 * @return true if there is another game level available to play, false
	 *         otherwise
	 */
	public boolean hasNextGameLevel()
	{
		return (_gameLevel > -1) && (_gameLevel + 1) < _numGameLevels;
	}
	
	/**
	 * @return the name of the maze file corresponding to the current level
	 */
	public String chooseMazeFile()
	{
		if (_listFileNames == null)
		{
			_listFileNames = new HashMap<Integer, ArrayList<String>>();
			try
			{
				URL mazeListFile = this.getClass().getResource(MAZE_LIST_FILE_NAME);
				InputStreamReader mazeListInputStream = new InputStreamReader(mazeListFile.openStream());
				BufferedReader br = new BufferedReader(mazeListInputStream);
				String curLine;
				Integer lvl;
				
				do
				{
					curLine = br.readLine();
					if (curLine != null)
					{
						String fields[] = curLine.split(MAZE_LIST_FILE_DELIMITER);
						lvl = Integer.parseInt(fields[0]);
						if (!_listFileNames.containsKey(lvl))
						{
							_listFileNames.put(lvl, new ArrayList<String>());
						}
						_listFileNames.get(lvl).add(fields[1]);
					}
				} while (curLine != null);
			}
			catch (Exception e)
			{
				System.err.println("ERROR: Problem parsing CPU's maze list file - '" + MAZE_LIST_FILE_NAME + "'");
				e.printStackTrace(System.err);
				return "";
			}
			_numGameLevels = _listFileNames.size();
		}
		
		//
		// Choose the appropriate level which will be random if the game level
		// is set to <0 otherwise it's chosen sequentially.
		// Then choose a random maze file within that level
		//
		int fileIndex;
		int level;
		if (BB.inst().getGameLevel() < 0)
		{
			level = (int) Math.round(Math.random() * (_numGameLevels - 1));
		}
		else
		{
			level = BB.inst().getGameLevel();
		}
		fileIndex = (int) Math.round(Math.random() * (_listFileNames.get(level).size() - 1));
		
		return MAZE_FILE_DIR_NAME + _listFileNames.get(level).get(fileIndex);
	}
	
	/**
	 * Get a new timer
	 * 
	 * @return a new timer
	 */
	public Timer getNewTimer()
	{
		Timer ret = new Timer();
		_timers.add(ret);
		if (_paused)
			ret.pause();
		return ret;
	}
	
	/**
	 * Pause all the game's timers
	 */
	public void pauseTimers()
	{
		for (Timer temp : _timers)
		{
			temp.pause();
		}
		_paused = true;
	}
	
	/**
	 * Unpause all the game's timers
	 */
	public void unpauseTimers()
	{
		for (Timer temp : _timers)
		{
			temp.unpause();
		}
		_paused = false;
	}

	/**
	 * Save the screen settings.
	 * 
	 * @param width
	 * @param height
	 */
	public void setScreen(int width, int height)
	{
		_variableMap.put(Variable.screenWidth, width);
		_variableMap.put(Variable.screenHeight, height);
		
		_variableMap.put(Variable.centerX, width / 2);
		_variableMap.put(Variable.centerY, height / 2);
	}
	
	/**
	 * Get one of the variables that don't change after initialization.
	 * 
	 * @param name
	 * @return
	 */
	public Object getVariableObject(Variable name)
	{
		return _variableMap.get(name);
	}
	
	public void setVariableObject(Variable name, Object value)
	{
		_variableMap.remove(name);
		_variableMap.put(name, value);
	}
	
	/**
	 * Add an object to the blackboard. An object is added to each location it
	 * covers physically
	 * 
	 * @param obj
	 * @param player
	 */
	public void addTeamObject(GameObject obj, Team player)
	{
		for (Location loc : obj.locationsCovered())
		{
			if (!_objects.get(player.index()).containsKey(loc))
			{
				_objects.get(player.index()).put(loc, new HashSet<GameObject>());
			}
			
			_objects.get(player.index()).get(loc).add(obj);
		}
	}
	
	/**
	 * Remove an object from the blackboard
	 * 
	 * @param obj
	 * @return
	 */
	public boolean removeTeamObject(GameObject obj)
	{
		boolean result = true;
		for (Location loc : obj.locationsCovered())
		{
			if (!_objects.get(obj.getTeam().index()).containsKey(loc))
			{
				continue;
			}
			
			result = _objects.get(obj.getTeam().index()).get(loc).remove(obj) ? result : false;
		}
		return true;
	}
	
	/**
	 * Update an object's location in the blackboard
	 * 
	 * @param obj
	 * @param oldVal
	 * @param newVal
	 */
	public void updateObjectLocation(GameObject obj, Location oldVal, Location newVal)
	{
		if (_objects.get(obj.getTeam().index()).containsKey(oldVal))
		{
			_objects.get(obj.getTeam().index()).get(oldVal).remove(obj);
		}
		
		if (!_objects.get(obj.getTeam().index()).containsKey(obj.getPosition()))
		{
			_objects.get(obj.getTeam().index()).put(newVal, new HashSet<GameObject>());
		}
		_objects.get(obj.getTeam().index()).get(newVal).add(obj);
	}

	public Collection<GameObject> getAll()
	{
		HashSet<GameObject> temp = new HashSet<GameObject>();
		for (Team t : Team.values())
		{
			for (Set<GameObject> l : _objects.get(t.index()).values())
			{
				temp.addAll(l);
			}
		}
		return temp;
	}
	
	public Collection<GameObject> getAllForTeam(Team team)
	{
		HashSet<GameObject> temp = new HashSet<GameObject>();
		for (Set<GameObject> l : _objects.get(team.index()).values())
		{
			temp.addAll(l);
		}
		return temp;
	}
	
	public Collection<TowerBase> getAllTowersForTeam(Team team)
	{
		HashSet<TowerBase> ret = new HashSet<TowerBase>();
		for (GameObject obj : getAllForTeam(team))
		{
			if (obj instanceof TowerBase)
			{
				ret.add((TowerBase) obj);
			}
		}
		return ret;
	}
	
	public Collection<MobObject> getAllMobsForTeam(Team team)
	{
		HashSet<MobObject> ret = new HashSet<MobObject>();
		for (GameObject obj : getAllForTeam(team))
		{
			if (obj instanceof MobObject)
			{
				ret.add((MobObject) obj);
			}
		}
		return ret;
	}

	public Collection<GameObject> getTeamObjectsAtLocation(Team team, Location loc)
	{
		HashSet<GameObject> ret = new HashSet<GameObject>();
		if (_objects.get(team.index()).containsKey(loc))
			ret.addAll(_objects.get(team.index()).get(loc));
		return ret;
		
	}
	
	public Collection<GameObject> getTeamObjectsAtLocations(Team team, Collection<Location> locs)
	{
		HashSet<GameObject> ret = new HashSet<GameObject>();
		
		for (Location loc : locs)
		{
			if (_objects.get(team.index()).containsKey(loc))
				ret.addAll(_objects.get(team.index()).get(loc));
		}
		
		return ret;
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
	
	public void keyPressed(int key)
	{
		for (KeyListener c : _keysPressed)
			c.keyPressed(key);
	}
	
	public void keyReleased(int key)
	{
		for (KeyListener c : _keysPressed)
		{
			c.keyReleased(key);
		}
	}
	
	public void addKeyListener(KeyListener c)
	{
		_keysPressed.add(c);
	}
	
	public void removeKeyListener(KeyListener c)
	{
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
		
		for (MouseListener d : _mouseClicked)
			d.MouseReleased(button, x, y);
		Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
		MouseListener c = _mouseClickedAtLocation.get(loc);
		if (c != null)
			c.MouseReleased(button, x, y);
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
	
	public TowerBase getHUD()
	{
		return this.HUD;
	}
	
	public void setHUD(TowerBase towerBase)
	{
		this.HUD = towerBase;
		if (this.HUD == null)
		{
			this.display.removeWidget(currentHUD[0]);
			this.display.removeWidget(currentHUD[1]);
			this.display.removeWidget(currentHUD[2]);
			return;
		}
		
		if (!this.HUDLayedOut)
		{
			
			buttonPressedListeners[0] = new IButtonPressedListener() {
				
				@Override
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					if (BB.inst().getHUD().canUpgrade())
						BB.inst().getHUD().doUpgrade();
					else
						currentHUD[0].setEnabled(false);
					System.out.println("Upgraded " + HUD.getType() + "(" + HUD.getId() + ") to level " + HUD.getLevel());
					BB.inst().setHUD(null);
				}
			};
			
			buttonPressedListeners[1] = new IButtonPressedListener() {
				
				@Override
				public void buttonPressed(ButtonPressedEvent arg0)
				{
					System.out.println("Sold " + HUD.getType() + "(" + HUD.getId() + ")");
					BB.inst().getHUD().sellTower();
				}
			};
			
			for (int i = 0; i < 3; i++)
			{
				currentHUD[i] = FengGUI.createWidget(Button.class);
				currentHUD[i].setShrinkable(false);
				currentHUD[i].setMultiline(true);
				currentHUD[i].setSize(128, 64);
				currentHUD[i].addButtonPressedListener(buttonPressedListeners[i]);
			}
			
			this.HUDLayedOut = true;
		}
		
		currentHUD[0].setPosition(new Point(384, 0));
		if (this.getHUD().getUpgradePrice() > getUsersPlayer().getGold())
		{
			currentHUD[0].setVisible(false);
		}
		else
		{
			currentHUD[0].setVisible(true);
		}

		currentHUD[1].setPosition(new Point(512, 0));
		currentHUD[2].setPosition(new Point(384, 64));
		currentHUD[2].setSize(128, 64);
		currentHUD[2].setEnabled(false);
		
		currentHUD[0].setText("Upgrade to level " + (this.HUD.getLevel() + 1) + "\nCost: $" + this.HUD.getUpgradePrice());
		currentHUD[1].setText("Sell for " + this.HUD.getSellPrice() + "g.");
		currentHUD[2].setText(this.HUD.getType() + " Level " + this.HUD.getLevel() + "\nAttack: " + this.HUD.getAttack() + "\nRange: " + this.HUD.getRadius() + "\nSpeed: " + this.HUD.getSpeed());
		
		if (this.HUD.canUpgrade())
		{
			if (currentHUD[0].getDisplay() == null)
				this.display.addWidget(currentHUD[0]);
		}
		else
		{
			if (currentHUD[0].getDisplay() != null)
				this.display.removeWidget(currentHUD[0]);
		}

		if (currentHUD[1].getDisplay() == null)
			this.display.addWidget(currentHUD[1]);
		if (currentHUD[2].getDisplay() == null)
			this.display.addWidget(currentHUD[2]);
	}
	
	public void checkTowerUpgradability()
	{
		if ((this.getHUD() != null) && (currentHUD != null) && (currentHUD[0] != null))
		{
			if ((!currentHUD[0].isVisible()) && (this.getHUD().getUpgradePrice() <= getUsersPlayer().getGold()))
			{
				currentHUD[0].setVisible(true);
			}
			else if ((currentHUD[0].isVisible()) && (this.getHUD().getUpgradePrice() > getUsersPlayer().getGold()))
			{
				currentHUD[0].setVisible(false);
			}

		}
	}

	public Player getUsersPlayer()
	{
		return players[Team.Player1.index()];
	}
	
	/**
	 * @param level
	 * @return the number of mobs spawned at the specified level
	 */
	public int getNumMobsSpawned(int level)
	{
		return _aNumMobsSpawned[level - 1];
	}

	public boolean spawnUsersMob(MobObject.Type type, int level)
	{
		boolean bSpawned;
		int price = MobObject.getMobPrice(type, level);
		
		if (getUsersPlayer().getGold() >= price)
		{
			bSpawned = true;
			_aNumMobsSpawned[level - 1]++;
			getUsersPlayer().addIncome(price / 2);
			getUsersPlayer().purchase(price);
			getUsersPlayer().addScore(level);
			MobMaker.MakeMob(type, level, Team.Player1);
			setMobTypeSelection(type);
			setMobLevelSelection(level);
		}
		else
		{
			bSpawned = false;
		}
		
		return bSpawned;
	}

	public boolean isAiEnabled()
	{
		return bAiEnabled;
	}
	
	public void setAiEnabled(boolean aiEnabled)
	{
		bAiEnabled = aiEnabled;
	}

	/**
	 * initializes all of the variables that need to be set to start a new level
	 */
	private void initVarsForLevel()
	{
		_nextId = 0;
		_variableMap = new HashMap<Variable, Object>();
		
		_objects = new ArrayList<HashMap<Location, Set<GameObject>>>();
		_objects.add(new HashMap<Location, Set<GameObject>>()); // Player 1's
		// objects
		_objects.add(new HashMap<Location, Set<GameObject>>()); // Player 2's
		// objects
		
		_keysPressed = new ArrayList<KeyListener>();
		_mouseClicked = new ArrayList<MouseListener>();
		_mouseClickedAtLocation = new HashMap<Location, MouseListener>();
		towerSelection = null;
		mobTypeSelection = null;
		mobLevelSelection = 1;
		players = new Player[2];
		players[0] = new Player();
		players[1] = new Player();
		
		currentHUD = new Button[3];
		buttonPressedListeners = new IButtonPressedListener[3];
		HUDLayedOut = false;
		HUD = null;
		_aNumMobsSpawned = new int[Constants.MAX_MOB_LEVEL];
		Arrays.fill(_aNumMobsSpawned, 0);
	}
}
