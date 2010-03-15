package com.teamamerica.games.unicodewars.factory;

import com.teamamerica.games.unicodewars.component.MoverComponent;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;

public class MobMaker
{
	private MobMaker()
	{
	}
	
	// Example Method
	public static MobObject MakeMob()
	{
		MobObject temp = new MobObject("mob", BB.inst().getNextId(), 10);
		MoverComponent pathPart = new MoverComponent(temp);
		temp.addComponent(pathPart);
		return temp;
	}
}
