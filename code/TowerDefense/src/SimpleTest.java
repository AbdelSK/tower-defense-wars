import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class SimpleTest extends BasicGame
{
	
	int mx;
	int my;
	ArrayList<int[]> towers;

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

	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		
		Input input = container.getInput();
		
		mx = input.getMouseX();
		my = input.getMouseY();
		
		if (input.isMouseButtonDown(0))
		{
			int loc[] = { (mx / 40) * 40 + 15, (my / 40) * 40 + 12 };
			
			if (towers.size() == 0)
				towers.add(loc);
			else if (towers.get(towers.size() - 1)[0] != loc[0] || towers.get(towers.size() - 1)[1] != loc[1])
				towers.add(loc);
		}

	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{

		int width = container.getWidth();
		int height = container.getHeight();
		
		for (int i = 0; i < height; i += 40)
			g.drawLine(0, i + 40, width, i + 40);
		
		for (int i = 0; i < width; i += 40)
			g.drawLine(i + 40, 0, i + 40, height);
		
		int row = (my / 40);
		int col = (mx / 40);
		
		g.drawString("row = " + row, 0, 100);
		g.drawString("col = " + col, 0, 200);

		g.fillRect(col * 40, row * 40, 40, 40);
		
		for (int[] i : towers)
			g.drawString("A", i[0], i[1]);

	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new SimpleTest());
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
