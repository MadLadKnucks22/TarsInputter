package tars;





import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gui.TarsScreen;
import neuralnet.Matrix;
import neuralnet.NeuralNetwork;
import tars.bots.Bot;
import tars.bots.ReleaseBot;
import tars.bots.TarsBot;
import tars.utility.AllBot;
import tars.utility.Position;
import yard.YardBot;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		
		
		AllBot bot = new AllBot();
		bot.runTarsGUI();
		
	
		
		
		//YardBot yBot = new YardBot();
		//ReleaseBot Rbot = new ReleaseBot();
		
		/*
		yBot.move(REF.YARD_MASTER_WINDOW_POSITION);
		yBot.sleep(200);
		yBot.rightClick();
		
		List<String> cars = yBot.getYardMasterInventory(1,2,4);
		Rbot.findTarsWindow();
		Rbot.populateShipDatesforBOL(cars, 293);
		*/
		
		/*
		yBot.move(REF.YARD_MASTER_WINDOW_POSITION);
		yBot.sleep(200);
		yBot.rightClick();
		yBot.printHTMLTrack(1,2,4);
		*/
	}

		
}
	



