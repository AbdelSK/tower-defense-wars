package com.teamamerica.games.unicodewars.utils;

public enum EventType
{
	/**
	 * SPAWN_PROJECTILE Payload: position - the position that the projectile
	 * should be fired from direction - the direction that the projectile should
	 * be fired.
	 */
	SPAWN_PROJECTILE,

	/**
	 * REMOVE_PROJECTILE Payload: id - the numeric id of the GameObject that is
	 * being removed.
	 */
	REMOVE_PROJECTILE,

	/**
	 * HEALTH_EVENT Payload: amount - the amount of health to add or remove.
	 */
	HEALTH_EVENT,

	/**
	 * DEATH_EVENT Payload: id - the id of the agent who is dead and needs to be
	 * cleaned up.
	 */
	DEATH_EVENT,

	/**
	 * COLLISION_EVENT Payload: contact-point - the contact information stored
	 * as a ContactPoint
	 */
	COLLISION_EVENT,

	/**
	 * 
	 */
	ENTER_SPACE,

	/**
	 * 
	 */
	LEAVE_SPACE
}
