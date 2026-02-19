package tom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents a list of tasks.
 * Provides basic operations to add, retrieve, remove, and query tasks in the list.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with an existing list of tasks.
     *
     * @param tasks Initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task Task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index Index of the task to retrieve.
     * @return The task at the given index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     * This is mainly used for persistence purposes.
     *
     * @return List of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword Keyword to search for.
     * @return A list of matching tasks.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Sorts tasks by type and relevant fields.
     * Order:
     * 1. Deadline (by date)
     * 2. Event (by from)
     * 3. Todo (by description)
     */
    public void sort() {
        Collections.sort(tasks, taskComparator());
    }

    private Comparator<Task> taskComparator() {
        return (a, b) -> {
            int rankA = typeRank(a);
            int rankB = typeRank(b);

            if (rankA != rankB) {
                return Integer.compare(rankA, rankB);
            }

            // Same type: compare within type
            if (a instanceof Deadline && b instanceof Deadline) {
                Deadline da = (Deadline) a;
                Deadline db = (Deadline) b;
                return da.getDeadline().compareTo(db.getDeadline());
            }

            if (a instanceof Event && b instanceof Event) {
                Event ea = (Event) a;
                Event eb = (Event) b;
                return ea.getFrom().compareToIgnoreCase(eb.getFrom());
            }

            // Todo or fallback: compare by description
            return a.getDescription().compareToIgnoreCase(b.getDescription());
        };
    }

    private int typeRank(Task task) {
        if (task instanceof Deadline) {
            return 0;
        }
        if (task instanceof Event) {
            return 1;
        }
        if (task instanceof Todo) {
            return 2;
        }
        return 3;
    }
}
