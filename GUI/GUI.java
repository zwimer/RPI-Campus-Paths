package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class GUI implements Observer{        

	//GUI Representation
	private final JFrame TheFrame;
	private final PaintedJPanel ThePanel;
	private final InstructionPanel Instructions;
	
	//GUI Representation created for efficiency
	private JScrollPane scroll;
	private final JPanel LeftSide, PathDisplayer;
	private int SelectedLength = 120, ClickedLength = 34;
	
	//Variables used to optimize resizing speed
	public int OldWidth, OldHeight;
	public double DefaultWidth = -1, DefaultHeight = -1;
	
	//The GUI's constructor
	public GUI(String filename, ArrayList<String> Buildings) throws IOException {          
		
		//Make a new frame with a border layout, and set it
		//such that the program will end when the frame is closed.
		TheFrame = new JFrame();
		TheFrame.setLayout(new BorderLayout());
		TheFrame.setMinimumSize(new Dimension(1100, 800));
		//TheFrame.setMinimumSize(new Dimension);
		TheFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      

		//Make a new PaintedJPanel, and send it, as an argument, the size
		//to set the JPanel and a cropped image of the RPI map to be printed
		ThePanel = new PaintedJPanel(ImageIO.read(new File(filename))
				.getSubimage(80,  90,  2025,  1885), 800, 800);

		//Add the frame to the panel
		TheFrame.add(ThePanel, BorderLayout.EAST);

		//Create the PathDisplayer and Instructions JPanel
		PathDisplayer = createPathDisplayer(400, 600);
		Instructions = new InstructionPanel(Buildings);

		//Create the left side panel
		//This Panel contains the PathDisplayer 
		//and Instructions and is created for looks
		LeftSide = new JPanel();
		LeftSide.setPreferredSize(new Dimension(400, 800));
		LeftSide.setLayout(new BorderLayout());
		LeftSide.add(Instructions, BorderLayout.NORTH);
		LeftSide.add(PathDisplayer, BorderLayout.SOUTH);
		
		//Add the LeftSide panel to TheFrame
		TheFrame.add(LeftSide, BorderLayout.WEST);
		
		//Add a resize listener to the frame
		//that will resize its components if needed
		TheFrame.addComponentListener(new ComponentListener() {
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		    public void componentResized(ComponentEvent e) {
		        resize();
		    }
		});
		
		//Pack the frame and set it so the user can see it
		TheFrame.pack();
		TheFrame.setVisible(true);
	}

	//The observer update function, display the error found
	public void update(Observable obs, Object o) {
		DisplayError((String)o);
	}

	//A small public function to load and print the path
	public void loadPath(ArrayList<Pair<Pair<Integer>>> Path, ArrayList<SimpleEdge> PathIDs) {
		ThePanel.loadPath(Path);
		if (PathIDs != null) DisplayPath(PathIDs);
	}

	//A small function to create and set the PathDisplayer's attributes
	private JPanel createPathDisplayer(int x, int y) {
		
		//Make the new JPanel
		JPanel PathDisplayer_ = new JPanel();
		
		//Sets the preferred size of the PathDisplayer and gives it a nice border
		PathDisplayer_.setPreferredSize(new Dimension(x, y));
		PathDisplayer_.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 3),
				"The Path: ", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, new Font("Verdana", Font.ITALIC, 20)));
		
		//Return the new JPanel
		return PathDisplayer_;
	}

	//A function called if no path was found
	public void DisplayError(String s) {

		//Tell the instruction panel no path was found
		Instructions.setNoPath();
		
		//Make a new text area that informs the user, make it wrap, and un-editable
		JTextArea NoPath = new JTextArea("There is no path between "+s);
		NoPath.setWrapStyleWord(true);
		NoPath.setLineWrap(true);
		NoPath.setEditable(false);
		
		//Make the JTextArea scrollable
		scroll = new JScrollPane (NoPath);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(PathDisplayer.getWidth()-20, 
				PathDisplayer.getHeight()-70));
		
		//Remove the old JTextArea and replace it with this one
		PathDisplayer.removeAll();
		PathDisplayer.add(scroll);
		PathDisplayer.repaint();
	}

	//Display the path via text
	public void DisplayPath(ArrayList<SimpleEdge> Path) {

		//If there is no path, do nothing
		if (Instructions.getNoPath()) return;
		
		//Disp += "To get from <Building1> to <Building2>:"
		StringBuffer Disp = new StringBuffer("To get from ");
		Disp.append(Path.get(0).Names.x);
		Disp.append(" to ");
		Disp.append(Path.get(Path.size()-1).Names.y);
		Disp.append(":");

		//If your stating and ending locations aren't the same
		if (Path.get(0).IDs.x != Path.get(0).IDs.y) 
			
			//For every leg of the path, repeat
			for(int i = 0; i < Path.size(); i++) {
				
				//Disp += "\n\n-Step [#]. Leave  
				Disp.append("\n\n-Step ");
				Disp.append(i+1);
				Disp.append(i+1<10?".  ":". ");
				Disp.append("Leave ");
				
				//If the location is an intersection, 
				//name it by its ID otherwise name the building
				if (Path.get(i).Names.x.equals("")) {
					Disp.append("Intersection ");
					Disp.append(Path.get(i).IDs.x);
				}
				else Disp.append(Path.get(i).Names.x);
				
				//List the direction to travel
				Disp.append(" and head ");
				Disp.append(Path.get(i).Direction); 
				Disp.append(" towards "); 
				
				//If the location is an intersection,
				//name it by its ID otherwise name the building
				if (Path.get(i).Names.y.equals("")) {
					Disp.append("intersection ");
					Disp.append(Path.get(i).IDs.y);
				}
				else Disp.append(Path.get(i).Names.y);
			}
		Disp.append("\n\nYou have arrived!");
		
		//Make a new JTextArea with the path, make it wrap the text, and un-changable
		JTextArea The_Path = new JTextArea(Disp.toString());
		The_Path.setWrapStyleWord(true);
		The_Path.setLineWrap(true);
		The_Path.setEditable(false);

		//Make the JTextArea scroll-able
		scroll = new JScrollPane (The_Path);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(PathDisplayer.getWidth()-20, 
				PathDisplayer.getHeight()-70));

		//Replace the old path, add the new one, and repaint it
		PathDisplayer.removeAll();
		PathDisplayer.add(scroll);
		PathDisplayer.repaint();
	}

	//A function which alters Instructions 
	//if the mouse if hovering over a location
	public void locationHovered(String s) {
		Instructions.changeInstructions(s, 120, SelectedLength);
		SelectedLength = 120 + s.length();
	}
	
	//A function which is called if a location was selected
	public void locationSelected(String s, int Which) {

		//If no locations have been selected yet
		if (ClickedLength == 34) {
			
			//Change the instructions
			Instructions.changeDisplay("\nStart building selected. "
					+ "Please select end building.\n", 0, ClickedLength);
			ClickedLength = 54;
			
			//If the location was CLICKED, set the first box to the name of the building clicked
			if (Which == -1) Instructions.setStartSelected(s);
		}
		
		//If at least one location has been selected so far
		else {
			
			//Change the instructions
			Instructions.changeDisplay("\nPlease click reset or "
					+ "change a selected building.\n", 0, ClickedLength);
			ClickedLength = 51;
			
			//If the location was CLICKED, set the second box to the name of the building clicked
			if (Which == -1) Instructions.setEndSelected(s);
		}
	}

	//A function used to return the start location
	public String getStart() {
		return Instructions.getStartSelectedItem();
	}

	//A function used to return the end location
	public String getEnd() {
		return Instructions.getEndSelectedItem();
	}

	//A public function used to resize the window
	public void resize() {

		//If the size of the window changed
		if ((OldWidth != TheFrame.getWidth())||(OldHeight != TheFrame.getHeight())) {

			//Make the frame hidden
			TheFrame.setVisible(false);

			//If this is the first time the function is running, set default height and width
			if (DefaultWidth == -1) DefaultWidth = TheFrame.getWidth();
			if (DefaultHeight == -1) DefaultHeight = TheFrame.getHeight();

			//Create temporary local variables that define how much to scale everything
			final double scaleX = TheFrame.getWidth()/DefaultWidth, scaleY = TheFrame.getHeight()/DefaultHeight;

			//Set the preferred size (if they exist) of objects 
			//by multiplying their default sizes by the scaling factors
			ThePanel.setPreferredSize(new Dimension((int)Math.round(800*scaleX),(int)Math.round(800*scaleY)));
			PathDisplayer.setPreferredSize(new Dimension((int)Math.round(400*scaleX), (int)Math.round(600*scaleY)));
			if (scroll != null) scroll.setPreferredSize(new Dimension(((int)Math.round(400*scaleX)-20), 
					(int)Math.round(600*scaleY)-70));
			Instructions.resize(scaleX, scaleY, SelectedLength, ClickedLength);
			LeftSide.setPreferredSize(new Dimension((int)Math.round(400*scaleX), (int)Math.round(800*scaleY)));

			//Repack ant make the frame visible again
			TheFrame.pack();
			TheFrame.setVisible(true);

			//Set record the new height and width of the frame
			OldWidth = TheFrame.getWidth();
			OldHeight = TheFrame.getHeight();
		}
	}
	
	//A function that returns true if two valid
	//buildings have been selected, and false otherwise.
	public boolean pathReady() {
		return Instructions.pathReady();
	}
	
	//A function used to reset the GUI
	public void reset() {

		//Reset the instructions panel
		Instructions.reset(SelectedLength, ClickedLength);
		SelectedLength = 120;
		ClickedLength = 34;

		//Remove the old path and repaint
		PathDisplayer.removeAll();
		PathDisplayer.repaint();
		
		//Remove the currently printed path
		ThePanel.loadPath(null);
	}
}
