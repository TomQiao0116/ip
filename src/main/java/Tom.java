import java.util.Scanner;
import java.util.ArrayList;
public class Tom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "Tom";

        ArrayList<Task> tasks = new ArrayList<>();

        //Greet the user
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");

        while(true){
            String User_input = scanner.nextLine();
            if (User_input.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            //list out the user inputs
            if (User_input.equalsIgnoreCase("list")) {
                for(int i = 0; i < tasks.size(); i++){
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
                }
                continue;
            }

            tasks.add(new Task(User_input));
            System.out.println("added: " + User_input);
        }
        scanner.close();
    }
}
