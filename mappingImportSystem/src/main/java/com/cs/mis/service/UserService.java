package com.cs.mis.service;

import com.cs.mis.entity.UserEntity;
import com.cs.mis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wcy
 */

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserEntity getUserByAccount(String userAccount){
        return userMapper.getUserByAccount(userAccount);
    }

    public int insertUser(UserEntity userEntity) {
        return userMapper.insertUser(userEntity);
    }

    public int updatePwdByAccount(String userAccount, String newPwd) {
        return userMapper.updatePwdByAccount(userAccount,newPwd);
    }

    public int updateStatusByAccount(String userAccount, int newStatus) {
        return userMapper.updateStatusByAccount(userAccount,newStatus);
    }
}
