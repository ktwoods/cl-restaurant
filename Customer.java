package restaurant;

/**
 * Restaurant patron or party of patrons. Stores the party's name (or other 
 * unique identifier - the ID of a coaster pager, for instance), size, and 
 * whether they have a reservation.
 * 
 * @author Katie Woods
 * @version 1.0
 */
public class Customer 
{
    private String name;
    private int size;
    private boolean reservation;
    
    /**
     * Creates a customer with the specified characteristics.
     * 
     * @param partyName name of customer, or other ID unique to this customer
     * @param partySize number of people in the group
     * @param hasReservation true if the customer has a reservation, false otherwise
     * 
     * @throws IllegalArgumentException if partyName is null or partySize is less than 1
     */
    public Customer(String partyName, int partySize, boolean hasReservation)
    {
        if (partySize < 1) throw new IllegalArgumentException("There must be at"
                + " least one person in the party.");
        if (partyName == null) throw new IllegalArgumentException("Customer must"
                + " have a name.");
        
        name = partyName;
        size = partySize;
        reservation = hasReservation;
    }
    
    /**
     * Returns the customer's name or ID.
     * 
     * @return unique string representing the customer or party
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the size of the party.
     * 
     * @return the number of people in the group, or 1 if an individual
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Returns boolean representing whether or not the customer has a reservation.
     * 
     * @return true if there is a reservation, false if not
     */
    public boolean hasReservation()
    {
        return reservation;
    }
    
    /**
     * Generates a string representing the customer. The string's format is 
     * "[ID] (party of [size])", with an optional "R" appearing before the party 
     * size if the customer had a reservation.
     * 
     * @return string in the given format
     */
    @Override
    public String toString()
    {
        return name + " (" + (reservation? "R, " : "") + "party of " + size + ")";
    }
}
