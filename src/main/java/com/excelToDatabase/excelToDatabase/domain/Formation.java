package com.excelToDatabase.excelToDatabase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "formation")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Formation {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "formationId", nullable = false, unique = true)
    private String formationId;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "categorie-de-formation", nullable = false)
    private String categorieFormation;
    @Column(name = "modalite", nullable = false)
    private String modalite;
    @Column(name = "duree-par-heure")
    private double dureePerHour;
    @Temporal(TemporalType.DATE)
    @Column(name = "date-de-debut", nullable = false)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    @Column(name = "date-de-fin",nullable = true)
    private Date dateFin;
    @Column(name="month", nullable = false)
    private Integer month;
    @Column(name = "presentataire", nullable = false)
    private String presentataire;
    @Column(name = "formatteur", nullable = false)
    private String formatteur;
    @Column(name = "evaluation-a-frois", nullable = false)
    private Boolean evaluationAFrois;
    @Column(name = "bilan", nullable = true)
    private String bilan;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "matricule")
    private Personel personelDetails;
}
