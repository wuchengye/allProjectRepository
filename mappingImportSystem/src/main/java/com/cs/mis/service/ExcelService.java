package com.cs.mis.service;

import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.mapper.ExcelMapper;
import com.cs.mis.utils.DateUtil;
import com.monitorjbl.xlsx.StreamingReader;
import io.netty.util.internal.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author wcy
 */

@Service
public class ExcelService {
    @Autowired
    private ExcelMapper excelMapper;

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
            
        }else {
            //获取上月最后一天日期
            String lastMonthDay = DateUtil.getDateOfLastMonth();


        }
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

}
