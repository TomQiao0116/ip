package tom;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Entry point of the chatbot application.
 * <p>
 * This class starts the program, reads user commands in a loop, and coordinates interactions
 * between the UI, task list, command parser, and storage.
 */
public class Tom {

    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructs a Tom chatbot instance.
     * Initializes storage and loads existing tasks.
     */
    public Tom() {
        storage = new Storage();
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot application in CLI mode.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Tom tom = new Tom();
        Ui ui = new Ui();
        ui.showWelcome("tom");

        while (true) {
            String userInput = ui.readCommand();
            String response = tom.getResponse(userInput);
            ui.showMessage(response);

            if (userInput.equals("bye")) {
                ui.close();
                break;
            }
        }
    }

    /**
     * Processes a single user input and returns the chatbot's response.
     * This method is used by the GUI.
     *
     * @param userInput The input entered by the user.
     * @return The chatbot's response as a String.
     */
    public String getResponse(String userInput) {
        assert userInput != null : "userInput should not be null";

        try {
            ParsedCommand pc = Parser.parse(userInput);
            assert pc != null : "Parser.parse should not return null";

            String command = pc.getCommandWord();
            String argsStr = pc.getArguments();

            assert command != null && !command.isEmpty()
                    : "command word should not be null or empty";
            assert argsStr != null : "arguments string should not be null";

            switch (command) {

                case "bye":
                    return "Bye. Hope to see you again soon!";

                case "list":
                    if (tasks.size() == 0) {
                        return "Your list is empty.";
                    }
                    StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
                    for (int i = 0; i < tasks.size(); i++) {
                        sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
                    }
                    return sb.toString().trim();

                case "mark": {
                    int index = Integer.parseInt(argsStr) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        return "Invalid task index.";
                    }
                    assert index >= 0 && index < tasks.size()
                            : "index out of range after validation";

                    tasks.get(index).mark();
                    storage.save(tasks.getTasks());
                    return "Nice! I've marked this task as done:\n  " + tasks.get(index);
                }

                case "unmark": {
                    int index = Integer.parseInt(argsStr) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        return "Invalid task index.";
                    }
                    assert index >= 0 && index < tasks.size()
                            : "index out of range after validation";

                    tasks.get(index).unmark();
                    storage.save(tasks.getTasks());
                    return "OK, I've marked this task as not done yet:\n  " + tasks.get(index);
                }

                case "todo":
                    if (argsStr.isEmpty()) {
                        throw new TomException("Todo description cannot be empty.");
                    }
                    Task todo = new Todo(argsStr);
                    tasks.add(todo);
                    storage.save(tasks.getTasks());
                    return "Got it. I've added this task:\n  "
                            + todo + "\nNow you have "
                            + tasks.size() + " tasks in the list.";

                case "deadline": {
                    String[] parts = argsStr.split(" /by ", 2);
                    if (parts.length < 2) {
                        throw new TomException("Deadline must have /by YYYY-MM-DD");
                    }
                    assert parts.length == 2
                            : "split with limit=2 should produce exactly 2 parts";

                    String desc = parts[0].trim();
                    String dateStr = parts[1].trim();

                    try {
                        LocalDate by = LocalDate.parse(dateStr);
                        Task d = new Deadline(desc, by);
                        tasks.add(d);
                        storage.save(tasks.getTasks());

                        return "Got it. I've added this task:\n  "
                                + d + "\nNow you have "
                                + tasks.size() + " tasks in the list.";

                    } catch (DateTimeParseException e) {
                        throw new TomException("Date must be in YYYY-MM-DD format.");
                    }
                }

                case "event": {
                    String[] parts = argsStr.split(" /from | /to ", 3);
                    if (parts.length < 3) {
                        throw new TomException("Event must have /from and /to.");
                    }
                    assert parts.length == 3
                            : "split with limit=3 should produce exactly 3 parts";

                    Task e = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(e);
                    storage.save(tasks.getTasks());

                    return "Got it. I've added this task:\n  "
                            + e + "\nNow you have "
                            + tasks.size() + " tasks in the list.";
                }

                case "delete": {
                    int index = Integer.parseInt(argsStr) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        return "Invalid task index.";
                    }
                    assert index >= 0 && index < tasks.size()
                            : "index out of range after validation";

                    Task removed = tasks.remove(index);
                    storage.save(tasks.getTasks());

                    return "Noted. I've removed this task:\n  "
                            + removed + "\nNow you have "
                            + tasks.size() + " tasks in the list.";
                }

                case "find":
                    if (argsStr.isEmpty()) {
                        throw new TomException("Keyword to find cannot be empty.");
                    }

                    ArrayList<Task> results = tasks.find(argsStr);
                    assert results != null : "find should not return null";

                    if (results.isEmpty()) {
                        return "No matching tasks found.";
                    }

                    StringBuilder findResult =
                            new StringBuilder("Here are the matching tasks in your list:\n");

                    for (int i = 0; i < results.size(); i++) {
                        findResult.append((i + 1))
                                .append(". ")
                                .append(results.get(i))
                                .append("\n");
                    }

                    return findResult.toString().trim();

                default:
                    assert false : "Unexpected command word: " + command;
                    throw new TomException("I don't know what that command means.");
            }

        } catch (TomException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Please enter a valid number.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Invalid command format.";
        }
    }
}
