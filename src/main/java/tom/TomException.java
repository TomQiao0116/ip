package tom;

/**
 * Represents an exception specific to the Tom application.
 * <p>
 * This exception is thrown when a user command is invalid or cannot be processed.
 */
public class TomException extends Exception {

    /**
     * Creates a TomException with the specified detail message.
     *
     * @param message Detail message explaining the cause of the exception.
     */
    public TomException(String message) {
        super(message);
    }
}
