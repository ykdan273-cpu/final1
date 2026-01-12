package com.example.elections.controller;

import com.example.elections.model.CountyResult;
import com.example.elections.service.CountyResultService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counties")
public class CountyResultController {

    private final CountyResultService service;

    public CountyResultController(CountyResultService service) {
        this.service = service;
    }

    @GetMapping
    public List<CountyResult> findAll() {
        return service.findAll();
    }

    @GetMapping("/{fips}")
    public ResponseEntity<CountyResult> findByFips(@PathVariable String fips) {
        Optional<CountyResult> result = service.findByFips(fips);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CountyResult> create(@RequestBody CountyResult result) {
        return ResponseEntity.ok(service.create(result));
    }

    @PutMapping("/{fips}")
    public ResponseEntity<CountyResult> update(@PathVariable String fips,
                                               @RequestBody CountyResult result) {
        Optional<CountyResult> updated = service.update(fips, result);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{fips}")
    public ResponseEntity<Void> delete(@PathVariable String fips) {
        if (!service.delete(fips)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
