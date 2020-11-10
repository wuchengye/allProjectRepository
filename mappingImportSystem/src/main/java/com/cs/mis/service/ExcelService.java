package com.cs.mis.service;

import com.cs.mis.entity.ExcelDataEntity;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wcy
 */

@Service
public class ExcelService {

    public void getExcelDataAndCheck(File temp) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(temp);
        Workbook wb = StreamingReader
                    .builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(fileInputStream);
        Sheet sheet = wb.getSheetAt(0);
        System.out.println("正在读取sheet==" + sheet.getSheetName());
        //从第5行开始解析
        int beginRow = 5;
        int rowPoint = 1;

        boolean isError = false;
        StringBuffer errorMes = new StringBuffer();

        for (Row row : sheet){
            if(rowPoint < 5){
                rowPoint++;
                continue;
            }
            errorMes.append(checkData(row,rowPoint));
        }
    }

    private String checkData(Row row, int rowPoint) {
        row.getCell(0).getStringCellValue().trim()
    }
}
