package com.teamamerica.games.unicodewars.gui;

import org.fenggui.event.ButtonPressedEvent;
import com.teamamerica.games.unicodewars.object.towers.TowerBase;
import com.teamamerica.games.unicodewars.object.towers.TowerBase.Type;
import com.teamamerica.games.unicodewars.system.BB;

public class TowerButton extends AwesomeButton
{
	private TowerBase.Type type;
	
	public TowerButton()
	{
		super();
	}
	
	public void init(String text, String hover, int x, int y, TowerBase.Type tt)
	{
		super.init(text, hover, x, y);
		this.type = tt;
		System.out.println("button init: setting to " + type);
	}

	@Override
	public void buttonPressed(ButtonPressedEvent arg0)
	{
		System.out.println("button pressed: creating " + type);
		BB.inst().setTowerSelection(this.type);
	}
}
