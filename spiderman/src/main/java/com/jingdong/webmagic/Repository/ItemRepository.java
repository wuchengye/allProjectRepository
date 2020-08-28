package com.jingdong.webmagic.Repository;

import com.jingdong.webmagic.Model.ItemEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ItemRepository extends PagingAndSortingRepository<ItemEntity,Long>,JpaSpecificationExecutor<ItemEntity> {
    @Query(value = "select distinct channel from item",nativeQuery = true)
    List<String> selectChannels();

    @Query(value = "select distinct brand from item where channel = :channels",nativeQuery = true)
    List<String> selectBrands(@Param("channels") String channels);

    @Query(value = "select distinct model from item where channel in (:channels) and brand = :brands",nativeQuery = true)
    List<String> selectModels(@Param("channels") List<String> channels,@Param("brands") String brands);

    @Query(value = "select distinct specs from item where channel in (:channels) and brand in (:brands) and model = :models",nativeQuery = true)
    List<String> selectSpecs(@Param("channels") List<String> channels,@Param("brands") List<String> brands,@Param("models") String models);

}
