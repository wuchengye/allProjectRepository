package com.cs.mis.controller;

import com.cs.mis.restful.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wcy
 */

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @PostMapping("/importExcel")
    public Result importExcel(){

    }
}
