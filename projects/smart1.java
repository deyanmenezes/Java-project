import java.time.LocalDateTime;
import java.util.*;

class Task {
    private int taskId;
    private String description;
    private int priority;  // 1=High, 2=Medium, 3=Low
    private LocalDateTime createdAt;
    private String status;
    
    public Task(int taskId, String description, int priority) {
        this.taskId = taskId;
        this.description = description;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.status = "Pending";
    }
    
    public int getTaskId() { return taskId; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        Map<Integer, String> priorityMap = new HashMap<>();
        priorityMap.put(1, "High");
        priorityMap.put(2, "Medium");
        priorityMap.put(3, "Low");
        return "[" + taskId + "] " + description + " | Priority: " + priorityMap.get(priority) + " | Status: " + status;
    }
}


class SmartTaskManager {
    private List<Task> allTasks;
    private Queue<Task> taskQueue;
    private Stack<Task> processedStack;
    private int nextId;
    
    public Queue<Task> getTaskQueue() { return taskQueue; }
    public List<Task> getAllTasks() { return allTasks; }
    
    public SmartTaskManager() {
        this.allTasks = new ArrayList<>();
        this.taskQueue = new LinkedList<>();
        this.processedStack = new Stack<>();
        this.nextId = 1;
    }
    
    public Task addTask(String description, int priority) {
        if (priority < 1 || priority > 3) {
            System.out.println("Invalid priority! Use 1 (High), 2 (Medium), or 3 (Low)");
            return null;
        }
        
        Task task = new Task(nextId, description, priority);
        allTasks.add(task);
        taskQueue.offer(task);
        nextId++;
        
        System.out.println("Task added successfully: " + task);
        return task;
    }
    
    public Task enqueueTask(String description, int priority) {
        return addTask(description, priority);
    }
    
    public Task dequeueTask() {
        if (taskQueue.isEmpty()) {
            System.out.println("No tasks in queue to process!");
            return null;
        }
        
        Task task = getHighestPriorityTask();
        
        if (task != null) {
            task.setStatus("Completed");
            processedStack.push(task);
            System.out.println("Task processed: " + task);
            return task;
        }
        
        return null;
    }
    
    private Task getHighestPriorityTask() {
        if (taskQueue.isEmpty()) {
            return null;
        }
        
        Task highestPriorityTask = Collections.min(taskQueue, 
            Comparator.comparingInt(Task::getPriority));
        taskQueue.remove(highestPriorityTask);
        return highestPriorityTask;
    }
    
    public Task undoLastTask() {
        if (processedStack.isEmpty()) {
            System.out.println("No processed tasks to undo!");
            return null;
        }
        
        Task task = processedStack.pop();
        task.setStatus("Pending");
        taskQueue.offer(task);
        
        System.out.println("Task undone: " + task);
        return task;
    }
    
