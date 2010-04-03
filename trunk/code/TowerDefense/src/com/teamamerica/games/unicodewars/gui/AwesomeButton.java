package com.teamamerica.games.unicodewars.gui;

import org.fenggui.Button;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.mouse.IMouseEnteredListener;
import org.fenggui.event.mouse.IMouseExitedListener;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;

@SuppressWarnings("deprecation")
public abstract class AwesomeButton extends Button implements IButtonPressedListener, IMouseEnteredListener, IMouseExitedListener
{
	private String defaultText;
	private String tooltipText;
	
	public AwesomeButton()
	{
		super();
	}
	
	public void init(String text, String hover, int x, int y)
	{
		this.defaultText = text;
		this.tooltipText = hover;
		this.setText(text);
		this.setSize(x, y);
		this.setMultiline(true);
		this.setShrinkable(false);
	}

	public void mouseEntered(MouseEnteredEvent arg0)
	{
		this.setText(this.tooltipText);
	}
	
	public void mouseExited(MouseExitedEvent arg0)
	{
		this.setText(this.defaultText);
	}

	public abstract void buttonPressed(ButtonPressedEvent arg0);
}
