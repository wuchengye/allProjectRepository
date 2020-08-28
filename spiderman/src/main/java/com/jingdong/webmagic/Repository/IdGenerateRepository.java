package com.jingdong.webmagic.Repository;

import com.jingdong.webmagic.Model.IdGenerateEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IdGenerateRepository extends PagingAndSortingRepository<IdGenerateEntity,String>, JpaSpecificationExecutor<IdGenerateEntity> {
    IdGenerateEntity findByItemName(String itemName);
}
