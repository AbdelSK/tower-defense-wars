package prototype;
import java.util.ArrayList;
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

public class SimpleTest extends BasicGame
{
	
	int mx;
	int my;
	ArrayList<Tower> towers;
	ArrayList<Mob> mobs;
	PrototypeMap searchMap;
	TiledMap map;
	AStarPathFinder pathFinder;
	int currMillseconds;

	public SimpleTest()
	{
		super("Prototype");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException
	{
		mx = 0;
		my = 0;
		towers = new ArrayList<Tower>();
		mobs = new ArrayList<Mob>();
		
		map = new TiledMap("data/map01.tmx");
		searchMap = new PrototypeMap(this.map);
		pathFinder = new AStarPathFinder(searchMap, 10000, true);
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
				if (towers.size() == 0)
					towers.add(new Tower('A', mrow, mcol));
				else if (towers.get(towers.size() - 1).xLoc != mrow || towers.get(towers.size() - 1).yLoc != mcol)
					towers.add(new Tower('A', mrow, mcol));
			}
		}
		else if (input.isMouseButtonDown(1))
		{
			if ((delta < 20) && !searchMap.blocked(null, mrow, mcol))
			{
				Mob temp = new Mob('x', mrow, mcol);
				Path path = pathFinder.findPath(temp, mrow, mcol, 1, 1);
				temp.path = path;
				if (mobs.size() == 0)
					mobs.add(temp);
				else if (mobs.get(mobs.size() - 1).xLoc != mrow || mobs.get(mobs.size() - 1).yLoc != mcol)
					mobs.add(temp);
			}

		}
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
					{
						job.path = new Path();
					}
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		map.render(0, 0, 0);
		
		int row = (my / 16);
		int col = (mx / 16);
		
		g.setColor(Color.cyan);
		g.drawString("row = " + row, 0, 100);
		g.drawString("col = " + col, 0, 200);

		g.setColor(Color.white);
		for (Tower i : towers)
			g.drawString("" + i.ch, i.xLoc * 16, i.yLoc * 16);
		for (Mob i : mobs)
			g.drawString("" + i.ch, i.xLoc * 16, i.yLoc * 16);

	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new SimpleTest());
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
