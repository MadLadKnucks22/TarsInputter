package tars.bots;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_AMPERSAND;
import static java.awt.event.KeyEvent.VK_ASTERISK;
import static java.awt.event.KeyEvent.VK_AT;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CIRCUMFLEX;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COLON;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOLLAR;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_NUMBER_SIGN;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PLUS;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_QUOTEDBL;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UNDERSCORE;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import neuralnet.NeuralNetwork;
import ocr.ImageProcessing;
import tars.REF;
import tars.exceptions.WindowNotFoundException;
import tars.utility.Position;

public class TarsBot {
	
	
	
	private NeuralNetwork nn;
	public Robot robot = null;
	//private String clipBoardContents; 
	private boolean pageAtTop = true;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Clipboard clipboard = toolkit.getSystemClipboard();
	
	
	public TarsBot() {
		try {
			robot = new Robot();
			nn = NeuralNetwork.load("neural net weights/nn.sav");
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private void populateClipboard(String str) {
		StringSelection selection = new StringSelection(str);
    	//Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	clipboard.setContents(selection, selection);
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
	 * Utility function that is just the robot moveMouse that uses a positon class and rail spot
	 * @param pos
	 * @param railSpot
	 */
	public void move(Position pos, int railSpot) {
		move(pos.getX(), pos.getY() + (REF.RAIL_SPOT_OFFSET * railSpot));
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
	
	
	public void signColumn(Position col, String sig) {
		move(col);
		sleep(100);
		rightClick();
		for(int i = 0; i < 10; i++) {
			sleep(5);
			type(sig);
			// this is just so enter is not hit on the last column
			if( i != 9) {
				enter();
			}
		}
	}
	
	/**
	 * Types out given string of characters
	 * @param characters String that is to be typed
	 */
	public void type(CharSequence characters) {
		sleep(40);
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

    private void type(char character) {
        switch (character) {
        case 'a': doType(VK_A); break;
        case 'b': doType(VK_B); break;
        case 'c': doType(VK_C); break;
        case 'd': doType(VK_D); break;
        case 'e': doType(VK_E); break;
        case 'f': doType(VK_F); break;
        case 'g': doType(VK_G); break;
        case 'h': doType(VK_H); break;
        case 'i': doType(VK_I); break;
        case 'j': doType(VK_J); break;
        case 'k': doType(VK_K); break;
        case 'l': doType(VK_L); break;
        case 'm': doType(VK_M); break;
        case 'n': doType(VK_N); break;
        case 'o': doType(VK_O); break;
        case 'p': doType(VK_P); break;
        case 'q': doType(VK_Q); break;
        case 'r': doType(VK_R); break;
        case 's': doType(VK_S); break;
        case 't': doType(VK_T); break;
        case 'u': doType(VK_U); break;
        case 'v': doType(VK_V); break;
        case 'w': doType(VK_W); break;
        case 'x': doType(VK_X); break;
        case 'y': doType(VK_Y); break;
        case 'z': doType(VK_Z); break;
        case 'A': doType(VK_SHIFT, VK_A); break;
        case 'B': doType(VK_SHIFT, VK_B); break;
        case 'C': doType(VK_SHIFT, VK_C); break;
        case 'D': doType(VK_SHIFT, VK_D); break;
        case 'E': doType(VK_SHIFT, VK_E); break;
        case 'F': doType(VK_SHIFT, VK_F); break;
        case 'G': doType(VK_SHIFT, VK_G); break;
        case 'H': doType(VK_SHIFT, VK_H); break;
        case 'I': doType(VK_SHIFT, VK_I); break;
        case 'J': doType(VK_SHIFT, VK_J); break;
        case 'K': doType(VK_SHIFT, VK_K); break;
        case 'L': doType(VK_SHIFT, VK_L); break;
        case 'M': doType(VK_SHIFT, VK_M); break;
        case 'N': doType(VK_SHIFT, VK_N); break;
        case 'O': doType(VK_SHIFT, VK_O); break;
        case 'P': doType(VK_SHIFT, VK_P); break;
        case 'Q': doType(VK_SHIFT, VK_Q); break;
        case 'R': doType(VK_SHIFT, VK_R); break;
        case 'S': doType(VK_SHIFT, VK_S); break;
        case 'T': doType(VK_SHIFT, VK_T); break;
        case 'U': doType(VK_SHIFT, VK_U); break;
        case 'V': doType(VK_SHIFT, VK_V); break;
        case 'W': doType(VK_SHIFT, VK_W); break;
        case 'X': doType(VK_SHIFT, VK_X); break;
        case 'Y': doType(VK_SHIFT, VK_Y); break;
        case 'Z': doType(VK_SHIFT, VK_Z); break;
        case '`': doType(VK_BACK_QUOTE); break;
        case '0': doType(VK_0); break;
        case '1': doType(VK_1); break;
        case '2': doType(VK_2); break;
        case '3': doType(VK_3); break;
        case '4': doType(VK_4); break;
        case '5': doType(VK_5); break;
        case '6': doType(VK_6); break;
        case '7': doType(VK_7); break;
        case '8': doType(VK_8); break;
        case '9': doType(VK_9); break;
        case '-': doType(VK_MINUS); break;
        case '=': doType(VK_EQUALS); break;
        case '~': doType(VK_SHIFT, VK_BACK_QUOTE); break;
        case '!': doType(VK_EXCLAMATION_MARK); break;
        case '@': doType(VK_AT); break;
        case '#': doType(VK_NUMBER_SIGN); break;
        case '$': doType(VK_DOLLAR); break;
        case '%': doType(VK_SHIFT, VK_5); break;
        case '^': doType(VK_CIRCUMFLEX); break;
        case '&': doType(VK_AMPERSAND); break;
        case '*': doType(VK_ASTERISK); break;
        case '(': doType(VK_LEFT_PARENTHESIS); break;
        case ')': doType(VK_RIGHT_PARENTHESIS); break;
        case '_': doType(VK_UNDERSCORE); break;
        case '+': doType(VK_PLUS); break;
        case '\t': doType(VK_TAB); break;
        case '\n': doType(VK_ENTER); break;
        case '[': doType(VK_OPEN_BRACKET); break;
        case ']': doType(VK_CLOSE_BRACKET); break;
        case '\\': doType(VK_BACK_SLASH); break;
        case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
        case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
        case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
        case ';': doType(VK_SEMICOLON); break;
        case ':': doType(VK_COLON); break;
        case '\'': doType(VK_QUOTE); break;
        case '"': doType(VK_QUOTEDBL); break;
        case ',': doType(VK_COMMA); break;
        case '<': doType(VK_SHIFT, VK_COMMA); break;
        case '.': doType(VK_PERIOD); break;
        case '>': doType(VK_SHIFT, VK_PERIOD); break;
        case '/': doType(VK_SLASH); break;
        case '?': doType(VK_SHIFT, VK_SLASH); break;
        case ' ': doType(VK_SPACE); break;
        default:
            throw new IllegalArgumentException("Cannot type character " + character);
        }
    }
    
    private void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }
        robot.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
    }
    
    /**
     * Pastes the given string to the current selection
     * @param str string that will be pasted
     */
    private void paste(String str) {
    	robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }
    
    /**
     * This function populates the coloums using a copy and paste function rather then type out each 
     * @param col
     * @param sig
     * @param empties
     */
    private void pasteColumn(Position col, String sig, boolean[] empties) {
		move(col);
		sleep(100);
		rightClick();
		for(int i = 0; i < 10; i++) {
			sleep(50);
			if(empties[i]) {
				paste(sig);
			}
			// this is just so enter is not hit on the last column
			if( i != 9) {
				enter();
			}
		}
    }
    
    
    /**
     * This function will populate the entire checklist with the given signature
     * @param sig signature of the crew 
     */
    public void fillCheckList(String sig) {
    	// copying the crew to be filled
    	populateClipboard(sig);
    }
    
    /**
     * Function that just fills the intial hook up checklist, requires the page to allready be on the checklist page with the slider not moved.
     * @param sig signature of the crew that hooked up and took the outages
     */
    public void fillIntialHookupAndOutageChecklist(String sig) {
    	boolean[] empties = getFilledSpotsFromCheckList(true);
    	populateClipboard(sig);
    	pasteColumn(REF.TEST_DATES_POSITION, sig, empties);
		pasteColumn(REF.MARKINGS_POSITION, sig, empties);
		pasteColumn(REF.LEAK_POSITION, sig, empties);
		pasteColumn(REF.PLACARDS_POSITION, sig, empties);
		pasteColumn(REF.INSPECTION_POSITION, sig, empties);
    }
    
    
    /**
     * Function that just fills the unhook up checklist, requires the page to allready be on the checklist page with the slider moved.
     * @param sig signature of the crew that unhooked the cars
     */
    public void fillUnhookChecklist(String sig) {
    	boolean[] empties = getFilledSpotsFromCheckList(false);
    	populateClipboard(sig);
    	pasteColumn(REF.BO_POSITION, sig, empties);
    	pasteColumn(REF.VALVES_POSITION, sig, empties);
		pasteColumn(REF.DIPTUBE_POSITION, sig, empties);
		pasteColumn(REF.THERMOWELL_POSITION, sig, empties);
		pasteColumn(REF.SAMPLETUBE_POSITION, sig, empties);
		pasteColumn(REF.PLUGS_POSITION, sig, empties);
		pasteColumn(REF.LEL_POSITION, sig, empties);
		pasteColumn(REF.DOME_POSITION, sig, empties);
		pasteColumn(REF.SEALS_POSITION, sig, empties);
		pasteColumn(REF.SECURITY_POSITION, sig, empties);
		//pasteColumn(REF.OUT_POSITION, sig, empties);
    }
    
    public void scrollTarsFrontPageUp() {
    	move(REF.SLIDER_FRONT_PAGE_BOTTOM);
    	robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		try {Thread.sleep(10);
		move(REF.SLIDER_FRONT_PAGE_TOP);
		} 
		catch (InterruptedException e) {e.printStackTrace();}
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);	
		pageAtTop = true;
    }
    
    public void scrollTarsFrontPageDown() {
    	move(REF.SLIDER_FRONT_PAGE_TOP);
    	robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		try {Thread.sleep(10);
		move(REF.SLIDER_FRONT_PAGE_BOTTOM);
		} 
		catch (InterruptedException e) {e.printStackTrace();}
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		pageAtTop = false;
    }
    
    public void goToCheckList() {
    	// put in an image capture to check if it is allready on the check list page or not
    	// TODO
    	move(REF.CHECKLIST_POSITION);
    	rightClick();
    }
    
    public String readLoadNumber(int spot) {
    	//NeuralNetwork nn = NeuralNetwork.load();
    	double[] X = new double[48];
    	
    	String number = "";
		sleep(200);
		
		// grab one extra pixel then determine to cut it off or not
		int x = REF.LOAD_POSITION.getX();
		int y = REF.LOAD_POSITION.getY() + (spot*82);
		BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 38+1, 8));
    	BufferedImage processedImage = ImageProcessing.preProcessingWhenSelected(bufferedImage);
    	
    	// this means there is one pixel less in the image so we have to move every sub image x ahead by 1 pixel
    	if(processedImage.getRGB(1, 2) == Color.WHITE.getRGB()) {
    		//System.out.println("There is one less pixel");
    		processedImage = processedImage.getSubimage(1, 0, processedImage.getWidth()-1, processedImage.getHeight());
    	}
    	
    	// if comma position is at 17 then commaOffset must be 4 due to the case of 111 in the first 3 digits
    	
    	int commaOffset = 3;
    	boolean three1s = false;
    	if(processedImage.getRGB(17, 7) == Color.BLACK.getRGB()) {
    		commaOffset = 2;
    		three1s = true;
    	}
    	for(int j = 0; j < 6; j++) {
    		
    		int xi = j*6;
    		if(j > 0) {
    			xi = j*6 - 1;
    		}
    		
    		if(three1s && j == 2) {
    			xi = (j*6 - 1) - 1;
    		}
    		
    		
    		if(j > 2) {
    			xi = (j*6 - 1) + commaOffset;
    		}
    		
    		//BufferedImage nonProccessedDigit = bufferedImage.getSubimage(xi, 0, 6, 8);
    		//System.out.println("Xi " + xi);
    		BufferedImage bufferedDigit = processedImage.getSubimage(xi, 0, 6, 8);
    		X = ImageProcessing.image2Matrix(bufferedDigit);
    		number += nn.prediction2int(X);
    	}
    	//System.out.println(number);
    	
		return number;
    }
    
