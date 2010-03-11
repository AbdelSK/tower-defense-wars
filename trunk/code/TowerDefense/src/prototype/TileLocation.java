package prototype;

public class TileLocation
{
	private int row;
	private int col;
	
	public TileLocation(int tCol, int tRow)
	{
		col = tCol;
		row = tRow;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public int getRow()
	{
		return row;
	}

	public int hashCode()
	{
		return col * Prototype.MAX_COLS + row;
	}
	
	public boolean equals(Object tl)
	{
		if ((tl != null) && (tl.hashCode() == this.hashCode()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
