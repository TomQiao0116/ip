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
<<<<<<< branch-A-CodeQuality
                    return handleList();
=======
                    if (tasks.size() == 0) {
                        return "Your list is empty.";
                    }
                    StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
                    for (int i = 0; i < tasks.size(); i++) {
                        sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
                    }
                    return sb.toString().trim();

                case "sort":
                    tasks.sort();
                    storage.save(tasks.getTasks());
                    // Show the sorted list immediately
                    if (tasks.size() == 0) {
                        return "Sorted your tasks.\nYour list is empty.";
                    }
                    StringBuilder sorted = new StringBuilder("Sorted your tasks.\nHere are the tasks in your list:\n");
                    for (int i = 0; i < tasks.size(); i++) {
                        sorted.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
                    }
                    return sorted.toString().trim();

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
>>>>>>> master

                case "mark":
                    return handleMark(argsStr);

                case "unmark":
                    return handleUnmark(argsStr);

                case "todo":
                    return handleTodo(argsStr);

                case "deadline":
                    return handleDeadline(argsStr);

                case "event":
                    return handleEvent(argsStr);

                case "delete":
                    return handleDelete(argsStr);

                case "find":
                    return handleFind(argsStr);

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

    private String handleList() {
        if (tasks.size() == 0) {
            return "Your list is empty.";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String handleMark(String argsStr) {
        Integer index = parseValidIndex(argsStr);
        if (index == null) {
            return "Invalid task index.";
        }
        assert index >= 0 && index < tasks.size() : "index out of range after validation";

        tasks.get(index).mark();
        storage.save(tasks.getTasks());
        return "Nice! I've marked this task as done:\n  " + tasks.get(index);
    }

    private String handleUnmark(String argsStr) {
        Integer index = parseValidIndex(argsStr);
        if (index == null) {
            return "Invalid task index.";
        }
        assert index >= 0 && index < tasks.size() : "index out of range after validation";

        tasks.get(index).unmark();
        storage.save(tasks.getTasks());
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(index);
    }

    private String handleDelete(String argsStr) {
        Integer index = parseValidIndex(argsStr);
        if (index == null) {
            return "Invalid task index.";
        }
        assert index >= 0 && index < tasks.size() : "index out of range after validation";

        Task removed = tasks.remove(index);
        storage.save(tasks.getTasks());

        return "Noted. I've removed this task:\n  "
                + removed + "\nNow you have "
                + tasks.size() + " tasks in the list.";
    }

    private Integer parseValidIndex(String argsStr) {
        int index = Integer.parseInt(argsStr) - 1;
        if (index < 0 || index >= tasks.size()) {
            return null;
        }
        return index;
    }

    private String handleTodo(String argsStr) throws TomException {
        if (argsStr.isEmpty()) {
            throw new TomException("Todo description cannot be empty.");
        }
        return addTaskAndConfirm(new Todo(argsStr));
    }

    private String handleDeadline(String argsStr) throws TomException {
        String[] parts = argsStr.split(" /by ", 2);
        if (parts.length < 2) {
            throw new TomException("Deadline must have /by YYYY-MM-DD");
        }
        assert parts.length == 2 : "split with limit=2 should produce exactly 2 parts";

        String desc = parts[0].trim();
        String dateStr = parts[1].trim();

        try {
            LocalDate by = LocalDate.parse(dateStr);
            return addTaskAndConfirm(new Deadline(desc, by));
        } catch (DateTimeParseException e) {
            throw new TomException("Date must be in YYYY-MM-DD format.");
        }
    }

    private String handleEvent(String argsStr) throws TomException {
        String[] parts = splitEventArgs(argsStr);
        if (parts == null) {
            throw new TomException("Event must have /from and /to.");
        }
        assert parts.length == 3 : "event args should contain exactly 3 parts";
        return addTaskAndConfirm(new Event(parts[0], parts[1], parts[2]));
    }

    /**
     * Splits event arguments into description, from, and to.
     * Returns null if required delimiters are missing.
     */
    private String[] splitEventArgs(String argsStr) {
        String[] first = argsStr.split(" /from ", 2);
        if (first.length < 2) {
            return null;
        }
        String[] second = first[1].split(" /to ", 2);
        if (second.length < 2) {
            return null;
        }
        return new String[]{first[0], second[0], second[1]};
    }

    private String addTaskAndConfirm(Task task) {
        tasks.add(task);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n  "
                + task + "\nNow you have "
                + tasks.size() + " tasks in the list.";
    }

    private String handleFind(String argsStr) throws TomException {
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
    }
}
