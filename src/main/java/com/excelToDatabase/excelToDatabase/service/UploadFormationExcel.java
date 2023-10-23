package com.excelToDatabase.excelToDatabase.service;

import com.excelToDatabase.excelToDatabase.model.FormationDto;
import com.excelToDatabase.excelToDatabase.model.FormationFromExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Months;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
@Service
public class UploadFormationExcel {

    public static List<FormationFromExcel> getFormationDataFromExcel(InputStream inputStream){
        List<FormationFromExcel>formationFromExcels=new ArrayList<>();
        try {
            XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
            XSSFSheet sheet=workbook.getSheet("Déploiement");
            int rowIndex=0;
            for (Row row: sheet){
                if (rowIndex<2 ){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator= row.iterator();
                int cellIndex=0;
                FormationFromExcel formationFromExcel=new FormationFromExcel();
                while (cellIterator.hasNext()){
                    Cell cell= cellIterator.next();
                    switch (cellIndex){
                        case 0-> {
                            if (cell.getCellType()==CellType.NUMERIC){
                                formationFromExcel.setMatricule((long) cell.getNumericCellValue());
                            }else {
                                formationFromExcel.setMatricule(0);
                            }
                        }
                        case 3->formationFromExcel.setType(cell.getStringCellValue());
                        case 5->formationFromExcel.setCategorieFormation(cell.getStringCellValue());
                        case 6->formationFromExcel.setModalite(cell.getStringCellValue());
                        case 7->{
                            if (cell.getCellType()==CellType.NUMERIC){
                                formationFromExcel.setDureePerHour((double) cell.getNumericCellValue());
                            }else {
                                formationFromExcel.setDureePerHour(0.0);
                            }
                        }
                        case 8-> {
                            formationFromExcel.setDateDebut(cell.getDateCellValue());
                        }
                        case 9->formationFromExcel.setDateFin(cell.getDateCellValue());
                        case 10->{
                            if (cell.getCellType()==CellType.NUMERIC){
                                formationFromExcel.setMonth((int) cell.getNumericCellValue());
                            }else {
                                if (formationFromExcel.getDateDebut()!=null
                                        && formationFromExcel.getDateFin()!=null){
                                    Calendar m_calendar=Calendar.getInstance();
                                    m_calendar.setTime(formationFromExcel.getDateDebut());
                                    int nMonth1=m_calendar.get(Calendar.MONTH);
                                    formationFromExcel.setMonth(java.lang.Math.abs(nMonth1)+1);
                                }
                            }
                        }
                        case 11->formationFromExcel.setPresentataire(cell.getStringCellValue());
                        case 12->formationFromExcel.setFormatteur(cell.getStringCellValue());
                        case 13 -> {
                            if (cell.getCellType() == CellType.BOOLEAN) {
                                formationFromExcel.setEvaluationAFrois(cell.getBooleanCellValue());
                            }else {
                                if (cell.getStringCellValue().equals("oui")||cell.getStringCellValue().equals("Oui")){
                                    formationFromExcel.setEvaluationAFrois(true);
                                }else {
                                    formationFromExcel.setEvaluationAFrois(false);
                                }
                            }
                        }
                        case 14->formationFromExcel.setBilan(cell.getStringCellValue());
                        default -> {

                        }
                    }
                    cellIndex++;
                }

                formationFromExcels.add(formationFromExcel);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }


        return formationFromExcels;
    }


}
