package com.jingdong.webmagic.Repository;

import com.jingdong.webmagic.Model.PriceEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface PriceRepository extends PagingAndSortingRepository<PriceEntity,Long>, JpaSpecificationExecutor<PriceEntity> {
    List<PriceEntity> findAllByItemEntity_ItemIdAndDateAfterOrderByDateAsc(Long itenId, Date date);
}
