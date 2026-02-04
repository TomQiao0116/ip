package tom;

/**
 * Represents a parsed user command.
 * <p>
 * A parsed command consists of a command word and the remaining arguments
 * provided by the user.
 */
public class ParsedCommand {

    private final String commandWord;
    private final String arguments;

    /**
     * Creates a parsed command with the given command word and arguments.
     *
     * @param commandWord The main command keyword.
     * @param arguments   The arguments associated with the command.
     */
    public ParsedCommand(String commandWord, String arguments) {
        this.commandWord = commandWord;
        this.arguments = arguments;
    }

    /**
     * Returns the command word.
     *
     * @return Command keyword.
     */
    public String getCommandWord() {
        return commandWord;
    }

    /**
     * Returns the arguments of the command.
     *
     * @return Command arguments.
     */
    public String getArguments() {
        return arguments;
    }
}
