package com.teamamerica.games.unicodewars.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.AiMazeInstruction;
import com.teamamerica.games.unicodewars.utils.Constants;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.AiMazeInstruction.Action;

public class AiSubsystem implements Subsystem
{
	private final int UNDEFINED = -1;
	private final int MAX_MOB_MEMBER_INTERVAL = 3000;
	private final String MAZE_FILE_DELIMITER = ",";
	private final int MAZE_FILE_TYPE_INDEX = 0;
	private final int MAZE_FILE_XLOC_INDEX = 1;
	private final int MAZE_FILE_YLOC_INDEX = 2;
	private final int MOB_SPAWN_INTERVAL = 20000;
	private final int MOB_SPAWN_FIRST_INTERVAL = 5000;
	private final int STANDARD_MOB_SIZE = 25;
	private final int TOWER_BUILDING_INTERVAL = 10000;
	private final int TOWER_UPGRADE_INTERVAL = 15000;
	/* start upgrade after 7 mobs are built */
	private final int TOWER_UPGRADE_FIRST_INTERVAL = TOWER_BUILDING_INTERVAL / 2 + TOWER_BUILDING_INTERVAL * 7;
	
	private LinkedList<AiMazeInstruction> _aiMazeInstructions;
	/* counts of mobs spawned by AI separated by level */
	private int _aNumMobsSpawned[];
	/* number of times we've cycled all of the highest level mobs */
	private int _curMaxMobCycle;
	/* current level to be used for spawning mobs */
	private int _curMobLevel;
	/* interval to spawn next member, chosen randomly each time */
	private int _curMobMemberIntervalTime;
	/* current amount of time that has passed since spawning the last member */
	private int _curMobMemberWaitTime;
	/* number of members spawned so far in the current mob */
	private int _curMobMembersSpawned;
	/* number of members that will be spawned in the current mob */
	private int _curMobSize;
	/* current Type of mob to spawn */
	private MobObject.Type _curMobType;
	/* current index of the Type enum of mob to spawn */
	private int _curMobTypeIndex;
	/* current amount of time that has passed since spawning the last mob */
	private int _curMobWaitTime;
	/* current amount of time that has passed since building the last tower */
	private int _curTowerBuildWaitTime;
	/* current amount of time that has passed since upgrading the last tower */
	private int _curTowerUpgradeWaitTime;
	/* old level used to spawn mobs */
	private int _oldMobLevel;
	/* old index of the Type enum of mob to spawn */
	private int _oldMobTypeIndex;
	/* list of towers created by AI separated by level, used for upgrades */
	private LinkedList<TowerBase> _towerLists[];
	
	public AiSubsystem()
	{
		_curMaxMobCycle = 0;
		_curMobMemberIntervalTime = determineMobMemberInterval();
		_curMobMemberWaitTime = 0;
		_curMobMembersSpawned = 0;
		_curMobSize = 0;
		_curMobWaitTime = MOB_SPAWN_INTERVAL - MOB_SPAWN_FIRST_INTERVAL;
		_curTowerBuildWaitTime = 0;
		_curTowerUpgradeWaitTime = TOWER_UPGRADE_INTERVAL - TOWER_UPGRADE_FIRST_INTERVAL;
		_oldMobLevel = UNDEFINED;
		_oldMobTypeIndex = UNDEFINED;
		_towerLists = new LinkedList[Constants.MAX_TOWER_LEVEL];
		_aNumMobsSpawned = new int[Constants.MAX_MOB_LEVEL];
		Arrays.fill(_aNumMobsSpawned, 0);
		for (int i = 0; i < _towerLists.length; i++)
		{
			_towerLists[i] = new LinkedList<TowerBase>();
		}
		if (BB.inst().getGameLevel() < 0)
		{
			_curMobTypeIndex = (int) Math.round(Math.random() * (MobObject.Type.values().length - 2));
			_curMobLevel = 1;
		}
		else
		{
			_curMobTypeIndex = BB.inst().getGameLevel() % 4;
			_curMobLevel = Math.min(MobObject.Type.values().length - 1, BB.inst().getGameLevel() / 4 + 1);
		}
	}
	
	@Override
	public void finish()
	{
		
	}
	
	@Override
	public int getId()
	{
		return GameSystem.Systems.AiSubsystem.ordinal();
	}
	
	@Override
	public void render(Graphics g)
	{
		
	}
	
