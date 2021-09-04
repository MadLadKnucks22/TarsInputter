package tars.bots;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import neuralnet.Matrix;
import neuralnet.NeuralNetwork;
import ocr.ImageProcessing;
import tars.REF;
import tars.exceptions.WindowNotFoundException;
import tars.utility.Position;

public class ReleaseBot extends Bot {
	
	private NeuralNetwork nnIDs;
	private NeuralNetwork nnNumbers;
	
	//path stuff
	private static final String TRAINING_CARIDS_IMAGES_DIR =  "res/training images/releasing/IDS";
	private static final String TRAINING_CAR_NUMBERS_IMAGES_DIR =  "res/training images/releasing/numbers";
	
	//Boundary stuff
	private static final int CARID_BOUNDARY_WIDTH = 28;
	private static final int CARID_BOUNDARY_HEIGHT = 9;
	private static final int CARID_BOUNDARY_OFFSET = 21;
	
	private static final int CAR_NUMBER_BOUNDARY_WIDTH = 35;
	private static final int CAR_NUMBER_BOUNDARY_HEIGHT = 9;
	
	
	public static Position car_id_start = new Position(33, 455);
	
	
	// this is used as a dynamic variable for the car IDS
	private String[] outputs;
	private String[] outputsNumbers;
	
	
	public ReleaseBot() {
		super();
		nnIDs = NeuralNetwork.load("neural net weights/releasingIDS.sav");
		nnNumbers = NeuralNetwork.load("neural net weights/releasingNumbers.sav");
		
		outputs = NeuralNetwork.getCarINTSOutputsReleasing();
		outputsNumbers = NeuralNetwork.getCarNumberOutputsReleasing();
	}
	
