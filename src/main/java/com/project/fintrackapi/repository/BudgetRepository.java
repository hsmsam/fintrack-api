package com.project.fintrackapi.repository;

import com.project.fintrackapi.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByAccountId(Long id);
}
