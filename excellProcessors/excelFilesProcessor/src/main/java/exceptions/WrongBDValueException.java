package exceptions;

public class WrongBDValueException extends Exception {
    private static final String DEFAULT_MESSAGE = "File`s BD is different from project`s BD";

    public WrongBDValueException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongBDValueException(String message) {
        super(message);
    }
}
