package com.jingdong.webmagic.Pipeline;

import com.jingdong.webmagic.Model.IdGenerateEntity;
import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Service.IdGenerateService;
import com.jingdong.webmagic.Service.ItemService;
import com.jingdong.webmagic.Service.PriceService;
import com.jingdong.webmagic.Utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

@Component
public class OfficialDetailPipeline implements Pipeline {

    @Autowired
    private ItemService itemService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private IdGenerateService idGenerateService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map map = resultItems.getAll();
        List<ItemEntity> itemEntityList = (List<ItemEntity>) map.get("item");
        List<PriceEntity> priceEntityList = (List<PriceEntity>) map.get("price");
        String brand = (String) map.get("brand");

        if(itemEntityList.size() == priceEntityList.size()){
            switch (brand){
                case "MIorVIVO":
                    for (int index = 0; index < itemEntityList.size(); index++){
                        itemEntityList.get(index).setChannel("官网");

                        ItemEntity temp = itemEntityList.get(index);
                        String itemName = temp.getChannel()+temp.getBrand()+temp.getModel()+temp.getColor()+temp.getSpecs();
                        IdGenerateEntity idGenerate = idGenerateService.findByItemName(itemName);
                        if(idGenerate == null){
                            idGenerate = new IdGenerateEntity();
                            idGenerate.setItemName(itemName);
                            idGenerate.setItemId(CommonUtils.getRandomNum());
                            idGenerateService.save(idGenerate);
                        }
                        itemEntityList.get(index).setItemId(idGenerate.getItemId());

                        itemService.insertItem(itemEntityList.get(index));
                        priceEntityList.get(index).setItemEntity(itemEntityList.get(index));
                        priceService.insertPrice(priceEntityList.get(index));
                    }
                    break;
                case "OPPO":
                    for (int index = 0; index < itemEntityList.size(); index++){
                        itemEntityList.get(index).setChannel("官网");
                        itemEntityList.get(index).setBrand("OPPO");
                        ItemEntity temp = itemEntityList.get(index);
                        String itemName = temp.getChannel()+temp.getBrand()+temp.getModel()+temp.getColor()+temp.getSpecs();
                        IdGenerateEntity idGenerate = idGenerateService.findByItemName(itemName);
                        if(idGenerate == null){
                            idGenerate = new IdGenerateEntity();
                            idGenerate.setItemName(itemName);
                            idGenerate.setItemId(CommonUtils.getRandomNum());
                            idGenerateService.save(idGenerate);
                        }
                        itemEntityList.get(index).setItemId(idGenerate.getItemId());

                        itemService.insertItem(itemEntityList.get(index));
                        priceEntityList.get(index).setItemEntity(itemEntityList.get(index));
                        priceService.insertPrice(priceEntityList.get(index));
                    }
                    break;
                default:
                    for (int index = 0; index < itemEntityList.size(); index++){
                        itemEntityList.get(index).setChannel("官网");
                        itemService.insertItem(itemEntityList.get(index));
                        priceEntityList.get(index).setItemEntity(itemEntityList.get(index));
                        priceService.insertPrice(priceEntityList.get(index));
                    }
                    break;
            }
        }
    }
}