	public String getCarID(int index) {
		
		BufferedImage caridImg = robot.createScreenCapture(new Rectangle(car_id_start.getX(), car_id_start.getY()+(index*CARID_BOUNDARY_OFFSET), CARID_BOUNDARY_WIDTH, CARID_BOUNDARY_HEIGHT));
		BufferedImage proccessedCarID = preProcessCarImages(caridImg);
		
		
		double[] X;
    	X = ImageProcessing.image2Matrix(proccessedCarID);
    	String carINT = nnIDs.prediction2CarINT(X, outputs);
		return carINT;
		
		/*
		// save just to see what is looks like
		try {
			ImageIO.write(proccessedCarID, "png", new File(TRAINING_IMAGES_PATH + "/test1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public void getCar(Position p, int index) {
		BufferedImage caridImg = robot.createScreenCapture(new Rectangle(p.getX(), p.getY()+(index*CARID_BOUNDARY_OFFSET), CARID_BOUNDARY_WIDTH, CARID_BOUNDARY_HEIGHT));
		BufferedImage proccessedCarID = preProcessCarImages(caridImg);
		
		
		double[] X;
    	X = ImageProcessing.image2Matrix(proccessedCarID);
    	String carINT = nnIDs.prediction2CarINT(X, outputs);
    	
    	
	}
	
	public String getCarNumber(int index) {
		
		int x = 60;
		int y = 455 + (index*CARID_BOUNDARY_OFFSET);
		
		// first find where the X terminates 
		int w = Color.WHITE.getRGB();
		int b = Color.BLACK.getRGB();
		
		int[] xTerminates = new int[] {b,b,w,w,w,w,w,b,b};
		
		BufferedImage imgTest = preProcessCarImages(robot.createScreenCapture(new Rectangle(x,y,CAR_NUMBER_BOUNDARY_WIDTH, CAR_NUMBER_BOUNDARY_HEIGHT)));
		
		
		int xStart = 0;
		
		for(int i = 0; i < 5; i++) {
			int xi = i;
			for(int j = 0; j < xTerminates.length; j++) {
				int rgb = imgTest.getRGB(xi, j);
				if(rgb != xTerminates[j]) {
					break;
				}
				if(j == xTerminates.length - 1) {
					xStart = x + xi + 2;
				}
			}
		}
		
		// now capture numbers
		BufferedImage carNumbers = preProcessCarImages(robot.createScreenCapture(new Rectangle(xStart,y, CAR_NUMBER_BOUNDARY_WIDTH+2, CAR_NUMBER_BOUNDARY_HEIGHT)));
		
		
		
		/*
		// save just to see what is looks like
		try {
			ImageIO.write(carNumbers, "png", new File("res/training images/releasing" + "/test1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		// now split up digits
		// but we first have to search through and look for 1s and if it meets the minimum 1 line for a one check extra pixels to see if it is a 4 or not
		
		/*
		int[] one = new int[] {b,b,b,b,b,b,b,b,b};
		ArrayList<Integer> onesLocation = new ArrayList<Integer>();
		
		for(int i = 0; i < carNumbers.getWidth(); i++) {
			for(int j = 0; j < one.length; j++ ) {
				int rgb = carNumbers.getRGB(i, j);
				if(rgb != one[j]) {
					break;
				}
				if(j == one.length-1) {
					// now check if it is a four or not
					rgb = carNumbers.getRGB(i+1, 6);
					if( rgb != b) {
						// then it is a one
						onesLocation.add(i);
					}
				}
			}
		}
		*/
		
		int width = 5;
		int height = 9;
		int onesCounter = 0;
		//int subX = 0;
		String num = "";
		
		for(int i = 0; i < 6; i++) {
			// first number will always be the 6x8
			int xi = width*i + onesCounter;
			if(i > 0 ) {
				xi = xi + 1*i;
			}
			
			/*
			// doing the ones checking
			for(int k = 0; k < onesLocation.size(); k++) {
				if( onesLocation.get(k) < xi + width && onesLocation.get(k) > xi ) {
					// then we have to add 1 to all future widths
					onesCounter = 0;
				}
			}
			*/
			
			BufferedImage digit = carNumbers.getSubimage(xi, 0, width, height);
			
			double[] X;
	    	X = ImageProcessing.image2Matrix(digit);
	    	
	    	num += nnNumbers.prediction2CarINT(X, outputsNumbers);
			/*
			// save just to see what is looks like
			try {
				ImageIO.write(digit, "png", new File("res/training images/releasing" + "/test digit" + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
			
		}
		
		return num;
		
	}
	
	public static void trainIDNeuralNet() {
		//NeuralNetwork nn = new NeuralNetwork(28*9, 28*9, 5);
		
		//double[][] X = NeuralNetwork.readInTrainingImages(TRAINING_CARIDS_IMAGES_DIR);
		
		
		File dir = new File(TRAINING_CARIDS_IMAGES_DIR);
    	int carInts = dir.listFiles().length;
    	double[][] X = new double[carInts][28*9];
    	double[][] Y = Matrix.genOnesArray(carInts);
    	
    	String[] outputs = new String[carInts];
    	
    	BufferedImage img = null;
    	int counter = 0;
    	
    	for(File f : dir.listFiles()) {
        	try {
        		//System.out.println("Processing file :" + f.getName());
    			img = ImageIO.read(f);
    			X[counter] = ImageProcessing.image2Matrix(img);
    			outputs[counter] = f.getName().substring(0, 4);
    			counter++;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	// the outputs will be based on alphabetical order ex CGTX will be 0, GATX will be 1 ... etc
    	
    	NeuralNetwork nn = new NeuralNetwork(28*9, 28*9, carInts);
    	nn.fit(X, Y, 30000);
    	nn.save("res/neural net weights/releasingIDS.sav");
	}
	
	public static void trainNumberNeuralNet() {
		
		File dir = new File(TRAINING_CAR_NUMBERS_IMAGES_DIR);
    	int numbers = dir.listFiles().length;
    	double[][] X = new double[numbers][5*9];
    	double[][] Y = Matrix.genOnesArray(numbers);
    	
    	String[] outputs = new String[numbers];
    	
    	BufferedImage img = null;
    	int counter = 0;
    	
    	for(File f : dir.listFiles()) {
        	try {
        		//System.out.println("Processing file :" + f.getName());
    			img = ImageIO.read(f);
    			X[counter] = ImageProcessing.image2Matrix(img);
    			String name = f.getName();
    			
    			// its a number
    			if(name.length() < 6) {
    				name = name.substring(0, 1);
    			}
    			else {
    				name = name.substring(0, 5);
    			}
    			System.out.println(name);
    			outputs[counter] = name;
    			counter++;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	NeuralNetwork nn = new NeuralNetwork(5*9, 5*9, numbers);
    	nn.fit(X, Y, 30000);
    	nn.save("res/neural net weights/releasingNumbers.sav");
	}
	
	private BufferedImage preProcessCarImages(BufferedImage img) {
		// keeps track if the image is selected
		int filter_color = Color.WHITE.getRGB();
		int blue = new Color(0, 120, 215).getRGB();
		
		// check down a column to see if there is any blue to see if its selected or not
		for(int i = 0; i < img.getHeight(); i ++) {
			if(img.getRGB(4, i) == blue) {
				//selected = true;
				filter_color = blue;
				break;
			}
		}
		
		for(int i = 0; i < img.getWidth(); i ++) {
			for(int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				
				if(rgb == filter_color) {
					img.setRGB(i, j, Color.WHITE.getRGB());
				}
				else {
					img.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		
		return img;
	}
	
	
	
	public void bringReleasePageDown() {
		// right click bottom of page;
		move(REF.CAR_ORDER_PAGE_FIRST_CAR);;
		sleep(100);
		rightClick();
		
		for(int i=0; i < 48; i++) {
			
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
			sleep(50);
		}
		
	}
	
	public void populateShipDatesforBOL(List<String> cars, int num) throws WindowNotFoundException {
    	
	//move(findTarsWindow());
	//rightClick();
	int counter = 0;
	move(REF.CAR_ORDER_PAGE_FIRST_CAR);
	
	// put cars into List
	List<String> chckedCars = new ArrayList<String>();
	
	clearClipboard();
	int indexCount = 0;
	// after the 25 car the cursor position dosnt have to be updated
	for(int i = 0; i < num; i++) {
		String car = getCarID(indexCount) + getCarNumber(indexCount).replace("W", "");
		System.out.println(car);
		sleep(100);
		// do some checking if this car has to be released
		if(cars.contains(car)) {
			move(REF.CAR_ORDER_PAGE_DATE.getX(), MouseInfo.getPointerInfo().getLocation().y+2);
			sleep(100);
			rightClick();
			sleep(500);
			enter();
			sleep(300);
			chckedCars.add(car);
			//System.out.println(car);
			counter++;
			// move mouse position back
			//move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), MouseInfo.getPointerInfo().getLocation().y);
			//sleep(50);
			//rightClick();
			//sleep(100);
		}
		
		// once it reads the first 23 index scroll the screen down
		// update mouse position if i < 24
		
		if (i % 23 == 0 && i > 0) {
			bringReleasePageDown();
			move(REF.CAR_ORDER_PAGE_FIRST_CAR);
			indexCount = 0;
		}
		else {
			move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY()+(21*(indexCount+1)));
			indexCount++;
		}
		
	}
	
	//************** error checking *****************************
	System.out.println("The total found was: " + counter);
	System.out.println("Cars not found: ");
	cars.forEach(e -> {
		if(!chckedCars.contains(e)) {
			System.out.println(e);
		}
	} );
	//************************************************************
	
	}
	
	/*
	public void legacy(List<String> cars, int num) throws WindowNotFoundException {
    	
		//move(findTarsWindow());
		//rightClick();
		int counter = 0;
		move(REF.CAR_ORDER_PAGE_FIRST_CAR);
		
		// put cars into List
		List<String> chckedCars = new ArrayList<String>();
		
		clearClipboard();
		int indexCount = 0;
		// after the 25 car the cursor position dosnt have to be updated
		for(int i = 0; i < num; i++) {
			String car = getCarID(indexCount) + getCarNumber(indexCount).replace("W", "");
			// also put in a catch if string is to long or short
			if(car.length() > 0) {
				
			}
			
			sleep(100);
			// do some checking if this car has to be released
			if(cars.contains(car)) {
				move(REF.CAR_ORDER_PAGE_DATE.getX(), MouseInfo.getPointerInfo().getLocation().y+2);
				sleep(100);
				rightClick();
				sleep(500);
				enter();
				sleep(300);
				chckedCars.add(car);
				System.out.println(car);
				counter++;
				// move mouse position back
				//move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), MouseInfo.getPointerInfo().getLocation().y);
				//sleep(50);
				//rightClick();
				//sleep(100);
			}
			
			// once it reads the first 23 index scroll the screen down
			// update mouse position if i < 24
			
			if (i % 23 == 0 && i > 0) {
				bringReleasePageDown();
				move(REF.CAR_ORDER_PAGE_FIRST_CAR);
				indexCount = 0;
			}
			move(REF.CAR_ORDER_PAGE_FIRST_CAR.getX(), REF.CAR_ORDER_PAGE_FIRST_CAR.getY()+(21*(indexCount+1)));
			
			indexCount++;
		}
		
		//************** error checking *****************************
		System.out.println("The total found was: " + counter);
		System.out.println("Cars not found: ");
		cars.forEach(e -> {
			if(!chckedCars.contains(e)) {
				System.out.println(e);
			}
		} );
		//************************************************************
		
		}
		*/

}
