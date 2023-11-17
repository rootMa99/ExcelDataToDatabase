package com.excelToDatabase.excelToDatabase.controller;


import com.excelToDatabase.excelToDatabase.domain.Personel;
import com.excelToDatabase.excelToDatabase.model.OperationStatus;
import com.excelToDatabase.excelToDatabase.model.OperationStatusResult;
import com.excelToDatabase.excelToDatabase.model.PersonelRest;
import com.excelToDatabase.excelToDatabase.service.PersonelService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("personel")
@AllArgsConstructor
public class PersonelController {

    private PersonelService personelService;

    @PostMapping(path = "/personel")
    public EntityModel<OperationStatusResult> uploadPersonelData(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {

        this.personelService.savePersonelDataToDataBase(file);
        OperationStatusResult resutl= new OperationStatusResult();
        resutl.setOperationName("Upload Personel Data");
        resutl.setOperationResult(OperationStatus.SUCCESS.name());
        Link personnelLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return EntityModel.of(resutl, personnelLink);
    }
    @GetMapping(path = "/personels")
    public CollectionModel<PersonelRest> getPersonelData(){

        List <PersonelRest> personels= personelService.getPersonels();
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withSelfRel();
        Link personelsLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");
        return CollectionModel.of(personels, selfLink, personelsLink);
    }
    @GetMapping(path = "/personel/{matricule}")
    public EntityModel<PersonelRest> getPersonel(@PathVariable long matricule){
        PersonelRest personelRest=personelService.getPersonelByMatricule(matricule);


        Link selfLink= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                        .getPersonel(matricule)).withSelfRel();
        Link personels= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonelController.class)
                .getPersonelData()).withRel("personels");

        return EntityModel.of(personelRest, personels, selfLink);
    }
}
