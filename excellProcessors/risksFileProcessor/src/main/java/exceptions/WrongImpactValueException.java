package exceptions;

public class WrongImpactValueException extends Exception {
    private static final String DEFAULT_MESSAGE = "Impact' field value should be '1-5' and cannot be empty";

    public WrongImpactValueException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongImpactValueException(String message) {
        super(message);
    }
}
