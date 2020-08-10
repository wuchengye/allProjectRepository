package com.jingdong.webmagic.Pipeline;


import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Service.ItemService;
import com.jingdong.webmagic.Service.PriceService;
import com.jingdong.webmagic.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JDDetailPipeline implements Pipeline {

    @Autowired
    private ItemService itemService;
    @Autowired
    private PriceService priceService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map map = resultItems.getAll();

        ItemEntity itemEntity = new ItemEntity();
        PriceEntity priceEntity = new PriceEntity();
        try{
            //京东商品ID
            itemEntity.setItemId(Long.valueOf((String) map.get("id")));
            //渠道：京东
            itemEntity.setChannel("京东");
            //手机颜色
            if(map.get("color") != null && map.get("color").toString() != null){
                itemEntity.setColor(map.get("color").toString().trim());
            }
            //手机规格：如8GB+128GB
            if(map.get("specs") != null && map.get("specs").toString() != null){
                itemEntity.setSpecs(map.get("specs").toString().trim());
            }
            //京东商品标题
            if(map.get("title") != null){
                itemEntity.setItemFullName(map.get("title").toString().trim());
            }
            //品牌
            if(map.get("brand") != null){
                itemEntity.setBrand(map.get("brand").toString().trim());
            }
            //型号:如P40
            if(map.get("model") != null){
                itemEntity.setModel(map.get("model").toString().trim());
            }
            //制式
            if (map.get("title") != null){
                itemEntity.setFormat(CommonUtils.regexFormat(map.get("title").toString()));
            }
            //url，商品的京东详情链接
            itemEntity.setItemUrl(resultItems.getRequest().getUrl());
            //累计评价
            if(map.get("salesVolume") != null){
                itemEntity.setSalesVolume(map.get("salesVolume").toString().trim());
            }


            //当前价格
            if(map.get("price") != null){
                priceEntity.setPrice(Double.valueOf(map.get("price").toString()));
            }
            //原始价格
            if(map.get("originPrice").toString() != null){
                priceEntity.setPreferentialType("秒杀 ");
            }
            //优惠券
            List<String> quan = (List<String>) map.get("youhuiquan");
            if(quan != null && quan.size() > 0){
                String temp = priceEntity.getPreferentialType();
                temp+="优惠券";
                priceEntity.setPreferentialType(temp);
            }
            //赠品
            List<String> gift = (List<String>) map.get("gift");
            if(gift != null && gift.size() > 0){
                priceEntity.setHaveGift("是");
            }else {
                priceEntity.setHaveGift("否");
            }
            //优惠+赠品详情
            StringBuffer detail = new StringBuffer();
            //参考价格
            Double referPrice = priceEntity.getPrice();
            List<Map<String,Double>> referList = new ArrayList<>();
            for(String s : quan){
                detail.append(s + ";");
                referList.add(CommonUtils.getNumFromString(s));
            }
            for (String s : gift){
                detail.append(s + ";");
            }
            if(referList.size() > 0){
                referPrice = CommonUtils.calcMinNum(priceEntity.getPrice(),referList);
            }
            priceEntity.setPreferentialDetail(detail.toString());
            priceEntity.setReferPrice(referPrice);

            itemService.insertItem(itemEntity);
            priceEntity.setItemEntity(itemEntity);
            priceService.insertPrice(priceEntity);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
