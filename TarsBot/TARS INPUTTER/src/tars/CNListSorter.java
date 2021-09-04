package tars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CNListSorter {
	
	public static void getCarNumbers(boolean zeros){
		
		String path = "res/CN List Sorter/RAW CN DATA";
		String p2   = "res/CN List Sorter/SORTED";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			PrintWriter wr    = new PrintWriter(new FileWriter(p2), false);
			
			String str;
			String fstr;
			  while ((str = br.readLine()) != null) {
				  
				  if(zeros) {
					  fstr = str.substring(5, 15).replace(" ", "0");
				  }else {
					  fstr = str.substring(5, 15).replace(" ", "");;
				  }
				  
				  wr.printf("%s"+ "%n", fstr);
			  }
			  wr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
