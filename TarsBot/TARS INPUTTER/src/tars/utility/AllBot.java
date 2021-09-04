package tars.utility;

import gui.TarsScreen;
import tars.bots.Bot;
import tars.bots.TarsBot;
import yard.YardBot;

public class AllBot extends Bot {
	
	private TarsBot tarsBot;
	private YardBot yardBot;
	
	public AllBot() {
		tarsBot = new TarsBot();
		yardBot = new YardBot();
	}
	
	
	public void runTarsGUI() {
		TarsScreen.main(null);
	}


	
}
