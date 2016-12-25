package hw7;

//Helper class which represents an edge between two locations
public class Edge implements Comparable<Edge>{
	
	//Constructors    
	/**	Constructor
    * @effects Constructs a new Edge with a Path being itself
    */
    public Edge(Integer A, Integer B, String G, String H, Double D, Double E, String F) {
    	StartID = A; EndID = B; StartName = G; EndName = H; Path = this; Distance = D; Total = E; Direction = F;
    }    
    
    /**	Constructor
     * @effects Constructs a new Edge
     */
    public Edge(Integer A, Integer B, String G, String H, Edge C, Double D, Double E, String F) {
    	StartID = A; EndID = B; StartName = G; EndName = H; Path = C; Distance = D; Total = E; Direction = F;
    }
    
    //Representation
    //These represent ways to identify the start and end locations
	public final Integer StartID;
	public final Integer EndID;
	public final String StartName;
	public final String EndName;
	
	//These represent the path taken to get to the current edge, and the vector from Start to End
	public final Edge Path;
	public final Double Distance;
	public final Double Total;
	public final String Direction;
    
	//Compare function
    @Override
    public int compareTo(Edge A) {
    	return this.Total.compareTo(A.Total);
    }
}
