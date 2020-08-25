package com.jingdong.webmagic.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "price")
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "price_id")
    private Long priceId;
    private Double price;
    private Double referPrice;
    private String haveGift;
    private String preferentialType = "";
    private String preferentialDetail;
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    //@JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;


    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getHaveGift() {
        return haveGift;
    }

    public void setHaveGift(String haveGift) {
        this.haveGift = haveGift;
    }

    public String getPreferentialType() {
        return preferentialType;
    }

    public void setPreferentialType(String preferentialType) {
        this.preferentialType = preferentialType;
    }

    public String getPreferentialDetail() {
        return preferentialDetail;
    }

    public void setPreferentialDetail(String preferentialDetail) {
        this.preferentialDetail = preferentialDetail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public Double getReferPrice() {
        return referPrice;
    }

    public void setReferPrice(Double referPrice) {
        this.referPrice = referPrice;
    }

    @Override
    public String toString() {
        return "PriceEntity{" +
                "priceId=" + priceId +
                ", price=" + price +
                ", referPrice=" + referPrice +
                ", haveGift='" + haveGift + '\'' +
                ", preferentialType='" + preferentialType + '\'' +
                ", preferentialDetail='" + preferentialDetail + '\'' +
                ", date=" + date +
                ", itemEntity=" + itemEntity +
                '}';
    }
}
