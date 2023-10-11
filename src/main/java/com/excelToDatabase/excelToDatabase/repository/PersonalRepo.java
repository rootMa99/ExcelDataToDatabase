package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepo extends JpaRepository<Personal, Integer> {
}
