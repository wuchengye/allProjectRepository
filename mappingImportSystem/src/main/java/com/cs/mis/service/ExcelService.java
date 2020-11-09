package com.cs.mis.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author wcy
 */

@Service
public class ExcelService {

    public void getExcelDataAndCheck(File temp) throws OpenXML4JException, IOException {
        OPCPackage opcPackage = OPCPackage.open(temp);
        XSSFReader xssfReader = new XSSFReader(opcPackage);

    }
}
