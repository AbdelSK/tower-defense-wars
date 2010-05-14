package com.teamamerica.games.unicodewars.system;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class BuildSubsystem implements Subsystem
{
	private Input input;
	private InputListener inputListener;
	private boolean shiftHeld = false;
	private boolean paused = false;
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
				
				if (key == Input.KEY_ESCAPE)
				{
					BB.inst().setTowerSelection(null);
				}
				
				MobObject.Type type = null;
				int level = 0;
				
				switch (key)
				{
					case Input.KEY_1:
						type = MobObject.Type.chinese;
						level = 1;
						break;
					case Input.KEY_2:
						type = MobObject.Type.chinese;
						level = 2;
						break;
					case Input.KEY_3:
						type = MobObject.Type.chinese;
						level = 3;
						break;
					case Input.KEY_4:
						type = MobObject.Type.chinese;
						level = 4;
						break;
					case Input.KEY_5:
						type = MobObject.Type.chinese;
						level = 5;
						break;
					case Input.KEY_Q:
						type = MobObject.Type.latin;
						level = 1;
						break;
					case Input.KEY_W:
						type = MobObject.Type.latin;
						level = 2;
						break;
					case Input.KEY_E:
						type = MobObject.Type.latin;
						level = 3;
						break;
					case Input.KEY_R:
						type = MobObject.Type.latin;
						level = 4;
						break;
					case Input.KEY_T:
						type = MobObject.Type.latin;
						level = 5;
						break;
					case Input.KEY_A:
						type = MobObject.Type.greek;
						level = 1;
						break;
					case Input.KEY_S:
						type = MobObject.Type.greek;
						level = 2;
						break;
					case Input.KEY_D:
						type = MobObject.Type.greek;
						level = 3;
						break;
					case Input.KEY_F:
						type = MobObject.Type.greek;
						level = 4;
						break;
					case Input.KEY_G:
						type = MobObject.Type.greek;
						level = 5;
						break;
					case Input.KEY_Z:
						type = MobObject.Type.cyrillic;
						level = 1;
						break;
					case Input.KEY_X:
						type = MobObject.Type.cyrillic;
						level = 2;
						break;
					case Input.KEY_C:
						type = MobObject.Type.cyrillic;
						level = 3;
						break;
					case Input.KEY_V:
						type = MobObject.Type.cyrillic;
						level = 4;
						break;
					case Input.KEY_B:
						type = MobObject.Type.cyrillic;
						level = 5;
						break;
				}
				
				if (type != null)
				{
					BB.inst().spawnUsersMob(type, level);
				}
			}
			
			@Override
			public void keyPressed(int key, char c)
			{
				if (key == Input.KEY_RSHIFT || key == Input.KEY_LSHIFT)
				{
					shiftHeld = true;
				}
				
				if (key == Input.KEY_ESCAPE)
				{
					BB.inst().setTowerSelection(null);
					BB.inst().setHUD(null);
				}
				
				if (key == Input.KEY_BACK || key == Input.KEY_DELETE)
					if (BB.inst().getHUD() != null)
						BB.inst().getHUD().sellTower();

				if (key == Input.KEY_U)
					if (BB.inst().getHUD() != null)
						if (BB.inst().getHUD().canUpgrade() && (BB.inst().getHUD().getUpgradePrice() <= BB.inst().getUsersPlayer().getGold()) )
						{
							BB.inst().getHUD().doUpgrade();
							BB.inst().setHUD(BB.inst().getHUD());
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
				return !paused;
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
		input.addListener(inputListener);
	}
	
	@Override
	public void end()
	{
		input.removeListener(inputListener);
	}

	public void handleClickEvent(int button, int x, int y)
	{
		if (button == Input.MOUSE_LEFT_BUTTON)
		{
			if (BB.inst().getTowerSelection() != null) // Build mode
			{
				Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
				if (loc != null)
				{
					if (button == Input.MOUSE_LEFT_BUTTON)
					{
						if (GameMap.inst().canBuildTower(loc, (short) 2, Team.Player1))
						{
							if (BB.inst().isAiEnabled())
							{
								TowerMaker.createTower(BB.inst().getTowerSelection(), loc, Team.Player1);
							}
							else
							{
								TowerMaker.createTower(BB.inst().getTowerSelection(), loc, Team.Player2);
							}
							if (!shiftHeld)
								BB.inst().setTowerSelection(null);
						}
					}
				}
			}
			else
			// Not build mode
			{
				Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
				if (loc != null)
				{
					if (BB.inst().getHUD() != null)
					{
						BB.inst().setHUD(null);
					}
				}
			}
		}
		else
		{
			BB.inst().setTowerSelection(null);
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

		if (BB.inst().getTowerSelection() != null)
		{
			Location loc = GameMap.inst().getGridLocationFromPixels(mouseLocation[0], mouseLocation[1]);
			if (loc != null)
			{
				int tileSize = GameMap.inst().tileSize;
				int x = loc.x * tileSize;
				int y = loc.y * tileSize;

				g.setClip(0, 0, GameMap.inst().columns * tileSize, GameMap.inst().rows * tileSize);
				Color drawColor;
				if (GameMap.inst().canBuildTower(loc, (short) 2, Team.Player1))
					drawColor = Color.green;
				else
					drawColor = Color.red;
				g.setColor(drawColor);
				g.fillRect(x, y, TowerBase.size * tileSize, TowerBase.size * tileSize);
				g.clearClip();
			}
		}
	}

	@Override
	public void update(int eps)
	{
		
	}
	
	@Override
	public void pause()
	{
		this.paused = true;
		
	}
	
	@Override
	public void unpause()
	{
		this.paused = false;
		
	}
	
}
