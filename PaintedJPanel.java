package hw9;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import java.awt.geom.Line2D;
import javax.swing.border.LineBorder;

//The JPanel which holds the map
public class PaintedJPanel extends JPanel {

	//A list which contains a bunch of pairs of
	//coordinates representing edges to be painted
	private ArrayList<Pair<Pair<Integer>>> Path;

	//For image manipulation. RPI_MAP = loaded image.
	//Altered is the scaled version, recorded for efficiency
	private Image dbImage, AlteredRPI_MAP;
	private final Image RPI_MAP;
	
	//Scaling factors and old dimensions, recorded for efficiency
	private double scaleX, scaleY;
	private int OldWidth, OldHeight;
	
	//Constants used to define the sizes of objects
	private final static int BUTTON_RADIUS = 7;
	private final static int BORDER_WIDTH = 4;
	
	//A constructor to make the a new Panel with width x and height y
	public PaintedJPanel(Image m, int x, int y) {
		super();
		RPI_MAP = m;
		this.setPreferredSize(new Dimension(x, y));
		setBorder(new LineBorder(Color.black, BORDER_WIDTH));
	}
	
	//A function to change the map size and everything dependent on it
	private void setMapSize() {
		
		//Set OldWidth and OldHeight to the new Width and Height 
		OldWidth = getWidth(); OldHeight = getHeight();
		
		//Redefine scaleX and scaleY with the new Panel width
		scaleX = (OldWidth-2*BORDER_WIDTH)/(double)RPI_MAP.getWidth(null);
		scaleY = (OldHeight-2*BORDER_WIDTH)/(double)RPI_MAP.getHeight(null);
		
		//Resize the map to fit within the panel
		AlteredRPI_MAP = RPI_MAP.getScaledInstance(OldWidth-2*BORDER_WIDTH, OldHeight-2*BORDER_WIDTH, Image.SCALE_SMOOTH);
		
		//Redefine the buttons in the new locations
		LoadButtons();
		
		//Repaint the Panel
		repaint();
	}
	
	//A function to load the buttons under the map
	private void LoadButtons() {

		//Allow for specific placement of elements
		setLayout(null);
		
		//A temporary variable to hold a map of Building IDs and their coordinates
		final Iterator<Map.Entry<Integer, Pair<Integer>>> i = 
				RPICampusPathsMain.getButtonCoordinates().entrySet().iterator();
		
		//For every building, make a button
		while (i.hasNext()) {
			Map.Entry<Integer, Pair<Integer>> temp = i.next();
		
			//Make a new button and set the size and location of the button
			SmartButton button = new SmartButton(temp.getKey());
			button.setBounds((int)Math.round(scaleX*(temp.getValue().x-80))-BUTTON_RADIUS+BORDER_WIDTH,
					(int)Math.round(scaleY*(temp.getValue().y-90))-BUTTON_RADIUS+BORDER_WIDTH, 
					2*BUTTON_RADIUS, 2*BUTTON_RADIUS);
			
			//Add listeners to this function
			button.addMouseListener(new Mouse_Listener());
			button.addActionListener(new Button_Listener(temp.getKey()));

			//Add the button to the panel
			add(button);
		}
	}

	//Load the path to pain and any exceptions to it
	public void loadPath(ArrayList<Pair<Pair<Integer>>> n) {
		
		//Repaint with the new path
		Path = n;
		repaint();
	}

	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		paintComponent(dbImage.getGraphics());
		g.drawImage(dbImage, 0, 0, this);  
	}

	public void paintComponent(Graphics g) {                
		super.paintComponents(g);
		
		//If the panel was resized then change elements dependent on it.
		if ((OldWidth != getWidth())||(OldHeight != getHeight())) {
			removeAll();
			setMapSize();
		}
		
		//Print the map and draw the path on it
		g.drawImage(AlteredRPI_MAP, BORDER_WIDTH, BORDER_WIDTH, this);
		printPath(g);

		//Draw a boarder around the map
		((Graphics2D) g).setStroke(new BasicStroke(BORDER_WIDTH));
		g.drawRect(BORDER_WIDTH/2,BORDER_WIDTH/2, getWidth()-BORDER_WIDTH, getHeight()-BORDER_WIDTH);
	}

	//Print the path requested
	private void printPath(Graphics g) {

		//If there is no path to print do nothing
		if (Path == null) return;
		
		//Use a thick stroke
		((Graphics2D) g).setStroke(new BasicStroke(5));
		
		//Print the path requested
		for(int i = 0; i < Path.size(); i++)
			((Graphics2D) g).draw(new Line2D.Double(
					scaleX*(Path.get(i).x.x-80)+BORDER_WIDTH,
					scaleY*(Path.get(i).x.y-90)+BORDER_WIDTH, 
					scaleX*(Path.get(i).y.x-80)+BORDER_WIDTH, 
					scaleY*(Path.get(i).y.y-90)+BORDER_WIDTH));
	}

	//To satisfy the compiler
	private static final long serialVersionUID = 1L;
}
