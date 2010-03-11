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
	/*
	 * @MobMover referencing these constants
	 * 
	 * max number of towers and mobs, assume we can cover the map for now
	 * MAX_COLS, MAX_ROWS, MIN_COL, MIN_ROW should probably be moved to the map
	 * but leaving here for now
	 */
	public static final int MAX_COLS = 38;
	public static final int MAX_ROWS = 28;
	public static final int MIN_COL = 1;
	public static final int MIN_ROW = 1;
	public static final int MAX_TOWERS = MAX_COLS * MAX_ROWS;
	public static final int MAX_MOBS = MAX_COLS * MAX_ROWS;

	boolean showVerbose;
	int mx;
	int my;
	LinkedList<Tower> towers;
	LinkedList<Mob> mobs;
	PrototypeMap searchMap;
	TiledMap map;
	AStarPathFinder pathFinder;
	MobMover mobMover;
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
		mobMover = new MobMover(this, searchMap);
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		this.currMillseconds += delta;
		this.currMillis += delta;
		Input input = container.getInput();
		
		mx = input.getMouseX();
		my = input.getMouseY();
		int mCow = mx / 16;
		int mRow = my / 16;
		
		if (input.isMouseButtonDown(0))
		{
			if (input.isKeyDown(Input.KEY_LSHIFT) && searchMap.blocked(null, mCow, mRow))
			{
				for (int i = 0; i < towers.size(); i++)
					if (towers.get(i).tileCol == mCow && towers.get(i).tileRow == mRow)
					{
						mobMover.unregisterTower(towers.get(i));
						towers.remove(i--);
						searchMap.removeBlocker(mCow, mRow);
					}
			}
			
			if (!searchMap.blocked(null, mCow, mRow) && !input.isKeyDown(Input.KEY_LSHIFT))
			{
				Tower temp = new Tower('#', mCow, mRow);
				mobMover.registerTower(temp);
				towers.add(temp);
				searchMap.addBlocker(mCow, mRow);

				for (Mob m : mobs)
					m.updatePath(pathFinder.findPath(m, m.tileCol, m.tileRow, 1, 1));
			}
		}
		else if (input.isMouseButtonDown(1))
		{
			if ((delta < 200) && !searchMap.blocked(null, mCow, mRow))
			{
				Character lol = new Character('@'); // \uF8FF = Apple logo: ð
				Mob temp = new Mob(lol, mCow, mRow);
				Path path = pathFinder.findPath(temp, mCow, mRow, 1, 1);
				temp.path = path;

				if (mobs.size() == 0)
					mobs.add(temp);
				else if (mobs.get(mobs.size() - 1).tileCol != mCow || mobs.get(mobs.size() - 1).tileRow != mRow)
					mobs.add(temp);
			}

		}
		if (input.isKeyPressed(Input.KEY_V))
			showVerbose = !showVerbose;
		if (this.currMillseconds / 5 > 1)
		{
			this.currMillseconds = 0;

			for (int i = 0; i < mobs.size(); i++)
			{
				if (mobs.get(i).path != null && mobs.get(i).path.getLength() != 0)
				{
					if (mobs.get(i).step < mobs.get(i).path.getLength())
					{
						mobMover.moveMob(mobs.get(i));
					}
					else
						mobs.get(i).path = new Path();
				}
				
				if (mobs.get(i).path == null)
				{
					for (int k = 0; k < towers.size(); k++)
						searchMap.removeBlocker(towers.get(k).tileCol, towers.get(k).tileRow);

					towers.clear();
					mobs.get(i).updatePath(pathFinder.findPath(mobs.get(i), mobs.get(i).tileCol, mobs.get(i).tileRow, 1, 1));
				}

				if (mobs.get(i).tileCol == 1 && mobs.get(i).tileRow == 1)
					mobs.get(i).hitPoints = 0;
				if (mobs.get(i).hitPoints <= 0)
					mobs.remove(i--);
			}
		}

		if (this.currMillis / 100 > 1)
		{
			this.currMillis = 0;

			for (Tower t : towers)
			{
				Mob m = t.getMobsInRange().peekFirst();
				if (m != null)
				{
					m.hitPoints -= t.damage;
					if (m.hitPoints <= 0)
					{
						mobs.remove(m);
						t.getMobsInRange().removeFirst();
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
		
        if (showVerbose)
        {
    		g.setColor(Color.cyan);
			g.drawString("row    = " + row, 0, 100);
			g.drawString("col    = " + col, 0, 120);
			g.drawString("towers = " + towers.size(), 0, 140);
			g.drawString("mobs   = " + mobs.size(), 0, 160);
			container.setShowFPS(true);
   		}
		else
			container.setShowFPS(false);

		g.setColor(Color.white);
		for (Tower i : towers)
			g.drawString("" + i.ch, i.tileCol * 16 + 4, i.tileRow * 16 - 2);

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
			g.drawString(i.ch.toString(), i.getXPixelLoc(), i.getYPixelLoc());
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
