package hw4;

import java.util.*;

public class Graph<N extends Comparable<N>, E extends Comparable<E> > {
	
	//Representation
	public HashMap<N, Graph_node<N, E> > Nodes;
	
	//Helper class
	public class TwoPair_<N extends Comparable<N>, E  extends Comparable<E> > {
		public TwoPair_(N A, E B) {Value = A; label = B;}
		public final N Value;
		public final E label;
	}
	
	//Abstraction function
	//The HashMap 'Nodes' represents the collection of nodes in the graph
	//Each Graph_node in Nodes represents a single node in the graph
	//The ArrayList children_list in each Graph_node, represents edges to children in the graph
	
	/**	Representation Invariant
     * Checks that the representation invariant holds
     * @throws a runtime exception if the map container does not exist
     * @throws a runtime exception if an edge points to a non-existent node
     **/
	/*private void checkRep() {
		//Check to see if the node container exists
		if (Nodes == null) throw new RuntimeException("Error, null map");
		
		//Checks to see if every edge points to a valid node in the graph
		Iterator<Entry<String, Graph_node>> i = Nodes.entrySet().iterator();
		while(i.hasNext()) {
			Graph_node temp = i.next().getValue();
			for(int a = 0; a < temp.Number_of_kids(); a++) {
				//If the edge points to a non-existent node, (null value already check in Graph_node invariant)
				if (Nodes.get(temp.getChild(a).getValue()) == null) throw new RuntimeException("Error, illegal edge");
			}
		}
	}*/    

    /**	Constructor
     * @effects Constructs a new Graph with no nodes
     */
	public Graph() {
		Nodes = new HashMap<N, Graph_node<N, E> > ();
		//checkRep();
	}

	/**	Returns the number of nodes in the graph
     * @returns the number of Nodes in the graph          
     */
	public int size() {
		//checkRep();
		return Nodes.size();
	}
	
	/**	Add a new node with the value of nodeData to the graph
	 * 
	 * The next line is to be ignored, as the functionality is currently commented out for efficiency
	 * 		If the node already exists, do nothing
	 * 
     * @param nodeData is the value of the node to be added to the graph
     * @requires nodeData is not null
     * @modifies Nodes
     * @effects Adds another Node with the value of nodeData to Nodes 
     */
	public void addNode(N nodeData) {
		//checkRep();
		
/*		//Don't add if already exists
		if (Nodes.containsKey(nodeData)) {
			//checkRep();
			return;
		}
*/		
		//Otherwise add it
		Nodes.put(nodeData, new Graph_node<N, E>(nodeData));
		//checkRep();
	}
	
	/**	Add edge linking two nodes in the graph
	 * 
	 * The next line is to be ignored, as the functionality is currently commented out for efficiency
	 * 		If either the parentNode node or the childNode node do not exist, or if the edge already exists, do nothing
	 * 
     * @param parentNode the value of the node the edge is coming from
     * @param childNode the value of the node the edge is going to
     * @param edgeLabel the label of the edge to be created
     * @requires parentNode is not null
     * @requires childNode is not null
     * @requires edgeLabel is not null
     * @modifies Nodes
     * @effects Modifies one of the nodes in Nodes by calling a member function of the Graph_node class to add and edge between two nodes
     */
	public void addEdge(N parentNode, N childNode, E edgeLabel) {
		//checkRep();
		
		//To make the function more efficient, set tempParent and tempChild equal
		//to the nodes that are to be linked to avoid searching for them each time 
		Graph_node<N, E> tempParent = Nodes.get(parentNode), tempChild = Nodes.get(childNode);
		
		//Do nothing if either of the nodes does not exist
		if ((tempParent == null) || (tempChild == null))  {
			//checkRep();
			return;
		}
/*		
		//Check to see if connection already exists, if so do nothing
		for(int i = 0; i < tempParent.Number_of_kids(); i++) {
			if ((tempParent.getChild(i) == tempChild) && (tempParent.getLabel(i).equals(edgeLabel))) {
				//checkRep();
				return;
			}
		}
*/		
		//Make the link
		tempParent.add_child(tempChild, edgeLabel);
		//checkRep();
	}
	
