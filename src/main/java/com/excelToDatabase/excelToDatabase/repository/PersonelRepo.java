package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonelRepo extends JpaRepository<Personel, Long> {
    Personel findByMatricule(long matricule);
}
