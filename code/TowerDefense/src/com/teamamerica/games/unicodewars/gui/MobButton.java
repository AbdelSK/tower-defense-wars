package com.teamamerica.games.unicodewars.gui;

import org.fenggui.event.ButtonPressedEvent;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Team;

public class MobButton extends AwesomeButton
{
	private MobObject.Type type;
	private int level;
	private int price;
	
	public MobButton()
	{
		super();
	}
	
	public void init(String text, String hover, int x, int y, MobObject.Type mt, int level)
	{
		super.init(text, hover, x, y);
		this.type = mt;
		this.level = level;
		this.price = MobObject.determinePrice(this.level);
	}

	@Override
	public void buttonPressed(ButtonPressedEvent arg0)
	{
		if (BB.inst().getUsersPlayer().getGold() >= price)
		{
			BB.inst().getUsersPlayer().addIncome(price / 2);
			BB.inst().getUsersPlayer().purchase(price);
			BB.inst().getUsersPlayer().addScore(this.level);
			MobMaker.MakeMob(type, level, Team.Player1);
			BB.inst().setMobTypeSelection(type);
			BB.inst().setMobLevelSelection(level);
		}
	}
	
	public int getPrice()
	{
		return this.price;
	}
}
