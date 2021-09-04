package ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class ImageProcessing {
	
	public static BufferedImage preProcessing(BufferedImage img) {
		
		//System.out.println(img.getRGB(0, 0));
		int blueColor = -1966081;
		// || 
		
		for(int i = 0; i < img.getWidth(); i ++) {
			for(int j = 0; j < img.getHeight(); j++) {
				
				if(img.getRGB(i, j) == blueColor) {
					img.setRGB(i, j, Color.WHITE.getRGB());
				}
				
				if(img.getRGB(i, j) != Color.WHITE.getRGB()) {
					img.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		
		return img;
	}
	
	public static BufferedImage preProcessingWhenSelected(BufferedImage img) {
		
		//System.out.println(img.getRGB(0, 0));
		Color tarBlue = new Color(0,120,215);
		int blue1 = tarBlue.getRGB();
		int blue2 = new Color(0,120,222).getRGB();
		int blue3 = new Color(0,120,229).getRGB();

		
		
		for(int i = 0; i < img.getWidth(); i ++) {
			for(int j = 0; j < img.getHeight(); j++) {
				
				//System.out.println(img.getRGB(i, j));
				Color col = new Color(img.getRGB(i, j));
				int rgb = col.getRGB();
				//System.out.println("red : " + r + " green :" + g + " blue: " + b);
				
				if(rgb == blue1 || rgb == blue2 || rgb == blue3)  {
					img.setRGB(i, j, Color.WHITE.getRGB());
				}
				else {
					img.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		
		return img;
	}
	
	private static boolean checkColor(int rgb, int[] colors) {
		
		for(int c : colors) {
			if(c == rgb) {
				return true;
			}
		}
		
		return false;
	}

	
	public static BufferedImage yardMasterPreprocessing(BufferedImage img) {
		//System.out.println(img.getRGB(0, 0));
				Color yardGray = new Color(214,211,206);
				int grey1 = yardGray.getRGB();
				int blue1 = new Color(8,36,107).getRGB();
				/*
				int col2 = new Color(214, 178, 115).getRGB();
				int col3 = new Color(181, 211, 140).getRGB();
				int col4 = new Color(181, 211, 206).getRGB();
				int col5 = new Color(148, 211, 206).getRGB();
				int col6 = new Color(82, 150, 206).getRGB();
				int col7 = new Color(181, 117, 41).getRGB();
				*/
				
				//int[] colors = new int[] {grey1, col2, col3, col4, col5, col6, col7};
				int[] colors = new int[] {grey1, blue1};
				
				for(int i = 0; i < img.getWidth(); i ++) {
					for(int j = 0; j < img.getHeight(); j++) {
						
						//System.out.println(img.getRGB(i, j));
						Color col = new Color(img.getRGB(i, j));
						int rgb = col.getRGB();
						//System.out.println("red : " + r + " green :" + g + " blue: " + b);
						
						if(checkColor(rgb, colors))  {
							img.setRGB(i, j, Color.WHITE.getRGB());
						}
						else {
							img.setRGB(i, j, Color.BLACK.getRGB());
						}
					}
				}
				
				return img;
	}
	
	
	
	// reads the image into a double array starting from the top row and going down
	public static double[] image2Matrix(BufferedImage img) {
		
		double[] data = new double[img.getWidth()*img.getHeight()];
		int counter = 0;
		
		int h = img.getHeight();
		int w = img.getWidth();
		
		for(int row = 0; row < w; row++) {
			for(int col = 0; col < h; col++) {
				data[counter] = (img.getRGB(row, col) == Color.BLACK.getRGB()) ? 1 : 0;
				counter++;
			}
		}
		return data;
	}
	
	public BufferedImage contrastImage(BufferedImage img) {
		return null;
	}
	
	
	
	
	
}

