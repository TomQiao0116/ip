package tom;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome(String name) {
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void close() {
        scanner.close();
    }
}
