import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Tom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "Tom";

        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();

        //Greet the user
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");

        while(true){
            String User_input = scanner.nextLine();
            try {
                if (User_input.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }

                //list out the user inputs
                if (User_input.equalsIgnoreCase("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                    continue;
                }

                //mark
                if (User_input.startsWith("mark ")) {
                    int index = Integer.parseInt(User_input.substring(5)) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        tasks.get(index).mark();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks.get(index));
                        storage.save(tasks);
                    }
                    continue;
                }

                //unmark
                if (User_input.startsWith("unmark ")) {
                    int index = Integer.parseInt(User_input.substring(7)) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        tasks.get(index).unmark();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks.get(index));
                        storage.save(tasks);
                    }
                    continue;
                }

                //TODOevent
                if (User_input.equalsIgnoreCase("todo")) {
                    throw new TomException("Todo description cannot be empty.");
                }
                if (User_input.startsWith("todo ")) {
                    String description = User_input.substring(5);
                    Task task = new Todo(description);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    storage.save(tasks);
                    continue;
                }

                //Deadline
                if (User_input.startsWith("deadline ")) {
                    String[] parts = User_input.substring(9).split(" /by ", 2);
                    if (parts.length < 2) {
                        throw new TomException("Deadline must have /by YYYY-MM-DD");
                    }

                    String desc = parts[0].trim();
                    String dateStr = parts[1].trim();

                    try {
                        LocalDate by = LocalDate.parse(dateStr); // 接受 yyyy-MM-dd
                        Task task = new Deadline(desc, by);
                        tasks.add(task);

                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + task);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    } catch (DateTimeParseException e) {
                        throw new TomException("Date must be in YYYY-MM-DD format, e.g., 2019-10-15");
                    }
                    storage.save(tasks);
                    continue;
                }

                //Event
                if (User_input.startsWith("event ")) {
                    String[] parts = User_input.substring(6).split(" /from | /to ", 3);
                    Task task = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(task);

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    storage.save(tasks);
                    continue;
                }

                //delete
                if (User_input.startsWith("delete ")){
                    String num = User_input.substring(7);
                    int index = Integer.parseInt(num) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        continue;
                    }
                    Task removed = tasks.remove(index);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    storage.save(tasks);
                }

                throw new TomException("I don't know what that command means.");
            } catch (TomException e){
                System.out.println(" " + e.getMessage());
            }
        }
        scanner.close();
    }
}
