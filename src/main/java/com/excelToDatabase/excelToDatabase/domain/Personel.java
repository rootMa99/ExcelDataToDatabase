package com.excelToDatabase.excelToDatabase.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "personel")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personel {
    @Id
    private Long matricule;
    @Column(name="nom", nullable = false, unique = false )
    private String nom;
    @Column(name="prenom", nullable = false, unique = false)
    private String prenom;
    @Column(name = "cin", nullable = false, unique = true)
    private String cin;
    @Column(name = "categorie", nullable = false)
    private String categorie;
    @Column(name = "fonction-departement", nullable = false)
    private String fonctionEntreprise;
    @Column(name = "departement", nullable = false)
    private String departement;
    @Column(name="date-embauche", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;
    @Column(name="date-depart")
    @Temporal(TemporalType.DATE)
    private Date dateDepart;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personelDetails")
    @JsonIgnore
    private List<Formation> formations;
}
