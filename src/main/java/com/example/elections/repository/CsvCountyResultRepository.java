package com.example.elections.repository;

import com.example.elections.model.CountyResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

@Repository("CsvRepository")
@Profile({"CsvEngine", "JdbcEngine"})
public class CsvCountyResultRepository implements CountyResultRepository {

    private static final String CSV_RESOURCE =
            "classpath:2024_US_County_Level_Presidential_Results.csv";

    private final Map<String, CountyResult> storage = new ConcurrentHashMap<>();

    public CsvCountyResultRepository(ResourceLoader resourceLoader) {
        loadCsv(resourceLoader);
    }

    @Override
    public List<CountyResult> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<CountyResult> findByFips(String countyFips) {
        return Optional.ofNullable(storage.get(countyFips));
    }

    @Override
    public CountyResult save(CountyResult result) {
        if (result == null || result.getCountyFips() == null) {
            throw new IllegalArgumentException("countyFips is required");
        }
        storage.put(result.getCountyFips(), result);
        return result;
    }

    @Override
    public void deleteByFips(String countyFips) {
        storage.remove(countyFips);
    }

    @Override
    public boolean existsByFips(String countyFips) {
        return storage.containsKey(countyFips);
    }

    private void loadCsv(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource(CSV_RESOURCE);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine(); // header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                CountyResult result = parseLine(line);
                if (result != null) {
                    storage.put(result.getCountyFips(), result);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load CSV data", e);
        }
    }

    private CountyResult parseLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 10) {
            return null;
        }
        return new CountyResult(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parseLong(parts[3]),
                parseLong(parts[4]),
                parseLong(parts[5]),
                parseDouble(parts[6]),
                parseDouble(parts[7]),
                parseDouble(parts[8]),
                parseDouble(parts[9])
        );
    }

    private long parseLong(String value) {
        return Long.parseLong(value.trim());
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }
}
