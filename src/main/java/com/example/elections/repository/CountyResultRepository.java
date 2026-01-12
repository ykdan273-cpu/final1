package com.example.elections.repository;

import com.example.elections.model.CountyResult;
import java.util.List;
import java.util.Optional;

public interface CountyResultRepository {
    List<CountyResult> findAll();

    Optional<CountyResult> findByFips(String countyFips);

    CountyResult save(CountyResult result);

    void deleteByFips(String countyFips);

    boolean existsByFips(String countyFips);
}
