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
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		this.currMillseconds += delta;
		Input input = container.getInput();
		
		mx = input.getMouseX();
		my = input.getMouseY();
		int mrow = mx / 16;
		int mcol = my / 16;
		
		if (input.isMouseButtonDown(0))
		{
			if (!searchMap.blocked(null, mrow, mcol))
			{
				Tower temp = new Tower('#', mrow, mcol);

				if (towers.size() == 0)
				{
					towers.add(temp);
					searchMap.addBlocker(mrow, mcol);

					for (Mob m : mobs)
						m.updatePath(pathFinder.findPath(m, m.xLoc, m.yLoc, 1, 1));
				}
				else if (towers.get(towers.size() - 1).xLoc != mrow || towers.get(towers.size() - 1).yLoc != mcol)
				{
					towers.add(temp);
					searchMap.addBlocker(mrow, mcol);

					for (Mob m : mobs)
						m.updatePath(pathFinder.findPath(m, m.xLoc, m.yLoc, 1, 1));
				}
			}
		}
		else if (input.isMouseButtonDown(1))
		{
			if ((delta < 20) && !searchMap.blocked(null, mrow, mcol))
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

			for (Mob job : mobs)
			{
				if (job.path != null && job.path.getLength() != 0)
				{
					job.step++;

					if (job.step < job.path.getLength())
					{
						job.xLoc = job.path.getX(job.step);
						job.yLoc = job.path.getY(job.step);
					}
					else
						job.path = new Path();
				}
				
				if (job.xLoc == 1 && job.yLoc == 1)
					job.hitPoints = 0;
			}
		}
		
		for (int i = 0; i < mobs.size(); i++)
			if (mobs.get(i).hitPoints == 0)
				mobs.remove(i--);
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

		g.setColor(Color.red);
		for (Mob i : mobs)
			g.drawString(i.ch.toString(), i.xLoc * 16 + 3, i.yLoc * 16 - 3);
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
