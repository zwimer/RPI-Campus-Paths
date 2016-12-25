package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class InstructionPanel extends JPanel {

	//Representation
	private boolean NoPath = false;
	private final ArrayList<String> TheBuildings;
	private final JComboBox<String> StartSelector, EndSelector;
	private JTextArea DisplaySelected, InstructionArea;

	//Constructor
	public InstructionPanel(ArrayList<String> Buildings) {
		
		//Set TheBuildings to Buildings and an empty string
		Buildings.add("");
		TheBuildings = Buildings;
		
		//Create sub-elements
		InstructionArea = new JTextArea();
		DisplaySelected = new JTextArea();
		StartSelector = new JComboBox<String>(Buildings.toArray(new String[Buildings.size()]));
		EndSelector = new JComboBox<String>(Buildings.toArray(new String[Buildings.size()]));
		createTextInterface(400, 200);
	}

	//This function creates the text interface.
	private void createTextInterface(int x, int y) {	

		//Make the JPanel, define its dimensions and border
		setBorder(new LineBorder(Color.red, 2));

		//Set the layout of the JPanel to GridBagLayout
		GridBagLayout TheLayout = new GridBagLayout();
		setLayout(TheLayout);

		//Define the text of instructions, then make
		//it wrap, and so that the user can't alter it
		InstructionArea.append("Resizable window. Click or select the buildings you wish to find "
				+ "a path between. Your mouse is currently hovering over:\n");
		InstructionArea.setWrapStyleWord(true);
		InstructionArea.setLineWrap(true);
		InstructionArea.setEditable(false);

		//Define the text of DisplaySelected, then make
		//it wrap, and so that the user can't alter it
		DisplaySelected.append("\nPlease selet the start building.\n");
		DisplaySelected.setWrapStyleWord(true);
		DisplaySelected.setLineWrap(true);
		DisplaySelected.setEditable(false);

		//Create set the selected item of the start JComboBox
		//to nothing, make it so the user can type in it, add
		//a listener to it, and set its preferred size
		StartSelector.setSelectedItem("");
		StartSelector.setEditable(true);
		StartSelector.addActionListener(new ComboBoxListener(StartSelector, 0));
		StartSelector.setPreferredSize(new Dimension(250, 25));

		//Create set the selected item of the start JComboBox
		//to nothing, make it so the user can type in it, add
		//a listener to it, and set its preferred size
		EndSelector.setSelectedItem("");
		EndSelector.setEditable(true);
		EndSelector.addActionListener(new ComboBoxListener(EndSelector, 1));
		EndSelector.setPreferredSize(new Dimension(250, 25));

		//Add all of the above to the TextInterface JPanel
		populateTextInterface();
	}

	//A function used to add the elements to TextInterface
	private void populateTextInterface() {

		//Create a new GridBagConstraint
		GridBagConstraints c = new GridBagConstraints();

		//Alter c to make the display pretty
		//Then add Instructions to the TextInterface
		c.gridwidth = 3;
		c.gridx = c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(InstructionArea, c);

		//Alter c to make the display pretty
		//Then add DisplaySelected to the TextInterface
		c.gridy = 1;
		add(DisplaySelected, c);

		//Alter c to make the display pretty
		//Then add a start label to the TextInterface
		c.gridy = 2;
		c.gridwidth = 1;
		add(new JLabel("Start building:"), c);

		//Alter c to make the display pretty
		//Then add StartSelector to the TextInterface
		c.gridx = 1;
		c.gridwidth = 2;
		add(StartSelector, c);

		//Alter c to make the display pretty
		//Then add an end label to the TextInterface
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		add(new JLabel("End building:"), c);

		//Alter c to make the display pretty
		//Then add EndSelector to the TextInterface
		c.gridx = 1;
		c.gridwidth = 2;
		add(EndSelector, c);

		//Alter c to make the display pretty
		//Then add the ResetButton to the TextInterface
		c.gridwidth = 1;
		c.gridy = 4;
		
		//Make a reset button, add a listener to it, and add it
		JButton ResetButton = new JButton("Reset");
		ResetButton.addActionListener(new Button_Listener(-1));
		add(ResetButton, c);
	}
	
	//A function called when the JFrame is resized
	void resize(double scaleX, double scaleY, int SelectedLength, int ClickedLength) {
		
		//Remove everything at first
		removeAll();
		
		//Define the text of instructions, then make
		//it wrap, and so that the user can't alter it
		InstructionArea = new JTextArea(InstructionArea.getText());
		InstructionArea.setWrapStyleWord(true);
		InstructionArea.setLineWrap(true);
		InstructionArea.setEditable(false);

		//Define the text of DisplaySelected, then make
		//it wrap, and so that the user can't alter it
		DisplaySelected = new JTextArea(DisplaySelected.getText());
		DisplaySelected.setWrapStyleWord(true);
		DisplaySelected.setLineWrap(true);
		DisplaySelected.setEditable(false);
		
		//Resize elements
		EndSelector.setPreferredSize(new Dimension((int)Math.round(250*scaleX), (int)Math.round(25*scaleY)));
		StartSelector.setPreferredSize(new Dimension((int)Math.round(250*scaleX), (int)Math.round(25*scaleY)));
		
		//ReAdd everything
		populateTextInterface();
	}
	
	//If no path was found, record so
	public void setNoPath() {
		NoPath = true;
	}

	//If no path was found, say so, then reset for next use
	public boolean getNoPath() {
		if (NoPath) return !(NoPath=false);
		return false;
	}

	//Returns true if two valid buildings have already been selected
	public boolean pathReady() {
		return (String)EndSelector.getSelectedItem() != "" &&
				(String)StartSelector.getSelectedItem() != "" &&
				TheBuildings.contains((String)EndSelector.getSelectedItem()) &&
				TheBuildings.contains((String)StartSelector.getSelectedItem());
	}

	//A small function to allow GUI to modify the instructions
	public void changeInstructions(String s, int Begin, int End) {
		InstructionArea.replaceRange(s, Begin, End);
	}

	//A small function to allow GUI to modify the display
	public void changeDisplay(String s, int Begin, int End) {
		DisplaySelected.replaceRange(s, Begin, End);
	}

	//A small function to allow GUI to set StartSelector's selected item
	public void setStartSelected(String s) {
		StartSelector.setSelectedItem(s);
	}

	//A small function to allow GUI to set EndSelector's selected item
	public void setEndSelected(String s) {
		EndSelector.setSelectedItem(s);
	}

	//A small function to return StartSelector's selected item
	public String getStartSelectedItem() {
		return (String)StartSelector.getSelectedItem();
	}

	//A small function to return EndSelector's selected item
	public String getEndSelectedItem() {
		return (String)EndSelector.getSelectedItem();
	}

	//A function used to reset this object
	public void reset(int SelectedLength, int ClickedLength) {
		
		//Set selected items to empty string
		StartSelector.setSelectedItem("");
		EndSelector.setSelectedItem("");
		
		//Reset the instructions and display
		InstructionArea.replaceRange("Resizable window. Click or select the buildings you wish to find a path"
				+ " between. Your mouse is currently hovering over:\n", 0, SelectedLength);
		DisplaySelected.replaceRange("\nPlease selet the start building.\n", 0, ClickedLength);
	}
	
	//To satisfy the compiler
	private static final long serialVersionUID = 1L;
}
