package com.excelToDatabase.excelToDatabase.controller;


import com.excelToDatabase.excelToDatabase.model.*;
import com.excelToDatabase.excelToDatabase.service.FormationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/formation")
@AllArgsConstructor
public class FormationController {

    private FormationService formationService;

    @GetMapping(path = "/formations/type/categorie")
    public List<FormationPersonelRest> getFormationByCatTypeRange(@RequestBody FormationDateRange fdr){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> formationDtoList= formationService.
                getFormationByCategorieAndTypeAndDateRange(fdr.getCategorieFormation(), fdr.getType(),
                        fdr.getStartDate(),fdr.getEndDate());
        List<FormationPersonelRest> formationPersonelRestList= new ArrayList<>();
        for (FormationDto f: formationDtoList){
            FormationPersonelRest formationPersonelRest= mp.map(f, FormationPersonelRest.class);
            formationPersonelRest.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            formationPersonelRestList.add(formationPersonelRest);
        }


        return formationPersonelRestList;
    }

    @GetMapping(path = "/formations")
    public List<FormationPersonelRest> getFormationByDateRange(@RequestBody FormationDateRange fdr){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<FormationDto> formationDtoList= formationService.getFormationsInDateRange(fdr.getStartDate()
                ,fdr.getEndDate() );
        List<FormationPersonelRest> fprList=new ArrayList<>();
        for (FormationDto fdto: formationDtoList){
            FormationPersonelRest fpr=mp.map(fdto,FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(fdto.getPersonelDetails(), PersonelFormationRest.class));
            fprList.add(fpr);
        }
        return fprList;
    }

    @PostMapping(path = "/{matricule}")
    public CollectionModel<FormationPersonelRest> addFormation(@PathVariable long matricule,
                                                       @RequestBody List<FormationRequest> formationRequest){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto>formationDtoList=new ArrayList<>();
        for (FormationRequest formationRequest1:formationRequest){
            FormationDto formationDto=new FormationDto();
            BeanUtils.copyProperties(formationRequest1, formationDto);
            formationDtoList.add(formationDto);
        }
        List<FormationDto> formationDtos=
                formationService.addFormationToPersonel(matricule,formationDtoList);
        List<FormationPersonelRest>formationRests=new ArrayList<>();
        for (FormationDto formationDto: formationDtos){
            FormationPersonelRest formationRest=new FormationPersonelRest();
            BeanUtils.copyProperties(formationDto, formationRest);
            formationRest.setPersonelDetails(mp.map(formationDto.getPersonelDetails()
                    , PersonelFormationRest.class));
            formationRests.add(formationRest);
        }
        Link personelLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonel(matricule)).withRel("personel");
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(formationRests, personelsLink, personelLink);
    }
    @GetMapping(path = "/formations/type")
    public List<FormationPersonelRest> getFormationByTYpe(@RequestBody FormationRequest type){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> formationDtoList=formationService.getFormationByType(type.getType());
        List<FormationPersonelRest> formationPersonelRestList= new ArrayList<>();
        for (FormationDto fdto: formationDtoList){
            FormationPersonelRest fpr= mp.map(fdto , FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(fdto.getPersonelDetails(), PersonelFormationRest.class));
            formationPersonelRestList.add(fpr);
        }
        return formationPersonelRestList;
    }
    @GetMapping(path = "/formations/categorie")
    public List<FormationPersonelRest> getFormationByCategorie(@RequestBody FormationRequest categorie){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationPersonelRest> fprs=new ArrayList<>();
        List<FormationDto> fdtos= formationService.getFormationByCategorie(categorie.getCategorieFormation());
        for (FormationDto f: fdtos){
            FormationPersonelRest fpr= mp.map(f,FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            fprs.add(fpr);
        }

        return fprs;
    }
    @PostMapping(path = "/uploadFormation")
    public List<FormationFromExcel> uploadFormationFromExcel(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {
        return formationService.addFormationFromExcel(file);
    }

    @PutMapping(path = "/{formationId}")
    public EntityModel<FormationRestPersonel> updateFormation(@PathVariable String formationId,
                                                              @RequestBody FormationRequest formationRequest){
        ModelMapper mp =new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FormationDto formationDto=mp.map(formationRequest, FormationDto.class);
        FormationRest formationRest=formationService.updateFormation(formationId, formationDto);
        FormationRestPersonel formationRestPersonel=new FormationRestPersonel();
        BeanUtils.copyProperties(formationRest,formationRestPersonel);
        Link personelLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonel(formationRest.getPersonelDetails().getMatricule())).withRel("personel");
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return EntityModel.of(formationRestPersonel, personelsLink, personelLink);
    }

    @DeleteMapping(path = "/{formationId}")
    public EntityModel<OperationStatusResult> deleteFormation(@PathVariable String formationId){
        OperationStatusResult operation=new OperationStatusResult();
        operation.setOperationName("DELETE");
        formationService.deleteFormation(formationId);
        operation.setOperationResult(OperationStatus.SUCCESS.name());
        Link personnelLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        Link personel= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class))
                .slash("personel").withRel("Matricule");
        return EntityModel.of(operation, personnelLink, personel);
    }


}
