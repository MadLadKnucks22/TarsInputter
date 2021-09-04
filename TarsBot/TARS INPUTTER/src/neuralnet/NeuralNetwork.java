package neuralnet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.imageio.ImageIO;


import ocr.ImageProcessing;
import tars.utility.FileResourceUtilities;

public class NeuralNetwork implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7278072051537940182L;
	
	
	Matrix weights_ih , weights_ho , bias_h , bias_o;    
    double l_rate=0.01;
    
    
    
    public NeuralNetwork(int i,int h,int o) {
        weights_ih = Matrix.intilizeRandomMatrix(h,i);
        weights_ho = Matrix.intilizeRandomMatrix(o,h);
        
        bias_h= Matrix.intilizeRandomMatrix(h,1);
        bias_o= Matrix.intilizeRandomMatrix(o,1);
        
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return this.toString();
    }
    
    public List<Double> predict(double[] X){
    	
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden.sigmoid();
        
        Matrix output = Matrix.multiply(weights_ho,hidden);
        output.add(bias_o);
        output.sigmoid();
        
        return output.toArray();
    }
    
    public int prediction2int(double[] X) {
    	List<Double> output = predict(X);
    	
    	int largest = 0;
    	  for ( int i = 1; i < output.size(); i++ )
    	  {
    	      if ( output.get(i) > output.get(largest) ) largest = i;
    	  }
    	
    	return largest;
    }
    
    public static String[] getCarINTSOutputs(){
    	
    	File dir = new File("res/yardmaster/training data/training letters"); 
    	String outputs[] = new String[dir.listFiles().length];
    	int counter = 0;
    	for(File f : dir.listFiles()) {
    		outputs[counter] = f.getName().substring(0, 4);
    		counter++;
    	}
    	
    	
    	return outputs;
    	
    }
    
    public static String[] getCarINTSOutputsReleasing(){
    	
    	File dir = new File("res/training images/releasing/IDS"); 
    	String outputs[] = new String[dir.listFiles().length];
    	int counter = 0;
    	for(File f : dir.listFiles()) {
    		outputs[counter] = f.getName().substring(0, 4);
    		counter++;
    	}
    	
    	
    	return outputs;
    	
    }
    
    public static String[] getCarNumberOutputsReleasing(){
    	
    	File dir = new File("res/training images/releasing/numbers"); 
    	String outputs[] = new String[dir.listFiles().length];
    	int counter = 0;
    	for(File f : dir.listFiles()) {
        	//System.out.println("Processing file :" + f.getName());
			String name = f.getName();
			
			// its a number
			if(name.length() < 6) {
				name = name.substring(0, 1);
			}
			else {
				name = "W";
			}
			outputs[counter] = name;
			counter++;
    	}
    	
    	
    	return outputs;
    	
    }
    
    public String prediction2CarINT(double[] X, String[] ints) {
    	List<Double> output = predict(X);
    	int largest = 0;
    	  for ( int i = 1; i < output.size(); i++ )
    	  {
    	      if ( output.get(i) > output.get(largest) ) largest = i;
    	  }
    	
    	return ints[largest];
    }
    
    
    public void train(double [] X,double [] Y){
    	
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden.sigmoid();
        
        Matrix output = Matrix.multiply(weights_ho,hidden);
        output.add(bias_o);
        output.sigmoid();
        
        Matrix target = Matrix.fromArray(Y);
        
        Matrix error = Matrix.subtract(target, output);
        Matrix gradient = output.dsigmoid();
        gradient.multiply(error);
        gradient.multiply(l_rate);
        
        Matrix hidden_T = Matrix.transpose(hidden);
        Matrix who_delta =  Matrix.multiply(gradient, hidden_T);
        
        weights_ho.add(who_delta);
        bias_o.add(gradient);
        
        Matrix who_T = Matrix.transpose(weights_ho);
        Matrix hidden_errors = Matrix.multiply(who_T, error);
        
        Matrix h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(l_rate);
        
        Matrix i_T = Matrix.transpose(input);
        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);
        
        weights_ih.add(wih_delta);
        bias_h.add(h_gradient);
        
    }
    
    public void fit(double[][]X,double[][]Y,int epochs)
    {
        for(int i=0;i<epochs;i++)
        {    
            int sampleN =  (int)(Math.random() * X.length );
            this.train(X[sampleN], Y[sampleN]);
        }
    }
    
    
    public static double[][] readInTrainingImages(String path){
    	//URL url = NeuralNetwork.class.getClassLoader().getResource("res/training images/0.png");
    	//File dir = new File("res/training images");
    	File dir = new File(path);
    	BufferedImage img = null;
    	double[][] data = new double[10][6*8];
    	int counter = 0;
    	
    	for(File f : dir.listFiles()) {
        	try {
        		//System.out.println("Processing file :" + f.getName());
    			img = ImageIO.read(f);
    			data[counter] = ImageProcessing.image2Matrix(img);
    			counter++;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return data;
    }
    
    
    public void save(String path) {
    	
    	try {
			//FileOutputStream fos = new FileOutputStream("res/neural net weights/nn.sav");
    		FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public static NeuralNetwork load(String path) {
    	
    	NeuralNetwork nn = null;
    	FileResourceUtilities res = new FileResourceUtilities();
    	try {
			//FileInputStream saveFile = new FileInputStream("res/neural net weights/nn.sav");
			
    		//String path = "neural net weights/nn.sav";
			InputStream is = res.getFileFromResourceAsStream(path);
    		
			ObjectInputStream restore = new ObjectInputStream(is);
			 nn = (NeuralNetwork) restore.readObject();
			 restore.close();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
    	return nn;
    }
    
    

    public void test(String path) {
    	try {
			BufferedImage img = ImageIO.read(new File(path));
			double[] data = ImageProcessing.image2Matrix(img);
			
			List<Double> output = predict(data);
			System.out.println(output.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void trainYardMasterDigits() {
    	NeuralNetwork nn = new NeuralNetwork(6*8, 6*8, 10);
    	double[][] X = readInTrainingImages("res/training images/yardmaster");
    	double[][] Y = TARGET_DATA;
    	nn.fit(X, Y, 50000);
    	nn.save("res/neural net weights/Yard Master.sav");
    }
    
    public static void trainYardMasterCarINTS() {
    	
    	BufferedImage img = null;
    	File dir = new File("res/yardmaster/training data/training letters");
    	int carInts = dir.listFiles().length;
    	String[] outputs = new String[carInts];
    	
    	
    	double[][] X = new double[carInts][26*8];
    	double[][] Y = Matrix.genOnesArray(carInts);
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
    	
    	
    	NeuralNetwork nn = new NeuralNetwork(26*8, 26*8, carInts);
    	nn.fit(X, Y, 30000);
    	nn.save("res/neural net weights/Yard Master Car INTS.sav");
    	
    	
    }
    
    public static final double[][] TARGET_DATA = {
    	{1,0,0,0,0,0,0,0,0,0}, 
    	{0,1,0,0,0,0,0,0,0,0}, 
    	{0,0,1,0,0,0,0,0,0,0}, 
    	{0,0,0,1,0,0,0,0,0,0}, 
    	{0,0,0,0,1,0,0,0,0,0}, 
    	{0,0,0,0,0,1,0,0,0,0}, 
    	{0,0,0,0,0,0,1,0,0,0}, 
    	{0,0,0,0,0,0,0,1,0,0}, 
    	{0,0,0,0,0,0,0,0,1,0},
    	{0,0,0,0,0,0,0,0,0,1}, 
    	};
    
	
}
