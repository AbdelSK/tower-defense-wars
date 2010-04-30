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
import com.teamamerica.games.unicodewars.utils.Team;

public class VisualComponent extends Component
{
	private Image img;

	public VisualComponent(GameObject owner, String imgPath)
	{
		super(owner);
		this.updateImage(imgPath);
	}
	
	public void updateImage(String imgPath)
	{
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
		int size = (this._parent.getSize() * GameMap.inst().tileSize) - 1;

		if (this._parent instanceof TowerBase)
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
				switch (temp.getType())
				{
					case chinese:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.red.brighter());
						break;
					case latin:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.blue.brighter());
						break;
					case greek:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.green.brighter());
						break;
					case cyrillic:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.orange.brighter());
						break;
					default:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.cyan.brighter());
						break;
				}
			}
			else
			{
				switch (this._parent.getTeam())
				{
					case Player1:
						g.setColor(Color.white);
						g.getColor().a = .5f;
						g.fillRect(renderLocTL.x + 1, renderLocTL.y + 1, size, size);
						img.draw(renderLocTL.x + 1, renderLocTL.y, size + 1, size, new Color(0, 0, 128));
						break;
					case Player2:
						g.setColor(Color.darkGray);
						g.fillRect(renderLocTL.x + 1, renderLocTL.y + 1, size, size);
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size, Color.red);
						break;
					default:
						img.draw(renderLocTL.x + 1, renderLocTL.y + 1, size, size);
						break;
				}
			}
			
		}
		else
			g.fillRect(renderLocTL.x + 1, renderLocTL.y + 1, size, size);
		
		if (this._parent instanceof BaseObject)
		{
			if (this._parent.getTeam() == Team.Player2)
			{
				g.setColor(Color.white);
			}
			else
			{
				g.setColor(Color.red);
			}
			int health = ((BaseObject) this._parent).getHealth();
			g.drawRect(renderLocTL.x + 5, renderLocTL.y + 5, size - 10, 5);
			float barLength = (size - 10) * (health / 200.0f);
			g.fillRect(renderLocTL.x + 5, renderLocTL.y + 5, barLength, 5);
			g.drawString("" + health, renderLocTL.x + 10, renderLocTL.y + 45);
		}

	}
}
