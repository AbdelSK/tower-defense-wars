package com.teamamerica.games.unicodewars.object.towers;

public interface Tower
{
	public String getStatusString();
	
	public String getInfoString();
	
	public boolean canUpgrade();
	
	public void doUpgrade();
	
	public int getUpgradePrice();
	
	public int getSellPrice();
	
	public int getRadius();
	
	public int getAttack();
	
	public int getSpeed();

	public TowerFactory.type getType();

	public void registerTower();
	
	public void unregisterTower();
}
