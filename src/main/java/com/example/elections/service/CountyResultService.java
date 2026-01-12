package com.example.elections.service;

import com.example.elections.model.CountyResult;
import com.example.elections.repository.CountyResultRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CountyResultService {

    private final CountyResultRepository repository;

    public CountyResultService(CountyResultRepository repository) {
        this.repository = repository;
    }

    public List<CountyResult> findAll() {
        return repository.findAll();
    }

    public Optional<CountyResult> findByFips(String countyFips) {
        return repository.findByFips(countyFips);
    }

    public CountyResult create(CountyResult result) {
        return repository.save(result);
    }

    public Optional<CountyResult> update(String countyFips, CountyResult result) {
        if (!repository.existsByFips(countyFips)) {
            return Optional.empty();
        }
        result.setCountyFips(countyFips);
        return Optional.of(repository.save(result));
    }

    public boolean delete(String countyFips) {
        if (!repository.existsByFips(countyFips)) {
            return false;
        }
        repository.deleteByFips(countyFips);
        return true;
    }
}
