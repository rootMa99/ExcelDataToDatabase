package com.excelToDatabase.excelToDatabase.controller;


import com.excelToDatabase.excelToDatabase.domain.Personel;
import com.excelToDatabase.excelToDatabase.model.PersonelRest;
import com.excelToDatabase.excelToDatabase.service.PersonelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("personel")
@AllArgsConstructor
public class PersonelController {

    private PersonelService personelService;

    @PostMapping(path = "/personel")
    public ResponseEntity<?> uploadPersonelData(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {

        this.personelService.savePersonelDataToDataBase(file);
        return ResponseEntity.ok(Map.of("Message", "Personal Data Uploaded Successfully"));
    }
    @GetMapping(path = "/personels")
    public ResponseEntity<List<PersonelRest>> getPersonelData(){
        return new ResponseEntity<>(personelService.getPersonels(), HttpStatus.FOUND);
    }
    @GetMapping(path = "/personel/{matricule}")
    public PersonelRest getPersonel(@PathVariable long matricule){
        PersonelRest personelRest=personelService.getPersonelByMatricule(matricule);
        return personelRest;
    }
}
