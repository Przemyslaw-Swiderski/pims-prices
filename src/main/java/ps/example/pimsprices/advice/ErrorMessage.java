package ps.example.pimsprices.advice;

import lombok.Data;

import java.util.Date;

/**
 * Standardized error response model used in exception handling.
 */
@Data
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    /**
     * Constructs an ErrorMessage instance with given details.
     *
     * @param statusCode  HTTP status code
     * @param timestamp   Timestamp of the error occurrence
     * @param message     Error message
     * @param description Additional details about the error
     */
    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}