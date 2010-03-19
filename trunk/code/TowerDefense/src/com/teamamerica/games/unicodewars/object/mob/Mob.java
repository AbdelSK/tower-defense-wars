package com.teamamerica.games.unicodewars.object.mob;

import com.teamamerica.games.unicodewars.utils.Location;


public interface Mob
{
	public int getAttack();
	
	public int getSpeed();
	
	public int getCurrentHP();
	
	public int getTotalHP();
	
	public int getDefense();
	
	public int getPrice();
	
	public int getVitality();
	
	public int getLevel();
	
	public MobFactory.type getType();
	
	public boolean isAlive();
	
	public Location getLocation();
}
