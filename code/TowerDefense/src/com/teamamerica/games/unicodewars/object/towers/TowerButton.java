package com.teamamerica.games.unicodewars.object.towers;

import org.fenggui.event.ButtonPressedEvent;
import com.teamamerica.games.unicodewars.system.BB;

public class TowerButton extends AwesomeButton
{
	private TowerBase.Type type;
	
	public TowerButton(String text, String hover, int x, int y, TowerBase.Type tt)
	{
		super(text, hover, x, y);
		this.type = tt;
	}
	
	public TowerButton()
	{
		super();
	}

	@Override
	public void buttonPressed(ButtonPressedEvent arg0)
	{
		BB.inst().setTowerSelection(type);
	}
}
