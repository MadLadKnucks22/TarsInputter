package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import tars.bots.TarsBot;
import tars.exceptions.WindowNotFoundException;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class TarsScreen {

	private JFrame frmTarsInputter;
	private JTextField textFieldSig;
	private JTextField textFieldTemperature;
	private JTextField textFieldOutage;
	
	
	//*********************** Tars Bot Inputs********************************
	// booleans to see what is selected
	private boolean intialHookUpFront = false;
	private boolean unHookFront       = false;
	private boolean intialHookUpBack  = false;
	private boolean unHookBack        = false;
	
	// front page stuff
	private String sig;
	private String temp;
	private String outage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TarsScreen window = new TarsScreen();
					window.frmTarsInputter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TarsScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTarsInputter = new JFrame();
		frmTarsInputter.setTitle("Tars Inputter");
		frmTarsInputter.setBounds(100, 100, 666, 160);
		frmTarsInputter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTarsInputter.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 645, 121);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.GRAY);
		frmTarsInputter.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lbSig = new JLabel("Crew");
		GridBagConstraints gbc_lbSig = new GridBagConstraints();
		gbc_lbSig.anchor = GridBagConstraints.WEST;
		gbc_lbSig.insets = new Insets(0, 0, 5, 5);
		gbc_lbSig.gridx = 0;
		gbc_lbSig.gridy = 0;
		panel.add(lbSig, gbc_lbSig);
		
		textFieldSig = new JTextField();
		textFieldSig.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// checking to see if enter key was hit
				if(e.getID() == 1001) {
					textFieldTemperature.requestFocus();
				}
				
			}
		});
		lbSig.setLabelFor(textFieldSig);
		textFieldSig.setText("BCREW");
		GridBagConstraints gbc_textFieldSig = new GridBagConstraints();
		gbc_textFieldSig.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldSig.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSig.gridx = 1;
		gbc_textFieldSig.gridy = 0;
		panel.add(textFieldSig, gbc_textFieldSig);
		textFieldSig.setColumns(10);
		
		JCheckBox chckbxInitialHookup = new JCheckBox("Intial Hookup & Outages");
		GridBagConstraints gbc_chckbxInitialHookup = new GridBagConstraints();
		gbc_chckbxInitialHookup.fill = GridBagConstraints.BOTH;
		gbc_chckbxInitialHookup.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxInitialHookup.gridx = 3;
		gbc_chckbxInitialHookup.gridy = 0;
		panel.add(chckbxInitialHookup, gbc_chckbxInitialHookup);
		
		JLabel lbTemp = new JLabel("Temperature");
		GridBagConstraints gbc_lbTemp = new GridBagConstraints();
		gbc_lbTemp.anchor = GridBagConstraints.EAST;
		gbc_lbTemp.insets = new Insets(0, 0, 5, 5);
		gbc_lbTemp.gridx = 0;
		gbc_lbTemp.gridy = 1;
		panel.add(lbTemp, gbc_lbTemp);
		
		textFieldTemperature = new JTextField();
		textFieldTemperature.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// checking to see if enter key was hit
				if(e.getID() == 1001) {
					textFieldOutage.requestFocus();
				}
				
			}
		});
		lbTemp.setLabelFor(textFieldTemperature);
		GridBagConstraints gbc_textFieldTemperature = new GridBagConstraints();
		gbc_textFieldTemperature.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTemperature.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTemperature.gridx = 1;
		gbc_textFieldTemperature.gridy = 1;
		panel.add(textFieldTemperature, gbc_textFieldTemperature);
		textFieldTemperature.setColumns(10);
		
		JCheckBox chckbxUnhook = new JCheckBox("Unhooking");
		GridBagConstraints gbc_chckbxUnhook = new GridBagConstraints();
		gbc_chckbxUnhook.fill = GridBagConstraints.BOTH;
		gbc_chckbxUnhook.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUnhook.gridx = 3;
		gbc_chckbxUnhook.gridy = 1;
		panel.add(chckbxUnhook, gbc_chckbxUnhook);
		
		JLabel lbOutage = new JLabel("Outage");
		GridBagConstraints gbc_lbOutage = new GridBagConstraints();
		gbc_lbOutage.anchor = GridBagConstraints.WEST;
		gbc_lbOutage.insets = new Insets(0, 0, 5, 5);
		gbc_lbOutage.gridx = 0;
		gbc_lbOutage.gridy = 2;
		panel.add(lbOutage, gbc_lbOutage);
		
		textFieldOutage = new JTextField();
		lbOutage.setLabelFor(textFieldOutage);
		GridBagConstraints gbc_textFieldOutage = new GridBagConstraints();
		gbc_textFieldOutage.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldOutage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOutage.gridx = 1;
		gbc_textFieldOutage.gridy = 2;
		panel.add(textFieldOutage, gbc_textFieldOutage);
		textFieldOutage.setColumns(10);
		
		JCheckBox chckbxIntialHookUpChecklist = new JCheckBox("Intial Hookup Check List");
		GridBagConstraints gbc_chckbxIntialHookUpChecklist = new GridBagConstraints();
		gbc_chckbxIntialHookUpChecklist.fill = GridBagConstraints.BOTH;
		gbc_chckbxIntialHookUpChecklist.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxIntialHookUpChecklist.gridx = 3;
		gbc_chckbxIntialHookUpChecklist.gridy = 2;
		panel.add(chckbxIntialHookUpChecklist, gbc_chckbxIntialHookUpChecklist);
		
		JCheckBox chckbxUnhookCheckList = new JCheckBox("Unhooking Check List");
		GridBagConstraints gbc_chckbxUnhookCheckList = new GridBagConstraints();
		gbc_chckbxUnhookCheckList.fill = GridBagConstraints.BOTH;
		gbc_chckbxUnhookCheckList.gridx = 3;
		gbc_chckbxUnhookCheckList.gridy = 3;
		panel.add(chckbxUnhookCheckList, gbc_chckbxUnhookCheckList);
		
		JButton btnEnter = new JButton("Enter");
		GridBagConstraints gbc_btnEnter = new GridBagConstraints();
		gbc_btnEnter.fill = GridBagConstraints.BOTH;
		gbc_btnEnter.insets = new Insets(0, 0, 0, 5);
		gbc_btnEnter.gridx = 1;
		gbc_btnEnter.gridy = 3;
		// handle enter event
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sig = textFieldSig.getText();
				temp = textFieldTemperature.getText();
				outage = textFieldOutage.getText();
				
				// put in checks so enter dosnt work if no check boxses are selected and the appropriate boxes arnt filled
				intialHookUpFront = chckbxInitialHookup.isSelected() ? true : false;
				intialHookUpBack  = chckbxIntialHookUpChecklist.isSelected() ? true : false;
				unHookFront       = chckbxUnhook.isSelected() ? true: false;
				unHookBack        = chckbxUnhookCheckList.isSelected() ? true : false;
				
				
				// check to make sure at least one box is checked
				boolean doTars = false;
				String error = "";
				if(intialHookUpFront || intialHookUpBack || unHookFront || unHookBack) {
					// a check to make sure signature is populated
					if(sig.length() > 1) {
						
						if(intialHookUpFront) {
							if((temp.length() > 0 && outage.length() > 0)) {
								doTars = true;
							}
							else if((temp.length() < 1 || outage.length() < 1)) {
								error = "Temp and outage need to be entered";
							}
						}
						else if(intialHookUpBack || unHookFront || unHookBack) {
							doTars = true;
						}
					}
					else {
						error = "No signature entered";
					}
				}
				else {
					error = "No check box was checked";
				}
				
				if(doTars) {
					doTARS();
				}
				else {
					System.out.println(error);
				}
				
			}
		});
		panel.add(btnEnter, gbc_btnEnter);
		
	}
	
	/**
	 * This method runs through the tars pages from start to finish and only does what is specified in the check boxes
	 */
	private void doTARS() {
		// intilize bot
		TarsBot bot = new TarsBot();
		
		// first bring up tars page
		try {
			
			bot.move(bot.findTarsWindow());
			bot.rightClick();
			// if the window was found
			bot.sleep(1000);
			
			if(intialHookUpFront) {
				// goto front
				System.out.println("doing front hookup");
				bot.populateCarsTempAndOutage(sig, temp, outage);
			}
			if(intialHookUpBack) {
				System.out.println("Doing back checklist");
				bot.goToCheckList();
				bot.sleep(500);
				bot.fillIntialHookupAndOutageChecklist(sig);
			}
			if(unHookFront) {
				System.out.println("Doing unhooking front");
				// goto front
				bot.sleep(2000);
				bot.populateCarsOutStatusSealIDsMTSignoff(sig);
			}
			if(unHookBack) {
			System.out.println("Doing unhook back");
				bot.goToCheckList();
				bot.sleep(500);
				bot.fillUnhookChecklist(sig);
			}
			
		} catch (WindowNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
