package restaurant;

import java.util.Scanner;

/**
 * Runs a command-line menu that gives users the ability to create a restaurant
 * with some basic stats on the restaurant and to manage incoming, waiting, and  
 * seated customers.
 * <p>
 * Version 1.0 of RestaurantSystem does not support a restaurant with tables of 
 * varying sizes; all tables are restricted to the default size of 4 seats. It 
 * and its accompanying classes are, however, designed so that such functionality 
 * could be added with minimal fuss. Very little code needs to be changed or 
 * expanded, other than adding some way or ways of getting those table sizes 
 * from the user during init().
 * <p>
 * 1.0 also does not support parties that contain more people than are able to
 * fit at the largest table. If the user wishes to accommodate a larger party, 
 * they will have to enter the party as two different parties (e.g. a 5-person group
 * entered as a 3-person group and a 2-person group) and then determine a way to 
 * seat those two groups at adjacent tables when they become available.
 * <p>
 * This is one of the reasons why this program offers seating suggestions, but 
 * allows the user to override and manually seat parties if necessary.
 * 
 * @author Katie Woods
 * @version 1.0
 */
public class RestaurantSystem 
{
    Restaurant restaurant; 
    int defaultTableCapacity;
    Scanner in;
    
    /**
     * Creates a new RestaurantSystem object and calls init() to do menu setup.
     * @param args 
     */
    public static void main(String[] args) 
    {
        RestaurantSystem system = new RestaurantSystem();
        system.init();
        System.exit(0);
    }
    
    /**
     * Gets the restaurant's name and number of tables from the user, creates a 
     * corresponding Restaurant, and prints a main menu to interact with.
     */
    public void init()
    {
        in = new Scanner(System.in);
        
        // Get name
        String name = "";
        boolean validInput = false;
        while (!validInput)
        {
            System.out.print("Name of restaurant: ");
            name = in.nextLine();
            name = name.trim().replaceAll("\t\n", "");
            if (!name.isEmpty()) validInput = true;
            else System.out.println("Please enter a name.");
        }
        // Get a (valid) number of tables
        validInput = false;
        while (!validInput)
        {
            System.out.print("How many tables does it have? ");
            if (in.hasNextInt())
            {
                int numTables = in.nextInt();
                if (numTables > 0) 
                {
                    restaurant = new Restaurant(name, numTables);
                    /*
                    For this version, all tables are assumed to be size 4. Future 
                    versions of this program could support restaurants that 
                    have tables of multiple different sizes, or of a size other 
                    than 4 - either by prompting the user for that info or by 
                    reading it from a file. Restaurant has two extra constructors 
                    with that functionality in mind.
                    */
                    defaultTableCapacity = 4;
                    validInput = true;
                }
                else System.out.println("Please enter a number greater than 0.");
            }
            else 
            {
                System.out.println("Please enter a valid number.");
            }
            in.nextLine();
        }
        
        // Print main menu
        boolean quit = false;
        while (!quit)
        {
            System.out.println("\n\t\t" + restaurant.getName().toUpperCase() + "\n");
            System.out.println("Please enter a number for one of the following options, or enter \"quit\" to quit:");
            System.out.println("1.\tAdd an incoming patron");
            System.out.println("2.\tView or edit an existing patron");
            System.out.println("3.\tView or edit the status of a table");
            System.out.println("4.\tView all tables");
            System.out.println("5.\tView the waitlist");
            System.out.println("6.\tView all patrons");
            
            // Get a valid input and process it before reprinting the main menu
            boolean validChoice = false;
            while (!validChoice)
            {
                if (in.hasNextInt()) 
                {
                    int choice = in.nextInt();
                    in.nextLine();
                    validChoice = processChoice(choice);
                }
                else
                {
                    String choice = in.next();
                    if (choice.equalsIgnoreCase("quit") || choice.equalsIgnoreCase("q"))
                    {
                        validChoice = true;
                        quit = true;
                    }
                }
                if (!validChoice) System.out.println("Please enter a number"
                        + " between 1 and 6, or \"quit\" to quit.");
            }
        }
        
        in.close();
    }
    
