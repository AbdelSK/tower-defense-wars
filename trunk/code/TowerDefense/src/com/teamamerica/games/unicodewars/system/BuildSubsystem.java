package com.teamamerica.games.unicodewars.system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BuildSubsystem implements Subsystem
{

	private int towerRow, towerCol;
	private Input input;
	private InputListener inputListener;
	private boolean shiftHeld = false;
	private int mouseLocation[];
	
	public BuildSubsystem(GameContainer container)
	{
		input = container.getInput();
		mouseLocation = new int[2];
		inputListener = new InputListener() {
			
			@Override
			public void controllerUpReleased(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerUpPressed(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerRightReleased(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerRightPressed(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerLeftReleased(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerLeftPressed(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerDownReleased(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerDownPressed(int controller)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerButtonReleased(int controller, int button)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controllerButtonPressed(int controller, int button)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(int key, char c)
			{
				if (key == Input.KEY_RSHIFT || key == Input.KEY_LSHIFT)
				{
					shiftHeld = false;
				}
			}
			
			@Override
			public void keyPressed(int key, char c)
			{
				if (key == Input.KEY_RSHIFT || key == Input.KEY_LSHIFT)
				{
					shiftHeld = true;
				}
			}
			
			@Override
			public void setInput(Input input)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isAcceptingInput()
			{
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public void inputEnded()
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseWheelMoved(int change)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseReleased(int button, int x, int y)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(int button, int x, int y)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseMoved(int oldx, int oldy, int newx, int newy)
			{
				mouseLocation[0] = newx;
				mouseLocation[1] = newy;
			}
			
			@Override
			public void mouseClicked(int button, int x, int y, int clickCount)
			{
				if (clickCount == 1)
				{
					handleClickEvent(button, x, y);
				}
			}
		};
	}
	
	public void start()
	{
		towerCol = (GameMap.inst().columns * 3) / 4;
		towerRow = 0;
		
		input.addListener(inputListener);
	}
	
	@Override
	public void end()
	{
		input.removeListener(inputListener);
	}

	public void handleClickEvent(int button, int x, int y)
	{
		if (BB.inst().getTowerSelection() != null)
		{
			Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
			if (loc != null)
			{
				if (button == Input.MOUSE_LEFT_BUTTON)
				{
					if (GameMap.inst().canBuildTower(loc, (short) 2, Team.Player2))
					{
						TowerMaker.createTower(BB.inst().getTowerSelection(), loc, Team.Player2);
						if (!shiftHeld)
							BB.inst().setTowerSelection(null);
					}
				}
			}
		}

	}
	
	public void buildTowersInRows()
	{
		Location loc = new Location(towerCol, towerRow);
		if (GameMap.inst().canBuildTower(loc, TowerBase.size, Team.Player2))
		{
			TowerMaker.MakeDiceTower(loc, Team.Player2);
			towerRow += TowerBase.size;
			if (towerRow >= GameMap.inst().rows)
			{
				towerRow = 0;
				towerCol += TowerBase.size;
			}
		}
	}

	@Override
	public void finish()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getId()
	{
		// TODO Auto-generated method stub
		return GameSystem.Systems.BuildSubsystem.ordinal();
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int eps)
	{
		
	}
	
}
