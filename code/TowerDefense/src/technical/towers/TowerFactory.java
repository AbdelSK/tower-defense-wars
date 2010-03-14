package technical.towers;

public class TowerFactory
{
	
	public static enum type
	{
		diceOne(DiceOne.price), ;
		
		public final int price;
		
		type(int price)
		{
			this.price = price;
		}
	}
	
	public static Tower createTower(type t, int gridX, int gridY)
	{
		switch (t)
		{
			case diceOne:
				return new DiceOne(gridX, gridY);
		}
		return null;
	}
}
