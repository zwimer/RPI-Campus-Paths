package hw7;

import hw4.Graph;
import hw9.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.PriorityQueue;

public class Model extends Observable {

	//Representation
	private HashMap<Integer, Trio> Input;
	private Graph<Integer, Double> TheGraph;
	private HashMap<Integer, String> Buildings = null;
	private HashMap<String, Integer> Buildings2 = null;
	private HashMap<Integer, Pair<Integer>> Locations = null;
	
	/** Constructor
	 * @effects creates a new model
	 */
	public Model() {}
	
	/**	A function to read the nodes into the graph
	 * 
     * @param filename a string which contains the name of the file to be read
     * @throws IOException if there is an error reading the file
     * @modifies Input
     * 
     * @modifies TheGraph
     * @effects reads the nodes into the graph
     */
	public void readNodes(String filename) throws IOException {

		//Create representation
		Input = new HashMap<Integer, Trio>();
		TheGraph = new Graph<Integer, Double>();
		
		//Create for reading in
		String line = null;
    	BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        //For each line
        while ((line = reader.readLine()) != null) {
        	
        	//Parse it and add it to input
        	String CurrentLine[] = line.split(",");
        	
        	Input.put(Integer.parseInt(CurrentLine[1]), 
            		 new Trio(CurrentLine[0],CurrentLine[2],CurrentLine[3])); 
        }
        
        //Place all the nodes in the graph
        Iterator<Integer> i = Input.keySet().iterator();
        while(i.hasNext()) TheGraph.addNode(i.next());
    }
	
	/**	A function to read the edges into the graph
	 * 
     * @param filename a string which contains the name of the file to be read
     * @throws IOException if there is an error reading the file
     * @modifies TheGraph
     * @effects reads the edges into the graph
     */
	public void readEdges(String filename) throws IOException {

		//Create for reading in
		String line = null;
    	BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        //For each line
        while ((line = reader.readLine()) != null) {
        	
        	//Parse it and add it to input
        	String CurrentLine[] = line.split(",");
        	
        	//Get the ID's that represent the edges to be linked
        	Integer Id1 = Integer.parseInt(CurrentLine[0]);
        	Integer Id2 = Integer.parseInt(CurrentLine[1]);
        	
        	//Find length of new edge
        	Double length = Math.pow(Math.pow(Input.get(Id1).x - Input.get(Id2).x, 2) +
        	Math.pow((Input.get(Id1).y - Input.get(Id2).y), 2), .5);

        	//Add undirected edges
        	TheGraph.addEdge(Id1,  Id2,  length);
        	TheGraph.addEdge(Id2,  Id1,  length);
        }
    }

	//A small function which returns the name of the location ID
	public String getName(int ID) {
		return Input.get(ID).name;
	}
	
	//This functions returns true if and only if the building Bname exists
	public boolean buildingExists(String Bname) {
		return Buildings.containsValue(Bname);
	}
	
	//A function to return a copy of Locations as well as construct it
	public HashMap<Integer, Pair<Integer>> getLocations() {
		
		//If locations has yet to be constructed, do so
		if (Locations == null) {
			Locations = new HashMap<Integer, Pair<Integer>>();
			
			//For every location, add an element which links its ID to its coordinates
			Iterator<Map.Entry<Integer, Trio>> i = Input.entrySet().iterator();
			while(i.hasNext()) {
				Map.Entry<Integer, Trio> temp = i.next();
				Locations.put(temp.getKey(), new Pair<Integer>(temp.getValue().x, temp.getValue().y));	
			}
		}
		
		//Returns a copy of Locations
		return new HashMap<Integer, Pair<Integer>>(Locations);
	}
	
	
	/**	A function to output a building within the radius of the input coordinates
	 * 
	 * @param x the x coordinate to be checked
	 * @param y the y coordinate to be checked
	 * @param radius the radius the coordinates can be in from locations
     * @modifies Buildings
     * @modifies Buildings2
     * @effects Locates the nearest building less than radius units from (x,y)
     * @returns Returns the ID of the nearest building less than radius units from (x,y) or -1 if NA 
     */
	public int inProximity(int x, int y, int radius) {
		
		//Initialize MinID to a bad ID to tell if this failed.
		int MinID = -1, MinDistance = radius*radius; 
		
		//For each intersection and building
		Iterator<Map.Entry<Integer, Trio>> i = Input.entrySet().iterator();
		while(i.hasNext()) {
			
			//If the location is within radius units of (x,y) and is closer
			//than all other previously checked, then set MinID to that building ID
			Map.Entry<Integer, Trio> temp = i.next();
			
			if ((Math.pow(x-temp.getValue().x,2)+Math.pow(y-temp.getValue().y,2)) < MinDistance) {
				MinDistance = (int) (Math.pow(x-temp.getValue().x,2)+Math.pow(y-temp.getValue().y,2));
				MinID = temp.getKey();
			}	
		}
		
		//Returns the nearest building's ID if it is within Radius units of (x,y)
		return MinID;
	}
	
