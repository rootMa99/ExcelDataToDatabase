package com.excelToDatabase.excelToDatabase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PersonelRest extends RepresentationModel<PersonelRest> {
    private Long matricule;
    private String nom;
    private String prenom;
    private String cin;
    private String categorie;
    public  String fonctionEntreprise;
    private String departement;
    private Date dateEmbauche;
    private Date dateDepart;
    private List<FormationRestPersonel> formations;
}
