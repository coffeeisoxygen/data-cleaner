package com.coffeecode.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.coffeecode.model.FileModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class CSVFileService implements FileReaderService {

    @Override
    public FileModel loadFile(String filePath, String encoding, String delimiter, boolean hasHeader) throws IOException, CsvException {
        List<String[]> data = new ArrayList<>();
        List<String> headers = null;

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
        }

        return new FileModel(filePath, encoding, hasHeader, delimiter, headers, data);
    }
}
