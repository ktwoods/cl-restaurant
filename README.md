# cl-restaurant
Command line restaurant seating program build as a class final project. The assignment specified that it had to be able to do certain things, such as handle a priority queue-based waitlist and managing table status (e.g. where any occupants are in their meal, and whether the table is vacant and clean).

I ended up really enjoying working on this, and plannet out several development stages that I knew I wasn't going to have time to work on, thinking I might use it later as an exercise for re-learning how to implement basic GUIs in Java. Once I get the next version of pollinatorproject implemented, I plan to return to this for those features.

### RestaurantSystem class
Prints the menus and parses user inputs.

### Restaurant class
Contains the Waitlist object and array of Table objects.

### Waitlist class
Manages a two-tiered priority queue of waiting customers.

### Table class
Contains basic table stats and the seated Customer object, if any.

### Customer class
Is used to create immutable Customer objects (representing a person or group of people) that are given to a Table or added to the Waitlist.
