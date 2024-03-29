package com.teamamerica.games.unicodewars.system;

import org.newdawn.slick.Graphics;

public interface Subsystem {

    /**
     * Returns a unique id for this system to distinguish
     * it from the other subsystems.
     * @return
     */
    public int getId();

    /**
     * Called every update cycle.  Order is predetermined
     * for some of the managers, but the mixin managers will
     * be called in the order that they are added.
     * @param eps
     */
	public void update(int eps);

    /**
     * Called every render cycle.  Most of the time there will 
     * probably be nothing to render, but occasionally you
     * may want to render
     * @param g
     */
    public void render(Graphics g);

    /**
     * Called when we are shutting everything down so that 
     * you can clean up everything that is outstanding.
     */
    public void finish();
	
	/**
	 * Called when the system is starting up
	 */
	public void start();
	
	/**
	 * Called when the system is ending (i.e. game over)
	 */
	public void end();
	
	/**
	 * Called when the game is paused.
	 */
	public void pause();
	
	/**
	 * Called when the game is unpaused.
	 */
	public void unpause();
}
