package com.cs.mis.controller;

import com.cs.mis.annotation.PassToken;
import com.cs.mis.restful.Result;
import com.cs.mis.requestbody.SelectConditionBody;
import com.cs.mis.scheduled.ReportCrontab;
import com.cs.mis.service.SelectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/select")
public class SelectController {
    @Autowired
    private SelectService selectService;
    @Autowired
    private ReportCrontab reportCrontab;

    @PostMapping("/getData")
    @ApiOperation(value = "查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "开始页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "Integer")
    })
    public Result getData(@RequestBody SelectConditionBody selectConditionBody,int pageNum, int pageSize){
        return Result.success(selectService.getData(selectConditionBody,pageNum,pageSize));
    }

    @GetMapping("/testCron")
    @PassToken
    public void testCron() throws IOException {
        reportCrontab.autoCreateDayForms();
    }

}
