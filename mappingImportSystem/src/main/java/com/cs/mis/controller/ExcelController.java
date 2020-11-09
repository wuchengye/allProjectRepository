package com.cs.mis.controller;

import com.auth0.jwt.JWT;
import com.cs.mis.restful.Result;
import com.cs.mis.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/excel")
public class ExcelController {

    public static final String XLSX_REGEX = "^.+\\.(?i)(xlsx)$";
    public static final String TEMP_PATH = "/home/mis/temp/";

    @Autowired
    private ExcelService excelService;

    @PostMapping("/importExcel")
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
        //提取并校验
        try {
            excelService.getExcelDataAndCheck(temp);
        }catch (Exception e){
            return Result.failure(e.getMessage());
        }
    }
}
