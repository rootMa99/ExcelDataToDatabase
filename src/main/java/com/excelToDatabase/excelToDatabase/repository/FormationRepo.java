package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FormationRepo extends JpaRepository<Formation, Long> {
    Formation findByFormationId(String formationId);
    Formation save(Formation formation);
    List<Formation> findAllFormationByDateDebutBetween(Date startDate, Date endDate);
    List<Formation> findAllByType(String type);
    List<Formation> findAllByCategorieFormation(String categorie);
}
