package com.cs.mis.controller;

import com.cs.mis.restful.Result;
import com.cs.mis.service.SelectService;
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
    public Result getData(@RequestBody ){

    }

}
