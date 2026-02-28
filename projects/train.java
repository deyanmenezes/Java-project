/**
 * Constructor to create a new customer
 */
public Customer(int customerId, String name, String destination, double ticketPrice) {
    this.customerId = customerId;
    this.name = name;
    this.destination = destination;
    this.ticketPrice = ticketPrice;
    this.ticketNumber = "TKT" + String.format("%04d", customerId);
}

// Getters
public int getCustomerId() { return customerId; }
public String getName() { return name; }
public String getDestination() { return destination; }
public double getTicketPrice() { return ticketPrice; }
public String getTicketNumber() { return ticketNumber; }

@Override
public String toString() {
    return String.format("| %-12s | %-20s | %-15s | Rs. %-8.2f |", 
                       ticketNumber, name, destination, ticketPrice);
}
}

public class RailwayTicketCounter {

// Data Structures
private Queue<Customer> customerQueue;      // Queue for waiting customers
private Stack<Customer> cancelledStack;     // Stack for cancelled tickets
private ArrayList<Customer> issuedTickets;  // Array to store all issued tickets
private int nextCustomerId;

/**
 * Constructor - Initialize all data structures
 */
public RailwayTicketCounter() {
    customerQueue = new LinkedList<>();      // LinkedList implements Queue interface
    cancelledStack = new Stack<>();          // Stack for LIFO operations
    issuedTickets = new ArrayList<>();       // ArrayList for dynamic array
    nextCustomerId = 1;
}

/**
 * Add a customer to the queue
 * Uses ENQUEUE operation (adds to rear of queue)
 */
public void addCustomer(String name, String destination, double ticketPrice) {
    if (name == null || name.trim().isEmpty()) {
        System.out.println("❌ Error: Customer name cannot be empty!");
        return;
    }
    if (destination == null || destination.trim().isEmpty()) {
        System.out.println("❌ Error: Destination cannot be empty!");
        return;
    }
    if (ticketPrice <= 0) {
        System.out.println("❌ Error: Ticket price must be positive!");
        return;
    }
    
    Customer customer = new Customer(nextCustomerId, name, destination, ticketPrice);
    customerQueue.offer(customer);  // ENQUEUE - add to queue
    nextCustomerId++;
    
    System.out.println("\n✅ Customer added to queue successfully!");
    System.out.println("   Name: " + name);
    System.out.println("   Destination: " + destination);
    System.out.println("   Ticket Price: Rs. " + ticketPrice);
    System.out.println("   Position in queue: " + customerQueue.size());
}

/**
 * Serve the next customer in queue
 * Uses DEQUEUE operation (removes from front of queue)
 * Issues ticket and stores in array
 */
public void serveCustomer() {
    if (customerQueue.isEmpty()) {
        System.out.println("\n⚠️  No customers in queue! Queue is empty.");
        return;
    }
    
    Customer customer = customerQueue.poll();  // DEQUEUE - remove from front
    issuedTickets.add(customer);               // Add to issued tickets array
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("🎫 TICKET ISSUED SUCCESSFULLY!");
    System.out.println("=".repeat(70));
    System.out.println("   Ticket Number: " + customer.getTicketNumber());
    System.out.println("   Customer Name: " + customer.getName());
    System.out.println("   Destination: " + customer.getDestination());
    System.out.println("   Amount Paid: Rs. " + customer.getTicketPrice());
    System.out.println("=".repeat(70));
    System.out.println("   Remaining customers in queue: " + customerQueue.size());
}

/**
 * Cancel the last issued ticket
 * Uses PUSH operation on stack (adds to top)
 * Removes ticket from array
 */
public void cancelLastTicket() {
    if (issuedTickets.isEmpty()) {
        System.out.println("\n⚠️  No tickets issued yet! Cannot cancel.");
        return;
    }
    
    // Remove last ticket from array (last issued)
    Customer cancelledCustomer = issuedTickets.remove(issuedTickets.size() - 1);
    
    // Push to cancelled stack (PUSH operation)
    cancelledStack.push(cancelledCustomer);
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("❌ TICKET CANCELLED!");
    System.out.println("=".repeat(70));
    System.out.println("   Ticket Number: " + cancelledCustomer.getTicketNumber());
    System.out.println("   Customer Name: " + cancelledCustomer.getName());
    System.out.println("   Refund Amount: Rs. " + cancelledCustomer.getTicketPrice());
    System.out.println("=".repeat(70));
}

/**
 * Display all customers waiting in queue
 * Shows FIFO order (first customer added will be served first)
 */
public void displayQueue() {
    if (customerQueue.isEmpty()) {
        System.out.println("\n📋 Queue is empty - No customers waiting");
        return;
    }
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("👥 CUSTOMERS WAITING IN QUEUE (FIFO Order)");
    System.out.println("=".repeat(70));
    System.out.println("Position | Customer ID | Name                 | Destination");
    System.out.println("-".repeat(70));
    
    int position = 1;
    for (Customer customer : customerQueue) {
        System.out.printf("   %-6d | %-11d | %-20s | %-15s\n",
                        position, customer.getCustomerId(), 
                        customer.getName(), customer.getDestination());
        position++;
    }
    System.out.println("=".repeat(70));
    System.out.println("Total customers in queue: " + customerQueue.size());
}

/**
 * Display all issued tickets stored in array
 */
public void displayIssuedTickets() {
    if (issuedTickets.isEmpty()) {
        System.out.println("\n📋 No tickets issued yet");
        return;
    }
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("🎫 ALL ISSUED TICKETS (Stored in Array)");
    System.out.println("=".repeat(70));
    System.out.println("| Ticket No.   | Customer Name        | Destination     | Price        |");
    System.out.println("-".repeat(70));
    
    for (Customer customer : issuedTickets) {
        System.out.println(customer);
    }
    System.out.println("=".repeat(70));
    System.out.println("Total tickets issued: " + issuedTickets.size());
}

/**
 * Display all cancelled tickets from stack
 * Shows LIFO order (most recently cancelled at top)
 */
public void displayCancelledTickets() {
    if (cancelledStack.isEmpty()) {
        System.out.println("\n📋 No cancelled tickets");
        return;
    }
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("❌ CANCELLED TICKETS (Stack - LIFO Order, Top to Bottom)");
    System.out.println("=".repeat(70));
    System.out.println("| Ticket No.   | Customer Name        | Destination     | Refund       |");
    System.out.println("-".repeat(70));
    
    // Display stack from top to bottom (using Iterator)
    Stack<Customer> tempStack = (Stack<Customer>) cancelledStack.clone();
    while (!tempStack.isEmpty()) {
        Customer customer = tempStack.pop();
        System.out.println(customer);
    }
    System.out.println("=".repeat(70));
    System.out.println("Total cancelled tickets: " + cancelledStack.size());
}

/**
 * Display system statistics
 */
public void displayStatistics() {
    System.out.println("\n" + "=".repeat(70));
    System.out.println("📊 RAILWAY TICKET COUNTER STATISTICS");
    System.out.println("=".repeat(70));
    System.out.println("Customers in Queue (waiting):        " + customerQueue.size());
    System.out.println("Tickets Issued (active):             " + issuedTickets.size());
    System.out.println("Tickets Cancelled:                   " + cancelledStack.size());
    
    // Calculate total revenue
    double totalRevenue = 0;
    for (Customer customer : issuedTickets) {
        totalRevenue += customer.getTicketPrice();
    }
    
    double totalRefund = 0;
    for (Customer customer : cancelledStack) {
        totalRefund += customer.getTicketPrice();
    }
    
    System.out.println("-".repeat(70));
    System.out.printf("Total Revenue from Active Tickets:   Rs. %.2f\n", totalRevenue);
    System.out.printf("Total Refunds:                       Rs. %.2f\n", totalRefund);
    System.out.printf("Net Revenue:                         Rs. %.2f\n", (totalRevenue - totalRefund));
    System.out.println("=".repeat(70));
}

/**
 * Main method - Program entry point with menu-driven interface
 */
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    RailwayTicketCounter counter = new RailwayTicketCounter();
    