    public void displayAllTasks() {
        if (allTasks.isEmpty()) {
            System.out.println("\nNo tasks in the system");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ALL TASKS IN SYSTEM");
        System.out.println("=".repeat(70));
        
        for (Task task : allTasks) {
            String statusIcon = task.getStatus().equals("Completed") ? "[DONE]" : "[PEND]";
            System.out.println(statusIcon + " " + task);
        }
        
        System.out.println("=".repeat(70));
    }
    
    public void displayPendingTasks() {
        List<Task> pending = new ArrayList<>();
        for (Task t : allTasks) {
            if (t.getStatus().equals("Pending")) {
                pending.add(t);
            }
        }
        
        if (pending.isEmpty()) {
            System.out.println("\nNo pending tasks");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("PENDING TASKS");
        System.out.println("=".repeat(70));
        
        pending.sort(Comparator.comparingInt(Task::getPriority));
        for (Task task : pending) {
            System.out.println("   " + task);
        }
        
        System.out.println("=".repeat(70));
    }
    
    public void displayCompletedTasks() {
        if (processedStack.isEmpty()) {
            System.out.println("\nNo completed tasks");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("COMPLETED TASKS (Stack - Last to First)");
        System.out.println("=".repeat(70));
        
        List<Task> reversed = new ArrayList<>(processedStack);
        Collections.reverse(reversed);
        for (Task task : reversed) {
            System.out.println("   " + task);
        }
        
        System.out.println("=".repeat(70));
    }
    
    public void getStatistics() {
        int total = allTasks.size();
        long pending = allTasks.stream().filter(t -> t.getStatus().equals("Pending")).count();
        int completed = processedStack.size();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TASK STATISTICS");
        System.out.println("=".repeat(70));
        System.out.println("Total Tasks:     " + total);
        System.out.println("Pending Tasks:   " + pending);
        System.out.println("Completed Tasks: " + completed);
        System.out.println("Tasks in Queue:  " + taskQueue.size());
        System.out.println("Tasks in Stack:  " + processedStack.size());
        System.out.println("=".repeat(70));
    }
}


public class SmartMain {
    public static void demo() {
        System.out.println("\n=== SMART TASK MANAGER SYSTEM ===");
        System.out.println("=".repeat(70));
        
        SmartTaskManager manager = new SmartTaskManager();
        
        System.out.println("\n1) ADDING TASKS");
        System.out.println("-".repeat(70));
        manager.addTask("Complete project documentation", 1);
        manager.addTask("Review pull requests", 2);
        manager.addTask("Update team on progress", 1);
        manager.addTask("Organize files", 3);
        manager.addTask("Schedule team meeting", 2);
        
        manager.displayAllTasks();
        manager.displayPendingTasks();
        
        System.out.println("\n2) PROCESSING TASKS (Dequeue)");
        System.out.println("-".repeat(70));
        manager.dequeueTask();
        manager.dequeueTask();
        manager.dequeueTask();
        
        manager.displayPendingTasks();
        manager.displayCompletedTasks();
        
        System.out.println("\n3) UNDO OPERATIONS (Stack)");
        System.out.println("-".repeat(70));
        manager.undoLastTask();
        manager.undoLastTask();
        
        manager.displayPendingTasks();
        manager.displayCompletedTasks();
        
        System.out.println("\n4) PROCESSING REMAINING TASKS");
        System.out.println("-".repeat(70));
        while (!manager.getTaskQueue().isEmpty()) {
            manager.dequeueTask();
        }
        
        manager.getStatistics();
        manager.displayAllTasks();
        
        System.out.println("\nDemo completed!\n");
    }
    
    public static void interactiveMode() {
        SmartTaskManager manager = new SmartTaskManager();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== SMART TASK MANAGER - INTERACTIVE MODE ===");
        System.out.println("=".repeat(70));
        
        while (true) {
            System.out.println("\nMENU:");
            System.out.println("1. Add Task (Enqueue)");
            System.out.println("2. Process Task (Dequeue)");
            System.out.println("3. Undo Last Task");
            System.out.println("4. Display All Tasks");
            System.out.println("5. Display Pending Tasks");
            System.out.println("6. Display Completed Tasks");
            System.out.println("7. Show Statistics");
            System.out.println("8. Exit");
            
            System.out.print("\nEnter your choice (1-8): ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    System.out.print("Enter task description: ");
                    String desc = scanner.nextLine().trim();
                    System.out.println("Priority: 1=High, 2=Medium, 3=Low");
                    try {
                        System.out.print("Enter priority (1-3): ");
                        int priority = Integer.parseInt(scanner.nextLine().trim());
                        manager.enqueueTask(desc, priority);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number.");
                    }
                    break;
                case "2": manager.dequeueTask(); break;
                case "3": manager.undoLastTask(); break;
                case "4": manager.displayAllTasks(); break;
                case "5": manager.displayPendingTasks(); break;
                case "6": manager.displayCompletedTasks(); break;
                case "7": manager.getStatistics(); break;
                case "8":
                    System.out.println("\nThank you for using Smart Task Manager!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please enter 1-8.");
            }
        }
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--interactive")) {
            interactiveMode();
        } else {
            demo();
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Would you like to try interactive mode? (y/n): ");
            try {
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y")) {
                    interactiveMode();
                }
            } catch (Exception e) {
                // Non-interactive environment, skip
            }
            scanner.close();
        }
    }
}
