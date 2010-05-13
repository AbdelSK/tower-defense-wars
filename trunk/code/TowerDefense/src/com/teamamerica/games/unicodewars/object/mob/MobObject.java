package com.teamamerica.games.unicodewars.object.mob;

import java.net.URL;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.util.pathfinding.Mover;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.utils.Constants;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public abstract class MobObject extends GameObject implements Mover
{
	public static enum Type
	{
		chinese, latin, greek, cyrillic, boss;
	}

	int attack = 0;
	int defense = 0;
	int speed = 0;
	int currentHP = 0;
	int totalHP = 0;
	int level = 0;
	int price = 0;
	int refund = 0;
	Type type;
	TowerBase.Type defenseType;
	String imagePath;
	private boolean dead = false;
	public static final int MOB_ATTACK_CHINESE = 1;
	public static final int MOB_ATTACK_CYRILLIC = 3;
	public static final int MOB_ATTACK_GREEK = 3;
	public static final int MOB_ATTACK_LATIN = 1;
	public static final int MOB_ATTACK_BOSS = 1000;
	public static final int MOB_DEFENSE_CHINESE[] = { 40, 60, 80, 100, 120 };
	public static final int MOB_DEFENSE_CYRILLIC[] = { 10, 30, 55, 70, 90 };
	public static final int MOB_DEFENSE_GREEK[] = { 30, 50, 70, 90, 100 };
	public static final int MOB_DEFENSE_LATIN[] = { 30, 50, 70, 90, 100 };
	public static final int MOB_DEFENSE_BOSS = 0;
	public static final int MOB_PRICE_CHINESE = 5;
	public static final int MOB_PRICE_CYRILLIC = 8;
	public static final int MOB_PRICE_GREEK = 7;
	public static final int MOB_PRICE_LATIN = 6;
	public static final int MOB_PRICE_BOSS = 0;
	public static final int MOB_SPEED_CHINESE[] = { 2, 4, 8, 8, 16 };
	public static final int MOB_SPEED_CYRILLIC[] = { 4, 6, 8, 8, 16 };
	public static final int MOB_SPEED_GREEK[] = { 3, 4, 6, 8, 8 };
	public static final int MOB_SPEED_LATIN[] = { 4, 6, 8, 8, 8 };
	public static final int MOB_SPEED_BOSS = 3;
	public static final int MOB_TOTAL_HP_CHINESE = 200; // 100
	public static final int MOB_TOTAL_HP_CYRILLIC = 400; // 300
	public static final int MOB_TOTAL_HP_GREEK = 400; // 150
	public static final int MOB_TOTAL_HP_LATIN = 200; // 200
	public static final int MOB_TOTAL_HP_BOSS = 600000;

	public MobObject(String name, int id, int renderPriority, Location loc, Team side, int level, Type type, String imgLoc)
	{
		super(name, id, side, renderPriority, loc);
		this._size = 1;
		
		this.level = level;
		this.type = type;
		this.price = MobObject.getMobPrice(type, level);
		this.speed = MobObject.getMobSpeed(type, level);
		this.attack = MobObject.getMobAttack(type, level);
		this.currentHP = this.totalHP = MobObject.getMobTotalHp(type, level);
		this.defense = MobObject.getMobDefense(type, level);
		this.defenseType = MobObject.getMobDefenseType(type, level);
		this.refund = this.price / 2;
		this.imagePath = imgLoc;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getCurrentHP()
	{
		return currentHP;
	}
	
	public int getTotalHP()
	{
		return totalHP;
	}
	
	public int getDefense()
	{
		return defense;
	}
	
	public TowerBase.Type getDefenseType()
	{
		return defenseType;
	}

	public int getSpeed()
	{
		return speed;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public String getImagePath()
	{
		return imagePath;
	}

	public int getRefundAmount()
	{
		return refund;
	}

	public boolean isAlive()
	{
		return currentHP > 0;
	}
	
	public boolean adjustHealth(int amount)
	{
		currentHP += amount;
		if (currentHP <= 0)
		{
			return false;
		}
		else if (currentHP > totalHP)
		{
			currentHP = totalHP;
			return true;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public void deleteObject()
	{
		currentHP = 0;
		super.deleteObject();
	}
	
	public void die()
	{
		if (!dead)
		{
			dead = true;
			ConfigurableEmitter emitter = null;
			try
			{
				URL xmlFile = this.getClass().getResource("/data/effects/explode.xml");
				emitter = ParticleIO.loadEmitter(xmlFile.openStream());
			}
			catch (Exception e)
			{
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
			emitter.setPosition(this._positionInPixels.x + 10, this._positionInPixels.y + 12);
			emitter.setImageName(imagePath);
			Event event = new Event(EventType.START_PARTICLE_EFFECT);
			event.addParameter("configurableEmitter", emitter);
			EventManager.inst().dispatch(event);
			this.setPositionInPixel(-1, -1);
			deleteObject();
		}
		else
		{
			System.err.println("WARNING: Mob being killed multiple times!!");
		}
	}

	public static int getMobAttack(MobObject.Type type, int level)
	{
		int mobAttack;
		
		switch (type)
		{
			case chinese:
				mobAttack = MOB_ATTACK_CHINESE * getAttackFactor(level);
				break;
			case latin:
				mobAttack = MOB_ATTACK_LATIN * getAttackFactor(level);
				break;
			case greek:
				mobAttack = MOB_ATTACK_GREEK * getAttackFactor(level);
				break;
			case cyrillic:
				mobAttack = MOB_ATTACK_CYRILLIC * getAttackFactor(level);
				break;
			default:
				mobAttack = 0;
				break;
		}
		
		return mobAttack;
	}

	public static int getMobDefense(MobObject.Type type, int level)
	{
		int mobDefense;
		
		if (level > 0 && level <= Constants.MAX_MOB_LEVEL)
		{
			switch (type)
			{
				case chinese:
					mobDefense = MOB_DEFENSE_CHINESE[level - 1];
					break;
				case latin:
					mobDefense = MOB_DEFENSE_LATIN[level - 1];
					break;
				case greek:
					mobDefense = MOB_DEFENSE_GREEK[level - 1];
					break;
				case cyrillic:
					mobDefense = MOB_DEFENSE_CYRILLIC[level - 1];
					break;
				default:
					mobDefense = 0;
					break;
			}
		}
		else
		{
			mobDefense = 0;
		}
		
		return mobDefense;
	}

	public static TowerBase.Type getMobDefenseType(MobObject.Type type, int level)
	{
		TowerBase.Type mobDefenseType;
		
		switch (type)
		{
			case chinese:
				mobDefenseType = TowerBase.Type.musicOne;
				break;
			case latin:
				mobDefenseType = TowerBase.Type.cardOne;
				break;
			case greek:
				mobDefenseType = TowerBase.Type.chessOne;
				break;
			case cyrillic:
				mobDefenseType = TowerBase.Type.diceOne;
				break;
			default:
				mobDefenseType = null;
				break;
		}
		
		return mobDefenseType;
	}

	public static int getMobPrice(MobObject.Type type, int level)
	{
		int mobPrice;
	
		switch (type)
		{
			case chinese:
				mobPrice = MOB_PRICE_CHINESE * getPriceFactor(level);
				break;
			case latin:
				mobPrice = MOB_PRICE_LATIN * getPriceFactor(level);
				break;
			case greek:
				mobPrice = MOB_PRICE_GREEK * getPriceFactor(level);
				break;
			case cyrillic:
				mobPrice = MOB_PRICE_CYRILLIC * getPriceFactor(level);
				break;
			case boss:
				mobPrice = MOB_PRICE_BOSS * getPriceFactor(level);
				break;
			default:
				mobPrice = 0;
				break;
		}
		
		return mobPrice;
	}

	public static int getMobSpeed(MobObject.Type type, int level)
	{
		int mobSpeed;
		
		if (level > 0 && level <= Constants.MAX_MOB_LEVEL)
		{
			switch (type)
			{
				case chinese:
					mobSpeed = MOB_SPEED_CHINESE[level - 1];
					break;
				case latin:
					mobSpeed = MOB_SPEED_LATIN[level - 1];
					break;
				case greek:
					mobSpeed = MOB_SPEED_GREEK[level - 1];
					break;
				case cyrillic:
					mobSpeed = MOB_SPEED_CYRILLIC[level - 1];
					break;
				default:
					mobSpeed = 0;
					break;
			}
		}
		else
		{
			mobSpeed = 0;
		}
		
		return mobSpeed;
	}

	public static int getMobTotalHp(MobObject.Type type, int level)
	{
		int mobTotalHp;
		
		switch (type)
		{
			case chinese:
				mobTotalHp = MOB_TOTAL_HP_CHINESE * getTotalHpFactor(level);
				break;
			case latin:
				mobTotalHp = MOB_TOTAL_HP_LATIN * getTotalHpFactor(level);
				break;
			case greek:
				mobTotalHp = MOB_TOTAL_HP_GREEK * getTotalHpFactor(level);
				break;
			case cyrillic:
				mobTotalHp = MOB_TOTAL_HP_CYRILLIC * getTotalHpFactor(level);
				break;
			default:
				mobTotalHp = 0;
				break;
		}
		
		return mobTotalHp;
	}

	private static int getPriceFactor(int level)
	{
		return (int) Math.pow(4, level - 1);
	}

	private static int getTotalHpFactor(int level)
	{
		return (int) Math.pow(2, level - 1);
	}
	
	private static int getAttackFactor(int level)
	{
		return (int) Math.pow(2, level - 1);
	}
	
}
