package com.cs.mis.mapper;

import com.cs.mis.entity.ExcelDataEntity;
import javafx.beans.binding.ObjectExpression;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wcy
 */
@Mapper
public interface ExcelMapper {

    /**
     * 通过账号和日期，删除当前日期该账号的excel数据
     * @date 2020年11月13日09点35分
     * @param userAccount 账号
     * @param date 日期
     */
    void deleteByAccountAndDate(String userAccount, String date);

    /**
     * 插入list的excel数据，和date日期以及id用户账号的map
     * @date 2020-11-16 14:26
     * @param map 以上数据集合
     * @return 成功条数
     */
    int insertListWithDateAndId(@Param("map") Map<String, Object> map);
}
