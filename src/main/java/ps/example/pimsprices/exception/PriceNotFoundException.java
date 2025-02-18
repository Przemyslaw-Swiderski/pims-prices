package ps.example.pimsprices.exception;

/**
 * Custom exception thrown when a requested Price entity is not found.
 */
public class PriceNotFoundException extends RuntimeException {
    /**
     * Constructs a new PriceNotFoundException with the specified detail message.
     * @param message the detail message
     */
    public PriceNotFoundException(String message) {
        super(message);
    }
}

