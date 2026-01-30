package tom;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Tom {
    public static void main(String[] args) {
        String name = "tom";
        Ui ui = new Ui();

        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.load());

        ui.showWelcome(name);

        while (true) {
            try {
                String userInput = ui.readCommand();
                ParsedCommand pc = Parser.parse(userInput);

                String command = pc.getCommandWord();
                String argsStr = pc.getArguments();

                switch (command) {
                    case "bye":
                        ui.showGoodbye();
                        ui.close();
                        return;

                    case "list":
                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showMessage((i + 1) + ". " + tasks.get(i));
                        }
                        break;

                    case "mark": {
                        int index = Integer.parseInt(argsStr) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            tasks.get(index).mark();
                            ui.showMessage("Nice! I've marked this task as done:");
                            ui.showMessage("  " + tasks.get(index));
                            storage.save(tasks.getTasks());
                        }
                        break;
                    }

                    case "unmark": {
                        int index = Integer.parseInt(argsStr) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            tasks.get(index).unmark();
                            ui.showMessage("OK, I've marked this task as not done yet:");
                            ui.showMessage("  " + tasks.get(index));
                            storage.save(tasks.getTasks());
                        }
                        break;
                    }

                    case "todo":
                        if (argsStr.isEmpty()) {
                            throw new TomException("tom.Todo description cannot be empty.");
                        }
                        Task todo = new Todo(argsStr);
                        tasks.add(todo);
                        ui.showMessage("Got it. I've added this task:");
                        ui.showMessage("  " + todo);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                        storage.save(tasks.getTasks());
                        break;

                    case "deadline": {
                        String[] parts = argsStr.split(" /by ", 2);
                        if (parts.length < 2) {
                            throw new TomException("tom.Deadline must have /by YYYY-MM-DD");
                        }

                        String desc = parts[0].trim();
                        String dateStr = parts[1].trim();

                        try {
                            LocalDate by = LocalDate.parse(dateStr);
                            Task d = new Deadline(desc, by);
                            tasks.add(d);

                            ui.showMessage("Got it. I've added this task:");
                            ui.showMessage("  " + d);
                            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                            storage.save(tasks.getTasks());
                        } catch (DateTimeParseException e) {
                            throw new TomException("Date must be in YYYY-MM-DD format, e.g., 2019-10-15");
                        }
                        break;
                    }

                    case "event": {
                        // 维持你原来的逻辑：按 " /from | /to " 拆
                        String[] parts = argsStr.split(" /from | /to ", 3);
                        Task e = new Event(parts[0], parts[1], parts[2]);
                        tasks.add(e);

                        ui.showMessage("Got it. I've added this task:");
                        ui.showMessage("  " + e);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                        storage.save(tasks.getTasks());
                        break;
                    }

                    case "delete": {
                        int index = Integer.parseInt(argsStr) - 1;
                        if (index < 0 || index >= tasks.size()) {
                            break;
                        }
                        Task removed = tasks.remove(index);
                        ui.showMessage("Noted. I've removed this task:");
                        ui.showMessage("  " + removed);
                        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                        storage.save(tasks.getTasks());
                        break;
                    }

                    default:
                        throw new TomException("I don't know what that command means.");
                }

            } catch (TomException e) {
                ui.showMessage(" " + e.getMessage());
            } catch (NumberFormatException e) {
                // 保持你原来的“无效数字就不做事”的风格：这里给个更友好提示也行
                ui.showMessage(" Please enter a valid number.");
            } catch (ArrayIndexOutOfBoundsException e) {
                // event 参数不足时你原来会直接崩，这里保护一下
                ui.showMessage(" Invalid command format.");
            }
        }
    }
}
