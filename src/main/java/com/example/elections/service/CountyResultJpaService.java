package com.example.elections.service;

import com.example.elections.model.CountyResult;
import com.example.elections.repository.CountyResultJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Profile("JpaEngine")
public class CountyResultJpaService implements CountyResultUseCases {

    private final CountyResultJpaRepository repository;

    public CountyResultJpaService(CountyResultJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<CountyResult> listAll() {
        return repository.findAll();
    }

    @Override
    public CountyResult getByFips(String countyFips) {
        return repository.findById(countyFips)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Result not found"
                ));
    }

    @Override
    public CountyResult create(CountyResult result) {
        if (repository.existsById(result.getCountyFips())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Result already exists");
        }
        return repository.save(result);
    }

    @Override
    public void replace(CountyResult result) {
        if (!repository.existsById(result.getCountyFips())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found");
        }
        repository.save(result);
    }

    @Override
    public void remove(String countyFips) {
        if (!repository.existsById(countyFips)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found");
        }
        repository.deleteById(countyFips);
    }
}