	@Override
	public void update(int millis)
	{
		if (BB.inst().isAiEnabled())
        {
			//
			// Handle mob spawning
			//
    		_curMobWaitTime += millis;
    		if (_curMobWaitTime > MOB_SPAWN_INTERVAL)
    		{ // time to spawn mob
    			if (_curMobMembersSpawned < _curMobSize)
    			{ // spawn more mob members
    				_curMobMemberWaitTime += millis;
    				if (_curMobMemberWaitTime > _curMobMemberIntervalTime)
    				{ // time to spawn next member
    					MobMaker.MakeMob(_curMobType, _curMobLevel, Team.Player2);
    					_curMobMembersSpawned++;
    					_curMobMemberIntervalTime = determineMobMemberInterval();
    					_curMobMemberWaitTime = 0;
    				}
    			}
    			else
    			{
    				_curMobSize = 0;
    				_curMobMembersSpawned = 0;
    				_curMobWaitTime = 0;
    			}
    		}
    		else if (_curMobSize == 0)
    		{
				_curMobType = chooseMobType();
    			_curMobSize = chooseMobSize();
    		}

    		
			//
			// Handle tower creation
			//
    		_curTowerBuildWaitTime += millis;
    		if (_curTowerBuildWaitTime > TOWER_BUILDING_INTERVAL && !_aiMazeInstructions.isEmpty())
    		{
				Collection<GameObject> collObjects = null;
				LinkedList<AiMazeInstruction> failedInstructions = new LinkedList<AiMazeInstruction>();
				;
				
				//
				// Loop until we successfully build a tower (or there aren't any
				// towers left). The instruction may be referring to a location
				// that is covered by a mob so need to temporarily skip that
				// instruction and try another one.
				//
				do
    			{
					AiMazeInstruction mazeInstruction = _aiMazeInstructions.remove();
					_curTowerBuildWaitTime = 0;
					collObjects = BB.inst().getTeamObjectsAtLocations(Team.Player1, getTowerCoveringLocations(mazeInstruction.getTowerLoc()));
					if (collObjects.isEmpty())
					{
						TowerBase tb = TowerMaker.createTower(mazeInstruction.getTowerType(), mazeInstruction.getTowerLoc(), Team.Player2);
						if (tb != null) // should never be null but just in case
						{
							_towerLists[0].add(tb);
						}
						else
						{
							System.out.println("WARNING: could not create tower at loc " + mazeInstruction.getTowerLoc().x + "." + mazeInstruction.getTowerLoc().y);
						}
					}
					else
					{
						failedInstructions.push(mazeInstruction);
					}
				} while (!collObjects.isEmpty() && !_aiMazeInstructions.isEmpty());
				
				//
				// Make sure we put any instructions that did not succeed back
				// on the list
				//
				while (!failedInstructions.isEmpty())
				{
					_aiMazeInstructions.push(failedInstructions.pop());
				}
    		}

			//
			// TODO: Handle tower upgrades
			//
    		_curTowerUpgradeWaitTime += millis;
    		if (_curTowerUpgradeWaitTime > TOWER_UPGRADE_INTERVAL)
    		{
				_curTowerUpgradeWaitTime = 0;
				upgradeTower();
			}
        }
	}
	
	@Override
	public void start()
	{
		String mazeFileName = BB.inst().chooseMazeFile();
		readDataFile(mazeFileName);
		for (int i = 0; i < _towerLists.length; i++)
		{
			_towerLists[i].clear();
		}
	}
	
	@Override
	public void end()
	{
		
	}
	
	@Override
	public void pause()
	{
	}
	
	@Override
	public void unpause()
	{
	}
	
	private void upgradeTower()
	{
		boolean bCandidateFound = false;
		int upgradeIndex = 0;
		int levelIndex = -1;
		TowerBase curCandidate = null;
		
		//
		// Find a tower at the lowest possible level and the one with the
		// highest number of escapes at that level. Don't bother looking in the
		// last level.
		//
		for (int i = 0; i < _towerLists.length - 1 && !bCandidateFound; i++)
		{
			if (!_towerLists[i].isEmpty())
			{
				bCandidateFound = true;
				levelIndex = i;
				for (int j = 1; j < _towerLists[i].size(); j++)
			    {
					if (((TowerBase) _towerLists[i].get(j)).getNumEscapes() > ((TowerBase) _towerLists[i].get(upgradeIndex)).getNumEscapes())
			    	{
						upgradeIndex = j;
			    	}
			    }
			}
		}
		
		//
		// Upgrade the tower. If the tower can still be upgraded after that then
		// put it on the next higher list, otherwise put it on the highest
		// level. This will ensure we stop upgrading when the tower is at its
		// highest level regardless of what its highest level is
		//
		if (bCandidateFound)
		{
			curCandidate = (TowerBase) _towerLists[levelIndex].remove(upgradeIndex);
			curCandidate.doUpgrade();
			if (curCandidate.canUpgrade())
			{
				_towerLists[curCandidate.getLevel() - 1].add(curCandidate);
			}
			else
			{
				_towerLists[_towerLists.length - 1].add(curCandidate);
			}
		}
	}

