import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final Path DIR = Paths.get("data");
    private static final Path FILE = DIR.resolve("duke.txt");

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
        //不存在这个文件
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(DIR);

            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(serialize(t));
            }

            Files.write(FILE, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            //cannot save
        }
    }

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
        // 不认识的类型就丢掉（或写默认）
        return "T | " + done + " | " + t.getDescription();
    }

    private Task parseLine(String line) {
        // 容错：防止空行/坏行（stretch 可更强，这里先做到不 crash）
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        // parts[0]=type, parts[1]=0/1, parts[2]=desc...
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
                t = new Deadline(desc, parts[3]);
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