	/**	A function to output a map of all buildings to their ID's
	 * 
     * @modifies Buildings
     * @modifies Buildings2
     * @effects Constructs Buildings and Buildings2, which are maps linking building IDs to names
     * @returns a clone of Buildings 
     */
    public HashMap<Integer, String> OutputBuildings() {
    	
    	//If the map buildings has yet to be created
		if (Buildings == null) {
			
			//Create it
			Buildings = new HashMap<Integer, String>();
			Buildings2 = new HashMap<String, Integer>();

			//Build the map from input, repeat for each location
			Iterator<Map.Entry<Integer, Trio>> i = Input.entrySet().iterator();
			while (i.hasNext()) {
				
				//If the location is a building, add its ID and name to buildings
				Map.Entry<Integer, Trio> temp = i.next();
				
				if (!temp.getValue().name.equals("")) {
					Buildings.put(temp.getKey(), temp.getValue().name);
					Buildings2.put(temp.getValue().name, temp.getKey());
				}
			}

		}
		
		//Return a clone of buildings
		return new HashMap<Integer, String>(Buildings);
	}
    
	/**	A function to return the coordinates of a location
	 * 
     * @param ID the ID number of the location
     * @returns the coordinates of the location 
     */
    public Pair<Integer> getCoordinates(Integer ID) {
		return new Pair<Integer>(Input.get(ID).x,Input.get(ID).y);
	}
    
	/**	A function which converts IDs to names, and names
	 *	to IDs If there is an error, return a string to say so
	 * 
	 * @param node1ID the ID of the first node
	 * @param node2ID the ID of the second node
	 * @param node1Name the name of the first node
	 * @param node1Name the name of the first node
	 * @returns and Edge containing the information to be altered
     */
    public Edge Clarify_IDs_and_Names(Integer node1ID, 
    		Integer node2ID, String node1Name, String node2Name) {
    	
    	String ret = "";
		if (node1ID == Integer.MIN_VALUE)
			if (Buildings2.containsKey(node1Name)) node1ID = Buildings2.get(node1Name);
			else ret += "Error: building1";
			
		if (node2ID == Integer.MIN_VALUE) 
			if (Buildings2.containsKey(node2Name)) node2ID = Buildings2.get(node2Name);
			else ret += "Error: building2";

		if (!Buildings.containsKey(node1ID) && node1ID != Integer.MIN_VALUE) ret += "Id1";
		if (!Buildings.containsKey(node2ID) && node2ID != Integer.MIN_VALUE) ret += "Id2";
		if (!Buildings.containsKey(node1ID) && !ret.contains("building1")) ret += "Error: building1";
		if (!Buildings.containsKey(node2ID) && !ret.contains("building2")) ret += "Error: building2";

		if (Buildings.containsKey(node1ID) && node1Name.equals("")) node1Name = Buildings.get(node1ID);
		if (Buildings.containsKey(node2ID) && node2Name.equals("")) node2Name = Buildings.get(node2ID);
		
		return new Edge(node1ID, node2ID, node1Name, node2Name, 0d, 0d, ret);
    }
    
