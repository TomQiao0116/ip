package tom;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Represents the main chatbot application.
 * Tom processes user commands, updates the task list,
 * and returns response messages for display in the GUI.
 */
public class Tom {
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Tom chatbot with tasks loaded from storage.
     */
    public Tom() {
        storage = new Storage();
        tasks = new TaskList(storage.load());
    }

    /**
     * Processes a single user input and returns the chatbot's response.
     * This method is used by the GUI.
     *
     * @param userInput The input entered by the user.
     * @return The chatbot's response as a String.
     */
    public String getResponse(String userInput) {
        assert userInput != null : "User input should not be null";

        try {
            ParsedCommand parsedCommand = Parser.parse(userInput);
            String commandWord = parsedCommand.getCommandWord();
            String arguments = parsedCommand.getArguments();

            return executeCommand(commandWord, arguments);
        } catch (TomException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Please enter a valid number.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Invalid command format.";
        }
    }

    /**
     * Executes the command based on the given command word and arguments.
     *
     * @param commandWord The command keyword.
     * @param arguments The arguments supplied with the command.
     * @return The chatbot's response after executing the command.
     * @throws TomException If the command is invalid or cannot be executed.
     */
    private String executeCommand(String commandWord, String arguments) throws TomException {
        switch (commandWord) {
            case "bye":
                return getByeMessage();
            case "list":
                return handleList();
            case "sort":
                return handleSort();
            case "mark":
                return handleMark(arguments);
            case "unmark":
                return handleUnmark(arguments);
            case "todo":
                return handleTodo(arguments);
            case "deadline":
                return handleDeadline(arguments);
            case "event":
                return handleEvent(arguments);
            case "delete":
                return handleDelete(arguments);
            case "find":
                return handleFind(arguments);
            default:
                throw new TomException("I don't know what that means.");
        }
    }

    /**
     * Returns the farewell message.
     *
     * @return Farewell message.
     */
    private String getByeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns all tasks currently in the task list.
     *
     * @return Formatted task list.
     */
    private String handleList() {
        if (tasks.size() == 0) {
            return "Your list is empty.";
        }

        return formatTaskList("Here are the tasks in your list:\n", tasks.getTasks());
    }

    /**
     * Sorts the tasks and returns the updated task list.
     *
     * @return Confirmation message together with the sorted task list.
     */
    private String handleSort() {
        tasks.sort();
        saveTasks();

        if (tasks.size() == 0) {
            return "Sorted your tasks.\nYour list is empty.";
        }

        return formatTaskList("Sorted your tasks.\nHere are the tasks in your list:\n", tasks.getTasks());
    }

    /**
     * Marks a task as done.
     *
     * @param arguments The task number to mark.
     * @return Confirmation message after marking the task.
     */
    private String handleMark(String arguments) {
        int index = parseTaskIndex(arguments);
        Task task = tasks.get(index);
        task.mark();
        saveTasks();

        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Marks a task as not done.
     *
     * @param arguments The task number to unmark.
     * @return Confirmation message after unmarking the task.
     */
    private String handleUnmark(String arguments) {
        int index = parseTaskIndex(arguments);
        Task task = tasks.get(index);
        task.unmark();
        saveTasks();

        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Adds a todo task to the task list.
     *
     * @param arguments The description of the todo task.
     * @return Confirmation message after adding the task.
     * @throws TomException If the description is empty.
     */
    private String handleTodo(String arguments) throws TomException {
        if (arguments == null || arguments.trim().isEmpty()) {
            throw new TomException("The description of a todo cannot be empty.");
        }

        Task todo = new Todo(arguments.trim());
        tasks.add(todo);
        saveTasks();

        return "Got it. I've added this task:\n  " + todo
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param arguments The description and deadline of the task.
     * @return Confirmation message after adding the task.
     * @throws TomException If the input format is invalid.
     */
    private String handleDeadline(String arguments) throws TomException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new TomException("The correct format is: deadline <description> /by <date>");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        try {
            LocalDate date = LocalDate.parse(by);
            Task deadline = new Deadline(description, date);
            tasks.add(deadline);
            saveTasks();

            return "Got it. I've added this task:\n  " + deadline
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            throw new TomException("Date must be in YYYY-MM-DD format.");
        }
    }

    /**
     * Adds an event task to the task list.
     *
     * @param arguments The description, start time, and end time of the event.
     * @return Confirmation message after adding the task.
     * @throws TomException If the input format is invalid.
     */
    private String handleEvent(String arguments) throws TomException {
        String[] fromSplit = arguments.split(" /from ", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty()) {
            throw new TomException("The correct format is: event <description> /from <start> /to <end>");
        }

        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split(" /to ", 2);

        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new TomException("The correct format is: event <description> /from <start> /to <end>");
        }

        String from = toSplit[0].trim();
        String to = toSplit[1].trim();

        Task event = new Event(description, from, to);
        tasks.add(event);
        saveTasks();

        return "Got it. I've added this task:\n  " + event
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Deletes a task from the task list.
     *
     * @param arguments The task number to delete.
     * @return Confirmation message after deleting the task.
     */
    private String handleDelete(String arguments) {
        int index = parseTaskIndex(arguments);
        Task removedTask = tasks.remove(index);
        saveTasks();

        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param arguments The keyword to search for.
     * @return Matching tasks, or a message if none are found.
     * @throws TomException If the keyword is empty.
     */
    private String handleFind(String arguments) throws TomException {
        if (arguments == null || arguments.trim().isEmpty()) {
            throw new TomException("The keyword for find cannot be empty.");
        }

        ArrayList<Task> matchingTasks = tasks.find(arguments.trim());

        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }

        return formatTaskList("Here are the matching tasks in your list:\n", matchingTasks);
    }

    /**
     * Parses the task index from user input.
     *
     * @param arguments The user input representing a task number.
     * @return Zero-based task index.
     */
    private int parseTaskIndex(String arguments) {
        int index = Integer.parseInt(arguments.trim()) - 1;

        if (index < 0 || index >= tasks.size()) {
            throw new NumberFormatException();
        }

        return index;
    }

    /**
     * Saves the current task list to storage.
     */
    private void saveTasks() {
        storage.save(tasks.getTasks());
    }

    /**
     * Formats a list of tasks into a numbered string.
     *
     * @param header The message header.
     * @param taskList The list of tasks to format.
     * @return Formatted string representation of the task list.
     */
    private String formatTaskList(String header, ArrayList<Task> taskList) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(i + 1).append(". ").append(taskList.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}