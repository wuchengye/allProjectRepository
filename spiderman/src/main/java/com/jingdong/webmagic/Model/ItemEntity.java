package com.jingdong.webmagic.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @Column(nullable = false,name = "item_id")
    private Long itemId;
    private String channel;
    private String brand;
    private String model;
    private String specs;
    private String color;
    private String format;
    @Column(name = "item_full_name")
    private String itemFullName;
    @Column(name = "sales_volume")
    private String salesVolume;
    @Column(name = "item_url")
    private String itemUrl;
    @JsonIgnore
    @OneToMany(mappedBy = "itemEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PriceEntity> priceEntityList;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getItemFullName() {
        return itemFullName;
    }

    public void setItemFullName(String itemFullName) {
        this.itemFullName = itemFullName;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public List<PriceEntity> getPriceEntityList() {
        return priceEntityList;
    }

    public void setPriceEntityList(List<PriceEntity> priceEntityList) {
        this.priceEntityList = priceEntityList;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "itemId=" + itemId +
                ", channel='" + channel + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", specs='" + specs + '\'' +
                ", color='" + color + '\'' +
                ", format='" + format + '\'' +
                ", itemFullName='" + itemFullName + '\'' +
                ", salesVolume='" + salesVolume + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                ", priceEntityList=" + priceEntityList +
                '}';
    }
}
