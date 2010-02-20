package prototype;

public class Tower
{
	public boolean alive;
	public int xLoc;
	public int yLoc;
	public char ch;
	
	public Tower(char ch, int xLoc, int yLoc)
	{
		this.ch = ch;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.alive = true;
	}
}
