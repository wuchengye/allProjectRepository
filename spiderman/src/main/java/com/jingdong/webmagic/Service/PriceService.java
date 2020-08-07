package com.jingdong.webmagic.Service;

import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PriceService {

    private PriceRepository priceRepository;
    @Autowired
    public void setPriceRepository(PriceRepository priceRepository){
        this.priceRepository = priceRepository;
    }




    public PriceEntity insertPrice(PriceEntity priceEntity){
        return priceRepository.save(priceEntity);
    }


    //分页查看
    public Page<PriceEntity> getInfoByWhere(List<String> brands, List<String> models, List<String> specs, Double priceLow, Double priceHigh, String dateStart, String dateEnd, Pageable page) {
        Page<PriceEntity> pageList = priceRepository.findAll(new Specification<PriceEntity>(){
            @Override
            public Predicate toPredicate(Root<PriceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> conditions = new ArrayList<>();
                if(brands != null && brands.size() > 0){
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("brand");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for (String brand : brands){
                        in.value(brand);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(models != null && models.size() > 0){
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("model");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for(String model : models){
                        in.value(model);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(specs != null && specs.size() > 0){
                    System.out.println("specs=======" + specs.get(0).toString());
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("specs");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for (String s : specs){
                        in.value(s);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(priceLow != null){
                    conditions.add(criteriaBuilder.and(criteriaBuilder.ge(root.get("price"),priceLow)));
                }
                if(priceHigh != null){
                    conditions.add(criteriaBuilder.and(criteriaBuilder.le(root.get("price"),priceHigh)));
                }
                if(dateStart != null || dateEnd != null ){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = new Date(946656000000L);;
                    Date end = new Date();
                    if(dateStart != null && !dateStart.equals("")){
                        try {
                            start = format.parse(dateStart);
                        } catch (ParseException e) {}
                    }
                    if(dateEnd != null && !dateEnd.equals("")){
                        try {
                            end = format.parse(dateEnd);
                        } catch (ParseException e) {}
                    }
                    conditions.add(criteriaBuilder.and(criteriaBuilder.between(root.<Date>get("date"),start,end)));
                }
                Order order = criteriaBuilder.desc(root.get("date"));
                Predicate[] parr = new Predicate[conditions.size()];
                parr=conditions.toArray(parr);
                return criteriaQuery.orderBy(order).where(parr).getRestriction();
                //return criteriaBuilder.and(parr);
            }
        },page);
        return pageList;
    }

    //不分页导出
    public List<PriceEntity> exportItemPrice(List<String> brands, List<String> models, List<String> specs, Double priceLow, Double priceHigh, String dateStart, String dateEnd) {
        List<PriceEntity> pageList = priceRepository.findAll(new Specification<PriceEntity>(){
            @Override
            public Predicate toPredicate(Root<PriceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> conditions = new ArrayList<>();
                if(brands != null && brands.size() > 0){
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("brand");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for (String brand : brands){
                        in.value(brand);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(models != null && models.size() > 0){
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("model");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for(String model : models){
                        in.value(model);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(specs != null && specs.size() > 0){
                    System.out.println("specs=======" + specs.get(0).toString());
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> path = join.get("specs");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    for (String s : specs){
                        in.value(s);
                    }
                    conditions.add(criteriaBuilder.and(in));
                }

                if(priceLow != null){
                    conditions.add(criteriaBuilder.and(criteriaBuilder.ge(root.get("price"),priceLow)));
                }
                if(priceHigh != null){
                    conditions.add(criteriaBuilder.and(criteriaBuilder.le(root.get("price"),priceHigh)));
                }
                if(dateStart != null || dateEnd != null ){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = new Date(946656000000L);;
                    Date end = new Date();
                    if(dateStart != null && !dateStart.equals("")){
                        try {
                            start = format.parse(dateStart);
                        } catch (ParseException e) {}
                    }
                    if(dateEnd != null && !dateEnd.equals("")){
                        try {
                            end = format.parse(dateEnd);
                            end.setTime(end.getTime() + 86400000L);
                        } catch (ParseException e) {}
                    }
                    conditions.add(criteriaBuilder.and(criteriaBuilder.between(root.<Date>get("date"),start,end)));
                }
                Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                Order ascBrand = criteriaBuilder.asc(join.get("brand"));
                Order ascModel = criteriaBuilder.asc(join.get("model"));
                Order descorder = criteriaBuilder.desc(root.get("date"));
                Predicate[] parr = new Predicate[conditions.size()];
                parr=conditions.toArray(parr);
                return criteriaQuery.orderBy(ascBrand,ascModel,descorder).where(parr).getRestriction();
            }
        });
        return pageList;
    }

    //导入excel形式查找
    public List<PriceEntity> findByImportExcel(List<Map<String,String>> mapList){
        List<PriceEntity> result = new ArrayList<>();
        for (Map<String,String> map : mapList){
            List<PriceEntity> pageList = priceRepository.findAll(new Specification<PriceEntity>(){
                @Override
                public Predicate toPredicate(Root<PriceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> conditions = new ArrayList<>();
                    Join<ItemEntity,PriceEntity> join = root.join("itemEntity",JoinType.LEFT);
                    Path<String> pathBrand = join.get("brand");
                    CriteriaBuilder.In<Object> inBrand = criteriaBuilder.in(pathBrand);
                    inBrand.value(map.get("brand"));
                    conditions.add(criteriaBuilder.and(inBrand));
                    Path<String> pathModel = join.get("model");
                    CriteriaBuilder.In<Object> inModel = criteriaBuilder.in(pathModel);
                    inModel.value(map.get("model"));
                    conditions.add(criteriaBuilder.and(inModel));
                    if(map.get("specs") != null && !map.get("specs").equals("")){
                        Path<String> pathSpecs = join.get("specs");
                        CriteriaBuilder.In<Object> inSpecs = criteriaBuilder.in(pathSpecs);
                        inSpecs.value(map.get("specs"));
                        conditions.add(criteriaBuilder.and(inSpecs));
                    }
                    if(map.get("format") != null && !map.get("format").equals("")){
                        Path<String> pathFormat = join.get("format");
                        CriteriaBuilder.In<Object> inFormat = criteriaBuilder.in(pathFormat);
                        inFormat.value(map.get("format"));
                        conditions.add(criteriaBuilder.and(inFormat));
                    }
                    if(map.get("date1") != null && map.get("date2") != null){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date start = format.parse(map.get("date1"));
                            Date end = format.parse(map.get("date2"));
                            conditions.add(criteriaBuilder.and(criteriaBuilder.between(root.<Date>get("date"),start,end)));
                        }catch (ParseException e){}
                    }
                    Order ascBrand = criteriaBuilder.asc(join.get("brand"));
                    Order ascModel = criteriaBuilder.asc(join.get("model"));
                    Order descorder = criteriaBuilder.desc(root.get("date"));
                    Predicate[] parr = new Predicate[conditions.size()];
                    parr=conditions.toArray(parr);
                    return criteriaQuery.orderBy(ascBrand,ascModel,descorder).where(parr).getRestriction();
                }
            });
            result.addAll(pageList);
        }
        return result;
    }


    public List<PriceEntity> findByItemId(Long itemId){
        Date now = new Date();
        //90天之内的数据
        Date trendStart = new Date(now.getTime() - 7776000000L);
        return priceRepository.findAllByItemEntity_ItemIdAndDateAfterOrderByDateAsc(itemId,trendStart);
    }
}
