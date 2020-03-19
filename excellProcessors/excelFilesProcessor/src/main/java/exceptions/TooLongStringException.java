package exceptions;

public class TooLongStringException extends Exception {
    private static final String DEFAULT_MESSAGE = "String is too long";

    public TooLongStringException() {
        super(DEFAULT_MESSAGE);
    }

    public TooLongStringException(String message) {
        super(message);
    }
}
