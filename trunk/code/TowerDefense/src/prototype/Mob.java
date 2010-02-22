package prototype;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

public class Mob implements Mover
{
	public boolean alive;
	public int xLoc;
	public int yLoc;
	public char ch;
	public Path path;
	public int step;
	
	public Mob(char ch, int xLoc, int yLoc)
	{
		this.ch = ch;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.alive = true;
		this.path = new Path();
		this.step = 0;
	}
	
	public void updatePath(Path p)
	{
		path = p;
		step = 0;
	}
}
