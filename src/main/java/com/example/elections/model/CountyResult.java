package com.example.elections.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "county_result")
public class CountyResult {
    @Column(name = "state_name", length = 100)
    private String stateName;

    @Id
    @Column(name = "county_fips", length = 10)
    private String countyFips;

    @Column(name = "county_name", length = 100)
    private String countyName;

    @Column(name = "votes_gop")
    private long votesGop;

    @Column(name = "votes_dem")
    private long votesDem;

    @Column(name = "total_votes")
    private long totalVotes;

    @Column(name = "diff")
    private double diff;

    @Column(name = "per_gop")
    private double perGop;

    @Column(name = "per_dem")
    private double perDem;

    @Column(name = "per_point_diff")
    private double perPointDiff;
}
