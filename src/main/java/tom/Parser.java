package tom;

public class Parser {

    public static ParsedCommand parse(String userInput) throws TomException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new TomException("Command cannot be empty.");
        }

        String trimmed = userInput.trim();

        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        return new ParsedCommand(commandWord, arguments);
    }
}
