package exceptions;

public class WrongFileFormatException extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong file format";

    public WrongFileFormatException() {
        super(DEFAULT_MESSAGE);
    }
}
