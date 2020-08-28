package com.jingdong.webmagic.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "id_generate")
public class IdGenerateEntity {
    @Id
    @Column(nullable = false,name = "item_name")
    private String itemName;
    @Column(nullable = false,name = "item_id")
    private Long itemId;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
