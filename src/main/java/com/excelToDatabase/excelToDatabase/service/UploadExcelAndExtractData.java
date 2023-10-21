package com.excelToDatabase.excelToDatabase.service;

import com.excelToDatabase.excelToDatabase.domain.Personel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class UploadExcelAndExtractData {

    public static boolean isValidFormat(MultipartFile file){
        return Objects.equals(file.getContentType(),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Personel> getPersonelDataFromExcel(InputStream inputStream){
        List<Personel> personels = new ArrayList<>();
        try {
            XSSFWorkbook workbook= new XSSFWorkbook(inputStream);
            XSSFSheet sheet= workbook.getSheet("ActifG");
            int rowIndex= 0;
            for (Row row : sheet){
                if (rowIndex==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator= row.iterator();
                int cellIndex=0;
                Personel personel=new Personel();
                while (cellIterator.hasNext()){
                    Cell cell= cellIterator.next();
                    switch(cellIndex){
                        case 0->personel.setMatricule(((long) cell.getNumericCellValue()));
                        case 1->personel.setNom(cell.getStringCellValue());
                        case 2->personel.setPrenom(cell.getStringCellValue());
                        case 3->personel.setCin(cell.getStringCellValue());
                        case 4->personel.setCategorie(cell.getStringCellValue());
                        case 5->personel.setFonctionEntreprise(cell.getStringCellValue());
                        case 6->personel.setDepartement(cell.getStringCellValue());
                        case 7->personel.setDateEmbauche( cell.getDateCellValue());
                        case 8->{
                            if (cell.getCellType() != CellType.BLANK &&
                                    cell.getCellType() == CellType.NUMERIC &&
                                    DateUtil.isCellDateFormatted(cell)) {
                                personel.setDateDepart(cell.getDateCellValue());
                            }
                        }

                        default -> {}
                    }
                    cellIndex++;
                }
                personels.add(personel);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return personels;
    }
}
