package com.teamamerica.games.unicodewars.gui;

import org.fenggui.Display;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.lwjgl.EventHelper;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.util.Alphabet;
import org.fenggui.util.fonttoolkit.FontFactory;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.opengl.SlickCallable;

/**
 *
 */
public class FengWrapper implements InputListener
{

	private Display _fengDisplay;
	private java.awt.Font awtFont;
	private ITextRenderer renderer;
	private GameContainer _container;
	private Input _input;
	
	public FengWrapper(GameContainer container)
	{
		_container = container;
		_container.getInput().addPrimaryListener(this);
		_container.getInput().enableKeyRepeat();
		LWJGLBinding binding = new LWJGLBinding();
		
		// try
		// {
			//awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/font/Friz_Quadrata_TT.ttf"));
			awtFont = new java.awt.Font("Friz_Quadrata_TT",java.awt.Font.TRUETYPE_FONT, 12);
		// }
	/*	catch (FontFormatException e)
		{
			System.err.println("Caught a FontFormatException when trying to load src/data/font/Friz_Quadrata_TT.ttf");
			// e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Caught an IOException when trying to load src/data/font/Friz_Quadrata_TT.ttf");
			// e.printStackTrace();
		}*/

		System.out.println("awtFont size = " + awtFont.getSize2D());
		ImageFont newFont = FontFactory.renderStandardFont(awtFont, true, Alphabet.ENGLISH);
		ImageFont.setDefaultFont(newFont);
		renderer = new DirectTextRenderer(newFont);
		_fengDisplay = new Display(binding);
		_fengDisplay.setDepthTestEnabled(true);
		Binding.getInstance().setUseClassLoader(true);
	}
	
	/**
	 * FengGUI has it's own handle to the OpenGL context. Anytime we access the
	 * OpenGL context it must be done safely. This is done by wrapping it in
	 * calls to SlickCallable.
	 * 
	 * @param container
	 * @param game
	 * @param g
	 */
	public void render(GameContainer container, Game game, Graphics g)
	{
		SlickCallable.enterSafeBlock();
		_fengDisplay.display();
		SlickCallable.leaveSafeBlock();
	}
	
	private MouseButton slickButtonToFeng(int button)
	{
		MouseButton pressed;
		switch (button)
		{
			case (1):
				pressed = MouseButton.RIGHT;
				break;
			case (2):
				pressed = MouseButton.MIDDLE;
				break;
			default:
				pressed = MouseButton.LEFT;
				break;
		}
		return pressed;
	}
	
	public void controllerButtonPressed(int controller, int button)
	{
	}
	
	public void controllerButtonReleased(int controller, int button)
	{
	}
	
	public void controllerDownPressed(int controller)
	{
	}
	
	public void controllerDownReleased(int controller)
	{
	}
	
	public void controllerLeftPressed(int controller)
	{
	}
	
	public void controllerLeftReleased(int controller)
	{
	}
	
	public void controllerRightPressed(int controller)
	{
	}
	
	public void controllerRightReleased(int controller)
	{
	}
	
	public void controllerUpPressed(int controller)
	{
	}
	
	public void controllerUpReleased(int controller)
	{
	}
	
	public void inputEnded()
	{
	}
	
	public boolean isAcceptingInput()
	{
		return true;
	}
	
	public void keyPressed(int key, char c)
	{
		_fengDisplay.fireKeyPressedEvent(EventHelper.mapKeyChar(), EventHelper.mapEventKey());
		_fengDisplay.fireKeyTypedEvent(EventHelper.mapKeyChar());
	}
	
	public void keyReleased(int key, char c)
	{
		_fengDisplay.fireKeyReleasedEvent(EventHelper.mapKeyChar(), EventHelper.mapEventKey());
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		if (!_container.getInput().isMouseButtonDown(0))
			_fengDisplay.fireMouseMovedEvent(newx, _container.getHeight() - newy, MouseButton.LEFT, 0);
		else
			_fengDisplay.fireMouseDraggedEvent(newx, _container.getHeight() - newy, MouseButton.LEFT, 1);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount)
	{
		// _fengDisplay.fireMouseClickEvent(x, _container.getHeight()-y,
		// slickButtonToFeng(button), clickCount);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (_fengDisplay.fireMousePressedEvent(x, _container.getHeight() - y, slickButtonToFeng(button), 1))
		{
			_container.getInput().consumeEvent();
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (_fengDisplay.fireMouseReleasedEvent(x, _container.getHeight() - y, slickButtonToFeng(button), 1))
		{
			_container.getInput().consumeEvent();
		}
	}
	
	public void mouseWheelMoved(int change)
	{
		int x = _container.getInput().getMouseX();
		int y = _container.getInput().getMouseY();
		
		_fengDisplay.fireMouseWheel(x, _container.getHeight() - y, change > 0, 1, change);
	}

	/**
	 * Return the FengGUI display so that we can layout the items on the canvas.
	 * 
	 * @return
	 */
	public Display getDisplay()
	{
		return _fengDisplay;
	}
	
	public void setInput(Input input)
	{
		_input = input;
	}

	public ITextRenderer getRenderer()
	{
		return renderer;
	}
	
}
