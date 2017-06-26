package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

@Entity(table = "picture_status_log")
public class PictureStatusLog {
    @Id
    private Long id;
    @Field(column = "approved_by")
    private Long approvedBy;
    private String comment;
    @Field(column = "status_id")
    private Long statusId;
    private Long timestamp;
    @Field(column = "picture_id")
    @Id(fk = true)
    private Long pictureId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }
}
