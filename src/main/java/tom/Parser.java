package tom;

/**
 * Parses raw user input into a structured command representation.
 * <p>
 * This class is responsible for splitting the user input into a command word
 * and its corresponding arguments.
 */
public class Parser {

    /**
     * Parses the given user input and returns a {@link ParsedCommand}.
     *
     * @param userInput Full command entered by the user.
     * @return A parsed command containing the command word and arguments.
     * @throws TomException If the input is empty or contains only whitespace.
     */
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