    /**
     * Checks whether the integer input matches a menu item, and if so, redirects 
     * to the method that performs that work.
     * 
     * @param choice integer input by user
     * @return true if the input was valid, false if not
     */
    private boolean processChoice(int choice)
    {
        switch (choice)
        {
            case 1: addCustomer(); return true;
            case 2: viewEditCustomer(); return true;
            case 3: viewEditTableStatus(); return true;
            case 4: viewTables(); return true;
            case 5: viewWaitlist(); return true;
            case 6: viewCustomers(); return true;
            default: return false;
        }
    }
    
    /**
     * Allows the user to enter the information for a new customer, and adds them 
     * to the waitlist if no tables are available. If there is at least one table
     * available, it offers that to the user and seats the customer once confirmation 
     * is received.
     */
    private void addCustomer()
    {
        System.out.println("\tADD AN INCOMING PATRON");
        String name = "";
        int partySize = 0;
        boolean hasReservation = false;
        boolean validInput = false;
        String input;
        
        // Get name
        while (!validInput)
        {
            System.out.print("Patron's name: ");
            name = in.nextLine().trim().replaceAll("\t\n", "");
            if (!name.isEmpty()) validInput = true;
            else System.out.println("Please enter a name.");
        }
        // Get party size
        validInput = false;
        while (!validInput)
        {
            System.out.print("Size of patron's party: ");
            if (in.hasNextInt())
            {
                partySize = in.nextInt();
                if (partySize > 0 && partySize <= defaultTableCapacity) validInput = true;
                else System.out.println("Please enter a number greater than 0 and "
                        + "no more than " + defaultTableCapacity + ".");
            }
            else System.out.println("Please enter a valid number.");
            in.nextLine();
        }
        // Get reservation info
        validInput = false;
        while (!validInput)
        {
            System.out.print("Does this " + ((partySize == 1) ? "person" : "party") 
                    + " have a reservation? (y/n) ");
            input = in.nextLine().trim().replaceAll("\t\n", "");
            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
            {
                hasReservation = true;
                validInput = true;
            }
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
            {
                hasReservation = false;
                validInput = true;
            }
            else System.out.println("Please enter yes or no.");
        }
        
        Customer newCustomer = new Customer(name, partySize, hasReservation);
        Table[] availTables = restaurant.getAvailTables(partySize);
        
        // No tables available; add customer to waitlist
        if (availTables.length == 0)
        {
            restaurant.getWaitlist().add(newCustomer);
            System.out.println("\n" + newCustomer + " has been added to the waitlist.");
            System.out.print("\n(Press enter to continue) ");
            in.nextLine();
            return;
        }
        
        Table seatHere = availTables[0];
        
        // Only one table available - don't add options
        if (availTables.length == 1)
        {
            System.out.println("\n" + newCustomer + " may be seated at Table " 
                    + seatHere.getLabel() + " (" + seatHere.getCapacity() 
                    + " seats).");
            System.out.print("\n(This is the only available table; press enter to continue) ");
            in.nextLine();
        }
        else
        {
            System.out.println("\n" + newCustomer + " may be seated at Table " 
                    + seatHere.getLabel() + " (" + seatHere.getCapacity() 
                    + " seats).");
            System.out.print("\n(Press enter to seat, or type \"cancel\" to seat at a "
                    + "different table) ");
            // Any input that isn't an empty line can be interpreted as "cancel," since 
            // what follows is essentially just an expanded version of the seating dialog.
            input = in.nextLine().trim().replaceAll("\t\n", "");
            boolean approvedToSeat = false;
            if (input.isEmpty()) 
                approvedToSeat = true;
            
            if (!approvedToSeat)
            {
                System.out.println("\nEnter a table's number to seat " + newCustomer 
                        + " at that table: ");
                for (Table option : availTables)
                    System.out.println("\t" + option);
                validInput = false;
                while (!validInput)
                {
                    input = in.nextLine().trim().replaceAll("\t\n", "");
                    for (Table option : availTables)
                    {
                        if (option.getLabel().equals(input))
                        {
                            validInput = true;
                            seatHere = option;
                        }
                    }
                    if (!validInput) System.out.print("Table " + input + " is"
                            + " not an available table. Please try again: ");
                }
                System.out.println("\n" + newCustomer + " may be seated at Table " + input + ".");
                System.out.print("\n(Press enter to continue) ");
                in.nextLine();
            }
        }
        boolean success = seatHere.seat(newCustomer);
        
        if (!success) // This should never happen
            System.out.println(newCustomer + " could not be seated "
                + "at Table " + seatHere.getLabel() + " (" + seatHere.getCapacity() 
                + " seats).");
    }
    
