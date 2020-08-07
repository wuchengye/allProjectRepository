package com.jingdong.webmagic.Repository;

import com.jingdong.webmagic.Model.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<UserEntity,Long> {
    UserEntity findUserEntityByUserName(String userName);
    List<UserEntity> findAllByUserName(String userName);
    UserEntity findUserEntityByUserId(Long userId);

}
