package com.coffeecode.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coffeecode.model.FileModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class CSVFileService implements FileReaderService {

    private static final Logger LOGGER = LogManager.getLogger(CSVFileService.class);

    @Override
    public FileModel loadFile(String filePath, String encoding, String delimiter, boolean hasHeader)
            throws IOException, CsvException {
        List<String[]> data = new ArrayList<>();
        List<String> headers = null;

        LOGGER.info("Loading file: {}", filePath);

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(delimiter.charAt(0))
                        .build())
                .build()) {

            // Read all rows
            List<String[]> allRows = reader.readAll();

            // Remove empty rows
            allRows.removeIf(row -> Arrays.stream(row).allMatch(String::isEmpty));

            // Check for headers
            if (hasHeader && !allRows.isEmpty()) {
                headers = Arrays.asList(allRows.get(0)); // First row as header
                allRows.remove(0); // Remove header row
            }

            data.addAll(allRows);
        } catch (IOException | CsvException e) {
            LOGGER.error("Error loading file: {}", filePath, e);
            throw new IOException("Failed to load file at path: " + filePath, e);
        }

        LOGGER.info("File loaded successfully: {}", filePath);
        return new FileModel(filePath, headers, data);
    }
}
