package com.cs.mis.mapper;

import com.cs.mis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wcy
 */

@Mapper
public interface UserMapper {

    /**
     * 通过account获取User对象.
     * @date 2020-11-02 11:29
     * @param userAccount 唯一账号
     * @return User对象
     */
    UserEntity getUserByAccount(String userAccount);

    /**
     * 插入UserEntity对象.
     * @date 2020-11-04 16:35
     * @param userEntity 对象
     * @return 数据库受影响列数
     */
    int insertUser(UserEntity userEntity);

    /**
     * 通过account更新密码
     * @date 2020-11-04 21:22
     * @param userAccount 唯一账号
     * @param newPwd 要更新的密码
     * @return 数据库受影响列数
     */
    int updatePwdByAccount(String userAccount, String newPwd);

    /**
     * 通过account更新状态信息
     * @date 2020-11-04 23:07
     * @param userAccount 唯一账号
     * @param newStatus 新的状态
     * @return 数据库受影响列数
     */
    int updateStatusByAccount(String userAccount, int newStatus);

    /**
     * 通过type或再根据账号查找所有用户的账号的基本信息.可直接返回前端
     * @date 2020-11-05 09:59
     * @param userType 用户类型
     * @param userAccount 账号
     * @return 返回查找到的对象列表
     */
    List<UserEntity> getUsersByTypeAndAccount(int userType,String userAccount);
}
