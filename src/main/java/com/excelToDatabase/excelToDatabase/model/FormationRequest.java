package com.excelToDatabase.excelToDatabase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormationRequest {
    private String formationId;
    private String type;
    private String categorieFormation;
    private String modalite;
    private double dureePerHour;
    private Date dateDebut;
    private Date dateFin;
    private int month;
    private String prestataire;
    private String formatteur;
    private boolean evaluationAFrois;
    private String bilan;
}
