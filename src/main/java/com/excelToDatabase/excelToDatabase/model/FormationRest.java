package com.excelToDatabase.excelToDatabase.model;

import com.excelToDatabase.excelToDatabase.domain.Personel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormationRest extends RepresentationModel<FormationRest> {
    private String formationId;
    private String type;
    private String categorieFormation;
    private String modalite;
    private double dureePerHour;
    private Date dateDebut;
    private Date dateFin;
    private int month;
    private String presentataire;
    private String formatteur;
    private boolean evaluationAFrois;
    private String  bilan;
    private Personel personelDetails;
}
