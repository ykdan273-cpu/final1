package com.example.elections.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountyResult {
    private String stateName;
    private String countyFips;
    private String countyName;
    private long votesGop;
    private long votesDem;
    private long totalVotes;
    private double diff;
    private double perGop;
    private double perDem;
    private double perPointDiff;
}
