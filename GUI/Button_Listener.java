package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button_Listener implements ActionListener {

	//Representation
	private int ID; 
	
	//Constructor
	public Button_Listener(int a) {
		ID = a;
	}
	
	//If a button is clicked, then if the 
	//button is not the reset button, inform Control
	public void actionPerformed(ActionEvent e) {
		
		//Reset button hit
		if (ID == -1) RPICampusPathsMain.reset();
		
		//Building selected
		else RPICampusPathsMain.locationSelected(ID, "", -1);
	}
}
