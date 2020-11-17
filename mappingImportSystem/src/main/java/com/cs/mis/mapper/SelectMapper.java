package com.cs.mis.mapper;

import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.requestbody.SelectConditionBody;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */

@Mapper
public interface SelectMapper {

    /**
     * 根据查询条件查询excel数据
     * @date 2020-11-17 15:05
     * @param selectConditionBody 查询条件
     * @return list集合
     */
    List<ExcelDataEntity> getData(SelectConditionBody selectConditionBody);
}
