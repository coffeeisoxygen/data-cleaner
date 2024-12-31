package com.coffeecode.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.coffeecode.model.FileModel;
import com.opencsv.exceptions.CsvException;

public class ExcelFileService implements FileReaderService {

    private static final Logger LOGGER = LogManager.getLogger(ExcelFileService.class);

    @Override
    public FileModel loadFile(String filePath, String encoding, String delimiter, boolean hasHeader)
            throws IOException, CsvException {
        List<String[]> data = new ArrayList<>();
        List<String> headers = null;

        LOGGER.info("Loading file: {}", filePath);

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
        } catch (IOException e) {
            LOGGER.error("Error loading file: {}", filePath, e);
            throw new IOException("Failed to load file at path: " + filePath, e);
        }

        LOGGER.info("File loaded successfully: {}", filePath);
        return new FileModel(filePath, headers, data);
    }
}