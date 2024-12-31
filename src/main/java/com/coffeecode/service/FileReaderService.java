package com.coffeecode.service;

import java.io.IOException;

import com.coffeecode.model.FileModel;
import com.opencsv.exceptions.CsvException;

public interface FileReaderService {
    FileModel loadFile(String filePath, String encoding, String delimiter, boolean hasHeader)
            throws IOException, CsvException;
}