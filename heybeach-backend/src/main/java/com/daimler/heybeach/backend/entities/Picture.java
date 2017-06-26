package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

import java.math.BigDecimal;

@Entity(table = "pictures")
public class Picture {
    @Id(generated = true)
    private Long id;
    private String path;
    @Id(fk = true)
    @Field(column = "user_id")
    private Long userId;
    private Long timestamp;
    @Field(column = "picture_status")
    private Long pictureStatus;
    private String title;
    private String description;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPictureStatus() {
        return pictureStatus;
    }

    public void setPictureStatus(Long pictureStatus) {
        this.pictureStatus = pictureStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
