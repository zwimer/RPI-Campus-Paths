package hw7;

//Private Helper class which holds a name of a building, and its location
public class Trio {
    /**	Constructor
     * @effects Constructs a new Trio
     */
    public Trio(String a, String b, String c)
    {name=a;x=Integer.parseInt(b);y=Integer.parseInt(c);}
    public final String name;
    public final int x, y;
}
