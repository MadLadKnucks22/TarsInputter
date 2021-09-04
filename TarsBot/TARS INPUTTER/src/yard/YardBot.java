package yard;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import neuralnet.NeuralNetwork;
import ocr.ImageProcessing;
import tars.REF;
import tars.utility.Position;

public class YardBot {
	
	private NeuralNetwork nnCarNumbers;
	private NeuralNetwork nnCarINTS;
	private String[] outputs;
	public Robot robot = null;
	//private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	
	public YardBot() {
		try {
			robot = new Robot();
			nnCarNumbers = NeuralNetwork.load("neural net weights/Yard Master.sav");
			nnCarINTS    = NeuralNetwork.load("neural net weights/Yard Master Car INTS.sav");
			outputs = NeuralNetwork.getCarINTSOutputs();
			//nn = NeuralNetwork.load();
			
		} catch (AWTException e) {
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
	
	/**
	 * This method simulates dragging one car in yard master to a spot
	 * @param pos1 the position of car to be move (screen)
	 * @param pos2 the position of where the car is going to be moved (screen)
	 */
	public void moveCar(Position pos1, Position pos2) {
		move(pos1);
		rightClick();
		sleep(500);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(100);
		//drag(pos1, pos2);
		//move(pos2.getX()-5, pos2.getY());
		//sleep(100);
		//move(pos2);
		drag(pos1, pos2);
		sleep(100);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		sleep(100);
	}
	
	public void fillRack(String pullTrack, String rack) {
		
		int cars = 10;
		int startingndex = 33;
		Position[] pos = new Position[cars];
		Position[] desPos = new Position[cars];
		// get positions of all the cars to be moved
		Position tmpPos;
		Position tmp2;
		for(int i = 0; i < cars; i ++) {
			// first grab car at bottom put into spot 1
			tmpPos = new Position(REF.YARD_MASTER_CAR_NUMBERS_START.getX(), REF.YARD_MASTER_CAR_NUMBERS_START.getY() + (REF.YARD_MASTER_BOX_OFFSET*(startingndex-i)));
			tmp2 = new Position(650, REF.YARD_MASTER_CAR_NUMBERS_START.getY() + (REF.YARD_MASTER_BOX_OFFSET*i));
			pos[i] = tmpPos;
			desPos[i] = tmp2;
		}
		
		for(int i = 0; i < pos.length; i ++) {
			//System.out.println("x: " + p.getX() + " y: " + p.getY());
			sleep(500);
			moveCar(pos[i], desPos[i]);
		}
	}
	
	public void drag(Position p1, Position p2) {
		double x1 = p1.getX();
		double x2 = p2.getX();
		
		double y1 = p1.getY();
		double y2 = p2.getY();
		
		double increments = 15;
		double xd = (x2 - x1)/increments;
		double yd = (y2 - y1)/increments;
		
		for(int i = 0; i < increments; i++) {
			int x = (int) (x1 + (xd*i));
			int y = (int) (y1 + (yd*i));
			robot.mouseMove(x, y);
			sleep(10);
		}
		
	}
	
	/**
     * This function captures the individual digits in the load number and saves them
     * the load in question must be selected for the digits to be read correctly
     * @param index spot of the rail car you are capturing
     */
    public void captureTrainingDigits(int index) {
    	
		int x = REF.YARD_MASTER_CAR_NUMBERS_START.getX();
		int y = REF.YARD_MASTER_CAR_NUMBERS_START.getY() + (index*(20-1));
		//System.out.println("X: " + x + " Y: " + y);
    	BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 36, 8));
    	BufferedImage processedImage = ImageProcessing.yardMasterPreprocessing(bufferedImage);
    	
    	/*
    	try {
			ImageIO.write(processedImage,"png", new File("res/training images/yardmaster/Test.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
    	
    	for(int j = 0; j < 6; j++) {
    		int xi = j*6;
    		BufferedImage digit = processedImage.getSubimage(xi, 0, 6, 8);
    		
    		
    		File file = new File("res/training images/yardmaster/digit " + j + ".png");
    		try {
    			ImageIO.write(digit, "png", file);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		
    	}

    }
    
    public void captureTrainingLetters(int index) {
    	int x = REF.YARD_MASTER_CAR_LETTERS_START.getX();
		int y = REF.YARD_MASTER_CAR_LETTERS_START.getY() + (index*(20-1));
		//System.out.println("X: " + x + " Y: " + y);
    	BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 26, 8));
    	BufferedImage processedImage = ImageProcessing.yardMasterPreprocessing(bufferedImage);
    	
    	File file = new File("res/yardmaster/training data/training letters/Car INT.png");
		try {
			ImageIO.write(processedImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public String readCarNumber(int index) {
    	int x = REF.YARD_MASTER_CAR_NUMBERS_START.getX();
		int y = REF.YARD_MASTER_CAR_NUMBERS_START.getY() + (index*(20-1));
		//System.out.println("X: " + x + " Y: " + y);
    	BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 36, 8));
    	BufferedImage processedImage = ImageProcessing.yardMasterPreprocessing(bufferedImage);
    	
    	nnCarNumbers = NeuralNetwork.load("neural net weights/Yard Master.sav");
    	String carNumber = "";
    	
    	double[] X;
    	for(int j = 0; j < 6; j++) {
    		int xi = j*6;
    		BufferedImage digit = processedImage.getSubimage(xi, 0, 6, 8);
    		X = ImageProcessing.image2Matrix(digit);
    		
    		carNumber += nnCarNumbers.prediction2int(X);
    	}
    	
    	return carNumber;
    	
    }
    
    public String readCarINT(int index, String[] outputs) {
    	int x = REF.YARD_MASTER_CAR_LETTERS_START.getX();
		int y = REF.YARD_MASTER_CAR_LETTERS_START.getY() + (index*(20-1));
		BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(x, y, 26, 8));
    	BufferedImage processedImage = ImageProcessing.yardMasterPreprocessing(bufferedImage);
    	
    	
    	double[] X;
    	X = ImageProcessing.image2Matrix(processedImage);
    	String carINT = nnCarINTS.prediction2CarINT(X, outputs);
    	
    	
    	
    	return carINT;
    }
    
    public String readCar(int index, boolean zeros) {
    	String car = "";
    	String ints = "";
    	String number = "";
    	
    	
    	ints = readCarINT(index, outputs);
    	number = readCarNumber(index);
    	
    	if(!zeros) {
    		number = number.replaceFirst("^0+(?!$)", "");
    	}
    	
    	car = ints + number;
    	
    	return car;
    }
    
    
    
    /**
     * This will result in a String[] of the given cars up to the indexed spot
     * @param spots number of cars to be read (ex 34 cars)
     */
    public String[] readCars(int spots, boolean zeros) {
    	String[] cars = new String[spots];
    	
    	for(int i = 0; i < spots; i++) {
    		cars[i] = readCar(i, zeros);
    	}
    	
    	return cars;
    }
    
    public List<String> getYardMasterInventory(int... trackNumbers) {
    	Arrays.sort(trackNumbers);
    	boolean RackID = false;
    	boolean YardID = false;
    	List<String> cars = new ArrayList<String>();
    	for(int i = 0; i < trackNumbers.length; i++) {
    		int t = trackNumbers[i];
    		// check if the number is greater then 5 if it is change the selection to the rack
    		if(t <= 5 && !YardID) {
    			move(280, 133);
    			sleep(300);
    			rightClick();
    			move(280, 190);
    			sleep(300);
    			rightClick();
    			YardID = true;
    		}
    		if(t > 5 && !RackID ) {
    			move(280, 133);
    			sleep(100);
    			rightClick();
    			move(280, 165);
    			sleep(200);
    			rightClick();
    			RackID = true;
    		}
    		
    		sleep(100);
    		move(280, 150);
			sleep(100);
			rightClick();
			sleep(300);
    		if(t == 1) {
    			// change to track 1
    			move(280,170);
    		}
    		else if(t == 2) {
    			// change to track 2
    			move(280,185);
    		}
    		else if(t == 3) {
    			// change to track 3
    			move(280,200);
    		}
    		else if(t == 4) {
    			// change to track 4
    			move(280,215);
    		}
    		else if(t == 5) {
    			// change to track 5
    			move(280,230);
    		}
    		else if(t == 6) {
    			// change to track 6
    			move(280, 170);
    		}
    		else if(t == 7) {
    			// change to track 7
    			move(280, 185);
    		}
    		
    		sleep(100);
    		rightClick();
    		sleep(1000);
    		String[] tmp = readCars(36, false);
    		cars.addAll(Arrays.asList(tmp));

    	}
    	
    	// filter out empty spots
    	cars.removeIf(e -> (e.equals("GATX333333")));
    	cars.removeIf(e -> (e.equals("NATX333333")));
    	
		return cars;
    }
    
    /**
     * This prints out the javacode variable of the cars to be copy and pasted in a String[]
     * @param spots how many spots in the track
     */
    public void printReleaseBotTrack(int spots) {
    	for(int i = 0; i < spots; i++) {
    		String car = readCar(i, false);
    		if( !car.equals("GATX333333")) {
    			System.out.println("\"" + readCar(i, false) + "\",");
    		}
    	}
    }
    
    /**
     * This prints out the html code to paste in the browser for CN sequencing
     * @param trackNumbers which tracks you want printed out
     */
    public void printHTMLTrack(int... trackNumbers) {
    	
    	for(int track : trackNumbers) {
    		List<String> tmp = getYardMasterInventory(new int[] {track});
    		System.out.println("var track" + track +  "= [");
    		for(int j = 0; j < tmp.size(); j++) {
        		if(j != (tmp.size()-1)) {
        			System.out.println("'" + tmp.get(j) + "',");
        		}
        		else {
        			System.out.println("'" + tmp.get(j) + "'");
        		}
        	}
        	System.out.println("]");
    	}
    	
    }
    
    /*
    /**
     * this prints out the html code to paste in the browser for the releasing
     * @param spots number of cars to be read (ex 34 cars)
     * @deprecated use {@link #printHTMLTrack(int... trackNumbers)} instead.  
     */
    //@Deprecated
    
    /*
    public void printHTMLTrack(int spots) {
    	System.out.println("var track1 = [");
    	for(int i = 0; i < spots; i++) {
    		if(i != (spots-1)) {
    			System.out.println("'" + readCar(i, false) + "',");
    		}
    		else {
    			System.out.println("'" + readCar(i, false) + "'");
    		}
    	}
    	System.out.println("]");
    	
    }
    */
    
    /*
    /**
     * This prints out the html code to paste in the browser for CN sequencing
     * @param cars arraylist gotten from yardmaster
     * @deprecated use {@link #printHTMLTrack(int... trackNumbers)} instead.  
     */
    /*
    @Deprecated
    public void printHTMLTrack(List<String> cars) {
    	int spots = cars.size();
    	System.out.println("var track1 = [");
    	for(int i = 0; i < spots; i++) {
    		if(i != (spots-1)) {
    			System.out.println("'" + cars.get(i) + "',");
    		}
    		else {
    			System.out.println("'" + cars.get(i) + "'");
    		}
    	}
    	System.out.println("]");
    	
    }
    */
    
	
}
