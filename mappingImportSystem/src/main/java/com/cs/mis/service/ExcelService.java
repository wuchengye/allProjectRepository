package com.cs.mis.service;

import com.cs.mis.controller.ExcelController;
import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.mapper.ExcelMapper;
import com.cs.mis.mapper.UserMapper;
import com.cs.mis.utils.DateUtil;
import com.monitorjbl.xlsx.StreamingReader;
import io.netty.util.internal.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author wcy
 */

@Service
public class ExcelService {
    @Autowired
    private ExcelMapper excelMapper;
    @Autowired
    private UserMapper userMapper;

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

        StringBuffer errorMes = new StringBuffer();
        //存储平台工号进行列校验
        Set<String> hs = new HashSet<>();

        for (Row row : sheet){
            if(rowPoint < beginRow){
                rowPoint++;
                continue;
            }
            String[] arg = checkData(row,rowPoint);
            errorMes.append(arg[0]);
            if(!"".equals(arg[1])){
                if(!hs.add(arg[1])){
                    errorMes.append("第" + rowPoint + "行" + "第3列平台工号重复\n");
                }
            }
            rowPoint++;
        }
        System.out.println("总" + rowPoint + "行");
        if(!StringUtil.isNullOrEmpty(errorMes.toString())){
            throw new Exception(errorMes.toString());
        }
    }

    public void saveExcelData(File temp, boolean isTodayData, String userAccount) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(temp);
        Workbook wb = StreamingReader
                .builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(fileInputStream);
        Sheet sheet = wb.getSheetAt(0);
        if(isTodayData){
            //获取今天日期
            String today = DateUtil.getDateOfToday();
            excelMapper.deleteByAccountAndDate(userAccount,today);
            String parentPath = ExcelController.TXT_DAT_PATH + today + "\\";
            String center = userMapper.getCenterByAccount(userAccount);
            String fileName = "staffinformation_" + center + "_day_" + today.replace("-","") + ".txt";
            getRowDataAndInsertWrite(userAccount,today,sheet,parentPath,fileName);
        }else {
            //获取上月最后一天日期
            String lastMonthDay = DateUtil.getDateOfLastMonth();
            excelMapper.deleteByAccountAndDate(userAccount,lastMonthDay);
            String parentPath = ExcelController.TXT_MONTH_PATH + lastMonthDay.substring(0,7) + "\\";
            String center = userMapper.getCenterByAccount(userAccount);
            String fileName = "staffinformation_" + center + "_month_" + lastMonthDay.substring(0,7).replace("-","") + ".txt";
            getRowDataAndInsertWrite(userAccount,lastMonthDay,sheet,parentPath,fileName);
        }
    }

    private void getRowDataAndInsertWrite(String userAccount, String date, Sheet sheet, String parentPath,String fileName) throws Exception {
        //创建文件夹,对天和月的进行分类
        File folder = new File(parentPath);
        folder.mkdirs();
        //写入流
        FileWriter fw = new FileWriter(parentPath + fileName);
        //暂存list
        List<ExcelDataEntity> entityList = new ArrayList<>();
        //从第5行开始解析
        int beginRow = 5;
        int rowPoint = 1;
        int resultLine = 0;

        for (Row row : sheet){
            if(rowPoint < beginRow){
                rowPoint++;
                continue;
            }
            entityList.add(readRow(row));
            resultLine++;
            //分批100行进行存储
            if(resultLine == 100){
                try {
                    saveTxtAndSql(entityList,userAccount,date,fw);
                } catch (Exception e) {
                    fw.close();
                    throw e;
                }
                resultLine = 0;
                entityList.clear();
            }
        }
        if(entityList.size() > 0){
            try {
                saveTxtAndSql(entityList,userAccount,date,fw);
            } catch (Exception e) {
                fw.close();
                throw e;
            }
        }
        fw.close();
    }

    /**
     * @date 2020-11-16 15:06
     * 行读取，返回对象
     */
    private ExcelDataEntity readRow(Row row) {
        ExcelDataEntity entity = new ExcelDataEntity();
        List<String> list = new ArrayList<>(10);
        for (int x = 0; x < ExcelDataEntity.EXCEL_CELL_SIZE; x++){
            switch (x){
                case 0:
                    entity.setCenter(row.getCell(x).getStringCellValue());
                    break;
                case 1:
                    entity.setSupport(row.getCell(x).getStringCellValue());
                    break;
                case 2:
                    entity.setPlatformNum(row.getCell(x).getStringCellValue());
                    break;
                case 3:
                    entity.setName(row.getCell(x).getStringCellValue());
                    break;
                case 4:
                    entity.setGroup(row.getCell(x).getStringCellValue());
                    break;
                case 5:
                    entity.setPositionName(row.getCell(x).getStringCellValue());
                    break;
                case 6:
                    entity.setMemberType(row.getCell(x).getStringCellValue());
                    break;
                case 7:
                    entity.setStandardPositionName(row.getCell(x).getStringCellValue());
                    break;
                case 8:
                    if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                        entity.setBeginTime(null);
                        break;
                    }
                    entity.setBeginTime(row.getCell(x).getStringCellValue());
                    break;
                case 9:
                    if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                        entity.setEndTime(null);
                        break;
                    }
                    entity.setEndTime(row.getCell(x).getStringCellValue());
                    break;
                case 10:
                    if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                        entity.setRemark(null);
                        break;
                    }
                    entity.setRemark(row.getCell(x).getStringCellValue());
                    break;
                default:
                    if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                        list.add(null);
                    }else {
                        list.add(row.getCell(x).getStringCellValue());
                    }
            }
        }
        entity.setJobNumList(list);
        return entity;
    }

    /**
     * @date 2020-11-16 15:06
     * 写入数据库和文件
     */
    private void saveTxtAndSql(List<ExcelDataEntity> entityList, String userAccount, String date, FileWriter fw) throws Exception {
        int id = userMapper.getIdByAccount(userAccount);
        Map<String,Object> map = new HashMap<>(3);
        map.put("id",id);
        map.put("date",date);
        map.put("entityList",entityList);
        int success = excelMapper.insertListWithDateAndId(map);
        if(success != entityList.size()){
            throw new Exception("插入数据库缺失");
        }
        for (ExcelDataEntity entity : entityList){
            fw.write(entity.toString() + "\n");
        }
        fw.flush();
    }

    /**
     * @date 2020-11-11 15:42
     * 行校验
     */
    private String[] checkData(Row row, int rowPoint) {
        StringBuffer sb = new StringBuffer();
        String platformNum = "";
        Set<String> hs = new HashSet<>();

        for(int x = 0; x < ExcelDataEntity.EXCEL_CELL_SIZE; x++){
            //前8列必填
            if(x < 8){
                if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                    sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列为空\n");
                    continue;
                }
            }
            //11列后生产工号序列
            if(x >= 11){
                if(row.getCell(x) == null || "".equals(row.getCell(x).getStringCellValue())){
                    continue;
                }
                if(!hs.add(row.getCell(x).getStringCellValue().trim())){
                    sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列生产工号重复\n");
                }
            }
            switch (x){
                case 0:
                    if(!ExcelDataEntity.CENTER_ALL.contains(row.getCell(x).getStringCellValue().trim())){
                        sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列所属中心填写错误\n");
                    }
                    break;
                case 1:
                    if(!ExcelDataEntity.SUPPORT_ALL.contains(row.getCell(x).getStringCellValue().trim())){
                        sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列所属支撑方填写错误\n");
                    }
                    break;
                case 2:
                    if(!row.getCell(x).getStringCellValue().trim().startsWith("ZB") &&
                            !row.getCell(x).getStringCellValue().trim().startsWith("08")){
                        sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列平台工号填写错误\n");
                    }else {
                        platformNum = row.getCell(x).getStringCellValue().trim();
                    }
                    break;
                case 5:
                    if(!ExcelDataEntity.POSITION_NAME_ALL.contains(row.getCell(x).getStringCellValue().trim())){
                        sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列岗位名称填写错误\n");
                    }
                    if("综援".equals(row.getCell(x).getStringCellValue().trim())){
                        if(row.getCell(1) == null || !"自营".equals(row.getCell(1).getStringCellValue().trim())){
                            sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列岗位名称与所属支撑方冲突\n");
                        }
                    }
                    break;
                case 6:
                    if(!ExcelDataEntity.MEMBER_TYPE_ALL.contains(row.getCell(x).getStringCellValue().trim())){
                        sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列人员类型填写错误\n");
                    }
                    break;
                case 8:
                    if(row.getCell(x) != null && !"".equals(row.getCell(x).getStringCellValue())){
                        if(!Pattern.matches(ExcelDataEntity.EXCEL_DATE_PATTERN,row.getCell(x).getStringCellValue().trim())){
                            sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列抢包时间填写错误\n");
                        }
                    }
                    break;
                case 9:
                    if(row.getCell(x) != null && !"".equals(row.getCell(x).getStringCellValue())){
                        if(!Pattern.matches(ExcelDataEntity.EXCEL_DATE_PATTERN,row.getCell(x).getStringCellValue().trim())){
                            sb.append("第" + rowPoint + "行" + "第" + (x+1) + "列离职时间填写错误\n");
                        }
                    }
                    break;
                default:
            }
        }
        String[] arg = new String[2];
        arg[0] = sb.toString();
        arg[1] = platformNum;
        return arg;
    }

    /**
     * 将数据写入导出模板
     * @date 2020-11-18 15:43
     * @return
     */
    public File insertExcel(InputStream inputStream, List<ExcelDataEntity> list,String tempPath) {
        SXSSFWorkbook sxssf = null;
        File exportTempFile = null;
        try {
            XSSFWorkbook xssf = new XSSFWorkbook(inputStream);
            sxssf = new SXSSFWorkbook(xssf);
            Sheet sheet = sxssf.getSheetAt(0);
            int rowPoiot = 2;
            for (ExcelDataEntity entity : list){
                Row row = sheet.createRow(rowPoiot);
                for (int x = 0; x < ExcelDataEntity.EXCEL_CELL_SIZE; x++){
                    switch (x){
                        case 0:
                            row.createCell(x).setCellValue(entity.getCenter());
                            break;
                        case 1:
                            row.createCell(x).setCellValue(entity.getSupport());
                            break;
                        case 2:
                            row.createCell(x).setCellValue(entity.getPlatformNum());
                            break;
                        case 3:
                            row.createCell(x).setCellValue(entity.getName());
                            break;
                        case 4:
                            row.createCell(x).setCellValue(entity.getGroup());
                            break;
                        case 5:
                            row.createCell(x).setCellValue(entity.getPositionName());
                            break;
                        case 6:
                            row.createCell(x).setCellValue(entity.getMemberType());
                            break;
                        case 7:
                            row.createCell(x).setCellValue(entity.getStandardPositionName());
                            break;
                        case 8:
                            if(entity.getBeginTime() != null){
                                row.createCell(x).setCellValue(entity.getBeginTime());
                            }
                            break;
                        case 9:
                            if(entity.getEndTime() != null){
                                row.createCell(x).setCellValue(entity.getEndTime());
                            }
                            break;
                        case 10:
                            if(entity.getRemark() != null){
                                row.createCell(x).setCellValue(entity.getRemark());
                            }
                            break;
                        default:
                            String num = entity.getJobNumList().get(x - 11);
                            if(num != null && !"".equals(num) && !"null".equals(num)){
                                row.createCell(x).setCellValue(num);
                            }
                    }
                }
                rowPoiot++;
            }
            if(!new File(tempPath).exists()){
                new File(tempPath).mkdirs();
            }
            exportTempFile = new File(tempPath + "export_" + System.currentTimeMillis() + ".xlsx");
            FileOutputStream out = new FileOutputStream(exportTempFile);
            sxssf.write(out);
            out.close();
        }catch (Exception e){
            if(exportTempFile != null && exportTempFile.exists()){
                exportTempFile.delete();
            }
            e.printStackTrace();
        }finally {
            if (sxssf != null){
                sxssf.dispose();
            }
        }
        return exportTempFile.exists() ? exportTempFile : null;
    }
}
