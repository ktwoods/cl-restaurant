package restaurant;

/**
 * Restaurant object containing a list of tables and of customers who are waiting 
 * to be seated. Actions that are done on the Tables, Customers, and Waitlist are
 * mainly handled by RestaurantSystem for better efficiency.
 * 
 * @author Katie Woods
 * @version 1.0
 */
public class Restaurant
{
    private String name;
    private final Table[] allTables;
    private Waitlist waitlist;
    
    /**
     * Creates a restaurant with the given name and number of tables. Tables are
     * given IDs consisting of capital letters starting with Table A, and each 
     * table has the default capacity of 4 seats. 
     * 
     * @param restaurantName name for restaurant (will simply be named "Restaurant" if null)
     * @param numTables number of tables; must be at least 1
     * @throws IllegalArgumentException if numTables is less than 1
     */
    public Restaurant(String restaurantName, int numTables)
    {
        this(restaurantName, numTables, 4);
    }
    
    /**
     * Creates a restaurant with the given name and number of tables, each of 
     * which has the given capacity. Tables are given number IDs, (1, 2, 3, etc).
     * 
     * @param restaurantName name for restaurant (will simply be named "Restaurant" if null)
     * @param numTables number of tables; must be at least 1
     * @param tableCapacity default table capacity; must be at least 1
     * @throws IllegalArgumentException if numTables or tableCapacity is less than 1
     */
    public Restaurant(String restaurantName, int numTables, int tableCapacity)
    {
        if (numTables < 1) throw new IllegalArgumentException("Must have at least one table.");
        
        name = (restaurantName != null) ? restaurantName : "Restaurant";
        
        allTables = new Table[numTables];
        for (int i = 0; i < numTables; i++)
        {
            String tableID = Integer.toString(i + 1);
            allTables[i] = new Table(tableID, tableCapacity);
        }
        
        waitlist = new Waitlist();
    }
    
    /**
     * Creates a restaurant with the given name and tables whose capacities are 
     * taken from the integer array. Tables are given number IDs, (1, 2, 3, etc).
     * 
     * @param restaurantName name for restaurant (will simply be named "Restaurant" if null)
     * @param tableCapacities array of positive integers
     * @throws IllegalArgumentException if any value in tableCapacities is less than 1
     */
    public Restaurant(String restaurantName, int[] tableCapacities)
    {
        name = (restaurantName != null) ? restaurantName : "Restaurant";
        
        int numTables = tableCapacities.length;
        allTables = new Table[numTables];
        for (int i = 0; i < numTables; i++)
        {
            String tableID = Integer.toString(i + 1);
            allTables[i] = new Table(tableID, tableCapacities[i]);
        }
        
        waitlist = new Waitlist();
    }
    
    /**
     * Returns the restaurant's name. 
     * 
     * @return name of restaurant
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the restaurant's full waitlist.
     * 
     * @return Waitlist containing all Customers not yet seated
     */
    public Waitlist getWaitlist()
    {
        return waitlist;
    }
    
    /**
     * Returns all tables at the restaurant.
     * 
     * @return array containing all tables mapped to their label or ID
     */
    public Table[] getAllTables()
    {
        return allTables;
    }
    
    /**
     * Returns all tables that are able to seat a party of the specified size.
     * 
     * @param partySize positive integer representing number of people in a party
     * @return array containing all tables of capacity at least equal to partySize mapped to their label or ID
     */
    public Table[] getAvailTables(int partySize)
    {
        /* 
        This is a little clunky, but allTables will always be a comparatively 
        small array, and it hardly seems worth using a more complicated data 
        structure.
        */
        int arrayLength = 0;
        for (Table t : allTables)
            if (t.getCapacity() >= partySize && t.getOccupant() == null) arrayLength++;
        
        Table[] availTables = new Table[arrayLength];
        
        int curIndex = 0;
        for (Table t : allTables)
        {
            if (t.getCapacity() >= partySize && t.getOccupant() == null)
            {
                availTables[curIndex] = t;
                curIndex++;
            }
        }
        return availTables;
    }
    
    /**
     * Returns string consisting of the restaurant's name, all tables, and all
     * customers in the waitlist.
     * 
     * @return Restaurant's name, list of tables, and waitlist
     */
    @Override
    public String toString()
    {
        String restaurant = "\t" + name + "\nTABLES:\n";
        for (Table t : allTables)
        {
            restaurant += t + "\n";
        }
        return restaurant + "\nWAITLIST:\n" + waitlist;
    }
}
