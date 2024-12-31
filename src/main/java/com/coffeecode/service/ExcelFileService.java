package com.coffeecode.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.coffeecode.model.FileModel;
import com.opencsv.exceptions.CsvException;

public class ExcelFileService implements FileReaderService {

    @Override
    public FileModel loadFile(String filePath, String encoding, String delimiter, boolean hasHeader)
            throws IOException, CsvException {
        List<String[]> data = new ArrayList<>();
        List<String> headers = null;

        try (FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = hasHeader;

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(cell.toString());
                }

                if (isHeader) {
                    headers = rowData;
                    isHeader = false;
                } else {
                    data.add(rowData.toArray(String[]::new));
                }
            }
        }

        return new FileModel(filePath, encoding, hasHeader, delimiter, headers, data);
    }
}
