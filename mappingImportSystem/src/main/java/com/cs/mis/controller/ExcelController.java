package com.cs.mis.controller;

import com.auth0.jwt.JWT;
import com.cs.mis.annotation.PassToken;
import com.cs.mis.restful.Result;
import com.cs.mis.service.ExcelService;
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
    public static final String TEMP_PATH = "d:\\home\\";

    @Autowired
    private ExcelService excelService;

    @PostMapping("/importExcel")
    @PassToken
    public Result importExcel(@RequestBody MultipartFile file,boolean isTodayData){
        if(!file.getOriginalFilename().matches(XLSX_REGEX)){
            return Result.failure();
        }
        //获取token中的account
        /*ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String userAccount = JWT.decode(token).getAudience().get(0);*/

        String userAccount = "test";

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
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.failure(e.getMessage());
        }
    }

    @GetMapping("/templateDownload")
    @PassToken
    public void templateDownload(HttpServletResponse response) throws IOException {
        //获取静态文件路径
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/人员工号映射导入模板.xlsx");
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode("人员工号映射导入模板.xlsx","UTF-8"));
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
