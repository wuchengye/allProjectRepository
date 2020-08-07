package com.jingdong.webmagic.Service;

import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ItemService {

    private ItemRepository itemRepository;
    @Autowired
    public void setItemRepository(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }




    public ItemEntity insertItem(ItemEntity itemEntity){
        return itemRepository.save(itemEntity);
    }

    public List<String> selectBrands(){
        return itemRepository.selectBrands();
    }

    public Map<String,List<String>> selectModels(List<String> brands){
        Map<String,List<String>> map = new HashMap<>();
        for (String brand : brands){
            map.put(brand,itemRepository.selectModels(brand));
        }
        return map;
    }

    public Map<String,List<String>> selectSpecs(List<String> brands,List<String> models){
        Map<String,List<String>> map = new HashMap<>();
        for (String model : models){
            map.put(model,itemRepository.selectSpecs(brands,model));
        }
        return map;
    }

}