	private Collection<Location> getTowerCoveringLocations(Location loc)
	{
		ArrayList<Location> covering = new ArrayList<Location>(TowerBase.size * TowerBase.size);
		
		for (int x = loc.x; x < loc.x + TowerBase.size; ++x)
		{
			for (int y = loc.y; y < loc.y + TowerBase.size; ++y)
			{
				covering.add(new Location(x, y));
			}
		}
		
		return covering;
	}
	
	/**
	 * Return the highest mob level spawned by the user. This method will figure
	 * out if the user is sending higher level mobs than the AI and if so will
	 * return that level. The AI will use this information to in turn send mobs
	 * at that level. Once the AI sends a comparable number of mobs at that
	 * level then it will continue to follow it's regular pattern for choosing
	 * the mob type.
	 * 
	 * @return level which is the highest level of mobs where the user has sent
	 *         more mobs at that level than the AI
	 */
	private int getHighestOppMobLevel()
	{
		int highestLevel = 0;
		
		for (int i = 1; i < Constants.MAX_MOB_LEVEL; i++)
		{
			// TODO: need to somehow reset after sending highest in case we need
			// to send second, third, or fourth highest also
			if (_aNumMobsSpawned[i] < BB.inst().getNumMobsSpawned(i + 1))
			{
				highestLevel = i;
			}
		}
		
		return highestLevel + 1;
	}

	private MobObject.Type chooseMobType()
	{
		int maxOppMobLevel = getHighestOppMobLevel();
		// TODO: take level into account when deciding to skip mob level type
		if (_oldMobLevel >= 0)
		{
			// resume where we left off before trying to keep up with the
			// joneses
			_curMobLevel = _oldMobLevel;
			_curMobTypeIndex = _oldMobTypeIndex;
			_oldMobLevel = UNDEFINED;
			_oldMobTypeIndex = UNDEFINED;
		}
		else if (maxOppMobLevel > _curMobLevel)
		{
			// gotta keep up with the joneses
			_oldMobLevel = _curMobLevel;
			_oldMobTypeIndex = _curMobTypeIndex;
			_curMobLevel = maxOppMobLevel;
			_curMobTypeIndex = (int) Math.round(Math.random() * (Constants.MAX_MOB_LEVEL - 1));
		}
		// loop through all mobs except for the boss
		if (_curMobTypeIndex >= (MobObject.Type.values().length - 1))
		{
			_curMobTypeIndex = 0;
			if (_curMobLevel < Constants.MAX_MOB_LEVEL)
			{
				_curMobLevel++;
			}
			else
			{
				_curMaxMobCycle++;
			}
		}

		return MobObject.Type.values()[_curMobTypeIndex++];
	}
	
	/**
	 * Determines what the mob size should be. Implementation may be changed
	 * later to randomly select a size.
	 */
	private int chooseMobSize()
	{
		int mobSize;
		if (_oldMobLevel < 0)
		{
			mobSize = STANDARD_MOB_SIZE;
		}
		else
		{
			int level = getHighestOppMobLevel();
			mobSize = BB.inst().getNumMobsSpawned(level) - _aNumMobsSpawned[level - 1];
			_aNumMobsSpawned[level - 1] = BB.inst().getNumMobsSpawned(level);
			if (level <= BB.inst().getNumGameLevels() / 2)
			{
				
			}
			else
			{
				
			}
		}
		return mobSize;
	}
	
	/**
	 * Determines what the interval will be for spawning the next member of a
	 * mob
	 */
	private int determineMobMemberInterval()
	{
		return (int) Math.round(Math.random() * MAX_MOB_MEMBER_INTERVAL);
	}

	private void readDataFile(String mazeDataFileName)
	{
		try
		{
			URL mazeFile = this.getClass().getResource(mazeDataFileName);
			InputStreamReader mazeInputStream = new InputStreamReader(mazeFile.openStream());
			BufferedReader br = new BufferedReader(mazeInputStream);
			String curLine;
			
			_aiMazeInstructions = new LinkedList<AiMazeInstruction>();
			do
			{
				curLine = br.readLine();
				if (curLine != null)
				{
					String fields[] = curLine.split(MAZE_FILE_DELIMITER);
					TowerBase.Type towerType = TowerBase.Type.valueOf(fields[MAZE_FILE_TYPE_INDEX]);
					int x = Integer.valueOf(fields[MAZE_FILE_XLOC_INDEX]);
					int y = Integer.valueOf(fields[MAZE_FILE_YLOC_INDEX]);
					Location loc = new Location(x, y);
					// TODO: need to handle tower upgrades
					_aiMazeInstructions.add(new AiMazeInstruction(Action.create, towerType, loc));
				}
			} while (curLine != null);
		}
		catch (Exception e)
		{
			System.err.println("ERROR: Problem parsing CPU's maze data file - '" + mazeDataFileName + "'");
			e.printStackTrace(System.err);
			return;
		}
	}
}
