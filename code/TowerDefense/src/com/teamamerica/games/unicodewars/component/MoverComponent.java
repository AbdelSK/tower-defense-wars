package com.teamamerica.games.unicodewars.component;

import org.newdawn.slick.util.pathfinding.Path;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.system.EventManager;
import com.teamamerica.games.unicodewars.system.GameMap;
import com.teamamerica.games.unicodewars.utils.Event;
import com.teamamerica.games.unicodewars.utils.EventListener;
import com.teamamerica.games.unicodewars.utils.EventType;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;
import com.teamamerica.games.unicodewars.utils.Timer;

public class MoverComponent extends Component
{
	private Path path;
	private GameObject _owner;
	private int pathStep;
	private Timer stopwatch;
	private int speedFactor;
	private EventListener towerListener;
	private EventType towerBuildListenerType;
	private EventType towerSoldListenerType;
	
	public MoverComponent(GameObject owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
		this.stopwatch = BB.inst().getNewTimer();
		this._owner = owner;
		
		towerListener = new EventListener() {
			
			@Override
			public void onEvent(Event e)
			{
				// TODO Auto-generated method stub
				if ((e.getId() == EventType.P1_TOWER_SOLD && _owner.getTeam() == Team.Player2) || (e.getId() == EventType.P2_TOWER_SOLD && _owner.getTeam() == Team.Player1))
				{
					updatePath();
				}
				else
				{
					checkAndUpdate(e.sender);
				}
			}
		};
		switch (owner.getTeam())
		{
			case Player1:
				this.towerBuildListenerType = EventType.P2_TOWER_BUILT;
				this.towerSoldListenerType = EventType.P2_TOWER_SOLD;
				break;
			case Player2:
				this.towerBuildListenerType = EventType.P1_TOWER_BUILT;
				this.towerSoldListenerType = EventType.P1_TOWER_SOLD;
				break;
			default:
				this.towerBuildListenerType = EventType.P2_TOWER_BUILT;
				this.towerSoldListenerType = EventType.P2_TOWER_SOLD;
				break;
		}
		EventManager.inst().registerForAll(towerBuildListenerType, towerListener);
		EventManager.inst().registerForAll(towerSoldListenerType, towerListener);
		pathStep = 0;
		
		if (owner instanceof MobObject)
			this.speedFactor = ((MobObject) owner).getSpeed();
		else
			this.speedFactor = 20;
	}
	
	@Override
	public void update(int elapsed)
	{
		if (this.path != null)
		{
			if (stopwatch.xMilisecondsPassed(100 / this.speedFactor))
			{
				Location nextStepLoc = GameMap.inst().getLocationInPixels(this.path.getStep(this.pathStep + 1));
				if (nextStepLoc.x - _parent.getPositionInPixels().x > 0)
				{
					_parent.getPositionInPixels().x++;
				}
				else if (nextStepLoc.x - _parent.getPositionInPixels().x < 0)
				{
					_parent.getPositionInPixels().x--;
				}
				if (nextStepLoc.y - _parent.getPositionInPixels().y > 0)
				{
					_parent.getPositionInPixels().y++;
				}
				else if (nextStepLoc.y - _parent.getPositionInPixels().y < 0)
				{
					_parent.getPositionInPixels().y--;
				}
				
				if ((_parent.getPositionInPixels().x == nextStepLoc.x) && (_parent.getPositionInPixels().y == nextStepLoc.y))
				{
					this.pathStep++;
					if (this.pathStep < this.path.getLength())
					{
						Path.Step temp = this.path.getStep(pathStep);
						Location newLoc = new Location(temp.getX(), temp.getY());
						this._parent.setPosition(newLoc);
					}
				}
			}
		}
	}
	
	public void updatePath()
	{
		if (this._parent instanceof MobObject)
		{
			MobObject temp = (MobObject) this._parent;
			Location startLoc = this._parent.getPosition();
			Location endLoc = null;
			switch (temp.getTeam())
			{
				case Player1:
					endLoc = GameMap.inst().getTeamBaseLocation(Team.Player2);
					break;
				case Player2:
					endLoc = GameMap.inst().getTeamBaseLocation(Team.Player1);
					break;
			}
			if (startLoc.equals(GameMap.inst().getTeamSpawnPoint(_parent.getTeam())))
				this.path = GameMap.inst().getSpawnPath(_parent.getTeam());
			else
				this.path = GameMap.inst().getPathFinder().findPath(temp, startLoc.x, startLoc.y, endLoc.x, endLoc.y);
			this.pathStep = 0;
		}

	}
	
	public Path getPath()
	{
		return path;
	}
	
	public void setPath(Path path)
	{
		this.path = path;
	}

	/**
	 * Returns true if the specified location is being used by the specified
	 * GameObject, false otherwise
	 * 
	 * @return boolean
	 */
	public boolean containsLocation(GameObject gameObj, Location loc)
	{
		boolean bCollisionExists = false;
		
		for (int x = gameObj.getPosition().x; x < gameObj.getPosition().x + gameObj.getSize(); x++)
		{
			for (int y = gameObj.getPosition().y; y < gameObj.getPosition().y + gameObj.getSize(); y++)
			{
				if ((loc.x == x) && (loc.y == y))
				{
					bCollisionExists = true;
					break;
				}
			}
		}
		
		return bCollisionExists;
	}

	private void checkAndUpdate(GameObject obj)
	{
		if (containsLocation(obj, _parent.getPosition()))
		{
			obj.deleteObject();
			return;
		}
		
		if (this.path == null)
		{
			return;
		}

		Path oldPath = new Path();
		for (int i = 0; i < this.path.getLength(); i++)
		{
			oldPath.appendStep(path.getX(i), path.getY(i));
		}

		int oldPathStep = this.pathStep;

		updatePath();
		if (this.path == null)
		{
			for (int i = 0; i < oldPath.getLength(); i++)
			{
				Path.Step step = oldPath.getStep(i);
				if (containsLocation(obj, new Location(step.getX(), step.getY())))
				{
					if (i < this.pathStep)
						return;
					obj.deleteObject();
					this.pathStep = oldPathStep;
					this.path = oldPath;
					return;
				}
			}
		}
	}
	
	@Override
	public void deleteComponent()
	{
		EventManager.inst().unregisterForAll(this.towerBuildListenerType, this.towerListener);
		EventManager.inst().unregisterForAll(this.towerSoldListenerType, this.towerListener);
		super.deleteComponent();

	}

}
