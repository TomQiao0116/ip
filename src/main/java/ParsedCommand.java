public class ParsedCommand {
    private final String commandWord;
    private final String arguments;

    public ParsedCommand(String commandWord, String arguments) {
        this.commandWord = commandWord;
        this.arguments = arguments;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getArguments() {
        return arguments;
    }
}
