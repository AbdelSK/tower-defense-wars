package prototype;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

public class Mob implements Mover
{
	public int tileCol;
	public int tileRow;
	public int hitPoints;
	public Character ch;
	public Path path;
	public int step;
	private int xPixelLoc;
	private int yPixelLoc;
	
	public Mob(Character ch, int tTileCol, int tTileRow)
	{
		this.hitPoints = 100;
		this.ch = new Character(ch);
		this.tileCol = tTileCol;
		this.tileRow = tTileRow;
		this.xPixelLoc = PrototypeMap.centerXInTile(tTileCol);
		this.yPixelLoc = PrototypeMap.centerYInTile(tTileRow);
		this.path = new Path();
		this.step = 0;
	}
	
	public void updatePath(Path p)
	{
		path = p;
		step = 0;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public int getStep()
	{
		return step;
	}

	/*
	 * @MobMover referencing this method
	 */
	public int getXPixelLoc()
	{
		// return tileCol * 16 + 3;
		return xPixelLoc;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public int getYPixelLoc()
	{
		// return tileRow * 16 - 3;
		return yPixelLoc;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public void incrementStep(int i)
	{
		step += i;
	}

	/*
	 * @MobMover referencing this method
	 */
	public void setXPixelLoc(int tXPixelLoc)
	{
		this.xPixelLoc = tXPixelLoc;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public void setYPixelLoc(int tYPixelLoc)
	{
		this.yPixelLoc = tYPixelLoc;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public void setTileCol(int tTileCol)
	{
		this.tileCol = tTileCol;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public void setTileRow(int tTileRow)
	{
		this.tileRow = tTileRow;
	}

}
