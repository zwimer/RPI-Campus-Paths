package GUI;

import javax.swing.JButton;

//A small button class to hold a JButton, and the building it represents
public class SmartButton extends JButton {

	//Representation
	public int ID;
	
	//Constructor
	public SmartButton(int x) {
		ID = x;
	}
	
	//To satisfy the compiler
	private static final long serialVersionUID = 1L;
}
