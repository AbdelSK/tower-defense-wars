import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class SimpleTest extends BasicGame
{
	final int[] CENTER_OFFSET = { 15, 12 };
	final int[] OPPONENT_BASE_LOC = { 615, 12 };
	final int[] SPAWN_LOC = { 15, 452 };
	boolean showVerbose;
	int curMillis;
	int lastTimeMark;
	int mx;
	int my;
	ArrayList<int[]> towers;
	ArrayList<int[]> mobs;

	private void placeCharacter(ArrayList<int[]> al, int[] loc)
	{
		if (al.size() == 0)
			al.add(loc);
		else if (al.get(al.size() - 1)[0] != loc[0] || al.get(al.size() - 1)[1] != loc[1])
			al.add(loc);
	}

	public SimpleTest()
	{
		super("SimpleTest");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException
	{
		mx = 0;
		my = 0;
		towers = new ArrayList<int[]>();
		mobs = new ArrayList<int[]>();
		showVerbose = true;
		curMillis = 0;
		lastTimeMark = 0;
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		
		Input input = container.getInput();
		
		curMillis += delta;
		mx = input.getMouseX();
		my = input.getMouseY();
		
		if (input.isMouseButtonDown(0))
		{
			int loc[] = { (mx / 40) * 40 + CENTER_OFFSET[0], (my / 40) * 40 + CENTER_OFFSET[1]};
			
			if (loc[0] != OPPONENT_BASE_LOC[0] || loc[1] != OPPONENT_BASE_LOC[1])
			{
				placeCharacter(towers, loc);
			}
		}
		else if (input.isMouseButtonDown(1))
		{
			placeCharacter(mobs, SPAWN_LOC);
		}
		
		if (input.isKeyPressed(Input.KEY_V))
		{
			showVerbose = !showVerbose;
		}
		if (curMillis / 100 > lastTimeMark)
		{
			lastTimeMark = curMillis / 100;
			for (int[] i : mobs)
			{
				i[0]++;
				i[1]--;
			}
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{

		int width = container.getWidth();
		int height = container.getHeight();
		
		g.setColor(Color.white);
		for (int i = 0; i < height; i += 40)
			g.drawLine(0, i + 40, width, i + 40);
		
		for (int i = 0; i < width; i += 40)
			g.drawLine(i + 40, 0, i + 40, height);
		
		int row = (my / 40);
		int col = (mx / 40);
		
		if (col != OPPONENT_BASE_LOC[0] / 40 || row != OPPONENT_BASE_LOC[1] / 40)
		{
			g.fillRect(col * 40, row * 40, 40, 40);
		}

		if (showVerbose)
		{
			g.setColor(Color.yellow);
			g.drawString("row = " + row, 0, 100);
			g.drawString("col = " + col, 0, 200);
		}
		

		g.setColor(Color.green);
		g.drawString("$", OPPONENT_BASE_LOC[0], OPPONENT_BASE_LOC[1]);
		g.setColor(Color.white);

		for (int[] i : towers)
			g.drawString("A", i[0], i[1]);

		g.setColor(Color.red);
		for (int[] i : mobs)
			g.drawString("<", i[0], i[1]);
	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new SimpleTest());
			app.setShowFPS(false);
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
