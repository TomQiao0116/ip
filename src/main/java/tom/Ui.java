package tom;

import java.util.Scanner;

/**
 * Handles all user interface interactions for the application.
 * <p>
 * This class is responsible for displaying messages to the user and
 * reading user input from the standard input.
 */
public class Ui {

    private final Scanner scanner;

    /**
     * Creates a UI instance that reads input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     *
     * @param name Name of the application or chatbot.
     */
    public void showWelcome(String name) {
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    /**
     * Reads a command entered by the user.
     *
     * @return The user input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a horizontal separator line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a message to the user.
     *
     * @param message Message to be shown.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Closes resources used by the UI.
     */
    public void close() {
        scanner.close();
    }
}
