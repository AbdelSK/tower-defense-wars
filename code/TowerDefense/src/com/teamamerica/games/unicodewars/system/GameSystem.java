package com.teamamerica.games.unicodewars.system;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
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
		SpawnSubsystem, PrototypeSubsytem
	};
	
	public long _frameCount;
	private Map<Systems, Subsystem> _systems;
	private Timer tickTimer;
	
	public GameSystem(int width, int height)
	{
		
		BB.inst().setScreen(width, height);
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
		_systems.put(Systems.PrototypeSubsytem, new PrototypeScriptingSystem());
		this.tickTimer = new Timer();
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
		
		for (List<GameObject> team : BB.inst().getAll())
		{
			for (GameObject obj : team)
			{
				obj.update(elapsed);
			}
		}
		
		if (this.tickTimer.xMilisecondsPassed(GameSystem.tickTime))
		{
			// Tick has passed. Update what is needed.
		}
	}

	public void render(Graphics g)
	{
		g.pushTransform();
		g.resetTransform();

		GameMap.inst().render(g);
		for (List<GameObject> team : BB.inst().getAll())
		{
			for (GameObject obj : team)
			{
				obj.render(g);
			}
		}

		g.setFont(font);
		g.setColor(Color.white);
		String tickCountdown = "Next Income: " + Math.round(this.tickTimer.timeUntilXMilisecondsPass(GameSystem.tickTime) / 1000);
		g.drawString(tickCountdown, 384, 520);
		g.drawString("Gold: " + BB.inst().getPlayer().getGold(), 384, 540);
		g.drawString("Income: " + BB.inst().getPlayer().getIncome(), 384, 560);
		g.popTransform();
	}
}