    public void getMouseLocation() {
    	
    	while(true) {
    		sleep(1000);
    		double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        	double Y = MouseInfo.getPointerInfo().getLocation().getY();
        	System.out.println("X: " + mouseX + " Y: " + Y);
    	}
    	
    }
    
    /**
     * This function captures the individual digits in the load number and saves them
     * the load in question must be selected for the digits to be read correctly
     * @param railSpot 0-9 the spot of the rail car you are capturing
     */
    public void captureTrainingDigits(int railSpot) {
    	
		// grab one extra pixel then determine to cut it off or not
		int x = REF.LOAD_POSITION.getX();
		int y = REF.LOAD_POSITION.getY() + (railSpot*82);
		//System.out.println("X: " + x + " Y: " + y);
    	BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 38+1, 8));
    	BufferedImage processedImage = ImageProcessing.preProcessingWhenSelected(bufferedImage);
    	
    	// this means there is one pixel less in the image so we have to move every sub image x ahead by 1 pixel
    	if(processedImage.getRGB(1, 2) == Color.WHITE.getRGB()) {
    		System.out.println("There is one less pixel");
    		processedImage = processedImage.getSubimage(1, 0, processedImage.getWidth()-1, processedImage.getHeight());
    	}
    	
    	
    	try {
			ImageIO.write(processedImage,"png", new File("res/Test.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	// if comma position is at 17 then commaOffset must be 4 due to the case of 111 in the first 3 digits
    	
    	int commaOffset = 3;
    	boolean three1s = false;
    	if(processedImage.getRGB(17, 7) == Color.BLACK.getRGB()) {
    		commaOffset = 2;
    		three1s = true;
    	}
    	for(int j = 0; j < 6; j++) {
    		
    		int xi = j*6;
    		if(j > 0) {
    			xi = j*6 - 1;
    		}
    		
    		if(three1s && j == 2) {
    			xi = (j*6 - 1) - 1;
    		}
    		
    		
    		if(j > 2) {
    			xi = (j*6 - 1) + commaOffset;
    		}
    		
    		//BufferedImage nonProccessedDigit = bufferedImage.getSubimage(xi, 0, 6, 8);
    		//System.out.println("Xi " + xi);
    		BufferedImage bufferedDigit = processedImage.getSubimage(xi, 0, 6, 8);
    		
    		File file = new File("res/digits/digit capture" + j + ".png");
    		try {
    			ImageIO.write(bufferedDigit, "png", file);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}
    }
    
    /**
     * This function populates the temperature and outage of the car in the given rail spot
     * @param railSpot 0-9
     * @param sig what crew hooked it up 
     * @param temp Temperature
     * @param intialOutage Best guess at what the outage is doesn't really matter
     */
    
    private void populateTempAndOutage(int railSpot, String sig, String temp, String intialOutage) {
    	sleep(200);
    	// correct for position if railSpot is 8 or 9
		if(railSpot >= 8 && pageAtTop) {
			//System.out.println("Scrolling down");
			System.out.println("Scrolling window down");
			scrollTarsFrontPageDown();
			railSpot = railSpot - 2;
		}
		else if(railSpot >= 8 && !pageAtTop) {
			railSpot = railSpot - 2;
		}
		
		sleep(500);
    	
    	move(REF.TEMP_POSITION, railSpot);
    	rightClick();
    	sleep(800);
    	// after the spot has been selected capture the current load weight
    	//double trueLoad = Double.parseDouble(getLoadNumber(railSpot));
    	double trueLoad = Double.parseDouble(readLoadNumber(railSpot));
    	
    	// use true load to verify if a rail car is there or not
    	if(trueLoad < 140000) {
    		
    		
    		
        	// enter the temp
        	type(temp);
        	sleep(200);
        	
        	// enter the intialoutage
        	move(REF.OUTAGE_POSITION, railSpot);
        	rightClick();
        	sleep(2000);
        	type(intialOutage);
        	sleep(200);
        	
        	// get updated outage
        	//double Y1 = Double.parseDouble(readLoadNumber(railSpot));
        	
        	// move cursor so position updates
        	move(REF.OUTAGE_ID_POSITION, railSpot);
        	rightClick();
        	sleep(200);
        	
        	double outageIncrement = 0.25;
        	double X1 = Double.parseDouble(intialOutage);
        	
        	double X2 = X1 + outageIncrement;
        	move(REF.OUTAGE_POSITION, railSpot);
        	rightClick();
        	sleep(200);
        	type(Double.toString(X2));
        	
        	//double outage2 = Double.parseDouble(getLoadNumber(railSpot));
        	double Y2 = Double.parseDouble(readLoadNumber(railSpot));
        	
        	// linear interpolation
        	//double m = (Y2-Y1)/(X2-X1);
        	//double b = Y2 - (m*X2);
        	//double xo = (trueLoad - b)/m;
        	//double delta = Y2-Y1;
        	
        	double error = Y2 - trueLoad;
        	double tol = 250;
        	double oldX = X1;
        	double newX = 0;
        	double newY = Y2;
        	int itteration = 0;
        	
        	while( Math.abs(error) > tol) {
        		sleep(400);
            	if(newY > trueLoad) {
            		newX = oldX + outageIncrement;
            	}
            	else {
            		newX = oldX - outageIncrement;
            	}
            	
            	move(REF.OUTAGE_POSITION, railSpot);
            	rightClick();
            	sleep(400);
            	type(Double.toString(newX));
            	oldX = newX;
            	
            	// move cursor so position updates
            	move(REF.OUTAGE_ID_POSITION, railSpot);
            	rightClick();
            	sleep(400);
            	newY = Double.parseDouble(readLoadNumber(railSpot));
            	error = newY - trueLoad;
            	System.out.println("error in spot: "+ railSpot + "is now: " + error);
            	itteration++;
            	if(itteration > 6) {
            		break;
            	}
        	}
        	
        	
        	
        	
        	//System.out.println("Outage should be: " + xo + " error: " + error );
        	//String roundedOutage = Double.toString(Math.round(xo*4)/4d);
        	String roundedOutage = Double.toString(newX);
        	
        	// Fill in ID position
        	move(REF.OUTAGE_ID_POSITION, railSpot);
        	rightClick();
        	sleep(200);
        	type(sig);
        	
        	
        	// move back to outage and fill in
        	// check to make sure outage is something within reason
        	if(Double.parseDouble(roundedOutage) <= 24 && Double.parseDouble(roundedOutage) >= 15) {
        		move(REF.OUTAGE_POSITION, railSpot);
            	rightClick();
            	sleep(200);
            	type(roundedOutage);
        	}
        	else {
        		System.out.println("outage does not make sense something went wrong in spot: " + railSpot);
        	}
        	
        	
        	/*
        	System.out.println("trueLoad: " + trueLoad);
        	System.out.println("outage1: " + Y1);
        	System.out.println("outage2: " + Y2);
        	System.out.println("m: " + m);
        	System.out.println("b: " + b);
        	System.out.println("xo: " + Math.round(xo*4)/4d);
        	*/
    	}
    	else {
    		//System.out.println("No car is in spot: " + railSpot);
    	}
    	
    	
    }
    
    
    
    /**
     * This function populates the temperature and outage of all cars
     * @param sig what crew hooked it up 
     * @param temp Temperature
     * @param intialOutage Best guess at what the outage is doesn't really matter
     */
    public void populateCarsTempAndOutage(String sig, String temp, String intialOutage) {
    	for(int i = 0; i < 10; i++) {
    		populateTempAndOutage(i, sig, temp, intialOutage);
    	}
    }
    
    private void populateOutStatusSealIDMTSignoff(int railSpot, String sig) {
    	
    	// checking that page is at top and scrolling page down if neccesassary
    	if(railSpot >= 8 && pageAtTop) {
			//System.out.println("Scrolling down");
			scrollTarsFrontPageDown();
			railSpot = railSpot - 2;
		}
		else if(railSpot >= 8 && !pageAtTop) {
			railSpot = railSpot - 2;
		}
    	
    	// first check to see if there is a car there
    	// first we have to select the row
    	move(REF.SELECT_SPOT_POSITION, railSpot);
    	rightClick();
    	sleep(400);
    	
    	double trueLoad = Double.parseDouble(readLoadNumber(railSpot));
    	int sleepTime = 1000;
    	if(trueLoad < 140000) {
    		
    		// changing status to out
    		move(REF.CARS_OUT_POSITION, railSpot);
    		rightClick();
    		sleep(sleepTime);
    		type("O");
    		
    		// Seal ID
    		move(REF.SEAL_ID_POSITION, railSpot);
    		rightClick();
    		sleep(sleepTime);
    		type(sig);
    		//paste2();
    		
    		// Sign off
    		move(REF.SIGN_OFF_POSITION, railSpot);
    		rightClick();
    		sleep(sleepTime);
    		type(sig);
    		//paste2();
    		
    		// Car Status
    		move(REF.CAR_STATUS_POSITION, railSpot);
    		rightClick();
    		sleep(sleepTime);
    		type("M");
    		sleep(200);
    		
    		// car status sign off
    		move(REF.CAR_STATUS_ID_POSITION, railSpot);
    		rightClick();
    		sleep(sleepTime);
    		type(sig);
    		//paste2();
    	}
    	else {
    		//System.out.println("There is no car in spot: " + railSpot);
    	}
    	
    }
    
    
    /**
     * Populates the Car out status seal ids, MT status, and Signoff on all cars
     * @param sig
     */
    public void populateCarsOutStatusSealIDsMTSignoff(String sig) {
    	for(int i = 0; i < 10; i++) {
    		populateOutStatusSealIDMTSignoff(i, sig);
    	}
    }
    
    /**
     * Returns a boolean array where each index is true or false 
     * where true indicates a car is there and the spot can be populated
     * @param hookup indicates weather this is the connect or disconnect checklist
     * @return
     */
    public boolean[] getFilledSpotsFromCheckList(boolean hookup) {
    	int checkListOffset = 23;
    	boolean[] empties = new boolean[10];
    	int bluergb = new Color(0,120,215).getRGB();
    	int x = hookup ? (463-13-1) : (800-13-1);
    	
    	
    	for(int i = 0; i < 10; i++) {
        	int y = (230 - 1)-3 + (i*checkListOffset);
        	int w = 17;
        	int h = 19;
        	
        	BufferedImage img = robot.createScreenCapture(new Rectangle(x, y, w, h));
        	int rgb = img.getRGB(8, 7);
        	
        	empties[i] = (rgb == Color.WHITE.getRGB() || rgb == bluergb) ? true : false;
    	}
    	
    	return empties;
    }
	
    
    
    public Position findTarsWindow() throws WindowNotFoundException {
    	
    	// first cap a test pic
    	System.out.println("Finding tars window");
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
    	int posx, posy;
    	
    	
    	for(int[] b : bounds) {
    		BufferedImage img = image.getSubimage(b[0], 0, 40, h);
    		
    		// check if it matchs tars window
    		if(imgMatch(img)) {
    			posx = b[0] + img.getWidth()/2 + xStart;
    			posy = (int) (REF.SCREEN_HEIGHT - img.getHeight()/2);
    			
    			return new Position(posx, posy);
    		}
    		
    	}
    	throw new WindowNotFoundException("The Window Was Not Found");
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
    
    private void clearClipboard() {
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
    
    public String getClipboard() throws UnsupportedFlavorException, IOException {
    	//Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		String result = null;
		
		result = (String) clipboard.getData(DataFlavor.stringFlavor);
		sleep(50);
		clearClipboard();
		
		return result;
    }
    
    public static ArrayList<String> getCarsForRelease(){
    	ArrayList<String> cars = new ArrayList<String>();
    	
    	String path = "res/CN Car Release/cars for release.txt";
    	try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line;
			StringBuilder sb;
			while( (line=br.readLine())!=null ) {
				sb = new StringBuilder(line);
				if(line.charAt(4) == '0') {
					sb.deleteCharAt(4);
				}
				cars.add(sb.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    	return cars;
    }
    
    
    public void populateShipDatesforBOL(ArrayList<String> cars, int num) {
    	
		move(REF.TARS_WINDOW_DIMENSION);
		rightClick();
		move(REF.CAR_ORDER_PAGE_FIRST_CAR);
		rightClick();
		
		clearClipboard();
		int erCount = 0;
		
		// after the 25 car the cursor position dosnt have to be updated
		for(int i = 0; i < num; i++) {
			sleep(2000);
			copy();
			String car = "";
			try {
				car = getClipboard();
			}
			catch(UnsupportedFlavorException | IOException | IllegalStateException e) {
				erCount++;
				if(erCount < 4) {
					System.out.print("Something didnt copy retry spot");
					// move the cursor up and back down
					if(i == 0) {
						move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY()+(21*(i+1)));
						sleep(100);
						rightClick();
						sleep(100);
						move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY());
					}
					else {
						//TODO fix this so it works when i > 24
						if(i < 24) {
							move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY()+(21*(i-1)));
							sleep(100);
							rightClick();
							sleep(100);
						}
						else {
							int x = MouseInfo.getPointerInfo().getLocation().x;
							int y = MouseInfo.getPointerInfo().getLocation().y;
							move(x, y - 21);
							sleep(100);
							rightClick();
							sleep(100);
							move(x, y);
						}
					}
					
					i--;
				}
				
			}
			// also put in a catch if string is to long or short
			if(car.length() > 0) {
				System.out.println(car);
			}
			
			sleep(100);
			// do some checking if this car has to be released
			if(cars.contains(car)) {
				move(REF.CAR_ORDER_PAGE_DATE.getX(), MouseInfo.getPointerInfo().getLocation().y);
				sleep(100);
				rightClick();
				sleep(50);
				enter();
				sleep(100);
				// move mouse position back
				move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), MouseInfo.getPointerInfo().getLocation().y);
				sleep(50);
				rightClick();
				sleep(100);
			}
			
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
			
			// update mouse position if i < 24
			if(i < 24) {
				move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY()+(21*(i+1)));
			}
		}
    }
    
}