	/**	Returns a list of the values of the nodes in the graph in alphabetical order
     * @param parentNode the value of the node the edge is coming from
     * @returns an iterator which returns the nodes in lexicographical (alphabetical) order.
     */
	public Iterator<N> listNodes() {
		//checkRep();
		
		//Make a new ArrayList to be returned
		ArrayList<N> Ret = new ArrayList<N> ();
		
		//Populate the ArrayList with the value of each node 
		Iterator<N> itr = Nodes.keySet().iterator();
		while(itr.hasNext()) Ret.add(itr.next());
		
		//Return an iterator to the list
		//checkRep();
		return Ret.iterator();
	}


	
	/**	Returns a list of TwoPair_ (childNode, edgeLabel)
	 * If the parentNode Node does not exist then return an empty list 
     * @param parentNode the value of the node the edge is coming from
     * @requires parentNode is not null
     * @returns a list of TwoPair_ (childNode, edgeLabel)
     */
	public ArrayList<TwoPair_<N, E> > UnsortedlistChildren(N parentNode) {		
		
		//To make the function more efficient, set temp equal
		//to the parent node to avoid searching for them each time 
		Graph_node<N, E> temp = Nodes.get(parentNode);
		
		//If node does not exist, return an iterator to the empty list
		if (temp == null) {
			//checkRep();
			return new ArrayList<TwoPair_<N, E> > ();				
		}
		
		//Add the edge to every child as a string to a new ArrayList
		ArrayList<TwoPair_<N, E> > ret = new ArrayList<TwoPair_<N, E> > ();
		for(int i = 0; i < temp.Number_of_kids(); i++)
		//	ret.add(new String(temp.getChild(i).getValue() + "(" + temp.getLabel(i) + ")"));
			ret.add(new TwoPair_<N, E>(temp.getChild(i).getValue(), temp.getLabel(i)));
				
	   	return ret;
	}
	
	
	/**	Returns a list of TwoPair_ (childNode, edgeLabel) in alphabetical order
	 * If the parentNode Node does not exist then return an empty list 
     * @param parentNode the value of the node the edge is coming from
     * @requires parentNode is not null
     * @returns a list of TwoPair_ (childNode, edgeLabel) in lexicographical (alphabetical) order by node name and secondarily by edge label.
     */
	public ArrayList<TwoPair_<N, E> > listChildren(N parentNode) {		
		//checkRep();

		//Get a list of TwoPair_ (childNode, edgeLabel)
		ArrayList<TwoPair_<N, E> > ret = UnsortedlistChildren(parentNode);
		
		//Sort and return the list
		Collections.sort(ret, new Comparator<TwoPair_<N, E> >() {
			public int compare(TwoPair_<N, E> g1, TwoPair_<N, E> g2) {
				int Comp = (g1.Value).compareTo(g2.Value);
				if (Comp == 0) return g1.label.compareTo(g2.label);
				return Comp;
			}
		});

		//checkRep();
	   	return ret;
	}
	
	/**	Returns a list of child_node values in alphabetical order
	 * If the parentNode Node does not exist then return an empty list 
     * @param parentNode the value of the node the edge is coming from
     * @requires parentNode is not null
     * @returns a list of childNodes in lexicographical (alphabetical) order by node name
     */
	public ArrayList<N> listValueOfChildren(N parentNode) {		
		
		//List to be returned
		ArrayList<N> ret = new ArrayList<N>();
		
		//For each item in the list returned by listChildren, push the value of each child into the list
		Iterator<TwoPair_<N, E> > i = listChildren(parentNode).iterator();
		while(i.hasNext()) ret.add(i.next().Value);
		
		//Return list
	   	return ret;
	}
	
	/**	Checks to see if an edge exists
     * @param parent the value of the node the edge is coming from
     * @param child the value of the node the edge is going to
     * @param label the label of the edge to be checked to see if it exists
     * @requires parent is not null
     * @requires label is not null
     * @requires child is not null
     * @returns true if the edges exists (meaning both the parent and child node exist as well), and false if it does not          
     */
	public boolean edgeExists(N parent, N child, E label) {
		//checkRep();
		
		//If either of the nodes does not exist then return false
		if (!Nodes.containsKey(parent) || !Nodes.containsKey(child)) return false;
		
		//Return whether or not the list contains the specified edge
		Iterator<TwoPair_<N, E> > _Children = listChildren(parent).iterator();
		
		//Check to see if the list contains the edges
		while(_Children.hasNext()) {
			TwoPair_<N, E> temp = _Children.next();
			if (temp.Value.equals(child) && temp.label.equals(label)) return true;
		}
		
		//Return false
		return false;
	}
	
	/**	Checks to see if a node exists
     * @param s the value of the node to be looked for
     * @returns true if the node exists          
     */
	public boolean NodeExists(N s) {
		return Nodes.containsKey(s);
	}
	
	/**	Checks to see if a node exists
     * @param parent the value of the node the edge is coming from
     * @param child the value of the node the edge is going to
     * @returns the value of the first edge label from the parent to the child
     */
	public E FindLabel(N parent, N child) {
		return Nodes.get(parent).FindLabel(child);
	}
	
	/** Breaks the rep invariant of a graph node to test it
	 * 	@modifies Nodes
	 * 	@effects sorts the children of each Node alphabetically, first by value,
	 * 	second by value of the edge labels linking them to their parent
	 */
	public void sort_children() {
		Iterator<Graph_node<N, E> > i = Nodes.values().iterator();
		while(i.hasNext()) i.next().sort_children();
	}
	
	/** Breaks the rep invariant of a graph node to test it
	 * 	@modifies Nodes
	 * 	@effects adds a new Graph_node to Nodes
	 *	@returns true if the rep invariant broke as expected
	 */
	/*public boolean Break_graph_node_rep() {
		//checkRep();
		
		//Check the invariants of a node in the graph
		addNode(new String("C"));
		return Nodes.get(new String("C")).Break_rep();
	}*/
}
