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
    public CollectionModel<FormationRest> addFormation(@PathVariable long matricule,
                                                       @RequestBody List<FormationRequest> formationRequest){
        List<FormationDto>formationDtoList=new ArrayList<>();
        for (FormationRequest formationRequest1:formationRequest){
            FormationDto formationDto=new FormationDto();
            BeanUtils.copyProperties(formationRequest1, formationDto);
            formationDtoList.add(formationDto);
        }
        List<FormationDto> formationDtos=
                formationService.addFormationToPersonel(matricule,formationDtoList);
        List<FormationRest>formationRests=new ArrayList<>();
        for (FormationDto formationDto: formationDtos){
            FormationRest formationRest=new FormationRest();
            BeanUtils.copyProperties(formationDto, formationRest);
            formationRests.add(formationRest);
        }
        Link personelLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonel(matricule)).withRel("personel");
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(formationRests, personelsLink, personelLink);
    }
    @PostMapping(path = "/uploadFormation")
    public List<FormationFromExcel> uploadFormationFromExcel(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {
        List<FormationFromExcel>notFound= formationService.addFormationFromExcel(file);
        return notFound;
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
