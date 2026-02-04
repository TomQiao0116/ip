package tom;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a deadline task with a due date.
 * A deadline task has a description and must be completed by a specific date.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private LocalDate deadline;

    /**
     * Creates a deadline task with the given description and due date.
     *
     * @param description Description of the task.
     * @param deadline    Due date of the task.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Creates a deadline task using a date string.
     * The date string is expected to be in {@code yyyy-MM-dd} format.
     *
     * @param description Description of the task.
     * @param deadlineStr Due date as a string.
     */
    public Deadline(String description, String deadlineStr) {
        this(description, LocalDate.parse(deadlineStr));
    }

    /**
     * Returns the due date of this deadline task.
     *
     * @return Due date.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Returns the type code of this task.
     *
     * @return "D" for deadline.
     */
    @Override
    public String getType() {
        return "D";
    }

    /**
     * Returns a user-facing string representation of this deadline task.
     *
     * @return Formatted deadline task string.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline.format(OUTPUT_FORMAT) + ")";
    }
}
