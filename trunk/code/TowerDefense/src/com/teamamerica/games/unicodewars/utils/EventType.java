package com.teamamerica.games.unicodewars.utils;

public enum EventType
{

	/**
	 * For when a moving game object enters a location
	 */
	ENTER_SPACE,

	/**
	 * For when a moving game object leaves a location
	 */
	LEAVE_SPACE,

	/**
	 * For when Player1 builds a tower
	 */
	P1_TOWER_BUILT,

	/**
	 * For when Player2 builds a tower
	 */
	P2_TOWER_BUILT,

	/**
	 * For when a base has been destroyed
	 */
	BASE_DESTROYED
}
