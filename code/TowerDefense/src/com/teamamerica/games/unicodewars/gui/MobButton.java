package com.teamamerica.games.unicodewars.gui;

import org.fenggui.event.ButtonPressedEvent;
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Team;

public class MobButton extends AwesomeButton
{
	private MobObject.Type type;
	
	public MobButton()
	{
		super();
	}
	
	public void init(String text, String hover, int x, int y, MobObject.Type mt)
	{
		super.init(text, hover, x, y);
		this.type = mt;
		System.out.println("button init: setting to " + type);
	}

	@Override
	public void buttonPressed(ButtonPressedEvent arg0)
	{
		System.out.println("button pressed: creating " + type);
		int level = arg0.getTrigger().getPosition().getX() / arg0.getTrigger().getSize().getWidth() + 1;
		MobMaker.MakeMob(type, level, Team.Player1);
		BB.inst().setMobTypeSelection(type);
		BB.inst().setMobLevelSelection(level);
	}
}
