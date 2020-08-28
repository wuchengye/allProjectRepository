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

    public List<String> selectChannels(){
        return itemRepository.selectChannels();
    }

    public Map<String,List<String>> selectBrands(List<String> channels){
        Map<String,List<String>> map = new HashMap<>();
        for (String channel : channels){
            map.put(channel,itemRepository.selectBrands(channel));
        }
        return map;
    }

    public Map<String,List<String>> selectModels(List<String> channels,List<String> brands){
        Map<String,List<String>> map = new HashMap<>();
        for (String brand : brands){
            map.put(brand,itemRepository.selectModels(channels,brand));
        }
        return map;
    }

    public Map<String,List<String>> selectSpecs(List<String> channels,List<String> brands,List<String> models){
        Map<String,List<String>> map = new HashMap<>();
        for (String model : models){
            map.put(model,itemRepository.selectSpecs(channels,brands,model));
        }
        return map;
    }

}
