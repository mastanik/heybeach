package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

@Entity(table = "likes")
public class Like {

    @Id(fk = true)
    @Field(column = "user_id")
    private Long userId;
    @Id(fk = true)
    @Field(column = "picture_id")
    private Long pictureId;

    public Like() {
    }

    public Like(Long userId, Long pictureId) {
        this.userId = userId;
        this.pictureId = pictureId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }
}
