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
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        Task t;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) return null;
                LocalDate by = LocalDate.parse(parts[3]);
                t = new Deadline(desc, by);
                break;
            case "E":
                if (parts.length < 5) return null;
                t = new Event(desc, parts[3], parts[4]);
                break;
            default:
                return null;
        }

        if (done) {
            t.mark();
        }
        return t;
    }
}
