package com.example.elections.service;

import com.example.elections.model.CountyResult;
import com.example.elections.repository.CountyResultRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Profile({"CsvEngine", "JdbcEngine"})
public class CountyResultService implements CountyResultUseCases {

    private final CountyResultRepository repository;

    public CountyResultService(CountyResultRepository repository,
                               @Qualifier("CsvRepository") CountyResultRepository csvRepository) {
        this.repository = repository;
        init(csvRepository);
    }

    private void init(CountyResultRepository csvRepository) {
        if (csvRepository.getClass().equals(repository.getClass())) {
            return;
        }
        List<CountyResult> csvData = csvRepository.findAll();
        if (!csvData.isEmpty() && repository.findAll().isEmpty()) {
            csvData.forEach(repository::save);
        }
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
