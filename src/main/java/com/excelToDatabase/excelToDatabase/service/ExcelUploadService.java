package com.excelToDatabase.excelToDatabase.service;

import com.excelToDatabase.excelToDatabase.domain.Personal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelUploadService {
    public static boolean isValidFormat(MultipartFile file){
        return Objects.equals(file.getContentType(),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static List<Personal> getPersonalDataFromExcel(InputStream inputStream){
        List<Personal> personals=new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet= workbook.getSheet("personel");
            int rowIndex=0;
            for(Row row : sheet){
                if(rowIndex==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator= row.iterator();
                int cellIndex=0;
                Personal personal=new Personal();
                while (cellIterator.hasNext()){
                    Cell cell= cellIterator.next();
                    switch (cellIndex){
                        case 0 -> personal.setPersonalId(( (int) cell.getNumericCellValue()));
                        case 1 -> personal.setFirstName(cell.getStringCellValue());
                        case 2 -> personal.setLastName(cell.getStringCellValue());
                        case 3 -> personal.setPhoneNumber((int) cell.getNumericCellValue());
                        case 4 -> personal.setEmail(cell.getStringCellValue());
                        case 5 -> personal.setAddress(cell.getStringCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                personals.add(personal);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return personals;
    }

}
