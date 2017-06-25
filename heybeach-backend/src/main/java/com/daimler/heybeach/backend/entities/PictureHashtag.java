package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

@Entity(table = "picture_hashtags")
public class PictureHashtag {

    @Id(fk = true)
    @Field(column = "picture_id")
    private Long pictureId;

    @Id(fk = true)
    @Field(column = "hashtag_id")
    private Long hashtagId;

    public PictureHashtag() {
    }

    public PictureHashtag(Long pictureId, Long hashtagId) {
        this.pictureId = pictureId;
        this.hashtagId = hashtagId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(Long hashtagId) {
        this.hashtagId = hashtagId;
    }
}
