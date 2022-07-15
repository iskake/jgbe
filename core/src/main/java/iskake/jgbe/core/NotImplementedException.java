package iskake.jgbe.core;

/**
 * The exception that is thrown when a requested method or operation is not implemented.
 * <p>
 * Should be thought as equivalent to the .NET {@code NotImplementedException}.
 */
public class NotImplementedException extends UnsupportedOperationException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
