package com.teamamerica.games.unicodewars.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.base.BaseObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;

public class VisualComponent extends Component
{
	private Image img;

	public VisualComponent(GameObject owner, String imgPath)
	{
		super(owner);
		try
		{
			img = new Image(imgPath);
		}
		catch (SlickException e)
		{
			img = null;
		}
		catch (NullPointerException e)
		{
			img = null;
		}
	}

	@Override
	public void update(int elapsed)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(Graphics g)
	{
		Location renderLocTL = this._parent.getPositionInPixels();
		int size = this._parent.getSize() * GameMap.inst().tileSize;

		if (this._parent instanceof MobObject)
		{
			switch (((MobObject) this._parent).getType())
			{
				case chinese:
					g.setColor(Color.blue);
					break;
				case latin:
					g.setColor(Color.green);
					break;
				case greek:
					g.setColor(Color.orange);
					break;
				case cyrillic:
					g.setColor(Color.yellow);
					break;
			}
		}
		else if (this._parent instanceof TowerBase)
		{
			switch (((TowerBase) this._parent).getType())
			{
				case cardOne:
					g.setColor(Color.red);
					break;
				case chessOne:
					g.setColor(Color.cyan);
					break;
				case currencyOne:
					g.setColor(Color.magenta);
					break;
				case diceOne:
					g.setColor(Color.white);
					break;
				case musicOne:
					g.setColor(Color.lightGray);
					break;
			}
		}
		else if (this._parent instanceof BaseObject)
		{
			g.setColor(Color.pink);
		}
		
		if (img != null)
		{
			if (this._parent instanceof MobObject)
			{
				MobObject temp = (MobObject) this._parent;
				float healthPercent = ((float) temp.getCurrentHP()) / ((float) temp.getTotalHP());
				img.setAlpha(healthPercent);
			}
			switch (this._parent.getTeam())
			{
				case Player1:
					img.draw(renderLocTL.x, renderLocTL.y, size, size, Color.blue);
					break;
				case Player2:
					img.draw(renderLocTL.x, renderLocTL.y, size, size, Color.red);
					break;
				default:
					img.draw(renderLocTL.x, renderLocTL.y, size, size);
					break;
			}
			
		}
		else
			g.fillRect(renderLocTL.x, renderLocTL.y, size, size);
		
		if (this._parent instanceof BaseObject)
		{
			g.setColor(Color.black);
			g.drawString("" + ((BaseObject) this._parent).getHealth(), renderLocTL.x + 10, renderLocTL.y + 50);
		}
	}

}
