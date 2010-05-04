package com.teamamerica.games.unicodewars.gui;

import org.fenggui.event.ButtonPressedEvent;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;

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
		this.price = MobObject.getMobPrice(mt, this.level);
	}

	@Override
	public void buttonPressed(ButtonPressedEvent arg0)
	{
		BB.inst().spawnUsersMob(type, level);
	}
	
	public int getPrice()
	{
		return this.price;
	}
}
