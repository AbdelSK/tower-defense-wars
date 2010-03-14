package technical.towers;

import java.util.Random;

public class DiceOne extends TowerBase
{
	
	public static int price = 10;
	
	private int enemyX = 0;
	private int enemyY = 0;
	
	public DiceOne(int gridX, int gridY)
	{
		super(TowerFactory.type.diceOne, 0, price, 0, 0);
	}

	public boolean canUpgrade()
	{
		return this.level < 6;
	}

	@Override
	public void doUpgrade()
	{
		this.level++;
		
		int total = 0;
		Random gen = new Random();
		
		if (level == 2)
		{
			total = 12;
		}
		else if (level == 3)
		{
			total = 19;
		}
		else if (level == 4)
		{
			total = 26;
		}
		else if (level == 5)
		{
			total = 33;
		}
		else if (level == 6)
		{
			total = 40;
		}

		this.attack = gen.nextInt(total);
		if (this.attack == 0)
		{
			this.attack = 1;
		}
		this.radius = gen.nextInt(total - this.attack);
		if (this.radius == 0)
		{
			this.radius = 1;
		}
		this.speed = total - (this.attack + this.radius);
		if (this.speed == 0)
		{
			this.speed = 1;
		}
	}
	
	@Override
	public String getInfoString()
	{
		return "Dice Tower Level " + this.level;
	}
	
	@Override
	public String getStatusString()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void registerTower()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unregisterTower()
	{
		// TODO Auto-generated method stub
		
	}
	
}
