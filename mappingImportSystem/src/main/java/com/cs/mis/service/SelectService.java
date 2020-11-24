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

    public PageInfo getData(SelectConditionBody selectConditionBody,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExcelDataEntity> list = selectMapper.getData(selectConditionBody);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public List getDataWithoutPage(SelectConditionBody selectConditionBody){
        List<ExcelDataEntity> list = selectMapper.getData(selectConditionBody);
        return list;
    }

    public ExcelDataEntity getOneDataByDateAndId(String date,List<String> userIds) {
        ExcelDataEntity entity = selectMapper.getOneDataByDateAndId(date,userIds);
        return entity;
    }

    public ExcelDataEntity getOneDataBeforeDateByIdDesc(String date, List<String> userIds) {
        ExcelDataEntity entity = selectMapper.getOneDataBeforeDateByIdDesc(date,userIds);
        return entity;
    }

    public List<ExcelDataEntity> getDataByDateAndId(String date, String userId) {
        List<ExcelDataEntity> entityList = selectMapper.getDataByDateAndId(date,userId);
        return entityList;
    }
}
