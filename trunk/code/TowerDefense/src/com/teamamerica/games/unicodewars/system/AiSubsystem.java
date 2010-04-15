package com.teamamerica.games.unicodewars.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.AiMazeInstruction;
import com.teamamerica.games.unicodewars.utils.Constants;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.AiMazeInstruction.Action;

public class AiSubsystem implements Subsystem
{
	private final int MAX_MOB_MEMBER_INTERVAL = 3000;
	private final String MAZE_FILE_DELIMITER = ",";
	private final String MAZE_FILE_NAME = "src/data/levels/maze1.txt";
	private final int MAZE_FILE_TYPE_INDEX = 0;
	private final int MAZE_FILE_XLOC_INDEX = 1;
	private final int MAZE_FILE_YLOC_INDEX = 2;
	private final int MOB_SPAWN_INTERVAL = 40000;
	private final int MOB_SPAWN_FIRST_INTERVAL = 5000;
	private final int TOWER_BUILDING_INTERVAL = 5000;
	
	private LinkedList<AiMazeInstruction> _aiMazeInstructions;
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
	private int _curTowerWaitTime;
	
	public AiSubsystem()
	{
		_curMobMemberIntervalTime = determineMobMemberInterval();
		_curMobMemberWaitTime = 0;
		_curMobMembersSpawned = 0;
		_curMobLevel = 1;
		_curMobTypeIndex = 0;
		_curMobSize = 0;
		_curMobWaitTime = MOB_SPAWN_INTERVAL - MOB_SPAWN_FIRST_INTERVAL;
		_curTowerWaitTime = 0;
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
    			_curMobSize = chooseMobSize();
    			_curMobType = chooseMobType();
    		}
    		_curTowerWaitTime += millis;
    		if (_curTowerWaitTime > TOWER_BUILDING_INTERVAL && !_aiMazeInstructions.isEmpty())
    		{
    			AiMazeInstruction mazeInstruction = _aiMazeInstructions.remove();
    			_curTowerWaitTime = 0;
    			if (mazeInstruction.getAction() == Action.create)
    			{
    				TowerMaker.createTower(mazeInstruction.getTowerType(), mazeInstruction.getTowerLoc(), Team.Player2);
    			}
    			else if (mazeInstruction.getAction() == Action.upgrade)
    			{
    				//TODO: need to handle tower upgrades
    			}
    		}
        }
	}
	
	@Override
	public void start()
	{
		readDataFile(MAZE_FILE_NAME);
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
	
	private MobObject.Type chooseMobType()
	{
		if (_curMobTypeIndex >= MobObject.Type.values().length)
		{
			_curMobTypeIndex = 0;
			if (_curMobLevel < Constants.MAX_MOB_LEVEL)
			{
				_curMobLevel++;
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
		return 20;
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
			File mazeDataFile = new File(mazeDataFileName);
			FileReader mazeDataFileInputStream = new FileReader(mazeDataFile);
			BufferedReader br = new BufferedReader(mazeDataFileInputStream);
			String curLine;
			int i = 0;
			
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
