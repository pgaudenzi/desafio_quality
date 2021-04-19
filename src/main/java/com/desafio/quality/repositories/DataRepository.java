package com.desafio.quality.repositories;

import java.util.List;

public interface DataRepository {

    List<String[]> loadDatabase(String filePath);

}
