package prototype;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tower
{
	public boolean alive;
	public int tileCol;
	public int tileRow;
	public Character ch;
	public int radius;
	public int attackSpeed;
	public int damage;
	public LinkedList<Mob> mobsInRange;
	
	public Tower(Character ch, int tTileCol, int tTileRow)
	{
		this.attackSpeed = 2;
		this.damage = 5;
		this.ch = ch;
		this.radius = 10;
		this.tileCol = tTileCol;
		this.tileRow = tTileRow;
		this.alive = true;
		mobsInRange = new LinkedList<Mob>();
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public LinkedList<Mob> getMobsInRange()
	{
		return mobsInRange;
	}
	
	/*
	 * @MobMover referencing this method
	 * 
	 * this method currently assumes that towers have a range of 3 tiles. This
	 * method will need to be changed to accommodate different radiuses
	 */
	public List<TileLocation> getTileRangeOffsets()
	{
		// 44 is the number of tiles in range for a radius of 3
		ArrayList<TileLocation> tileOffsets = new ArrayList<TileLocation>(44);

		for (int col=-3; col<4; col++)
		{
			for (int row=-3; row<4; row++)
			{
				// add all tile offsets except at the location where the tower
				// itself is and at the corners
				if ( !( (col == 0 && row == 0) ||
					    ( (col == -3) && ( (row == -3) || (row == 3) ) ) ||
					    ( (col == 3) && ( (row == -3) || (row == 3) ) ) ) )
				{
				    tileOffsets.add(new TileLocation(col, row));
				}
			}
		}

		return tileOffsets;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public int getTileCol()
	{
		return tileCol;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public int getTileRow()
	{
		return tileRow;
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
	
	/*
	 * @MobMover referencing this method
	 */
	public int hashCode()
	{
		return tileCol * Prototype.MAX_COLS + tileRow;
	}
}
