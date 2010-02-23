package prototype;

public class Tower
{
	public boolean alive;
	public int xLoc;
	public int yLoc;
	public Character ch;
	public int radius;
	public int attackSpeed;
	
	public Tower(Character ch, int xLoc, int yLoc)
	{
		this.attackSpeed = 2;
		this.ch = ch;
		this.radius = 10;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.alive = true;
	}
}
