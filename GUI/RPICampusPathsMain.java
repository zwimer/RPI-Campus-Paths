package GUI;

import SmartGraph.Edge;
import SmartGraph.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;

public class RPICampusPathsMain extends JFrame{

	//Representation 
	private static Model m = null;
	private static GUI TheInterface = null;
	private static boolean IgnoreComboBoxListener = false;
	
	//A small function which returns a map of buildings and their coordinates
	public static HashMap<Integer, Pair<Integer>> getButtonCoordinates() {
		return m.getLocations();
	}
	
	//A function used to find the shortest path between two buildings
	public static void findShortestPath(String node1Name, String node2Name) {	

		//Since only an ID or a Name is given for a node, find the other
		//Therefore, if a name is given, find the ID. And visa versa
		Edge tempEdge = m.Clarify_IDs_and_Names(Integer.MIN_VALUE, Integer.MIN_VALUE, node1Name, node2Name);   
		
		//Try to find the shortest path between two locations, if this fails then
		//catch the Runtime Exception, and set SpecialCase equal to the message
		try {tempEdge = m.findPath(tempEdge.StartID, tempEdge.EndID, node1Name, node2Name, "");}
		catch (RuntimeException UnexpectedOutput) {
			TheInterface.loadPath(null, null);
			return;
		}
		
		//Put the path in an ArrayList so that the GUI can interpret and
		//display it. If SpecialCase is not "" then no path was found: skip this
		ArrayList<SimpleEdge> ThePathIDs = new ArrayList<SimpleEdge>();
		ArrayList<Pair<Pair<Integer>>> ThePath = new ArrayList<Pair<Pair<Integer>>>();
		
		//Repeat for each edge
		//Note, the reason this function is written with a break is because
		//a path of length 0 is input as the same size as a path of length 1.
		//This function covers that corner case without any extra code or ifs
		while(true) {
			
			//Add the next edge to ThePathIDs
			ThePathIDs.add(new SimpleEdge(new Pair<Integer>(tempEdge.StartID, 
					tempEdge.EndID),  new Pair<String>(m.getName(tempEdge.StartID), m.getName(
							tempEdge.EndID)), m.getDirection(tempEdge.StartID, tempEdge.EndID)));

			//Add the coordinates of the ends of the lines ot be drawn to ThePath
			ThePath.add(new Pair<Pair<Integer>> 
			(m.getCoordinates(tempEdge.EndID), m.getCoordinates(tempEdge.StartID)));
			
			//If (tempEdge.Path == tempEdge) then tempEdge.Start is the first building
			//in the path, therefore ThePath has been successfully constructed
			if (tempEdge.Path == tempEdge) break;
			tempEdge = tempEdge.Path;
		}
		
		//Correct the order
		Collections.reverse(ThePathIDs);
		
		//Present the user with the results.
		TheInterface.loadPath(ThePath, ThePathIDs);
	}
	
	//A function the Mouse_Listener will
	//call if the mouse is hovering over a button 
	public static void locationHovered(int InputID) {
		
		//If the mouse is hovering over nothing, say so
		if (InputID == -1) TheInterface.locationHovered("");
		
		//If the mouse is hovering over an intersection, say so
		else if (m.getName(InputID).length() == 0) TheInterface.locationHovered("Intersection "+InputID);
		
		//If the mouse is hovering over a building, say so
		else TheInterface.locationHovered(m.getName(InputID));
	}

	//A function called by a listener if a location was selected 
	public static void locationSelected(int InputID, String InputBuilding, int Which) {

		//If this function was called due 
		//to setting a JComboBox, do nothing
		//Then prevent this function from being called recursively
		if (IgnoreComboBoxListener) return;
		IgnoreComboBoxListener = true;
		
		//If this function was called via a button listener
		if (InputID != 0) {
			
			//If a button represents an intersection, do nothing
			if (m.getName(InputID).length() == 0) {
				
				//Allow this function to be called again before exiting it
				IgnoreComboBoxListener = false;
				return;
			}
			
			//If the button represents a building, set InputBuilding to its name
			InputBuilding = m.getName(InputID);
		}

		//If the building entered does not exist, do nothing
		else if (!m.buildingExists(InputBuilding)) {
			
			//Allow this function to be called again before exiting it
			IgnoreComboBoxListener = false;
			return;
		}
		
		//Tell the GUI a location was selected
		TheInterface.locationSelected(InputBuilding, Which);
		
		//If the GUI has two buildings loaded, find the shortest path
		if (TheInterface.pathReady()) findShortestPath(TheInterface.getStart(), TheInterface.getEnd());
		
		//Allow this function to be called again
		IgnoreComboBoxListener = false;
	}
	
	public static void reset() {
		TheInterface.reset();
	}
	
	//Main function
	public static void main(String args[]) {
		
		//Create the model
		m = new Model();
		
		//A list of building names to be built below
		ArrayList<String> Buildings = new ArrayList<String>();
		
		//First load the nodes into the graph
		try {m.readNodes("MapInfo/RPI_map_data_Nodes.csv");} 
		catch (IOException e) {e.printStackTrace();}

		//Then load the edges into the graph
		try {m.readEdges("MapInfo/RPI_map_data_Edges.csv");} 
		catch (IOException e) {e.printStackTrace();}

		//Note: the first call of output buildings builds
		//essential data structures in Model to be used later one.
		//Then, generate a list of building names to send to the GUI constructor
		Iterator<Map.Entry<Integer, String>> i = m.OutputBuildings().entrySet().iterator();
		while(i.hasNext()) Buildings.add(i.next().getValue());
		
		//Run the GUI
		try {TheInterface = new GUI("MapInfo/RPI_campus_map_2010_extra_nodes_edges.png", Buildings);}
		catch (IOException e) {e.printStackTrace();}
		
		//Make m observable
		m.addObserver(TheInterface);
	}
	
	//To satisfy the compiler
	private static final long serialVersionUID = 1L;
}
