package com.example.elections.repository;

import com.example.elections.model.CountyResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountyResultJpaRepository extends JpaRepository<CountyResult, String> {
}
