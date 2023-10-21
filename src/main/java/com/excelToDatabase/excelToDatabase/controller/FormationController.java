package com.excelToDatabase.excelToDatabase.controller;


import com.excelToDatabase.excelToDatabase.model.*;
import com.excelToDatabase.excelToDatabase.service.FormationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/formation")
@AllArgsConstructor
public class FormationController {

    private FormationService formationService;

    @PostMapping(path = "/{matricule}")
    public List<FormationRest> addFormation(@PathVariable long matricule,
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
        return formationRests;
    }
    @PostMapping(path = "/uploadFormation")
    public List<FormationFromExcel> uploadFormationFromExcel(@RequestParam("file")MultipartFile file)
            throws IllegalAccessException {
        List<FormationFromExcel>notFound= formationService.addFormationFromExcel(file);
        return notFound;
    }

    @PutMapping(path = "/{formationId}")
    public FormationRestPersonel updateFormation(@PathVariable String formationId,
                                                 @RequestBody FormationRequest formationRequest){
        ModelMapper mp =new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FormationDto formationDto=mp.map(formationRequest, FormationDto.class);
        FormationRest formationRest=formationService.updateFormation(formationId, formationDto);
        FormationRestPersonel formationRestPersonel=new FormationRestPersonel();
        BeanUtils.copyProperties(formationRest,formationRestPersonel);
        return formationRestPersonel;
    }

    @DeleteMapping(path = "/{formationId}")
    public OperationStatusResult deleteFormation(@PathVariable String formationId){
        OperationStatusResult operation=new OperationStatusResult();
        operation.setOperationName("DELETE");
        formationService.deleteFormation(formationId);
        operation.setOperationResult(OperationStatus.SUCCESS.name());
        return operation;
    }


}
