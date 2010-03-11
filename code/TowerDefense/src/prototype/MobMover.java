package prototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MobMover
{
	HashMap<TileLocation, HashMap<Tower, Tower>> regTowerLists;
	Prototype basicGame;
	PrototypeMap gameMap; // could just have a getter in Prototype for the map
	
	public MobMover(Prototype tBasicGame, PrototypeMap tMap)
	{
		basicGame = tBasicGame;
		gameMap = tMap;
		regTowerLists = new HashMap<TileLocation, HashMap<Tower, Tower>>((gameMap.getHeightInTiles() + 1) * (gameMap.getWidthInTiles() + 1));
		for (int row = 1; row <= gameMap.getHeightInTiles(); row++)
		{
			for (int col = 1; col <= gameMap.getWidthInTiles(); col++)
			{
				regTowerLists.put(new TileLocation(col, row), new HashMap<Tower, Tower>(Prototype.MAX_TOWERS));
			}
		}
	}
	
	/*
	 * gets all tile locations within range of this tower
	 */
	private List<TileLocation> getTilesInRange(Tower tower)
	{
		ArrayList<TileLocation> tileLocs = new ArrayList<TileLocation>();
		
		List<TileLocation> lstTileOffsets = tower.getTileRangeOffsets();
		TileLocation curTl;
		
		for (TileLocation curTlOffset : lstTileOffsets)
		{
			curTl = new TileLocation(tower.getTileCol() + curTlOffset.getCol(), tower.getTileRow() + curTlOffset.getRow());
			// make sure the current tile is on the map in case the tower is
			// near the edge
			if ((curTl.getCol() >= Prototype.MIN_COL) && (curTl.getCol() <= Prototype.MAX_COLS) && (curTl.getRow() >= Prototype.MIN_ROW) && (curTl.getRow() <= Prototype.MAX_ROWS))
			{
				tileLocs.add(curTl);
			}
		}

		return tileLocs;
	}

	/*
	 * add the tower as a listener for any tiles in its range
	 */
	public void registerTower(Tower tower)
	{
		List<TileLocation> lstTileLocs = getTilesInRange(tower);
		
		for (TileLocation t : lstTileLocs)
		{
			HashMap<Tower, Tower> hmTowers = regTowerLists.get(t);
			hmTowers.put(tower, tower);
		}
	}
	
	/*
	 * remove the tower as a listener for any tiles
	 */
	public void unregisterTower(Tower tower)
	{
		List<TileLocation> lstTileLocs = getTilesInRange(tower);
		
		for (TileLocation t : lstTileLocs)
		{
			HashMap<Tower, Tower> hmTowers = regTowerLists.get(t);
			hmTowers.remove(tower);
		}
	}
	
	/*
	 * used to move mobs smoothly across the board and step through the path.
	 * the necessary towers' mob queues are also updated as needed depending on
	 * whether the mob is moving within a tower's range or out of it's range
	 */
	public void moveMob(Mob mob)
	{
		int deltaX = 0;
		int deltaY = 0;
		int targetX = PrototypeMap.centerXInTile(mob.path.getStep(mob.getStep() + 1).getX());
		int targetY = PrototypeMap.centerYInTile(mob.path.getStep(mob.getStep() + 1).getY());
		
		//
		// change the actual pixel location so the mob moves towards the next tile smoothly
		//
		if (targetX - mob.getXPixelLoc() > 0)
		{
			deltaX = 1;
		}
		else if (targetX - mob.getXPixelLoc() < 0)
		{
			deltaX = -1;
		}
		if (targetY - mob.getYPixelLoc() > 0)
		{
			deltaY = 1;
		}
		else if (targetY - mob.getYPixelLoc() < 0)
		{
			deltaY = -1;
		}
		mob.setXPixelLoc(mob.getXPixelLoc() + deltaX);
		mob.setYPixelLoc(mob.getYPixelLoc() + deltaY);
		
		//
		// if the mob has reached a new tile
		// -remove it from queues of any towers that it is no longer in range of
		// -add it to the queues of any towers that it is now in range of
		//
		if (mob.getXPixelLoc() == targetX && mob.getYPixelLoc() == targetY)
		{
			TileLocation srcTl = new TileLocation(mob.path.getX(mob.getStep()), mob.path.getY(mob.getStep()));
			mob.incrementStep(1);
			TileLocation tgtTl = new TileLocation(mob.path.getX(mob.getStep()), mob.path.getY(mob.getStep()));
			HashMap<Tower, Tower> srcTowers = regTowerLists.get(srcTl);
			HashMap<Tower, Tower> tgtTowers = regTowerLists.get(tgtTl);
			
			mob.setTileCol(tgtTl.getCol());
			mob.setTileRow(tgtTl.getRow());

			for (Tower t : srcTowers.values())
			{
				if (!tgtTowers.containsKey(t))
				{
					t.getMobsInRange().remove(mob);
				}
			}
			for (Tower t : tgtTowers.values())
			{
				if (!srcTowers.containsKey(t))
				{
					t.getMobsInRange().add(mob);
				}
			}
		}
	}
}
