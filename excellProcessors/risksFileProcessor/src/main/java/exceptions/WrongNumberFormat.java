package exceptions;

public class WrongNumberFormat extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong number format";

    public WrongNumberFormat() {
        super(DEFAULT_MESSAGE);
    }

    public WrongNumberFormat(String message) {
        super(message);
    }
}
