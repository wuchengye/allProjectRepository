package com.cs.mis.controller;

import com.auth0.jwt.JWT;
import com.cs.mis.annotation.PassToken;
import com.cs.mis.restful.Result;
import com.cs.mis.service.ExcelService;
import com.cs.mis.service.SelectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/excel")
public class ExcelController {

    public static final String XLSX_REGEX = "^.+\\.(?i)(xlsx)$";
    //public static final String TEMP_PATH = "/home/mis/temp/";
    //public static final String TXT_DAT_PATH = "/home/mis/day/";
    //public static final String TXT_MONTH_PATH = "/home/mis/month/";
    public static final String TEMP_PATH = "d:\\home\\mis\\";
    public static final String TXT_DAT_PATH = "d:\\home\\mis\\day\\";
    public static final String TXT_MONTH_PATH = "d:\\home\\mis\\month\\";

    @Autowired
    private ExcelService excelService;
    @Autowired
    private SelectService selectService;

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入接口")
    @ApiImplicitParam(name = "isTodayData", value = "当天数据：false为重跑前月数据", required = true, dataType = "boolean")
    public Result importExcel(@RequestBody MultipartFile file,boolean isTodayData){
        if(!file.getOriginalFilename().matches(XLSX_REGEX)){
            return Result.failure();
        }
        //获取token中的account
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String userAccount = JWT.decode(token).getAudience().get(0);

        //存入临时文件夹
        if(!new File(TEMP_PATH).exists()){
            new File(TEMP_PATH).mkdirs();
        }
        File temp = new File(TEMP_PATH + userAccount + String.valueOf(System.currentTimeMillis()) + file.getOriginalFilename());
        try {
            file.transferTo(temp);
        } catch (IOException e) {
            e.printStackTrace();
            temp.delete();
            return Result.failure();
        }
        //读取excel中的数据，并进行校验
        try {
            excelService.getExcelDataAndCheck(temp);
        }catch (Exception e){
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
        //读取excel中的数据，并转存txt和分批导入数据库
        try {
            excelService.saveExcelData(temp,isTodayData,userAccount);
        }catch (Exception e){
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/templateDownload")
    @ApiOperation(value = "导入模板下载接口")
    @PassToken
    public void templateDownload(HttpServletResponse response) throws IOException {
        //获取静态文件路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/importTemplate.xlsx");
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode("importTemplate.xlsx","UTF-8"));
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
}
