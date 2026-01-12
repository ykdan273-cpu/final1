package com.example.elections.service;

import com.example.elections.model.CountyResult;

public interface CountyResultUseCases {

    Iterable<CountyResult> listAll();

    CountyResult getByFips(String countyFips);

    CountyResult create(CountyResult result);

    void replace(CountyResult result);

    void remove(String countyFips);
}
