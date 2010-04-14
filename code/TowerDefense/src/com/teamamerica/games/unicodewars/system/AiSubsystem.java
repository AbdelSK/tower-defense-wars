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
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.AiMazeInstruction.Action;

public class AiSubsystem implements Subsystem
{
	private final String MAZE_FILE_DELIMITER = ",";
	private final String MAZE_FILE_NAME = "src/data/levels/maze1.txt";
	private final int MAZE_FILE_TYPE_INDEX = 0;
	private final int MAZE_FILE_XLOC_INDEX = 1;
	private final int MAZE_FILE_YLOC_INDEX = 2;
	private final int TOWER_BUILDING_INTERVAL = 5000;
	private final int MOB_SPAWN_INTERVAL = 2000;
	
	private LinkedList<AiMazeInstruction> _aiMazeInstructions;
	private boolean _paused = false;
	private int _curMobWaitTime;
	private int _curTowerWaitTime;
	private int _mobLevel;
	
	public AiSubsystem()
	{
		_curMobWaitTime = 0;
		_curTowerWaitTime = 0;
		_mobLevel = 1;
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
		_curMobWaitTime += millis;
		_curTowerWaitTime += millis;
		if (_curMobWaitTime > MOB_SPAWN_INTERVAL)
		{
			_curMobWaitTime = 0;
			MobMaker.MakeMob(chooseMobType(), _mobLevel, Team.Player2);
		}
		if (_curTowerWaitTime > TOWER_BUILDING_INTERVAL)
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
		this._paused = true;
	}
	
	@Override
	public void unpause()
	{
		this._paused = false;
	}
	
	private MobObject.Type chooseMobType()
	{
		return MobObject.Type.latin;
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
