package com.cs.mis.service;

import com.cs.mis.entity.UserEntity;
import com.cs.mis.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageInfo getUsersByTypeAndAccount(int userType, String userAccount,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<UserEntity> list = userMapper.getUsersByTypeAndAccount(userType,userAccount);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
