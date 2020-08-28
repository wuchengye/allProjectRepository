package com.jingdong.webmagic.Service;

import com.jingdong.webmagic.Model.IdGenerateEntity;
import com.jingdong.webmagic.Repository.IdGenerateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGenerateService {
    @Autowired
    private IdGenerateRepository idGenerateRepository;

    public IdGenerateEntity findByItemName(String itemName){
        return idGenerateRepository.findByItemName(itemName);
    }

    public IdGenerateEntity save(IdGenerateEntity idGenerateEntity){
        return idGenerateRepository.save(idGenerateEntity);
    }

}
