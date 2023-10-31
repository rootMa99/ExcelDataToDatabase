package com.excelToDatabase.excelToDatabase.service.impl;

import com.excelToDatabase.excelToDatabase.domain.Formation;
import com.excelToDatabase.excelToDatabase.domain.Personel;
import com.excelToDatabase.excelToDatabase.exception.FileStorageException;
import com.excelToDatabase.excelToDatabase.model.FormationDto;
import com.excelToDatabase.excelToDatabase.model.FormationFromExcel;
import com.excelToDatabase.excelToDatabase.model.FormationRest;
import com.excelToDatabase.excelToDatabase.repository.FormationRepo;
import com.excelToDatabase.excelToDatabase.repository.PersonelRepo;
import com.excelToDatabase.excelToDatabase.service.FormationService;
import com.excelToDatabase.excelToDatabase.service.UploadExcelAndExtractData;
import com.excelToDatabase.excelToDatabase.service.UploadFormationExcel;
import com.excelToDatabase.excelToDatabase.ui.Utils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FormationServiceImpl implements FormationService {

    private PersonelRepo personelRepo;
    private Utils utils;
    private FormationRepo formationRepo;


    @Override
    public List<FormationDto> getFormationsInDateRange(Date startDate, Date endDate) {
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= formationRepo.findAllFormationByDateDebutBetween(startDate, endDate);
        List<FormationDto> formationDtos= new ArrayList<>();
        for (Formation f: formationList){
            FormationDto formationDto= mp.map(f, FormationDto.class);
            formationDtos.add(formationDto);
        }
        return formationDtos;
    }

    @Override
    public List<FormationDto> getFormationByType(String type) {
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= formationRepo.findAllByType(type);
        List<FormationDto> formationDtoList= new ArrayList<>();
        for (Formation f: formationList){
            FormationDto fdto= mp.map(f, FormationDto.class);
            formationDtoList.add(fdto);
        }
        return formationDtoList;
    }

    @Override
    public List<FormationDto> getFormationByCategorie(String categorie) {

        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= formationRepo.findAllByCategorieFormation(categorie);
        List<FormationDto> formationDtos=new ArrayList<>();
        for (Formation f: formationList){
            FormationDto fdto= mp.map(f, FormationDto.class);
            formationDtos.add(fdto);
        }

        return formationDtos;
    }

    @Override
    public List<FormationDto> getFormationByCategorieAndTypeAndDateRange(String categorie, String type,
                                                                         Date startDate, Date endDate) {
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= new ArrayList<>();

        if(categorie!=null && type!=null && startDate!=null && endDate!=null){
            formationList= formationRepo.
                    findAllByCategorieFormationAndTypeAndDateDebutBetween(categorie, type, startDate, endDate);
        }

        if(categorie==null){
            formationList= formationRepo.
                    findAllByTypeAndDateDebutBetween(type, startDate, endDate);
        }
        if(type==null){
            formationList= formationRepo.findAllByCategorieFormationAndDateDebutBetween(categorie, startDate, endDate);
        }

        List<FormationDto> formationDtoList = new ArrayList<>();
        for (Formation f: formationList){
            FormationDto fd= mp.map(f, FormationDto.class);
            formationDtoList.add(fd);
        }

        return formationDtoList;
    }

    @Override
    public List<FormationDto> addFormationToPersonel(long matricule, List<FormationDto> formations) {
        List<Formation> formationList= new ArrayList<>();
        for (FormationDto formation : formations){
            Formation formation1=new Formation();
            formation.setFormationId(utils.getFormationId(22));
            BeanUtils.copyProperties(formation, formation1);
            formationList.add(formation1);
        }

        Personel personel= personelRepo.findByMatricule(matricule);
        if (personel==null) throw new FileStorageException("Personal Not Found Matriclue: "+matricule);
        List<FormationDto> formationDtos=new ArrayList<>();
       for (Formation formation: formationList){
            FormationDto formationDto=new FormationDto();
            formation.setPersonelDetails(personel);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(formation.getDateDebut());
            int month=calendar.get(Calendar.MONTH);
            formation.setMonth(month+1);
            Formation formation1= formationRepo.save(formation);
            BeanUtils.copyProperties(formation1, formationDto);
            formationDtos.add(formationDto);
        }
        return formationDtos;
    }

    @Override
    public List<FormationFromExcel> addFormationFromExcel(MultipartFile file) throws IllegalAccessException {
        List<FormationFromExcel>notFound=new ArrayList<>();
        if (UploadExcelAndExtractData.isValidFormat(file)){
            try {
                List<FormationFromExcel>formation=
                        UploadFormationExcel.getFormationDataFromExcel(file.getInputStream());
                for (FormationFromExcel f:formation){
                    Formation formation1=new Formation();
                    f.setFormationId(utils.getFormationId(22));
                    Personel personel=personelRepo.findByMatricule(f.getMatricule());
                    if (personel==null){
                        notFound.add(f);
                        continue;
                    }
                    BeanUtils.copyProperties(f,formation1);
                    formation1.setPersonelDetails(personel);
                      formationRepo.save(formation1);
                }
            } catch (IOException e) {
                throw new IllegalAccessException("The File is Not An Excel File");
            }
        }
        return notFound;
    }


    @Override
    public FormationRest updateFormation(String formationId, FormationDto formationDto) {
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Formation formation=formationRepo.findByFormationId(formationId);
        if (formation==null) throw new RuntimeException("No Formation Found With the ID: "+formationId);
        if(formationDto.getType()!=null){
            formation.setType(formationDto.getType());
        }
        if (formationDto.getCategorieFormation()!=null){
            formation.setCategorieFormation(formationDto.getCategorieFormation());
        }
        if (formationDto.getModalite()!=null){
            formation.setModalite(formationDto.getModalite());
        }
            formation.setDureePerHour(formationDto.getDureePerHour());
        if (formationDto.getDateDebut()!=null){
            formation.setDateDebut(formationDto.getDateDebut());
            Calendar m_calendar=Calendar.getInstance();
            m_calendar.setTime(formationDto.getDateDebut());
            int nMonth1=m_calendar.get(Calendar.MONTH);
            formation.setMonth(java.lang.Math.abs(nMonth1)+1);
        }
        if (formationDto.getDateFin()!=null){
            formation.setDateFin(formationDto.getDateFin());
        }
        if (formationDto.getPresentataire()!=null){
            formation.setPresentataire(formationDto.getPresentataire());
        }
        if (formationDto.getFormatteur()!=null){
            formation.setFormatteur(formationDto.getFormatteur());
        }

        if (formationDto.getBilan()!=null){
            formation.setBilan(formationDto.getBilan());
        }
        Formation formation1;
        try{
            formation1= formationRepo.save(formation);
        }catch (Exception e){
            throw new RuntimeException("Something Went Wrong");
        }

        return mp.map(formation1,FormationRest.class);
    }

    @Override
    public void deleteFormation(String formationId) {
        Formation formation=formationRepo.findByFormationId(formationId);
        if (formation==null) throw new RuntimeException("Fomation Does NOt Exist," +
                " You Try To Delete Formation With ID: "+formationId);
        try {
            formationRepo.delete(formation);
        }catch (Exception e){
            throw new RuntimeException("Could Not Delete This record Something Went Wrong");
        }
    }
}
