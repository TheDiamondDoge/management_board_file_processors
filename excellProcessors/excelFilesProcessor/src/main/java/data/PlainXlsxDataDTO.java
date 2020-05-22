package data;

public class PlainXlsxDataDTO {
    private String[] header;
    private String[] data;

    public PlainXlsxDataDTO(String[] header, String[] data) {
        this.header = header;
        this.data = data;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
