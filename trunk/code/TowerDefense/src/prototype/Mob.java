package prototype;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

public class Mob implements Mover
{
	public int xLoc;
	public int yLoc;
	public int hitPoints;
	public Character ch;
	public Path path;
	public int step;
	public boolean hit;
	
	public Mob(Character ch, int xLoc, int yLoc)
	{
		this.hitPoints = 100;
		this.ch = new Character(ch);
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.path = new Path();
		this.step = 0;
		this.hit = false;
	}
	
	public void updatePath(Path p)
	{
		path = p;
		step = 0;
	}
}
