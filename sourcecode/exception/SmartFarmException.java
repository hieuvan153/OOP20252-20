package exception;

/**
 * Base unchecked exception for the Smart Farm domain.
 *
 * <p>Used to signal <b>programming / invariant violations</b> — a {@code null}
 * dependency injected into a constructor, an out-of-range coordinate, a corrupt
 * catalog entry — i.e. situations the caller is not expected to recover from at
 * runtime.</p>
 *
 * <p>It is deliberately <em>not</em> used for ordinary gameplay outcomes such as
 * "not enough money" or "can't plant here". Those are expected control flow and
 * stay as {@code boolean} return values plus a human-readable message (see
 * {@code ShopController.getLastMessage()}), so the UI can react without
 * exception handling on the hot path.</p>
 */
public class SmartFarmException extends RuntimeException {

    public SmartFarmException(String message) {
        super(message);
    }

    public SmartFarmException(String message, Throwable cause) {
        super(message, cause);
    }
}