    /**
     * Prompts the user for a customer's name and then locates that customer, if 
     * they exist, and displays their name. Offers user the ability to delete 
     * the customer in question.
     */
    private void viewEditCustomer()
    {
        System.out.println("\tVIEW OR EDIT A PATRON");
        Table[] allTables = restaurant.getAllTables();
        Waitlist waiting = restaurant.getWaitlist();
        
        // Exit early if there isn't at least one customer in the restaurant 
        // (avoids infinite loop later)
        boolean noCustomers = true;
        for (Table table : allTables)
        {
            if (table.getOccupant() != null) 
            {
                noCustomers = false;
                break;
            }
        }
        if (noCustomers)
        {
            System.out.println("There are no patrons currently at " + restaurant.getName() + ".");
            System.out.print("\n(Press enter to continue) ");
            in.nextLine();
            return;
        }
        
        // Get name and find customer
        Customer requested = null;
        Table seatedAt = null;
        while (requested == null)
        {
            System.out.print("Patron's name/identifier: ");
            String name = in.nextLine().trim().replaceAll("\t\n", "");
            
            for (Table table : allTables) // Check tables
            {
                Customer occupant = table.getOccupant();
                if (occupant != null && occupant.getName().equalsIgnoreCase(name))
                {
                    requested = table.getOccupant();
                    seatedAt = table;
                    break;
                }
            }
            if (requested == null) // Not found at a table
                requested = waiting.find(name);
            if (requested == null) // Not found in the waitlist either
            {
                System.out.println("Patron named " + name + " was not found. "
                        + "The patrons at this restaurant are:\n");
                printAllSeatedCustomers();
                if (waiting.length() != 0) System.out.println(waiting.toString());
                System.out.println();
            }
        }
        
        if (seatedAt != null) System.out.println("\n" + seatedAt);
        else System.out.println("\n" + requested + " is waiting for a table.");
        
        System.out.print("\n(Enter \"delete\" to delete this patron, or press enter "
                + "to return to the main menu) ");
        // All input not equal to "delete" is interpreted as a return to the menu
        String input = in.nextLine().trim().replaceAll("\t\r", "");
        if (input.equalsIgnoreCase("delete"))
        {
            System.out.print("This will remove " + requested + " from the "
                    + "restaurant. Are you sure you want to continue? (yes/no)");
            input = in.nextLine().trim().replaceAll("\t\r", "");
            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
            {
                if (seatedAt != null)
                {
                    boolean success = seatedAt.vacate();
                    if (!success) System.out.println("Unable to remove patron from table.");
                    System.out.println("\n" + seatedAt);
                }
                else 
                {
                    waiting.remove(requested.getName());
                    System.out.println("\n" + requested + " has been removed "
                            + "from the waitlist.");
                }
                System.out.print("\n(Press enter to continue) ");
                in.nextLine();
            }
        }
    }
    
