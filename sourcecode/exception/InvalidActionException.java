package exception;

/**
 * Thrown when a game action is invoked with arguments that violate a documented
 * pre-condition (for example a factory that returns {@code null}, or a tool
 * handed a malformed target). Distinct from an ordinary "the move did nothing"
 * result, which callers signal with a {@code boolean}.
 */
public class InvalidActionException extends SmartFarmException {

    public InvalidActionException(String message) {
        super(message);
    }

    public InvalidActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
