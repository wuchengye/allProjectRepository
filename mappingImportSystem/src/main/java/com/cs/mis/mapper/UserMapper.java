package com.cs.mis.mapper;

import com.cs.mis.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

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


}
