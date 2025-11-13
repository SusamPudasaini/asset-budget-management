package com.project.budget.repository;

import com.project.budget.entity.FiscalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface FiscalRepository extends JpaRepository<FiscalEntity, Long> {

    // Find the fiscal year that contains a specific date
    @Query("SELECT f FROM FiscalEntity f WHERE :currentDate BETWEEN f.startDate AND f.endDate")
    FiscalEntity findByDate(@Param("currentDate") LocalDate currentDate);

    // Latest fiscal year by ID
    FiscalEntity findTopByOrderByFiscalIdDesc();

    // Last 5 fiscals
    List<FiscalEntity> findTop5ByOrderByStartDateDesc();

    // All fiscals ordered by start date descending
    List<FiscalEntity> findAllByOrderByStartDateDesc();
}
