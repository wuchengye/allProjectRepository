package com.jingdong.webmagic.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jingdong.webmagic.Model.UserEntity;
import com.jingdong.webmagic.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String getToken(UserEntity user) {
        String token="";
        token= JWT.create().withAudience(user.getUserName())
                .sign(Algorithm.HMAC256(user.getPassWord()));
        return token;
    }

    public UserEntity findUserByName(String userName){
        return userRepository.findUserEntityByUserName(userName);
    }

    public List<UserEntity> repeatUserByName(String userName){
        return userRepository.findAllByUserName(userName);
    }

    public UserEntity insertUser(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    public UserEntity findUserById(Long userId){
        return userRepository.findUserEntityByUserId(userId);
    }
}
