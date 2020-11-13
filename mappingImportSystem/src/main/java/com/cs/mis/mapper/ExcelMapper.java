package com.cs.mis.mapper;

import org.apache.ibatis.annotations.Mapper;

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
}
