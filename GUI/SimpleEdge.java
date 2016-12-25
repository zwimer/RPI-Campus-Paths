package GUI;

//A small little edge class
public class SimpleEdge {

	//Representation
	String Direction;
	Pair<String> Names;
	Pair<Integer> IDs;
	
	//Constructor
	public SimpleEdge(Pair<Integer> a, Pair<String> b, String c) {
		IDs = a;
		Names = b;
		Direction = c;
	}
}
