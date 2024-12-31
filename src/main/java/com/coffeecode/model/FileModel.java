package com.coffeecode.model;

import java.util.List;

public class FileModel {
    private String fileName;
    private List<String> headers; // Simpan header terpisah
    private List<String[]> data; // Simpan data

    // Constructor
    public FileModel(String fileName, List<String> headers, List<String[]> data) {
        this.fileName = fileName;
        this.headers = headers;
        this.data = data;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    // Utility Method: Get specific row
    public String[] getRow(int index) {
        return data.get(index);
    }

    // Utility Method: Get specific column by index
    public String getCell(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    // Utility Method: Get column by header name
    public int getColumnIndex(String headerName) {
        if (headers != null && headers.contains(headerName)) {
            return headers.indexOf(headerName);
        }
        return -1; // Header not found
    }
}
