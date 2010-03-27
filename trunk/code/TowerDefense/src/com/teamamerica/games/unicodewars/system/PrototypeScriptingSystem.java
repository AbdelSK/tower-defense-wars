package com.teamamerica.games.unicodewars.system;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.factory.TowerMaker;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.utils.KeyListener;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.MouseListener;
import com.teamamerica.games.unicodewars.utils.Team;

public class PrototypeScriptingSystem implements Subsystem
{

	private int towerRow, towerCol;
	
	public PrototypeScriptingSystem()
	{

		BB.inst().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(int key)
			{
				
			}
			
			@Override
			public void keyReleased(int key)
			{
				if (key == Input.KEY_M)
				{
					MobMaker.MakeMobChinese(1, Team.Player1);
				}
				
				if (key == Input.KEY_T)
				{
					buildTowersInRows();
				}
			}
		});
		
		BB.inst().addMouseListenerListener(new MouseListener() {
			private int tx[] = { -1, -1 };
			private int ty[] = { -1, -1 };
			
			@Override
			public void MouseReleased(int button, int x, int y)
			{
				if (tx[button] > 0 && ty[button] > 0)
					handleClickEvent(button, tx[button], ty[button]);
				tx[button] = -1;
				ty[button] = -1;
			}
			
			@Override
			public void MouseClicked(int button, int x, int y)
			{
				tx[button] = x;
				ty[button] = y;
			}
		});
		
		towerCol = (GameMap.inst().columns * 3) / 4;
		towerRow = 0;
	}
	
	public void handleClickEvent(int button, int x, int y)
	{
		Location loc = GameMap.inst().getGridLocationFromPixels(x, y);
		if (loc != null)
		{
			if (button == Input.MOUSE_LEFT_BUTTON)
			{
				if (GameMap.inst().canBuildTower(loc, (short) 2, Team.Player2))
				{
					TowerMaker.MakeDiceTower(loc, Team.Player2);
				}
			}
			else if (button == Input.MOUSE_RIGHT_BUTTON)
			{
				TowerBase tower = GameMap.inst().getTowerAtLoc(loc);
				if (tower != null)
				{
					tower.deleteObject();
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
		return 0;
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(int eps)
	{
		// TODO Auto-generated method stub
		
	}
	
}
