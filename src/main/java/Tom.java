import java.util.Scanner;
public class Tom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "Tom";
        String[] tasks = new String[100];
        int count_tasks = 0;

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
                for(int i = 0; i < count_tasks; i++){
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                continue;
            }

            //Add user inputs
            if(count_tasks<100){
                tasks[count_tasks] = User_input;
                count_tasks++;
                System.out.println("added: " + User_input);
            } else{
                System.out.println("List is already full!");
            }
        }
        scanner.close();
    }
}
