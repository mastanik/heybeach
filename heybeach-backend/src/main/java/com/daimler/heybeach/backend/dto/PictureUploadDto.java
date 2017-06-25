package com.daimler.heybeach.backend.dto;

import java.util.List;

public class PictureUploadDto {
    private List<String> hashtags;

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
