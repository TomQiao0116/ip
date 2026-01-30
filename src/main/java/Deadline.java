import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private LocalDate deadline;

    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    // 如果你还想保留 String 构造器（可选，方便调用）
    public Deadline(String description, String deadlineStr) {
        this(description, LocalDate.parse(deadlineStr)); // 默认接受 yyyy-MM-dd
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline.format(OUTPUT_FORMAT) + ")";
    }
}
