package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepo extends JpaRepository<Formation, Long> {
    Formation findByFormationId(String formationId);
    Formation save(Formation formation);
}
