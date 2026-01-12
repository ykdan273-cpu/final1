package com.example.elections.service;

import com.example.elections.model.CountyResult;
import com.example.elections.repository.CountyResultRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Profile("CsvEngine")
public class CountyResultService implements CountyResultUseCases {

    private final CountyResultRepository repository;

    public CountyResultService(CountyResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<CountyResult> listAll() {
        return repository.findAll();
    }

    @Override
    public CountyResult getByFips(String countyFips) {
        return repository.findByFips(countyFips)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Result not found"
                ));
    }

    @Override
    public CountyResult create(CountyResult result) {
        if (repository.existsByFips(result.getCountyFips())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Result already exists");
        }
        return repository.save(result);
    }

    @Override
    public void replace(CountyResult result) {
        if (!repository.existsByFips(result.getCountyFips())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found");
        }
        repository.save(result);
    }

    @Override
    public void remove(String countyFips) {
        if (!repository.existsByFips(countyFips)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found");
        }
        repository.deleteByFips(countyFips);
    }
}
