package com.teamamerica.games.unicodewars.states;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import com.teamamerica.games.unicodewars.Main;
import com.teamamerica.games.unicodewars.utils.Timer;

public class CreditsState extends BHGameState
{
	private Main.States stateToLeaveTo;
	private boolean leaving;
	private int _centerX;
	private Music _creditsMusic;
	private UnicodeFont font;
	private int topCreditsLine;
	private Timer scrollTimer;
	private ArrayList<String> credits;

	@Override
	public int getID()
	{
		return Main.States.CreditsState.ordinal();
	}
	

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException
	{
		credits = new ArrayList<String>();
		_creditsMusic = new Music("src/data/sounds/Credits Music.ogg");
		
		try
		{
			font = new UnicodeFont("data/font/Friz_Quadrata_TT.ttf", 24, false, false);

		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		java.awt.Color fontColor = java.awt.Color.BLACK;
		font.getEffects().add(new ColorEffect(fontColor));
		font.addAsciiGlyphs();
		try
		{
			font.loadGlyphs();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			FileReader creditsFile = new FileReader("src/data/text/Credits.txt");
			BufferedReader br = new BufferedReader(creditsFile);
			String curLine;
			curLine = br.readLine();
			while (curLine != null)
			{
				credits.add(curLine.trim());
				curLine = br.readLine();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace(System.err);
			return;
		}
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);

		_centerX = container.getWidth() / 2;

		if (game instanceof Main)
		{
			Main realGame = (Main) game;
			stateToLeaveTo = realGame.LastState;
		}
		else
		{
			stateToLeaveTo = Main.States.MainMenuState;
		}
		_creditsMusic.play();
		topCreditsLine = font.getHeight("Credits") + 60;
		leaving = false;
		
		scrollTimer = new Timer();
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		leaving = false;
		scrollTimer = null;
		_creditsMusic.stop();
		super.leave(container, game);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		g.setBackground(Color.white);
		
		g.setFont(font);
		float offset = font.getWidth("Credits") / 2;
		int startHeight = font.getHeight("Credits") + 50;
		g.drawString("Credits", _centerX - offset, 0);
		int maxFontHeight = font.getHeight("|ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		
		g.setClip(0, startHeight, container.getWidth(), container.getHeight() - startHeight);
		int yOffset = 0;
		for (String s : credits)
		{
			offset = font.getWidth(s) / 2;
			g.drawString(s, _centerX - offset, topCreditsLine + yOffset);
			yOffset += maxFontHeight + 5;
		}
		g.clearClip();
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException
	{
		if (leaving)
		{
			arg1.enterState(stateToLeaveTo.ordinal(), new FadeOutTransition(), new FadeInTransition());
		}
		
		if (scrollTimer.xMilisecondsPassed(60))
		{
			topCreditsLine--;
		}

	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		if (key != Input.KEY_LSHIFT && key != Input.KEY_RSHIFT && key != Input.KEY_RCONTROL && key != Input.KEY_LCONTROL && key != Input.KEY_LALT && key != Input.KEY_RALT)
			leaving = true;
	}

}
