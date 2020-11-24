package com.cs.mis.mapper;

import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.requestbody.SelectConditionBody;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据日期和用户id查找一条数据，以确定是否存在
     * @date 2020-11-24 10:13
     * @param date 日期
     * @param userId 用户id集合，正常size应为1
     * @return 对象
     */
    ExcelDataEntity getOneDataByDateAndId(@Param("date") String date, @Param("ids") List<String> userId);

    /**
     * 查找日期之前的用户id倒序的一条数据，以确定是否存在
     * @date 2020-11-24 14:59
     *  @param date 日期
     *  @param userIds 用户ids，size正常为1
     *  @return 对象
     */
    ExcelDataEntity getOneDataBeforeDateByIdDesc(@Param("date")String date,@Param("ids") List<String> userIds);

    /**
     * 根据日期和用户id查找数据
     * @date 2020-11-24 15:59
     * @param date 日期
     * @param userId 用户id
     * @return list
     */
    List<ExcelDataEntity> getDataByDateAndId(String date, String userId);
}