    /**
     * Prompts the user for the number of a table, displays the table, and offers 
     * to update the table's status. If the status is getting updated to "Ready 
     * for patrons," this method also checks the waitlist to see if there's a waiting 
     * Customer who can be seated, and offers to do so.
     */
    private void viewEditTableStatus()
    {
        System.out.println("\tVIEW OR ALTER A TABLE'S STATUS");
        Table[] allTables = restaurant.getAllTables();
        
        // Get table number and find corresponding table
        Table requested = null;
        while (requested == null)
        {
            System.out.print("Table number: ");
            String num = in.nextLine().trim().replaceAll("\t\n", "");
            
            for (Table table : allTables) // Check tables
            {
                if (table.getLabel().equalsIgnoreCase(num))
                {
                    requested = table;
                    break;
                }
            }
            if (requested == null) // Table not found
            {
                System.out.println("Table " + num + " was not found. "
                        + "The tables at this restaurant are:\n");
                for (Table table : allTables)
                    System.out.println(table);
                System.out.println();
            }
        }
        System.out.println("\n" + requested);
        System.out.print("\nUpdate this table's status? (y/n) ");
        // All input not equal to "yes" or "y" is interpreted as "no"
        String input = in.nextLine().trim().replaceAll("\t\r", "");
        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
        {
            System.out.println("Enter a number for the new status, or enter \"+\" to increment by one level:");
            System.out.println("0\tReady for patrons");
            System.out.println("1\tWaiting to order");
            System.out.println("2\tWaiting for food");
            System.out.println("3\tServed");
            System.out.println("4\tCheck delivered");
            System.out.println("5\tTable vacated");

            // Get valid new status
            boolean validInput = false;
            int newStatus = requested.getStatus();
            int oldStatus = requested.getStatus();
            while (!validInput)
            {
                if (in.hasNextInt())
                {
                   newStatus = in.nextInt();
                   in.nextLine();
                   if (newStatus >= 0 && newStatus <= 5)
                   {
                       if ((oldStatus == 0 || oldStatus == 5) && 
                                (newStatus != 0 && newStatus != 5))
                       {
                            System.out.print("Invalid status; no one is seated "
                                    + "at Table " + requested.getLabel() + ". Please try again: ");
                       }
                       else validInput = true;
                   }
                   else System.out.print("Please enter \"+\" or a number between 0 and 5: ");
                }
                else
                {
                    input = in.nextLine().trim().replaceAll("\t\r", "");
                    if (input.equals("+"))
                    {
                        newStatus++;
                        if (newStatus == 6) newStatus = 0;
                        validInput = true;
                        if (newStatus == 1) // updating a vacant table (0) to seated status (1)
                        {
                            System.out.print("Invalid status; no one is seated "
                                    + "at Table " + requested.getLabel() + ". Please try again: ");
                            validInput = false;
                        }
                    }
                    else System.out.print("Please enter \"+\" or a number between 0 and 5: ");
                }
            }
            
            // Update the status
            boolean success;
            if ((oldStatus != 0 && oldStatus != 5) && (newStatus == 0 || newStatus == 5))
            {
                System.out.print("This will remove " + requested.getOccupant()
                    + " from the restaurant. Are you sure you want to continue? (yes/no) ");
                input = in.nextLine().trim().replaceAll("\t\r", "");
                if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
                {
                    success = requested.vacate();
                    if (!success) System.out.println("Unable to clear table occupant.");
                }
            }
            else 
            {
                success = requested.setStatus(newStatus);
                if (!success) System.out.println("Unable to update table status.");
            }
            System.out.println("\n" + requested + "\n");
            
            // If table is now "Ready for patrons," check to see if someone from 
            // the waitlist can be seated at it
            if (requested.getStatus() == 0)
            {
                System.out.print("Table " + requested.getLabel() + " is now available");
                Waitlist allWaiting = restaurant.getWaitlist();
                Waitlist seatable = allWaiting.getPartiesSeatable(requested.getCapacity());
                
                // No options for seating at this table
                if (seatable.length() == 0)
                {
                    if (allWaiting.length() == 0)
                        System.out.println(". There are no patrons waiting to be seated.");
                    else System.out.println(". Any parties waiting to be seated are too large for this table.");
                }
                else if (seatable.length() != 1) // Results in repetitive messaging when there's only one option
                {
                    Customer toSeat = seatable.peek();
                    System.out.println(" for " + toSeat + ".");
                    System.out.print("\n(Press enter to seat, or type \"cancel\" "
                        + "to seat a different patron at this table) ");
                    // Any input that isn't an empty line can be interpreted as "cancel," since 
                    // what follows is essentially just an expanded version of the seating dialog.
                    input = in.nextLine().trim().replaceAll("\t\n", "");
                    boolean approvedToSeat = false;
                    if (input.isEmpty()) 
                        approvedToSeat = true;

                    if (!approvedToSeat)
                    {
                        System.out.println("\nThe patrons waiting for a table of this size are: " + seatable);
                        System.out.print("\nEnter a patron's name to seat them at Table " + requested.getLabel() + ": ");
                        success = false;
                        while (!success)
                        {
                            input = in.nextLine().trim().replaceAll("\t\n", "");
                            toSeat = seatable.find(input);
                            if (toSeat == null) System.out.print(input + " was not found. Please try again: ");
                        }
                    }
                    success = restaurant.getWaitlist().remove(toSeat.getName());
                    if (!success) System.out.println("Could not remove from waitlist.");
                    success = requested.seat(toSeat);
                    if (!success) System.out.println("Could not seat patron at table.");
                    System.out.println("\n" + toSeat + " may be seated at Table " + requested.getLabel() + ".");
                }
            }
            System.out.print("\n(Press enter to continue) ");
            in.nextLine();
        }
    }
    
