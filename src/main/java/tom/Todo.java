package tom;

/**
 * Represents a todo task.
 * A todo task has a description and no associated date or time.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type code of this task.
     *
     * @return "T" for todo.
     */
    @Override
    public String getType() {
        return "T";
    }
}
