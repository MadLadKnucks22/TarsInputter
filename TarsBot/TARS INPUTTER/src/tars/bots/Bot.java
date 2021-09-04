package tars.bots;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import tars.REF;
import tars.exceptions.WindowNotFoundException;
import tars.utility.Position;

public class Bot {
	
	protected Robot robot = null;
	protected Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected Clipboard clipboard = toolkit.getSystemClipboard();
	
	
	/**
	 * This is a basic robot class with utility functions such as moving, right clicking, enter, etc
	 */
	public Bot() {
		try {
			robot = new Robot();
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Utility function that sleeps the Tars Bot (bascially just a thread sleep)
	 * @param time Sleep time in ms
	 */
	public void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility function that is just the robot moveMouse
	 * @param x horizontal pixel position
	 * @param y vertical pixel position
	 */
	public void move(int x, int y) {
		sleep(20);
		robot.mouseMove(x, y);
		sleep(20);
	}
	
	/**
	 * Utility function that is just the robot moveMouse that uses a positon class
	 * @param pos Position 
	 */
	public void move(Position pos) {
		robot.mouseMove(pos.getX(), pos.getY());
		sleep(50);
	}
	
	/**
	 * Simulates a right click with a mouse
	 */
	public void rightClick() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		try {Thread.sleep(10);} 
		catch (InterruptedException e) {e.printStackTrace();}
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		sleep(20);
	}
	
	/**
	 * Simulates Control + C
	 */
	public void copy() {
		sleep(50);
		robot.keyPress(KeyEvent.VK_CONTROL);
		sleep(50);
		robot.keyPress(KeyEvent.VK_C);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_C);
	}
	
	
	/**
	 * Simulates clicking enter on a keyboard
	 */
	public void enter() {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	
	
	public void populateClipboard(String str) {
		StringSelection selection = new StringSelection(str);
    	clipboard.setContents(selection, selection);
	}
	
	public String getClipboard() throws UnsupportedFlavorException, IOException {
    	//Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		String result = null;
		
		result = (String) clipboard.getData(DataFlavor.stringFlavor);
		sleep(50);
		clearClipboard();
		
		return result;
    }
	
	public void clearClipboard() {
    	clipboard.setContents(new Transferable() {
			
			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return false;
			}
			
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[0];
			}
			
			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				throw new UnsupportedFlavorException(flavor);
			}
		}, null);
    }
	
	 private boolean imgMatch(BufferedImage img1) {
	    	int tarsGreen = new Color(0, 255, 0).getRGB();

	    	int rgb1;
	    	// check if it matchs tars window
			for(int x = 0; x < img1.getWidth(); x++) {
				for(int y = 0; y < img1.getHeight(); y++) {
	    			rgb1 = img1.getRGB(x, y);
	    			if(rgb1 == tarsGreen) {
	    				return true;
	    			}
	    		}
			}
			
			return false;
	 }
	
	 public void findTarsWindow() throws WindowNotFoundException {
	    	
	    	// first cap a test pic
	    	int xStart = 491-100;
	    	BufferedImage image = robot.createScreenCapture(new Rectangle(xStart, (int) (REF.SCREEN_HEIGHT-40), 800, 40));
	    	
	    	
	    	// first find the boundars of the blue bars and split each of them into seperate images
	    	int blueBar = new Color(118, 185, 237).getRGB();
	    	ArrayList<int[]> bounds = new  ArrayList<int[]>();
	    	int[] bound = new int[2];
	    	
	    	int h = image.getHeight();
	    	
	    	for(int i = 0; i < image.getWidth()-1; i++) {
	    		
	    		int rgb1 = image.getRGB(i, h-1);
	    		int rgb2 = image.getRGB(i+1, h-1);
	    		
	    		if(rgb1 != blueBar && rgb2 == blueBar) {
	    			bound[0] = i+1;
	    		}
	    		else if(rgb1 == blueBar && rgb2 != blueBar) {
	    			bound[1] = i;
	    			bounds.add(bound);
	    			bound = new int[2];
	    		}
	    	}
	    	
	    	// get sub images
	    	int posx = 0, posy = 0;
	    	
	    	
	    	for(int[] b : bounds) {
	    		BufferedImage img = image.getSubimage(b[0], 0, 40, h);
	    		
	    		// check if it matchs tars window
	    		if(imgMatch(img)) {
	    			posx = b[0] + img.getWidth()/2 + xStart;
	    			posy = (int) (REF.SCREEN_HEIGHT - img.getHeight()/2);
	    			
	    			break;
	    		}
	    		
	    	}
	    	if(posx == 0 || posy == 0) {
	    		throw new WindowNotFoundException("The Window Was Not Found");
	    	}
	    	else {
	    		Position p = new Position(posx, posy);
	    		move(p);
	    		rightClick();
	    		sleep(1000);
	    	}
	    	
	    }
	 
}
