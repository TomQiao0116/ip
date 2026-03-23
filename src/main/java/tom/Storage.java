package tom;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving tasks to a local file for persistence.
 * <p>
 * Tasks are stored in a plain text format, one task per line.
 * This class is responsible for converting between {@link Task} objects and their
 * serialized representation in the storage file.
 */
public class Storage {

    private static final Path DIR = Paths.get("data");
    private static final Path FILE = DIR.resolve("tom.Tom.txt");

    /**
     * Loads tasks from the storage file.
     * If the file does not exist or cannot be read, an empty list will be returned.
     *
     * @return A list of tasks loaded from storage.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(FILE)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(FILE);
            for (String line : lines) {
                Task t = parseLine(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            // Returns whatever has been loaded so far (or empty list) if reading fails.
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     * If the data directory does not exist, it will be created automatically.
     *
     * @param tasks List of tasks to be saved.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(DIR);

            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(serialize(t));
            }

            Files.write(FILE, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            // Saving failed; the program continues without crashing.
        }
    }

    /**
     * Converts a task into a single-line string representation for storage.
     *
     * @param t Task to be serialized.
     * @return Serialized task line.
     */
    private String serialize(Task t) {
        int done = t.isDone() ? 1 : 0;

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDescription();
        }
        if (t instanceof Deadline d) {
            return "D | " + done + " | " + d.getDescription() + " | " + d.getDeadline();
        }
        if (t instanceof Event e) {
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        }

        // Falls back to saving as a todo if the type is unrecognized.
        return "T | " + done + " | " + t.getDescription();
    }

    /**
     * Parses a single line from storage into a {@link Task}.
     * Returns {@code null} if the line is invalid or cannot be parsed.
     *
     * @param line One line from the storage file.
     * @return Parsed task, or {@code null} if parsing fails.
     */
    private Task parseLine(String line) {
        if (isBlankLine(line)) {
            return null;
        }

        String[] parts = splitLine(line);
        if (!isValidParts(parts)) {
            return null;
        }

        Task task = createTask(parts);
        if (task == null) {
            return null;
        }

        markIfDone(task, parts);

        return task;
    }

    /**
     * Checks if a line is null or empty.
     */
    private boolean isBlankLine(String line) {
        return line == null || line.trim().isEmpty();
    }

    /**
     * Splits a line into parts using the delimiter.
     */
    private String[] splitLine(String line) {
        return line.split(" \\| ");
    }

    /**
     * Checks if the parsed parts array is valid.
     */
    private boolean isValidParts(String[] parts) {
        return parts.length >= 3;
    }

    /**
     * Creates a task object based on the parsed parts.
     */
    private Task createTask(String[] parts) {
        String type = parts[0];
        String desc = parts[2];

        switch (type) {
            case "T":
                return new Todo(desc);
            case "D":
                return createDeadline(parts, desc);
            case "E":
                return createEvent(parts, desc);
            default:
                return null;
        }
    }

    /**
     * Creates a deadline task from parsed parts.
     */
    private Task createDeadline(String[] parts, String desc) {
        if (parts.length < 4) {
            return null;
        }
        LocalDate by = LocalDate.parse(parts[3]);
        return new Deadline(desc, by);
    }

    /**
     * Creates an event task from parsed parts.
     */
    private Task createEvent(String[] parts, String desc) {
        if (parts.length < 5) {
            return null;
        }
        return new Event(desc, parts[3], parts[4]);
    }

    /**
     * Marks the task as done if indicated in the parsed parts.
     */
    private void markIfDone(Task task, String[] parts) {
        boolean done = "1".equals(parts[1]);
        if (done) {
            task.mark();
        }
    }
}