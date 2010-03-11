package prototype;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class PrototypeMap implements TileBasedMap
{
	TiledMap map;
	boolean[][] collisionMap;
	
	public PrototypeMap(TiledMap m)
	{
		this.map = m;
		this.collisionMap = new boolean[m.getWidth()][m.getHeight()];
		
		for (int x = 0; x < m.getWidth(); x++)
		{
			for (int y = 0; y < m.getHeight(); y++)
			{
				int tileID = m.getTileId(x, y, 1);
				String prop = m.getTileProperty(tileID, "Passable", "0");
				int property = Integer.parseInt(prop);
				if (property == 2)
					collisionMap[x][y] = true;
				else
					collisionMap[x][y] = false;
			}
			
		}
	}
	
	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty)
	{
		if (tx < map.getWidth() && tx >= 0 && ty < map.getHeight() && ty >= 0)
			return collisionMap[tx][ty];
		else
			return true;
	}
	
	@Override
	public float getCost(PathFindingContext context, int tx, int ty)
	{
		return 1;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	@Override
	public int getHeightInTiles()
	{
		return map.getHeight();
	}
	
	/*
	 * @MobMover referencing this method
	 */
	@Override
	public int getWidthInTiles()
	{
		return map.getWidth();
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public TiledMap getMap()
	{
		return map;
	}

	@Override
	public void pathFinderVisited(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Set a tile as blocked
	 * 
	 * @param tx
	 *            the x-coordinate
	 * @param ty
	 *            the y-coordinate
	 */
	public void addBlocker(int tx, int ty)
	{
		this.collisionMap[tx][ty] = true;
	}
	
	/**
	 * Remove a block from a tile
	 * 
	 * @param tx
	 *            the x-coordinate
	 * @param ty
	 *            the y-coordinate
	 */
	public void removeBlocker(int tx, int ty)
	{
		this.collisionMap[tx][ty] = false;
	}
	
	/*
	 * @MobMover referencing this method
	 */
	public static int centerXInTile(int tileCol)
	{
		return tileCol * 16 + 3;
	}

	/*
	 * @MobMover referencing this method
	 */
	public static int centerYInTile(int tileRow)
	{
		return tileRow * 16 - 3;
	}
}
