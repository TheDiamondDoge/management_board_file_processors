package exceptions;

public class WrongFileFormat extends Exception {
    public static final String DEFAULT_MESSAGE = "Wrong file format";

    public WrongFileFormat() {
        super(DEFAULT_MESSAGE);
    }
}
