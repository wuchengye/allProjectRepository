package com.jingdong.webmagic.Repository;

import com.jingdong.webmagic.Model.LogEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LogRepository extends PagingAndSortingRepository<LogEntity,Long>, JpaSpecificationExecutor<LogEntity> {

}
