package com.jingdong.webmagic.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "log")
public class LogEntity {
    @Id
    @Column(nullable = false,name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private String ip;
    @Column(name = "user_name")
    private String userName;
    private String method;
    @Lob
    @Column(columnDefinition="text")
    private String params;
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "logId=" + logId +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", date=" + date +
                '}';
    }
}
