package tom;

/**
 * Represents a task with a description and a completion status.
 * <p>
 * A task can be marked as done or not done. Subclasses specify the
 * concrete type of the task (e.g., Todo, Deadline, Event).
 */
public abstract class Task {

    private String description;
    private boolean status;

    /**
     * Creates a task with the given description.
     * The task is initially marked as not done.
     *
     * @param description Description of the task.
     */
    public Task(String description){
        this.description = description;
        this.status = false;
    }

    /**
     * Marks this task as done.
     */
    public void mark(){
        status = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark(){
        status = false;
    }

    /**
     * Returns the status icon of this task.
     *
     * @return "X" if the task is done; otherwise a single space.
     */
    public String getStatus(){
        if(status) {
            return "X";
        } else {
            return " ";
        }
    }

    /**
     * Returns the description of this task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return true if the task is done, false otherwise.
     */
    public boolean isDone() {
        return status;
    }

    /**
     * Returns the type code of this task.
     * Subclasses provide the concrete type code.
     *
     * @return Task type code.
     */
    public abstract String getType();

    /**
     * Returns a user-facing string representation of this task.
     *
     * @return Formatted task string.
     */
    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatus() + "] " + description;
    }
}
