package com.example.elections.controller;

import com.example.elections.model.CountyResult;
import com.example.elections.service.CountyResultUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/counties")
public class CountyResultController {

    private final CountyResultUseCases service;

    public CountyResultController(CountyResultUseCases service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<CountyResult> list() {
        return service.listAll();
    }

    @GetMapping("/{fips}")
    public CountyResult get(@PathVariable String fips) {
        return service.getByFips(fips);
    }

    @PostMapping
    public ResponseEntity<CountyResult> create(@RequestBody CountyResult result) {
        CountyResult created = service.create(result);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    public void replace(@RequestBody CountyResult result) {
        service.replace(result);
    }

    @DeleteMapping("/{fips}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String fips) {
        service.remove(fips);
    }
}
