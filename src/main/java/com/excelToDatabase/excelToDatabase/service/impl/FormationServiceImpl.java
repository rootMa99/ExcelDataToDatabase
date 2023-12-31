package com.excelToDatabase.excelToDatabase.service.impl;

import com.excelToDatabase.excelToDatabase.domain.Formation;
import com.excelToDatabase.excelToDatabase.domain.Personel;
import com.excelToDatabase.excelToDatabase.exception.FileStorageException;
import com.excelToDatabase.excelToDatabase.model.*;
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
import java.util.*;

@Service
@AllArgsConstructor
public class FormationServiceImpl implements FormationService {

    private PersonelRepo personelRepo;
    private Utils utils;
    private FormationRepo formationRepo;


    @Override
    public List<FormationDto> getEmployeeNotForm(String type, Date startDate, Date endDate, String fonction) {

        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= new ArrayList<>();
        List<FormationDto> formationDtoList=new ArrayList<>();
        if (type!=null && fonction!=null){
            formationList= formationRepo.findAllByTypeNotAndDateDebutBetweenAndPersonelDetailsCategorie
                    (type, startDate, endDate, fonction);
        }
        for (Formation f:formationList){
            FormationDto fdto= mp.map(f, FormationDto.class);
            formationDtoList.add(fdto);
        }


        return formationDtoList;
    }

    public List<Formation> getMinimize(String categorie, String type, Date startDate, Date endDate,
                                       String fonction){
        List<Formation> formationList=new ArrayList<>();

        if (categorie!=null && type!=null && fonction!=null){
            formationList= formationRepo.
                    findAllFormationByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsCategorie
                            (categorie, type, startDate,endDate,fonction);
        }
        if (fonction==null && type!=null && categorie!=null ){
            formationList= formationRepo.findAllByCategorieFormationAndTypeAndDateDebutBetween
                    (categorie, type, startDate, endDate);
        }
        if (categorie==null && type!=null && fonction!=null){
            formationList= formationRepo.findAllByTypeAndDateDebutBetweenAndPersonelDetailsCategorie
                    (type, startDate, endDate, fonction);
        }
        if (type==null && categorie!=null && fonction!=null){
            formationList= formationRepo.findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsCategorie
                    (categorie, startDate, endDate, fonction);
        }
        if (type==null && fonction==null && categorie==null){
            formationList= formationRepo.findAllFormationByDateDebutBetween
                    (startDate, endDate);
        }
        if (type==null && fonction==null && categorie!=null){
            formationList= formationRepo.findAllByCategorieFormationAndDateDebutBetween(categorie, startDate, endDate);
        }
        if (type== null && categorie==null && fonction!=null){
            formationList= formationRepo.findAllByDateDebutBetweenAndPersonelDetailsCategorie(startDate, endDate, fonction);
        }
        if (categorie==null && fonction==null && type!=null){
            formationList= formationRepo.findAllByTypeAndDateDebutBetween(type, startDate, endDate);
        }


        return formationList;
    }

    @Override
    public List<FormationDto> getDashboardData(String categorieFormation, String type, Date startDate, Date endDate,
                                               String personelCategorie, String personelDepartement) {
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList= new ArrayList<>();
        List<FormationDto> formationDtos= new ArrayList<>();

        if (personelDepartement==null){
            formationList=getMinimize(categorieFormation,type , startDate, endDate, personelCategorie);
        }

        if (categorieFormation!=null && type!=null && personelCategorie!=null && personelDepartement!=null){
            formationList= formationRepo.findAllByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
                    (categorieFormation, type, startDate,endDate, personelCategorie, personelDepartement);
        }

        if (categorieFormation==null && type==null && personelCategorie==null && personelDepartement!=null){
            formationList= formationRepo.findAllByDateDebutBetweenAndPersonelDetailsDepartement
                    (startDate, endDate, personelDepartement);
        }

        if (categorieFormation!=null && type==null && personelCategorie==null && personelDepartement!=null){
            formationList= formationRepo.findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsDepartement
                    (categorieFormation, startDate, endDate, personelDepartement);
        }
        if (categorieFormation==null && type!=null && personelCategorie==null && personelDepartement!=null){
            formationList= formationRepo.findAllByTypeAndDateDebutBetweenAndPersonelDetailsDepartement
                    (type, startDate, endDate, personelDepartement);
        }
        if (categorieFormation==null && type==null && personelCategorie!=null && personelDepartement!=null){
            formationList= formationRepo.findAllByDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
                    (startDate, endDate, personelCategorie, personelDepartement);
        }
        if (categorieFormation==null && type!=null && personelCategorie!=null && personelDepartement!=null){
            formationList= formationRepo.findAllByTypeAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
                    (type, startDate, endDate, personelCategorie, personelDepartement);
        }
        if (categorieFormation!=null && type==null && personelCategorie!=null && personelDepartement!=null){
            formationList= formationRepo.findAllByCategorieFormationAndDateDebutBetweenAndPersonelDetailsCategorieAndPersonelDetailsDepartement
                    (categorieFormation, startDate, endDate, personelCategorie, personelDepartement);
        }
        if (categorieFormation!=null && type!=null && personelCategorie==null && personelDepartement!=null){
            formationList= formationRepo.findAllByCategorieFormationAndTypeAndDateDebutBetweenAndPersonelDetailsDepartement
                    (categorieFormation, type, startDate, endDate, personelDepartement);
        }



        for (Formation f: formationList){
            FormationDto fdto=mp.map(f, FormationDto.class);
            formationDtos.add(fdto);
        }

        return formationDtos;
    }