    System.out.println("\n" + "=".repeat(70));
    System.out.println("🚂 WELCOME TO RAILWAY TICKET COUNTER SIMULATION");
    System.out.println("=".repeat(70));
    System.out.println("This system demonstrates Queue, Stack, and Array data structures");
    System.out.println("=".repeat(70));
    
    boolean running = true;
    
    while (running) {
        // Display menu
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 MAIN MENU");
        System.out.println("=".repeat(70));
        System.out.println("1. Add Customer to Queue");
        System.out.println("2. Serve Next Customer (Issue Ticket)");
        System.out.println("3. Cancel Last Issued Ticket");
        System.out.println("4. Display Queue (Waiting Customers)");
        System.out.println("5. Display All Issued Tickets");
        System.out.println("6. Display Cancelled Tickets");
        System.out.println("7. Display Statistics");
        System.out.println("8. Exit");
        System.out.println("=".repeat(70));
        System.out.print("Enter your choice (1-8): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    // Add customer
                    System.out.println("\n--- ADD CUSTOMER TO QUEUE ---");
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();
                    
                    System.out.print("Enter ticket price (Rs.): ");
                    double price = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    
                    counter.addCustomer(name, destination, price);
                    break;
                    
                case 2:
                    // Serve customer
                    counter.serveCustomer();
                    break;
                    
                case 3:
                    // Cancel last ticket
                    counter.cancelLastTicket();
                    break;
                    
                case 4:
                    // Display queue
                    counter.displayQueue();
                    break;
                    
                case 5:
                    // Display issued tickets
                    counter.displayIssuedTickets();
                    break;
                    
                case 6:
                    // Display cancelled tickets
                    counter.displayCancelledTickets();
                    break;
                    
                case 7:
                    // Display statistics
                    counter.displayStatistics();
                    break;
                    
                case 8:
                    // Exit
                    System.out.println("\n" + "=".repeat(70));
                    System.out.println("👋 Thank you for using Railway Ticket Counter System!");
                    System.out.println("=".repeat(70));
                    running = false;
                    break;
                    
                default:
                    System.out.println("\n❌ Invalid choice! Please enter a number between 1-8.");
            }
            
        } catch (InputMismatchException e) {
            System.out.println("\n❌ Invalid input! Please enter a valid number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
    
    scanner.close();
}
}