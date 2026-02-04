package tom;

/**
 * Represents an event task with a start time and an end time.
 * An event task has a description and occurs between two specified time points.
 */
public class Event extends Task {

    private String from;
    private String to;

    /**
     * Creates an event task with the given description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from        Start time of the event.
     * @param to          End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return Start time.
     */
    public String getFrom(){
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return End time.
     */
    public String getTo(){
        return to;
    }

    /**
     * Returns the type code of this task.
     *
     * @return "E" for event.
     */
    @Override
    public String getType() {
        return "E";
    }

    /**
     * Returns a user-facing string representation of this event task.
     *
     * @return Formatted event task string.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
