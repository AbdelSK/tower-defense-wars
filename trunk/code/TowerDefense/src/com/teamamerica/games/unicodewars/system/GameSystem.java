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
import com.teamamerica.games.unicodewars.factory.MobMaker;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.BossMob;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Timer;

public class GameSystem
{
	public static final int BASE_POINTS_PER_BOSS_SPEED = 25;

	private static Logger logger = Logger.getLogger(GameSystem.class);
	private static final long tickTime = 30000;
	private static final long bossTime = 315000;
	private UnicodeFont font;
	
	public enum Systems
	{
		BuildSubsystem, AiSubsystem
	};
	
	public long _frameCount;
	private Map<Systems, Subsystem> _systems;
	private Timer tickTimer;
	private Timer bossTimer;
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
		
		if (this.bossTimer.xMilisecondsPassed(GameSystem.bossTime) && !BB.inst().isBossSpawned())
		{
			// Bosses being released
			MobObject temp1 = BB.inst().spawnUsersMob(MobObject.Type.boss, 1);
			MobObject temp2 = MobMaker.MakeMob(MobObject.Type.boss, 1, Team.Player2);
			BB.inst().setBossSpawned(true);
			((BossMob) temp1).setSpeed(BB.inst().getBossSpeed(Team.Player1));
			((BossMob) temp2).setSpeed(BB.inst().getBossSpeed(Team.Player2));
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
		int xpos1 = 325;
		int xpos2 = 510;
		int ypos = 515;
		if (!this.bossTimer.paused() && !BB.inst().isBossSpawned())
		{
			int timeLeftSecs = Math.round(this.bossTimer.timeUntilXMilisecondsPass(GameSystem.bossTime) / 1000);
			// bossCountdown = "Time left: " + (timeLeftSecs / 60) + ":" +
			// ((timeLeftSecs % 60 < 10) ? "0" : "") + (timeLeftSecs % 60);
			bossCountdown = "Time left: " + (timeLeftSecs / 60) + ":" + String.format("%02d", (timeLeftSecs % 60));
		}
		else if (!this.bossTimer.paused() && BB.inst().isBossSpawned())
		{
			bossCountdown = "Bosses Released!";
		}
		else
		{
			bossCountdown = "PAUSED!";
		}
		tickCountdown = "Next income: " + Math.round(this.tickTimer.timeUntilXMilisecondsPass(GameSystem.tickTime) / 1000);

		g.drawString("Match " + (BB.inst().getGameLevel() + 1), xpos1, ypos);
		g.drawString(bossCountdown, xpos2, ypos);
		ypos += 20;
		g.drawString("Gold: " + BB.inst().getUsersPlayer().getGold(), xpos1, ypos);
		ypos += 20;
		g.drawString("Income: " + BB.inst().getUsersPlayer().getIncome(), xpos1, ypos);
		g.drawString(tickCountdown, xpos2, ypos);
		ypos += 20;
		g.drawString("Score: " + BB.inst().getUsersPlayer().getScore(), xpos1, ypos);
		ypos += 20;
		// g.drawString("Boss Speed: PLR1=" +
		// BB.inst().getBossSpeed(Team.Player1) + " PLR2=" +
		// BB.inst().getBossSpeed(Team.Player2), xpos1, ypos);
		g.drawString("Player 1 Boss Speed: " + BB.inst().getBossSpeed(Team.Player1), xpos1, ypos);
		ypos += 20;
		g.drawString("Player 2 Boss Speed: " + BB.inst().getBossSpeed(Team.Player2), xpos1, ypos);
		ypos += 20;
		g.popTransform();
	}
}
