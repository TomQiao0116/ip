import java.util.Scanner;
public class Tom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "Tom";
        //Greet the user
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");

        while(true){
            String User_input = scanner.nextLine();
            if (User_input.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            System.out.println(User_input);
        }
        scanner.close();
    }
}
