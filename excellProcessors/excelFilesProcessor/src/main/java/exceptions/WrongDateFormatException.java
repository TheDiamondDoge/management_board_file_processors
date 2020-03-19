package exceptions;

public class WrongDateFormatException extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong date format";

    public WrongDateFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongDateFormatException(String message) {
        super(message);
    }
}