	/**	A small private helper function to determine the
	 * 	direction one must travel to get from Start to End.
	 * 
     * @param Start the ID of the starting node
     * @param End the ID of the ending node
     * @requires both IDs to be valid
     * @returns the direction one must travel to go from Start to End
     */
	public String getDirection(Integer Start, Integer End) {
		
		Double angle = (360+Math.toDegrees(Math.atan2(Input.get(End).x-Input.get(Start).x,
				Input.get(End).y-Input.get(Start).y)))%360;

		//Determine direction
		if (angle > 22.5 && angle <= 67.5) return "SouthEast";
		else if (angle > 67.5 && angle <= 112.5) return "East";
		else if (angle > 112.5 && angle <= 157.5) return "NorthEast";
		else if (angle > 157.5 && angle <= 202.5) return "North";
		else if (angle > 202.5 && angle <= 247.5) return "NorthWest";
		else if (angle > 247.5 && angle <= 292.5) return "West";
		else if (angle > 292.5 && angle <= 337.5) return "SouthWest";
		else return "South";
	}
	
	/**	A function to find the shortest path between two nodes
	 * 
	 * The next line is to be ignored, as the functionality is currently commented out for efficiency
	 * 		If either the parentNode node or the childNode node do not exist, or if the edge already exists, do nothing
	 * 
	 * @param node1ID the ID of the first node
	 * @param node2ID the ID of the second node
	 * @param node1Name the name of the first node
	 * @param node1Name the name of the first node
	 * @param ret a string which will informs the function if a corner case occurs
	 * @requires no arguments are null
	 * @throws RuntimeException if a corner case as occurred
	 * @returns an Edge containing the path requested
	 */
    public Edge findPath(Integer node1ID, Integer node2ID, 
    		String node1Name, String node2Name, String ret) throws RuntimeException {
    	
    	//If there is an error, throw a RuntimeException 
    	if (!ret.equals("")) {
    		setChanged();
    		notifyObservers(ret);
    		throw new RuntimeException(ret);
    	}

    	//If the length is 0
    	if (node1ID.equals(node2ID)) return new Edge(node1ID, node2ID, 
    			node1Name, node2Name, 0d, 0d, "");
    	
    	//Perform Dijkstra's algorithm   	
    	//Representation of the algorithm
    	HashSet<Integer> finished = new HashSet<Integer>();  
    	PriorityQueue<Edge> active = new PriorityQueue<Edge>();

    	//First add each edge from the start node to the priority queue
    	Iterator<Graph<Integer, Double>.TwoPair_<Integer, Double>> j = 
    			TheGraph.UnsortedlistChildren(node1ID).iterator();
    	while(j.hasNext()) {
    		Graph<Integer, Double>.TwoPair_<Integer, Double> temp = j.next();
    		active.offer(new Edge(node1ID, temp.Value, node1Name, Input.get(temp.Value).name,
    				temp.label, temp.label, getDirection(node1ID, temp.Value)));
    	}
    	
    	//For each edge in the queue
    	while(active.size() != 0) {
    		
    		//Take the next item from the queue
    		Edge NextEdge = active.poll();	 		
    		
    		//If the next edge leads to the next node, return the path to it
			if (NextEdge.EndID.equals(node2ID)) return NextEdge;
    		
			//If the next edge leads to an already explored node, do nothing
    		if (finished.contains(NextEdge.EndID)) continue;
    		
    		//For every edge from node being explored
    		Iterator<Graph<Integer, Double>.TwoPair_<Integer, Double>> i = 
    				TheGraph.UnsortedlistChildren(NextEdge.EndID).iterator();
    		while(i.hasNext()) {
    			Graph<Integer, Double>.TwoPair_<Integer, Double> tmp = i.next();
    			
    			//If it points to an already finished node, do nothing
    			if (finished.contains(tmp.Value)) continue;
    			
    			//Otherwise add it to the queue of to be explored edges
    			active.offer(new Edge(NextEdge.EndID, tmp.Value, 
    					Input.get(NextEdge.EndID).name, Input.get(tmp.Value).name, NextEdge,
    					tmp.label, tmp.label+NextEdge.Total, getDirection(NextEdge.EndID, tmp.Value))); 
    		}
    		
    		//Finish exploring the node
    		finished.add(NextEdge.EndID);
    	}
    	
    	//If the end node hasn't been found, say so
    	setChanged();
		notifyObservers(node1Name+" and "+node2Name);
		throw new RuntimeException("No Path");
    }
}
