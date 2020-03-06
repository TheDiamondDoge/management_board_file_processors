public class Error {
    private int cellIndex;
    private int rowIndex;
    private String message;

    public Error() {
    }

    public Error(int cellIndex, int rowIndex, String message) {
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
        this.message = message;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
