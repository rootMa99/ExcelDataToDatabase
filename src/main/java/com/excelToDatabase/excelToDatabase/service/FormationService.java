package com.excelToDatabase.excelToDatabase.service;

import com.excelToDatabase.excelToDatabase.model.FormationDto;
import com.excelToDatabase.excelToDatabase.model.FormationFromExcel;
import com.excelToDatabase.excelToDatabase.model.FormationRest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface FormationService {
    List<FormationDto> addFormationToPersonel(long matricule, List<FormationDto> formations);
    List<FormationFromExcel> addFormationFromExcel(MultipartFile file) throws IllegalAccessException;


    FormationRest updateFormation(String formationId, FormationDto formationDto);
    List<FormationDto> getFormationsInDateRange(Date startDate, Date endDate);
    void deleteFormation(String formationId);
}
