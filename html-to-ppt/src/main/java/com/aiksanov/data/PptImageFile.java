package com.aiksanov.data;

public class PptImageFile {
    private String filename;
    private byte[] imageInBytes;

    public PptImageFile() {
    }

    public PptImageFile(String filename, byte[] imageInBytes) {
        this.filename = filename;
        this.imageInBytes = imageInBytes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getImageInBytes() {
        return imageInBytes;
    }

    public void setImageInBytes(byte[] imageInBytes) {
        this.imageInBytes = imageInBytes;
    }
}