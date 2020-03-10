package exceptions;

public class WrongDateFormat extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong date format";

    public WrongDateFormat() {
        super(DEFAULT_MESSAGE);
    }

    public WrongDateFormat(String message) {
        super(message);
    }
}
