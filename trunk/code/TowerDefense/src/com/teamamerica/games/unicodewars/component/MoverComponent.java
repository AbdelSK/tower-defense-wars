package com.teamamerica.games.unicodewars.component;

import org.newdawn.slick.util.pathfinding.Path;
import com.teamamerica.games.unicodewars.object.GameObject;
import com.teamamerica.games.unicodewars.object.mob.MobObject;
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
	private int pathStep;
	private Timer stopwatch;
	
	public MoverComponent(GameObject owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
		this.stopwatch = new Timer();
		
		EventListener temp = new EventListener() {
			
			@Override
			public void onEvent(Event e)
			{
				// TODO Auto-generated method stub
				checkAndUpdate(e.sender);
			}
		};
		EventType type;
		switch (owner.getTeam())
		{
			case Player1:
				type = EventType.P2_TOWER_BUILT;
				break;
			case Player2:
				type = EventType.P1_TOWER_BUILT;
				break;
			default:
				type = EventType.P2_TOWER_BUILT;
				break;
		}
		EventManager.inst().registerForAll(type, temp);
		pathStep = 0;
	}
	
	@Override
	public void update(int elapsed)
	{
		if (stopwatch.xMilisecondsPassed(15))
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
					GameMap.inst().leaveSpace(_parent, _parent.getPosition());
					Path.Step temp = this.path.getStep(pathStep);
					Location newLoc = new Location(temp.getX(), temp.getY());
					this._parent.setPosition(newLoc);
					GameMap.inst().visitSpace(_parent, _parent.getPosition());
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
		GameMap.inst().leaveSpace(_parent, _parent.getPosition());
	}

}
