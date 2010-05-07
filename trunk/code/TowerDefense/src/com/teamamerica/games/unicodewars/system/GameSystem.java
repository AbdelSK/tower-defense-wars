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
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.utils.Timer;

public class GameSystem
{
	private static Logger logger = Logger.getLogger(GameSystem.class);
	private static final long tickTime = 30000;
	private static final long bossTime = 300000;
	private UnicodeFont font;
	
	public enum Systems
	{
		BuildSubsystem, AiSubsystem
	};
	
	public long _frameCount;
	private Map<Systems, Subsystem> _systems;
	private Timer tickTimer;
	private Timer bossTimer;
	private boolean playerBossSent = false;
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
		this.bossTimer = BB.inst().getNewTimer();
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
		
		if (this.bossTimer.xMilisecondsPassed(GameSystem.bossTime) && !this.playerBossSent)
		{
			// Boss is being released
			BB.inst().spawnUsersMob(MobObject.Type.boss, 1);
			this.playerBossSent = true;
		}

		BB.inst().checkTowerUpgradability();
	}

	public void render(Graphics g)
	{
		g.pushTransform();
		g.resetTransform();
		g.setFont(font);
		
		GameMap.inst().render(g);
		
		for (GameObject obj : BB.inst().getAll())
		{
			obj.render(g);
		}

		for (Subsystem s : _systems.values())
		{
			s.render(g);
		}

		g.setColor(Color.white);
		String tickCountdown = "";
		String bossCountdown = "";
		int xpos = 384;
		int ypos = 520;
		g.drawString("Match " + (BB.inst().getGameLevel() + 1), xpos, ypos);
		ypos += 20;
		if (!this.tickTimer.paused())
		{
			tickCountdown = "Next income: " + Math.round(this.tickTimer.timeUntilXMilisecondsPass(GameSystem.tickTime) / 1000);
		}
		else if (this.playerBossSent)
		{
			tickCountdown = "PAUSED!";
		}
		g.drawString(tickCountdown, xpos, ypos);
		ypos += 20;
		g.drawString("Gold: " + BB.inst().getUsersPlayer().getGold(), xpos, ypos);
		ypos += 20;
		g.drawString("Income: " + BB.inst().getUsersPlayer().getIncome(), xpos, ypos);
		ypos += 20;
		g.drawString("Score: " + BB.inst().getUsersPlayer().getScore(), xpos, ypos);
		ypos += 20;
		if (!this.bossTimer.paused() && !this.playerBossSent)
		{
			bossCountdown = "Time left: " + Math.round(this.bossTimer.timeUntilXMilisecondsPass(GameSystem.bossTime) / 1000);
		}
		else if (!this.bossTimer.paused() && this.playerBossSent)
		{
			bossCountdown = "Boss Released!";
		}
		else
		{
			bossCountdown = "PAUSED!";
		}
		g.drawString(bossCountdown, xpos, ypos);
		g.popTransform();
	}
}
