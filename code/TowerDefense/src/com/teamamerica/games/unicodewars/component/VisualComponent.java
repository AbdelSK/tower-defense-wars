package com.teamamerica.games.unicodewars.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.base.BaseObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Location;

public class VisualComponent extends Component
{
	
	public VisualComponent(GameObject owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(int elapsed)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(Graphics g)
	{
		Location renderLocTL = GameMap.inst().getLocationInPixels(this._parent.getPosition());
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
			g.setColor(Color.red);
		}
		else if (this._parent instanceof BaseObject)
		{
			g.setColor(Color.pink);
		}
		
		g.fillRect(renderLocTL.x, renderLocTL.y, size, size);
	}

}
