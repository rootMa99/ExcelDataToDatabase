package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Personel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PersonelRepo extends JpaRepository<Personel, Long> {
    Personel findByMatricule(long matricule);
    List<Personel> findByCategorieAndFormationsDateDebutBetweenAndFormationsTypeNot
            (String personelCategorie, Date startDate, Date endDate, String type);
    List<Personel> findByFormationsDateDebutBetweenAndFormationsTypeNot
            (Date startDate, Date endDate, String type);
}
