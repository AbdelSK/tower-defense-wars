package prototype;

import java.util.LinkedList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

public class Prototype extends BasicGame
{

	boolean showVerbose;
	int mx;
	int my;
	LinkedList<Tower> towers;
	LinkedList<Mob> mobs;
	PrototypeMap searchMap;
	TiledMap map;
	AStarPathFinder pathFinder;
	int currMillseconds;
	int currMillis;

	public Prototype()
	{
		super("Prototype");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException
	{
		mx = 0;
		my = 0;
		towers = new LinkedList<Tower>();
		mobs = new LinkedList<Mob>();
		showVerbose = true;

		map = new TiledMap("prototype-data/map01.tmx");
		searchMap = new PrototypeMap(this.map);
		pathFinder = new AStarPathFinder(searchMap, 10000, false);
		this.currMillseconds = 0;
		this.currMillis = 0;
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		this.currMillseconds += delta;
		this.currMillis += delta;
		Input input = container.getInput();
		
		mx = input.getMouseX();
		my = input.getMouseY();
		int mrow = mx / 16;
		int mcol = my / 16;
		
		if (input.isMouseButtonDown(0))
		{
			if (input.isKeyDown(Input.KEY_LSHIFT) && searchMap.blocked(null, mrow, mcol))
			{
				for (int i = 0; i < towers.size(); i++)
					if (towers.get(i).xLoc == mrow && towers.get(i).yLoc == mcol)
					{
						towers.remove(i--);
						searchMap.removeBlocker(mrow, mcol);
					}
			}
			
			if (!searchMap.blocked(null, mrow, mcol) && !input.isKeyDown(Input.KEY_LSHIFT))
			{
				Tower temp = new Tower('#', mrow, mcol);
				towers.add(temp);
				searchMap.addBlocker(mrow, mcol);

				for (Mob m : mobs)
					m.updatePath(pathFinder.findPath(m, m.xLoc, m.yLoc, 1, 1));
			}
		}
		else if (input.isMouseButtonDown(1))
		{
			if ((delta < 200) && !searchMap.blocked(null, mrow, mcol))
			{
				Character lol = new Character('@'); // \uF8FF = Apple logo: ð
				Mob temp = new Mob(lol, mrow, mcol);
				Path path = pathFinder.findPath(temp, mrow, mcol, 1, 1);
				temp.path = path;

				if (mobs.size() == 0)
					mobs.add(temp);
				else if (mobs.get(mobs.size() - 1).xLoc != mrow || mobs.get(mobs.size() - 1).yLoc != mcol)
					mobs.add(temp);
			}

		}
		if (input.isKeyPressed(Input.KEY_V))
			showVerbose = !showVerbose;
		if (this.currMillseconds / 100 > 1)
		{
			this.currMillseconds = 0;

			for (int i = 0; i < mobs.size(); i++)
			{
				if (mobs.get(i).path != null && mobs.get(i).path.getLength() != 0)
				{
					mobs.get(i).step++;

					if (mobs.get(i).step < mobs.get(i).path.getLength())
					{
						mobs.get(i).xLoc = mobs.get(i).path.getX(mobs.get(i).step);
						mobs.get(i).yLoc = mobs.get(i).path.getY(mobs.get(i).step);
					}
					else
						mobs.get(i).path = new Path();
				}
				
				if (mobs.get(i).path == null)
				{
					for (int k = 0; k < towers.size(); k++)
						searchMap.removeBlocker(towers.get(k).xLoc, towers.get(k).yLoc);

					towers.clear();
					mobs.get(i).updatePath(pathFinder.findPath(mobs.get(i), mobs.get(i).xLoc, mobs.get(i).yLoc, 1, 1));
				}

				if (mobs.get(i).xLoc == 1 && mobs.get(i).yLoc == 1)
					mobs.get(i).hitPoints = 0;
				if (mobs.get(i).hitPoints <= 0)
					mobs.remove(i--);
			}
		}

		if (this.currMillis / 500 > 1)
		{
			this.currMillis = 0;

			for (Tower t : towers)
				for (Mob m : mobs)
					if (Math.sqrt(Math.pow(m.xLoc - t.xLoc, 2) + Math.pow(m.yLoc - t.yLoc, 2)) < t.radius)
					{
						m.hitPoints -= t.damage;
						if (m.hitPoints <= 0)
						{
							mobs.remove(m);
						}
						break;
					}
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		map.render(0, 0, 0);
		
		int row = (my / 16);
		int col = (mx / 16);
		
        if (showVerbose)
        {
    		g.setColor(Color.cyan);
			g.drawString("row    = " + row, 0, 100);
			g.drawString("col    = " + col, 0, 120);
			// g.drawString("towers = " + towers.size(), 0, 140);
			// g.drawString("mobs   = " + mobs.size(), 0, 160);
			container.setShowFPS(true);
   		}
		else
			container.setShowFPS(false);

		g.setColor(Color.white);
		for (Tower i : towers)
			g.drawString("" + i.ch, i.xLoc * 16 + 4, i.yLoc * 16 - 2);

		g.setColor(Color.blue);
		for (Mob i : mobs)
		{
			if (i.hitPoints > 90)
			{
				g.setColor(Color.blue);
			}
			else if (i.hitPoints > 80)
			{
				g.setColor(Color.cyan);
			}
			else if (i.hitPoints > 70)
			{
				g.setColor(Color.yellow);
			}
			else if (i.hitPoints > 60)
			{
				g.setColor(Color.orange);
			}
			else if (i.hitPoints > 50)
			{
				g.setColor(Color.pink);
			}
			else
			{
				g.setColor(Color.red);
			}
			g.drawString(i.ch.toString(), i.xLoc * 16 + 3, i.yLoc * 16 - 3);
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new Prototype());
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
