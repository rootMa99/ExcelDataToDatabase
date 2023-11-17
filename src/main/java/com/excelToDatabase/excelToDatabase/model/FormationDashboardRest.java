package com.excelToDatabase.excelToDatabase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FormationDashboardRest {

    private long matricule;
    private int month;
    private String type;
    private Date dateDebut;
    private double dureePerHour;
    private String categorieFormation;
    private String categoriePersonel;
    private String departmentPersonel;


}
