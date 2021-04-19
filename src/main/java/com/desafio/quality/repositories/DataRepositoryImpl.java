package com.desafio.quality.repositories;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepositoryImpl implements DataRepository {

    private static final String ABS_PATH = new File("").getAbsolutePath();

    @Override
    public List<String[]> loadDatabase(final String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader(ABS_PATH + filePath))
                .withSkipLines(1).build()){

            String[] row;
            while ((row = reader.readNext()) != null) {
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }
}
