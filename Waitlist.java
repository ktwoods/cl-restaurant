package restaurant;

/**
 * Restaurant waiting list consisting of a Customer queue with two priority levels, 
 * based on whether or not customers have a reservation. All customers with 
 * reservations will be offered for seating before any customer without a 
 * reservation.
 * <p>
 * Insertion is the same as with a typical queue, but removal works slightly 
 * differently in order to accommodate tables with different sizes - for instance,
 * if the only open table can seat 2 and the next customer in line is part of a 
 * 4-person group, a typical poll method would remove them for seating even though 
 * they would not be able to fit at that table.
 * <p>
 * A call to getSeatable will return a Waitlist consisting only of parties 
 * below the given size threshold. The user may wish to seat parties out of order 
 * due to circumstances that are not handled by RestaurantSystem; however, it is 
 * recommended that remove() generally be used to remove the first customer in 
 * this smaller Waitlist.
 * 
 * @author Katie Woods
 * @version 1.0
 */
public class Waitlist
{
    private class ListNode 
    {
        ListNode next;
        Customer data;
        
        ListNode(ListNode next, Customer data)
        {
            this.next = next;
            this.data = data;
        }
    }
    
    private ListNode frontOfQueue;
    private ListNode endOfReservationQueue; // storing this pointer makes all insertions O(1)
    private ListNode endOfQueue;
    private int length;
    private int numReservations;
    
    /**
     * Creates an empty waitlist of length 0.
     */
    public Waitlist()
    {
        frontOfQueue = endOfQueue = endOfReservationQueue = null;
        length = 0;
        numReservations = 0;
    }
    
    /**
     * Returns the length of the list.
     * 
     * @return number of Customers in the waitlist
     */
    public int length()
    {
        return length;
    }
    
    /**
     * Returns the number of people in the list who have reservations.
     * 
     * @return number of Customers with reservations
     */
    public int numWithReservations()
    {
        return numReservations;
    }
    
    /**
     * Adds a new person or party to the waitlist. If the customer has no 
     * reservation, they will be added to the end of the waitlist. If they do have 
     * a reservation, they will be added between the most recent customer to arrive 
     * with a reservation and the longest-waiting customer to arrive without a 
     * reservation.
     * 
     * @param newCustomer Customer to be added
     * @return true if successful
     */
    public boolean add(Customer newCustomer)
    {
        if (frontOfQueue == null) // waitlist is empty
        {
            frontOfQueue = endOfQueue = new ListNode(null, newCustomer);
            if (newCustomer.hasReservation()) endOfReservationQueue = frontOfQueue;
        }
        else if (newCustomer.hasReservation())
        {
            // waitlist is not empty, but no one waiting has a reservation - make newCustomer front of queue
            if (endOfReservationQueue == null)
            {
                endOfReservationQueue = frontOfQueue = new ListNode(frontOfQueue, newCustomer);
            }
            else // at least one reservation, so add after the last reservation
            {
                ListNode newNode = new ListNode(null, newCustomer);
                newNode.next = endOfReservationQueue.next;
                endOfReservationQueue.next = newNode;
                endOfReservationQueue = newNode;
            }
            numReservations++;
        }
        else // newCustomer doesn't have reservation; add to end
        {
            ListNode newNode = new ListNode(null, newCustomer);
            endOfQueue.next = newNode;
            endOfQueue = newNode;
        }
        
        length++;
        
        return true;
    }
    
    /**
     * Removes the Customer with the specified name from the waitlist. Each 
     * customer is assumed to have a unique ID; if more than one customer with 
     * the same ID has been added, this method will remove the first one 
     * encountered. If no Customer with the given name is found, this method 
     * will return false and leave the list unchanged.
     * 
     * @param partyName name or ID of a Customer in this list
     * @return true if successful, false otherwise
     */
    public boolean remove(String partyName)
    {
        // List is empty
        if (frontOfQueue == null) return false;
        
        ListNode current = frontOfQueue;
        ListNode prev = null;
        while (current.next != null && !current.data.getName().equalsIgnoreCase(partyName))
        {
            prev = current;
            current = current.next;
        }
        // Reached end of list without finding the customer
        if (!current.data.getName().equals(partyName)) return false;
        
        // If removing first item in queue, next item (if any) is now the first item
        if (current == frontOfQueue) frontOfQueue = current.next;
        
        // If removing last item in queue, previous item (if any) is now end of queue
        if (current == endOfQueue) endOfQueue = prev;
        
        // If removing last reservation, previous item (if any) is now last reservation
        if (current == endOfReservationQueue) endOfReservationQueue = prev;
        
        if (prev != null) prev.next = current.next; // Remove node from list
        current.next = null; // Free removed node for garbage collection
        
        length--;
        if (current.data.hasReservation()) numReservations--;
        return true;
    }
    
    /**
     * Finds the customer with the given name, if they exist in this waitlist. 
     * Each customer is assumed to have a unique ID; if more than one customer 
     * with the same ID has been added, this method will return the one closest 
     * to the front of the list.
     * 
     * @param partyName name or ID of a Customer
     * @return first Customer found with matching name, or null if not found
     */
    public Customer find(String partyName)
    {
        ListNode current = frontOfQueue;
        while (current != null)
        {
            if (current.data.getName().equalsIgnoreCase(partyName)) return current.data;
            current = current.next;
        }
        // customer with this name is not in waitlist
        return null;
    }
    
    /**
     * Returns the first Customer in the waitlist.
     * 
     * @return the first Customer, or null if waitlist is empty
     */
    public Customer peek()
    {
        if (frontOfQueue == null) return null;
        return frontOfQueue.data;
    }
    
    /**
     * Returns a subset of the Waitlist consisting of all Customers whose party 
     * size is less than or equal to the given table capacity. If none are found, 
     * returns an empty Waitlist.
     * 
     * @param capacity maximum number of people who may be seated at a particular table
     * @return Waitlist of Customers that will fit at that table
     */
    public Waitlist getPartiesSeatable(int capacity)
    {
        Waitlist lessThan = new Waitlist();
        
        ListNode current = frontOfQueue;
        while (current != null)
        {
            if (current.data.getSize() <= capacity) lessThan.add(current.data);
            current = current.next;
        }
        return lessThan;
    }
    
    /**
     * Returns all Customers in the waitlist in order, separated by a newline 
     * character. If the waitlist is empty, returns, "There are no waiting patrons."
     * 
     * @return string representing the waitlist
     */
    @Override
    public String toString()
    {
        if (frontOfQueue == null) return "There are no waiting patrons.";
        String returnString = "";
        ListNode current = frontOfQueue;
        while (current != null)
        {
            returnString += current.data;
            if (current.next != null) returnString += "\n";
            current = current.next;
        }
        return returnString;
    }
}