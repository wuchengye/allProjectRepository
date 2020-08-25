package com.jingdong.webmagic.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jingdong.webmagic.Annotation.LogOperator;
import com.jingdong.webmagic.Annotation.UserLoginToken;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.RESTful.Result;
import com.jingdong.webmagic.Service.ItemService;
import com.jingdong.webmagic.Service.PriceService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Excel")
public class ExcelController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private PriceService priceService;

    @PostMapping("/uploadExcel")
    @UserLoginToken
    @LogOperator(method = "导入Excel文档查找")
    @ResponseBody
    public Result uploadExcel(@RequestBody MultipartFile file) throws Exception{
        if(file.isEmpty()){
            return Result.failure();
        }
        if (!file.getOriginalFilename().matches("^.+\\.(?i)(xls)$") &&
                !file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")) {
            return Result.failure();
        }
        List<Map<String,String>> itemList = getExcelData(file);
        //return Result.success(itemList);
        return Result.success(priceService.findByImportExcel(itemList),itemList);
    }

    @RequestMapping("/downloadTemplate")
    @LogOperator(method = "下载模板")
    @UserLoginToken
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        //获取静态文件路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/京东爬虫导入示例.xlsx");
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode("导入示例.xlsx","UTF-8"));
        response.setContentType("content-type:octet-stream");
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer ,0 , len);
        }
        inputStream.close();
        outputStream.close();
    }

    @RequestMapping("/exportExcel")
    @LogOperator(method = "手机条目导出Excel")
    @UserLoginToken
    public void exportExcel(@RequestParam(name = "_brands",required = false) List<String> _brands,
                            @RequestParam(name = "_models",required = false) List<String> _models,
                            @RequestParam(name = "_specs",required = false) List<String> _specs,
                            @RequestParam(name = "_price_low",required = false) Double _price_low,
                            @RequestParam(name = "_price_high",required = false) Double _price_high,
                            @RequestParam(name = "_date_start",required = false) String _date_start,
                            @RequestParam(name = "_date_end",required = false) String _date_end,
                            @RequestParam(name = "importExcelData",required = false) List<String> importExcelData,
                            HttpServletResponse response) throws IOException {
        List<PriceEntity> list = new ArrayList<>();
        if(importExcelData == null || importExcelData.size() == 0 ){
            list =  priceService.exportItemPrice(_brands,_models,_specs,_price_low,_price_high,_date_start,_date_end);
        }else {
            Gson gson = new Gson();
            List<Map<String,String >> mapList = new ArrayList<>();
            for (String m : importExcelData){
                //参数是以逗号为来区分的list，前端把逗号全部替换了，不然出错转换不了map
                m = m.replaceAll("@#",",");
                Map<String, String> map = new HashMap<>();
                map = gson.fromJson(m,map.getClass());
                mapList.add(map);
            }
            list = priceService.findByImportExcel(mapList);
        }
        HSSFWorkbook workbook = getWorkbook(list);
        String fileName = "Item.xls";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }


    public List<Map<String,String>> getExcelData(MultipartFile file) {
        List<Map<String,String>> list = new ArrayList<>();
        //判断excel版本
        boolean isExcel2003 = true;
        if(file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$")){
            isExcel2003 = false;
        }
        Workbook wb = null;
        try {
            InputStream fis=file.getInputStream();
            if (isExcel2003) {
                wb = new HSSFWorkbook(fis);
            } else  {
                wb = new XSSFWorkbook(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 总共有多少张sheet表
        int sheetnum = wb.getNumberOfSheets();
        for(int i = 0;i < sheetnum; i++){
            Sheet sheet = wb.getSheetAt(i);
            // 表头数据
            Row headrow = sheet.getRow(0);
            if (headrow != null) {
                // 总行数
                int rowNum = sheet.getPhysicalNumberOfRows();
                System.out.println("总共" + rowNum + "行");
                // 总列数
                //int colNum = headrow.getPhysicalNumberOfCells();
                // 判断工作表是否为空
                if (rowNum == 0) {
                    continue;
                }
                // 循环行
                for (int j = 1; j <= rowNum; j++) {
                    Row row = sheet.getRow(j);
                    if (null != row) {
                        if(row.getCell(0) == null || row.getCell(1) == null){
                            continue;
                        }
                        Map<String,String> map = new HashMap<>();
                        map.put("brand",row.getCell(0).getStringCellValue());
                        map.put("model",row.getCell(1).getStringCellValue());
                        if(row.getCell(2) != null){
                            map.put("specs",row.getCell(2).getStringCellValue());
                        }
                        if(row.getCell(3) != null){
                            map.put("format",row.getCell(3).getStringCellValue());
                        }
                        if(row.getCell(4) != null){
                            String[] time = row.getCell(4).getStringCellValue().split("-");
                            int index = 1;
                            for (String s : time){
                                map.put("date" + index,s.replace("/","-"));
                                index++;
                            }
                        }
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    public HSSFWorkbook getWorkbook(List<PriceEntity> list){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("条目信息");
        int rowNum = 1;
        String[] heads = {"时间","品牌","型号","规格","颜色","制式","标价","参考价格","优惠详情","优惠形式","累计评价","是否赠品","渠道","链接地址"};

        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<heads.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(heads[i]);
            cell.setCellValue(text);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(PriceEntity p : list){
            HSSFRow row1 = sheet.createRow(rowNum);
            String date = format.format(p.getDate());
            row1.createCell(0).setCellValue(date);
            row1.createCell(1).setCellValue(p.getItemEntity().getBrand());
            row1.createCell(2).setCellValue(p.getItemEntity().getModel());
            row1.createCell(3).setCellValue(p.getItemEntity().getSpecs());
            row1.createCell(4).setCellValue(p.getItemEntity().getColor());
            row1.createCell(5).setCellValue(p.getItemEntity().getFormat());
            if(p.getPrice() != null){
                row1.createCell(6).setCellValue(p.getPrice());
            }
            if(p.getReferPrice() != null){
                row1.createCell(7).setCellValue(p.getReferPrice());
            }
            row1.createCell(8).setCellValue(p.getPreferentialDetail());
            row1.createCell(9).setCellValue(p.getPreferentialType());
            row1.createCell(10).setCellValue(p.getItemEntity().getSalesVolume());
            row1.createCell(11).setCellValue(p.getHaveGift());
            row1.createCell(12).setCellValue(p.getItemEntity().getChannel());
            row1.createCell(13).setCellValue(p.getItemEntity().getItemUrl());
            rowNum++;
        }
        return workbook;
    }

}
