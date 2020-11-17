package com.cs.mis.controller;

import com.cs.mis.annotation.PassToken;
import com.cs.mis.restful.Result;
import com.cs.mis.requestbody.SelectConditionBody;
import com.cs.mis.service.SelectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wcy
 */
@RestController
@RequestMapping("/select")
public class SelectController {
    @Autowired
    private SelectService selectService;

    @PostMapping("/getData")
    @PassToken
    @ApiOperation(value = "查询接口")
    public Result getData(@RequestBody SelectConditionBody selectConditionBody){
        return Result.success(selectService.getData(selectConditionBody));
    }

}
