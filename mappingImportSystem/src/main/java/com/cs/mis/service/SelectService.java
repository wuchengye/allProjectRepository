package com.cs.mis.service;

import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.mapper.SelectMapper;
import com.cs.mis.requestbody.SelectConditionBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wcy
 */

@Service
public class SelectService {
    @Autowired
    private SelectMapper selectMapper;

    public PageInfo getData(SelectConditionBody selectConditionBody) {
        PageHelper.startPage(selectConditionBody.getPageNum(),selectConditionBody.getPageSize());
        List<ExcelDataEntity> list = selectMapper.getData(selectConditionBody);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
