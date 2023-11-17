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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/formation")
@AllArgsConstructor
public class FormationController {

    private FormationService formationService;

    @GetMapping(path = "/allType")
    public SelectHelper getAllType(){
        return formationService.getAllTypeExist();
    }


    @PostMapping(path="/formations/Dashboard")
    public CollectionModel<FormationDashboardRest> getDashboardData(@RequestBody FormationDateRange fdr){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> formationDtos= formationService.getFormationsInDateRange(fdr.getStartDate(), fdr.getEndDate());
        List<FormationDashboardRest>fprs= new ArrayList<>();
        for (FormationDto f: formationDtos){
            FormationDashboardRest fpr=mp.map(f, FormationDashboardRest.class);
            fpr.setCategoriePersonel(f.getPersonelDetails().getCategorie());
            fpr.setMatricule(f.getPersonelDetails().getMatricule());
            fpr.setDepartmentPersonel(f.getPersonelDetails().getDepartement());
            fprs.add(fpr);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getDashboardData(fdr)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(fprs, selfLink, personelsLink);
    }
    @GetMapping(path = "/formations/{catFormation}/{type}/{startdate}/{enddate}/{catPer}")
    public CollectionModel<FormationPersonelRest> getFormationByCatTypeRangeP(
            @PathVariable String catFormation,@PathVariable String type,@PathVariable("startdate") String startdate
            ,@PathVariable("enddate") String enddate, @PathVariable String catPer) throws ParseException {
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = dateFormat.parse(startdate);
        Date edate = dateFormat.parse(enddate);

        List<FormationDto> fdto= formationService.getFormationByCategorieAndTypeAndDateRangeAndFonction
                (catFormation,type, sdate, edate, catPer);
        List<FormationPersonelRest> fpr=new ArrayList<>();

        for (FormationDto f:fdto){
            FormationPersonelRest formationPersonelRest= mp.map(f, FormationPersonelRest.class);
            formationPersonelRest.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + formationPersonelRest.getPersonelDetails().getMatricule());
            formationPersonelRest.getPersonelDetails().add(personDetailsSelfLink);
            fpr.add(formationPersonelRest);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByCatTypeRangeP(catFormation, type, startdate, enddate, catPer)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(fpr, selfLink, personelsLink);
    }
    @PostMapping(path = "/formations/type/categorie/percat")
    public CollectionModel<FormationPersonelRest> getFormationByCatTypeRangeFonc(
                                                                      @RequestBody FormationDateRange fdr){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> fdto= formationService.getFormationByCategorieAndTypeAndDateRangeAndFonction
                (fdr.getCategorieFormation(), fdr.getType(), fdr.getStartDate(), fdr.getEndDate(), fdr.getCategoriePersonel());
        List<FormationPersonelRest> fpr=new ArrayList<>();

        for (FormationDto f:fdto){
            FormationPersonelRest formationPersonelRest= mp.map(f, FormationPersonelRest.class);
            formationPersonelRest.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + formationPersonelRest.getPersonelDetails().getMatricule());
            formationPersonelRest.getPersonelDetails().add(personDetailsSelfLink);
            fpr.add(formationPersonelRest);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByCatTypeRangeFonc(fdr)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(fpr, selfLink, personelsLink);
    }

    @GetMapping(path = "/formations/type/categorie")
    public CollectionModel<FormationPersonelRest> getFormationByCatTypeRange(@RequestBody FormationDateRange fdr){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> formationDtoList= formationService.
                getFormationByCategorieAndTypeAndDateRange(fdr.getCategorieFormation(), fdr.getType(),
                        fdr.getStartDate(),fdr.getEndDate());
        List<FormationPersonelRest> formationPersonelRestList= new ArrayList<>();
        for (FormationDto f: formationDtoList){
            FormationPersonelRest formationPersonelRest= mp.map(f, FormationPersonelRest.class);
            formationPersonelRest.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + formationPersonelRest.getPersonelDetails().getMatricule());
            formationPersonelRest.getPersonelDetails().add(personDetailsSelfLink);
            formationPersonelRestList.add(formationPersonelRest);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByCatTypeRange(fdr)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");

        return CollectionModel.of(formationPersonelRestList, selfLink, personelsLink);
    }

    @GetMapping(path = "/formations")
    public CollectionModel<FormationPersonelRest> getFormationByDateRange(@RequestBody FormationDateRange fdr){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<FormationDto> formationDtoList= formationService.getFormationsInDateRange(fdr.getStartDate()
                ,fdr.getEndDate() );
        List<FormationPersonelRest> fprList=new ArrayList<>();
        for (FormationDto fdto: formationDtoList){
            FormationPersonelRest fpr=mp.map(fdto,FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(fdto.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + fpr.getPersonelDetails().getMatricule());
            fpr.getPersonelDetails().add(personDetailsSelfLink);
            fprList.add(fpr);
        }

        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByDateRange(fdr)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(fprList, selfLink, personelsLink);
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
    public CollectionModel<FormationPersonelRest> getFormationByTYpe(@RequestBody FormationRequest type){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationDto> formationDtoList=formationService.getFormationByType(type.getType());
        List<FormationPersonelRest> formationPersonelRestList= new ArrayList<>();
        for (FormationDto fdto: formationDtoList){
            FormationPersonelRest fpr= mp.map(fdto , FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(fdto.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + fpr.getPersonelDetails().getMatricule());
            fpr.getPersonelDetails().add(personDetailsSelfLink);
            formationPersonelRestList.add(fpr);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByTYpe(type)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(formationPersonelRestList, selfLink, personelsLink);
    }
    @GetMapping(path = "/formations/categorie")
    public CollectionModel<FormationPersonelRest> getFormationByCategorie(@RequestBody FormationRequest categorie){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<FormationPersonelRest> fprs=new ArrayList<>();
        List<FormationDto> fdtos= formationService.getFormationByCategorie(categorie.getCategorieFormation());
        for (FormationDto f: fdtos){
            FormationPersonelRest fpr= mp.map(f,FormationPersonelRest.class);
            fpr.setPersonelDetails(mp.map(f.getPersonelDetails(), PersonelFormationRest.class));
            Link personDetailsSelfLink = Link.of("/personel/personel/" + fpr.getPersonelDetails().getMatricule());
            fpr.getPersonelDetails().add(personDetailsSelfLink);
            fprs.add(fpr);
        }
        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormationController.class)
                .getFormationByCategorie(categorie)).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(fprs, selfLink, personelsLink);
    }
    @PostMapping(path = "/uploadFormation")
    public List<FormationFromExcel> uploadFormationFromExcel(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {
        return formationService.addFormationFromExcel(file);
    }

    @PutMapping(path = "/{formationId}")
    public EntityModel<FormationPersonelRest> updateFormation(@PathVariable String formationId,
                                                              @RequestBody FormationRequest formationRequest){
        ModelMapper mp =new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FormationDto formationDto=mp.map(formationRequest, FormationDto.class);
        FormationRest formationRest=formationService.updateFormation(formationId, formationDto);
        FormationPersonelRest formationRestPersonel=mp.map(formationRest, FormationPersonelRest.class);
        formationRestPersonel.setPersonelDetails(mp.map(formationRest.getPersonelDetails(), PersonelFormationRest.class));
        Link personelLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonel(formationRest.getPersonelDetails().getMatricule())).withRel("personel");
        formationRestPersonel.getPersonelDetails().add(personelLink);
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return EntityModel.of(formationRestPersonel, personelsLink);
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
