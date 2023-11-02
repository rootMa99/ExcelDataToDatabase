package com.excelToDatabase.excelToDatabase.repository;

import com.excelToDatabase.excelToDatabase.domain.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FormationRepo extends JpaRepository<Formation, Long> {
    Formation findByFormationId(String formationId);


    List<Formation> findAllFormationByDateDebutBetween(Date startDate, Date endDate);
    List<Formation> findAllByType(String type);
    List<Formation> findAllByCategorieFormation(String categorie);
    List<Formation> findAllByCategorieFormationAndTypeAndDateDebutBetween(String categorie, String type, Date startDate, Date endDate);
    List<Formation> findAllByTypeAndDateDebutBetween(String type, Date startDate, Date endDate);
    List<Formation> findAllByCategorieFormationAndDateDebutBetween(String categorie, Date startDate, Date endDate);

    List<Formation> findAllFormationByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsCategorie
            (String categorie, String type, Date startDate, Date endDate, String fonction);
    List<Formation> findAllByTypeAndDateDebutBetweenAndPersonelDetailsCategorie
            (String type, Date startDate, Date endDate, String fonction);
    List<Formation> findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsCategorie
            (String categorie, Date startDate, Date endDate, String fonction);
    List<Formation> findAllByDateDebutBetweenAndPersonelDetailsCategorie(Date startDate, Date endDate, String fonction);

    List<Formation> findAllByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
            (String categorieFormation, String type, Date startDate, Date endDate, String personelCategorie, String personelDepartement);

    List<Formation> findAllByDateDebutBetweenAndPersonelDetailsDepartement(Date startdate, Date endDate, String personelDepartement);

    List <Formation> findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsDepartement
            (String categorie, Date startDate, Date endDate, String personelDepartement);
    List<Formation> findAllByTypeAndDateDebutBetweenAndPersonelDetailsDepartement
            (String type, Date startDate, Date endDate, String personelDepartement);
    List<Formation> findAllByDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
            (Date startDate, Date endDate, String personelCategorie, String personelDepartement);
    List<Formation> findAllByTypeAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
            (String type, Date startDate, Date endDate, String personelCategorie, String personelDepartement);
    List<Formation> findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
            (String categorieFormation, Date startDate, Date endDate, String personelCategorie, String personelDepartement);

    List<Formation> findAllByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsDepartement
            (String categorie, String type, Date startDate, Date endDate, String personelDeprtement);

}
