package exceptions;

public class NoSheetFoundException extends Exception {
    public static final String DEFAULT_MESSAGE = "No sheet found";

    public NoSheetFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public NoSheetFoundException(String sheetName) {
        super("'" + sheetName + "' sheet not found");
    }
}
