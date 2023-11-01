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
public class FormationDateRange {
    private Date startDate;
    private Date endDate;
    private String type;
    private String categorieFormation;
    private String categoriePersonel;
}
