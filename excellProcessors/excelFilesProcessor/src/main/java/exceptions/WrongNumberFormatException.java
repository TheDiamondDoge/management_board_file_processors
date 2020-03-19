package exceptions;

public class WrongNumberFormatException extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong number format";

    public WrongNumberFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongNumberFormatException(String message) {
        super(message);
    }
}
