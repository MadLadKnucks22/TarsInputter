package tars;

import java.awt.Dimension;
import java.awt.Toolkit;

import tars.utility.Position;

public class REF {
	
	private REF() {};
	
	private static final Dimension SIZE    = Toolkit.getDefaultToolkit().getScreenSize();
	public static final double SCREEN_WIDTH   = SIZE.getWidth();
	public static final double SCREEN_HEIGHT  = SIZE.getHeight();
	private static final int COLUMN_OFFSET = 55;
	public static final int RAIL_SPOT_OFFSET = 82;
	
	
	//public static final Position TARS_WINDOW_DIMENSION = new Position(620, 695);
	public static final Position TARS_WINDOW_DIMENSION = new Position( (int)(SCREEN_WIDTH * (0.27)), (int)(SCREEN_HEIGHT * (0.97685)));
	public static final Position TEST_DATES_POSITION   = new Position(470, 230);
	public static final Position MARKINGS_POSITION     = new Position(TEST_DATES_POSITION.getX() + COLUMN_OFFSET, TEST_DATES_POSITION.getY());
	public static final Position LEAK_POSITION         = new Position(TEST_DATES_POSITION.getX() + 2*COLUMN_OFFSET, TEST_DATES_POSITION.getY());
	public static final Position PLACARDS_POSITION     = new Position(TEST_DATES_POSITION.getX() + 3*COLUMN_OFFSET, TEST_DATES_POSITION.getY());
	public static final Position INSPECTION_POSITION   = new Position(TEST_DATES_POSITION.getX() + 4*COLUMN_OFFSET, TEST_DATES_POSITION.getY());
	public static final Position GASKET_POSITION       = new Position(TEST_DATES_POSITION.getX() + 5*COLUMN_OFFSET, TEST_DATES_POSITION.getY());
	
	// unhook position stuff
	public static final Position CHECKLIST_POSITION   = new Position(447, 125);
	
	// Position updated on 7/5/2021
	public static final Position UNHOOK_POSITION       = new Position( (int) (SCREEN_WIDTH*0.423) , (int)(SCREEN_HEIGHT*0.22));
	public static final Position BO_POSITION           = new Position(UNHOOK_POSITION.getX() + 0*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position VALVES_POSITION       = new Position(UNHOOK_POSITION.getX() + 1*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position DIPTUBE_POSITION      = new Position(UNHOOK_POSITION.getX() + 2*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position THERMOWELL_POSITION   = new Position(UNHOOK_POSITION.getX() + 3*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position SAMPLETUBE_POSITION   = new Position(UNHOOK_POSITION.getX() + 4*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position PLUGS_POSITION        = new Position(UNHOOK_POSITION.getX() + 5*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position LEL_POSITION          = new Position(UNHOOK_POSITION.getX() + 6*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position DOME_POSITION         = new Position(UNHOOK_POSITION.getX() + 7*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position SEALS_POSITION        = new Position(UNHOOK_POSITION.getX() + 8*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position SECURITY_POSITION     = new Position(UNHOOK_POSITION.getX() + 9*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	public static final Position OUT_POSITION          = new Position(UNHOOK_POSITION.getX() + 10*COLUMN_OFFSET, UNHOOK_POSITION.getY());
	
	// front page stuff
	public static final Position TEMP_POSITION         = new Position((int)(SCREEN_WIDTH*0.295), (int)(SCREEN_HEIGHT*0.256));
	public static final Position OUTAGE_POSITION       = new Position((int)(SCREEN_WIDTH*0.325), (int)(SCREEN_HEIGHT*0.256));
	public static final Position OUTAGE_ID_POSITION    = new Position((int)(SCREEN_WIDTH*0.353), (int)(SCREEN_HEIGHT*0.256));
	public static final Position CARS_OUT_POSITION     = new Position(340, 275);
	public static final Position SEAL_ID_POSITION      = new Position(750, 275);
	public static final Position CAR_STATUS_POSITION   = new Position(700, 300);
	public static final Position CAR_STATUS_ID_POSITION= new Position(765, 300);
	public static final Position SIGN_OFF_POSITION     = new Position(860, 275);
	public static final Position SELECT_SPOT_POSITION  = new Position(390, 275);
	
	
	// slider stuff on front page
	public static final Position SLIDER_FRONT_PAGE_BOTTOM = new Position((int)(SCREEN_WIDTH*0.953), (int)(SCREEN_HEIGHT*0.82));
	public static final Position SLIDER_FRONT_PAGE_TOP    = new Position((int)(SCREEN_WIDTH*0.953), (int)(SCREEN_HEIGHT*0.25));
	
	
	// order stuff
	public static final Position CAR_ORDER_PAGE_FIRST_CAR = new Position((int)(SCREEN_WIDTH*0.032), (int)(SCREEN_HEIGHT*0.425));
	public static final Position CAR_ORDER_PAGE_DATE      = new Position((int)(SCREEN_WIDTH*0.15), (int)(SCREEN_HEIGHT*0.425));
	
	
	// load info stuff
	public static final Position LOAD_POSITION = new Position(1170, 296);
	
	
	// Yard Master Stuff
	public static final Position YARD_MASTER_WINDOW_POSITION   = new Position( (int)(SCREEN_WIDTH * (0.27)), (int)(SCREEN_HEIGHT * (0.97685)));
	public static final Position YARD_MASTER_CAR_NUMBERS_START = new Position(140, 226);
	public static final Position YARD_MASTER_CAR_LETTERS_START = new Position(100, 226);
	public static final int      YARD_MASTER_BOX_OFFSET        = 19;
	

	
}
