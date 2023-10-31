package com.excelToDatabase.excelToDatabase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FormationRestPersonel extends RepresentationModel<FormationPersonelRest> {

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
}
