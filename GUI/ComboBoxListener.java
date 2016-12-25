package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

//A small JComboBoxListener class
public class ComboBoxListener implements ActionListener {

	//Representation
	private final int Which;
	private final JComboBox<String> TheBox;
	
	//Constructor
	public ComboBoxListener(JComboBox<String> b, int w) {
		TheBox = b;
		Which = w;
	}
	
	//If something was selected in a JComboBox
	public void actionPerformed(ActionEvent e) {
		
		//If the JComboBox selection is empty, do nothing 
		if (TheBox.getSelectedItem().equals("")) return;
		
		//Otherwise, inform main which JCombox has what newly selected
		RPICampusPathsMain.locationSelected(0, (String)TheBox.getSelectedItem(), Which);
	}
}
