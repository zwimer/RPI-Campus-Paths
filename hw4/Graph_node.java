package hw4;

import java.util.*;

//A class representing a node on the graph
public class Graph_node<N extends Comparable<N>, E extends Comparable<E> > {
	
	//Helper class used also in Graph's Break_rep
	public class Pair_<N  extends Comparable<N>, E  extends Comparable<E> > {
		public Pair_(Graph_node<N, E> A, E B) {child = A; label = B;}
		public final Graph_node<N, E> child;
		public final E label;
	}
	
	//Representation
	private final N Value;
	private ArrayList<Pair_<N, E> > Children_list;
	
	/*//Representation Invariant helper
	//This function is independent of checkRep, simply so that in the break_rep function, try blocks are not needed, thus providing more code coverage
	private int checkRep_helper() {
		if (Value == null) return 0;
		for(int i = 0; i < Children_list.size(); i++) {
			if (Children_list.get(i) == null) return 1;
			if (Children_list.get(i).child == null) return 2;
			if (Children_list.get(i).label == null) return 3;
		}
		return -1;
	}
	
	//Representation Invariant
	private void checkRep() {
		int temp = checkRep_helper();
		if (temp == 0) throw new RuntimeException("Error, null value");
		else if (temp == 1) throw new RuntimeException("Error, null pair");
		else if (temp == 3) throw new RuntimeException("Error, null child's value");
		else if (temp == 2) throw new RuntimeException("Error, null child");
	}*/
	
	//Constructor
	public Graph_node(N input_) {
		Children_list = new ArrayList<Pair_<N, E> >();
		Value = input_;
		//checkRep();
	}
	
	/*//private constructor solely used in break_rep
	//This constructor specifically does not use check rep so that it may violate the rep invariant
	private Graph_node() {
		Children_list = new ArrayList<Pair_>();
		Value = null;
	}*/

	//Adds node
	public void add_child(Graph_node<N, E> new_child, E label) {
		//checkRep();
		Children_list.add(new Pair_<N, E>(new_child, label));
		//checkRep();
	}
	
	//Returns value of node, copy
	public N getValue() {
		//checkRep();
		return Value;
	}
	
	//Returns number of kids
	public int Number_of_kids() {
		//checkRep();
		return Children_list.size();
	}

	//Returns the 'which' child of the current node.
	public Graph_node<N, E> getChild (int which) {
		//checkRep();
		return Children_list.get(which).child;
	}
	
	//Returns the 'which' label of the current node.
	public E getLabel(int which) {
		//checkRep();
		return Children_list.get(which).label;
	}
	
	//A function to return the label, edge linking two nodes
	public E FindLabel(N child) {
		for(int i = 0; i < Children_list.size(); i++)
			if (Children_list.get(i).child.Value.equals(child)) 
				return Children_list.get(i).label;
		throw new RuntimeException("Label not found");
	}
	
	//Sort the list of children alphabetically, first by value, second by edge label
	public void sort_children() {
		Collections.sort(Children_list, new Comparator<Pair_<N, E> >() {
			public int compare(Pair_<N, E> g1, Pair_<N, E> g2) {
				int Comp = (g1.child.Value).compareTo(g2.child.Value);
				if (Comp == 0) return g1.label.compareTo(g2.label);
				return Comp;
			}
		});
	}
	
	/*//Test breaking the rep invariant to ensure more coverage, returns true if the rep broke as predicted
	//This function is implemented in an odd way to maximize coverage
	public boolean Break_rep() {
		
		//sum is the sum total of the integer value denoting each error. It should be 6 is everything fails as predicted
		int sum = 0;
		
		//Test making a node with a null value
		Graph_node error_node = new Graph_node(); 
		sum += error_node.checkRep_helper();
		Children_list = new ArrayList<Pair_>();
		
		//Test adding a null Pair_ to Children_list
		Children_list.add(null); 
		sum += checkRep_helper();
		Children_list = new ArrayList<Pair_>();

		//Test adding a node with a null child node	
		Children_list.add(new Pair_(null, null));
		sum += checkRep_helper();
		Children_list = new ArrayList<Pair_>();
		
		//Try adding a node with a null valued child
		Children_list.add(new Pair_(new Graph_node("A"), null));
		sum += checkRep_helper();
		
		//Return the result
		return (sum == 6);
	}*/
}