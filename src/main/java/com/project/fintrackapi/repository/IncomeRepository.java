package com.project.fintrackapi.repository;

import com.project.fintrackapi.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByAccountId(Long id);
}
