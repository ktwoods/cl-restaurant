package restaurant;

/**
 * Table within the restaurant. Tables have a default capacity of 4 seats. Each
 * table also has a unique label, a Customer occupant (which is null if the 
 * table is unoccupied), and a status marker between 0 and 5 inclusive with the  
 * following meaning:
 * <ul>
 * <li>0 = Ready for patrons
 * <li>1 = Waiting to order
 * <li>2 = Waiting for food
 * <li>3 = Served
 * <li>4 = Check delivered
 * <li>5 = Table vacated
 * </ul>
 * 
 * @author Katie Woods
 * @version 1.0
 */
public class Table 
{
    private String ID;
    private final int capacity;
    private int status;
    private Customer occupant;
    
    /**
     * Creates an unoccupied table with the given label and a default capacity 
     * of 4 people.
     * 
     * @param label unique identifier for this table
     * @throws IllegalArgumentException if label is null
     */
    public Table(String label)
    {
        this(label, 4);
    }
    
    /**
     * Creates an unoccupied table with the given label and capacity.
     * 
     * @param label unique identifier for this table
     * @param capacity number of seats available at the table
     * @throws IllegalArgumentException if label is null or capacity is less than 1
     */
    public Table(String label, int capacity)
    {
        if (capacity < 1) throw new IllegalArgumentException("Table must be able"
                + " to seat at least one person.");
        if (label == null) throw new IllegalArgumentException("Table must"
                + " have a label.");
        
        ID = label;
        this.capacity = capacity;
        status = 0;
        occupant = null;
    }
    
    /**
     * Returns the table's unique identifier. 
     * 
     * @return string representing this table
     */
    public String getLabel()
    {
        return ID;
    }
    
    /**
     * Returns the maximum capacity of the table. 
     * 
     * @return number of seats available at the table
     */
    public int getCapacity()
    {
        return capacity;
    }
    
    /**
     * Returns an integer representing the table's current status, with the 
     * following meanings.
     * <ul>
     * <li>0 = Ready for patrons
     * <li>1 = Waiting to order
     * <li>2 = Waiting for food
     * <li>3 = Served
     * <li>4 = Check delivered
     * <li>5 = Table vacated
     * </ul>
     * 
     * @return integer between 0 and 5 inclusive
     */
    public int getStatus()
    {
        return status;
    }
    
    /**
     * Updates the status of the table with a value from the following chart.
     * <ul>
     * <li>0 = Ready for patrons
     * <li>1 = Waiting to order
     * <li>2 = Waiting for food
     * <li>3 = Served
     * <li>4 = Check delivered
     * <li>5 = Table vacated
     * </ul>
     * If the table is occupied (current status is 1, 2, 3, or 4) this method 
     * cannot be used to change it to a vacant status (0 or 5), and vice versa. 
     * Attempting to call this method will return false without altering any 
     * fields. The vacate and seat methods should be used instead, to properly 
     * update both the table's status and its occupant.
     * 
     * @param newStatus integer between 0 and 5 inclusive
     * @return true if status has been successfully changed, false otherwise
     */
    public boolean setStatus(int newStatus)
    {
        // status must be between 0 and 5
        if (newStatus < 0 || newStatus > 5) return false;
        // if table is occupied, status cannot be "Ready for patrons" or "Table vacated"
        if (occupant != null && (newStatus == 0 || newStatus == 5)) return false;
        // if table is vacant, it cannot be updated to a status other than 0 or 5
        if (occupant == null && (newStatus != 0 && newStatus != 5)) return false;
        
        status = newStatus;
        return true;
    }
    
    /**
     * Returns the customer or party currently seated at this table.
     * 
     * @return Customer occupying this table, or null if the table is empty
     */
    public Customer getOccupant()
    {
        return occupant;
    }
    
    /**
     * Removes the table's occupant and marks the table status as 5, or "Table 
     * vacated."
     * 
     * @return true if table was occupied and has successfully been vacated, 
     * false otherwise
     */
    public boolean vacate()
    {
        if (occupant == null) return false;
        
        occupant = null;
        status = 5;
        return true;
    }
    
    /**
     * Seats the given customer or party at this table. The customer cannot be 
     * null and must have a party size that is less than or equal to the table's 
     * maximum capacity. The table must be vacant and have a status of 0, or 
     * "Ready for patrons." If any of these conditions are not met, the method 
     * will return false and will not alter the table.
     * 
     * @param newOccupant Customer to be seated at this table
     * @return true if table was vacant and its fields have successfully been 
     * updated, false otherwise
     */
    public boolean seat(Customer newOccupant)
    {
        // cannot take null as an argument
        if (newOccupant == null) return false;
        // table must be empty
        if (occupant != null) return false;
        // table must be "Ready for patrons"
        if (status != 0) return false;
        // new party must be able to fit at the table
        if (newOccupant.getSize() > capacity) return false;
        
        occupant = newOccupant;
        status = 1;
        return true;
    }
    
    /**
     * Generates a string representing the table and its current status. The
     * general structure of this string is as follows:
     * <p> Table [ID] ([capacity] seats):\t[status]\t[occupant]
     * <p>
     * For example:
     * <p> Table D (4 seats):\tServed\tCurtis (R, party of 4)
     * <p>
     * The occupant is not displayed for tables that are currently unoccupied. 
     * The table status is an integer between 0 and 5 inclusive, with the 
     * following meanings:<ul>
     * <li>0 = Ready for patrons
     * <li>1 = Waiting to order
     * <li>2 = Waiting for food
     * <li>3 = Served
     * <li>4 = Check delivered
     * <li>5 = Table vacated
     * </ul>
     * 
     * @return string representing the table and all data fields
     */
    @Override
    public String toString()
    {
        String returnString = "Table " + ID + " (" + capacity + " seats):\t";
        switch (status)
        {
            case 0: returnString += "Ready for patrons"; break;
            case 1: returnString += "Waiting to order"; break;
            case 2: returnString += "Waiting for food"; break;
            case 3: returnString += "Served"; // add tabs to achieve same indentation
                    if (status != 0 && status != 5) returnString += "\t\t"; break;
            case 4: returnString += "Check delivered"; 
                    if (status != 0 && status != 5) returnString += "\t\t"; break;
            case 5: returnString += "Table vacated";
        }
        if (status != 0 && status != 5) returnString += "\t" + occupant;
        return returnString;
    }
    
}
