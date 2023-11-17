package com.excelToDatabase.excelToDatabase.service;

import com.excelToDatabase.excelToDatabase.domain.Personel;
import com.excelToDatabase.excelToDatabase.exception.FileStorageException;
import com.excelToDatabase.excelToDatabase.model.PersonelRest;
import com.excelToDatabase.excelToDatabase.repository.PersonelRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonelService {

    private PersonelRepo personelRepo;

    public void savePersonelDataToDataBase(MultipartFile file) throws IllegalAccessException {
        if (UploadExcelAndExtractData.isValidFormat(file)){
            try {
                List<Personel> personels= UploadExcelAndExtractData.getPersonelDataFromExcel(file.getInputStream());
                List<Personel> personelList=new ArrayList<>();
                for (Personel p:personels){
                    Personel personel=this.personelRepo.findByMatricule(p.getMatricule());
                    if (personel==null){
                        personelList.add(p);
                    }
                }
                this.personelRepo.saveAll(personelList);
            } catch (IOException e) {
                throw new IllegalAccessException("The File Is Not An Excel File");
            }
        }
    }
    public List<PersonelRest> getPersonels(){
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<PersonelRest>personelRests=new ArrayList<>();
        List<Personel> personels=personelRepo.findAll();
        for (Personel personel:personels){
            PersonelRest personelRest=mp.map(personel, PersonelRest.class);
            personelRests.add(personelRest);
        }


        return personelRests;
    }
    public PersonelRest getPersonelByMatricule(long matricule ){
        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Personel persone=personelRepo.findByMatricule(matricule);
        if (persone==null) throw new FileStorageException("Personnel Not Found Matricule is: "+matricule);
        return mp.map(persone, PersonelRest.class);
    }


}