    @Override
    public List<FormationDto> getFormationByCategorieAndTypeAndDateRangeAndFonction
            (String categorie, String type, Date startDate, Date endDate, String fonction) {

        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Formation> formationList=getMinimize(categorie, type, startDate, endDate, fonction);
        List<FormationDto> fdto= new ArrayList<>();
        for (Formation f: formationList){
            FormationDto formationDto= mp.map(f, FormationDto.class);
            fdto.add(formationDto);
        }
        return fdto;
    }

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
                List<Formation>formationList= new ArrayList<>();
                for (FormationFromExcel f:formation){
                    Formation formation1=new Formation();
                    Personel personel=personelRepo.findByMatricule(f.getMatricule());
                    if (personel==null){
                        f.setFormationId(utils.getFormationId(22));
                        notFound.add(f);
                        continue;
                    }
                    formation1.setPersonelDetails(personel);
                      //formationRepo.save(formation1);
                    if (personel.getFormations().isEmpty()){
                        f.setFormationId(utils.getFormationId(22));
                        BeanUtils.copyProperties(f,formation1);
                        formationList.add(formation1);
                    }else{
                        boolean found=false;
                            for (Formation fr: personel.getFormations()){
                                if (f.getDateDebut().equals(fr.getDateDebut())
                                        && f.getDateFin().equals(fr.getDateFin())
                                        && f.getType().equals(fr.getType())
                                        && f.getCategorieFormation().equals(fr.getCategorieFormation())
                                        && f.getDureePerHour()==fr.getDureePerHour()){
                                    found=true;
                                    break;
                                }
                            }
                            if (!found){
                                f.setFormationId(utils.getFormationId(22));
                                BeanUtils.copyProperties(f,formation1);
                                formationList.add(formation1);
                                System.out.println("formation added "+ personel.getMatricule());
                            }
                        }
                }
                formationRepo.saveAll(formationList);
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
        if (formationDto.getPrestataire()!=null){
            formation.setPrestataire(formationDto.getPrestataire());
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

    @Override
    public SelectHelper getAllTypeExist() {
        List<Formation> formationList=formationRepo.findAll();
        SelectHelper selectHelper=new SelectHelper();
        List<String> fonction= new ArrayList<>();
        List<String> departement= new ArrayList<>();
        Map<String, List<String>> catList = new HashMap<>();


        for (Formation f: formationList){
            if (fonction.isEmpty()||departement.isEmpty()||catList.isEmpty()){
                fonction.add(f.getPersonelDetails().getCategorie());
                departement.add(f.getPersonelDetails().getDepartement());
                List<String>typo= new ArrayList<>();
                typo.add(f.getType());
                catList.put(f.getCategorieFormation(),typo);

                continue;
            }
            if (catList.containsKey(f.getCategorieFormation())){
                if (!catList.get(f.getCategorieFormation()).contains(f.getType())){
                    catList.get(f.getCategorieFormation()).add(f.getType());
                }
                Collections.sort(catList.get(f.getCategorieFormation()));
            }else {
                List<String>typo= new ArrayList<>();
                typo.add(f.getType());
                catList.put(f.getCategorieFormation(),typo);
            }


            if (!fonction.contains(f.getPersonelDetails().getCategorie())){
                fonction.add(f.getPersonelDetails().getCategorie());
            }
            if (!departement.contains(f.getPersonelDetails().getDepartement())){
                departement.add(f.getPersonelDetails().getDepartement());
            }
        }


        Collections.sort(fonction);
        Collections.sort(departement);


        selectHelper.setCategoriePersonel(fonction);
        selectHelper.setDepartement(departement);
        selectHelper.setCatList(catList);
        return selectHelper;
    }
}