    /**
     * Prints all tables and their statuses.
     */
    private void viewTables()
    {
        System.out.println("\tVIEW ALL TABLES");
        Table[] allTables = restaurant.getAllTables();
        if (allTables.length == 1)
            System.out.print("There is 1 table in " + restaurant.getName());
        else
            System.out.print("There are " + allTables.length + " tables in " 
                + restaurant.getName());
        
        int full = 0;
        int vacant = 0;
        for (Table table : allTables)
        {
            if (table.getOccupant() == null) vacant++;
            else full++;
        }
        System.out.println(" (" + full + " in use and " + vacant + " empty).\n");
        
        for (Table table : allTables)
            System.out.println(table);
        
        System.out.print("\n(Press enter to continue) ");
        in.nextLine();
    }
    
    /**
     * Prints some basic stats about the waitlist and then the waitlist itself.
     */
    private void viewWaitlist()
    {
        System.out.println("\tVIEW WAITLIST");
        Waitlist waiting = restaurant.getWaitlist();
        int total = waiting.length();
        int withRes = waiting.numWithReservations();
        int withoutRes = total - withRes;
        
        if (total != 0)
        {
            if (total == 1)
                System.out.println("There is 1 person or party waiting to be seated.");
            else System.out.println("There are " + total + " people or parties waiting "
                    + "to be seated.");
            System.out.println("Parties with a reservation:\t" + withRes);
            System.out.println("Parties without a reservation:\t" + withoutRes + "\n");
        }
        
        System.out.println(waiting);
        
        System.out.print("\n(Press enter to continue) ");
        in.nextLine();
    }
    
    /**
     * Prints all occupied tables, and then prints the waitlist, followed by the 
     * combined total number of customers at the restaurant.
     */
    private void viewCustomers()
    {
        System.out.println("\tVIEW ALL PATRONS");
        Table[] allTables = restaurant.getAllTables();
        Waitlist waiting = restaurant.getWaitlist();
        
        System.out.println("\tSEATED:");
        int numFound = printAllSeatedCustomers();
        if (numFound == 0) System.out.println("There are no seated patrons.");
        
        System.out.println("\n\tWAITING:\n" + waiting);
        
        System.out.println("\nTotal patrons: " + (numFound + waiting.length()));
        
        System.out.print("\n(Press enter to continue) ");
        in.nextLine();
    }
    
    /**
     * Helper method used by viewCustomers and viewEditCustomer to print seated
     * customers.
     * 
     * @return total number of seated customers
     */
    private int printAllSeatedCustomers()
    {
        Table[] allTables = restaurant.getAllTables();
        
        int numFound = 0;
        for (Table table : allTables)
        {
            if (table.getOccupant() != null) 
            {
                System.out.println(table.getOccupant() + "\tSeated at Table " + 
                        table.getLabel() + "");
                numFound++;
            }
        }
        
        return numFound;
    }
}
