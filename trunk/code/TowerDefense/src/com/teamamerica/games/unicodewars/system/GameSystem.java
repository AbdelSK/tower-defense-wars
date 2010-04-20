package com.teamamerica.games.unicodewars.system;

import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.utils.Timer;

public class GameSystem
{
	private static Logger logger = Logger.getLogger(GameSystem.class);
	private static final long tickTime = 30000;
	private UnicodeFont font;
	
	public enum Systems
	{
		BuildSubsystem, AiSubsystem
	};
	
	public long _frameCount;
	private Map<Systems, Subsystem> _systems;
	private Timer tickTimer;
	private GameContainer _container;
	
	public GameSystem(GameContainer container, int width, int height)
	{
		_container = container;
		BB.inst().setScreen(width, height);
		BB.inst().pauseTimers();
		try
		{
			font = new UnicodeFont("data/font/Friz_Quadrata_TT.ttf", 16, false, false);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		font.getEffects().add(new ColorEffect());
		font.addAsciiGlyphs();
		try
		{
			font.loadGlyphs();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		_systems = new TreeMap<Systems, Subsystem>();

	}
	
	public void start()
	{
		loadLevel("");
		_systems.put(Systems.BuildSubsystem, new BuildSubsystem(_container));
		_systems.put(Systems.AiSubsystem, new AiSubsystem());
		this.tickTimer = BB.inst().getNewTimer();
		for (Subsystem s : _systems.values())
		{
			s.start();
		}
	}
	
	public void end()
	{
		for (Subsystem s : _systems.values())
		{
			s.end();
		}
		BB.$delete();
		EventManager.$delete();
		GameMap.$delete();
		_systems.clear();
	}
	
	public void pause()
	{
		BB.inst().pauseTimers();
		for (Subsystem s : _systems.values())
		{
			s.pause();
		}
	}
	
	public void unpause()
	{
		BB.inst().unpauseTimers();
		for (Subsystem s : _systems.values())
		{
			s.unpause();
		}
	}

	/**
	 * Add a subsystem to our map.
	 * 
	 * @param id
	 * @param s
	 */
	public void addSubsystem(Systems id, Subsystem s)
	{
		_systems.put(id, s);
	}
	
	public void loadLevel(String levelName)
	{
		BB.inst();
		EventManager.inst();
		GameMap.inst().LoadMap();
	}
	
	public void update(int elapsed)
	{
		for (Subsystem s : _systems.values())
		{
			s.update(elapsed);
		}
		
		EventManager.inst().update(elapsed);
		GameMap.inst().update(elapsed);
		
		for (GameObject obj : BB.inst().getAll())
		{
			obj.update(elapsed);
		}
		
		if (this.tickTimer.xMilisecondsPassed(GameSystem.tickTime))
		{
			// Tick has passed. Update what is needed. Adjust the players total
			// gold and next income value
			BB.inst().getUsersPlayer().addGold(BB.inst().getUsersPlayer().getIncome());
		}
	}

	public void render(Graphics g)
	{
		g.pushTransform();
		g.resetTransform();
		
		GameMap.inst().render(g);
		
		for (GameObject obj : BB.inst().getAll())
		{
			obj.render(g);
		}

		for (Subsystem s : _systems.values())
		{
			s.render(g);
		}

		g.setFont(font);
		g.setColor(Color.white);
		String tickCountdown = "";
		if (!this.tickTimer.paused())
		{
			tickCountdown = "Next income: " + Math.round(this.tickTimer.timeUntilXMilisecondsPass(GameSystem.tickTime) / 1000);
		}
		else
		{
			tickCountdown = "PAUSED!";
		}
		g.drawString(tickCountdown, 384, 520);
		g.drawString("Gold: " + BB.inst().getUsersPlayer().getGold(), 384, 540);
		g.drawString("Income: " + BB.inst().getUsersPlayer().getIncome(), 384, 560);
		g.drawString("Score: " + BB.inst().getUsersPlayer().getScore(), 384, 580);
		g.popTransform();
	}
}
