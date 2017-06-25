package com.daimler.heybeach.backend.service;

public enum PictureStatus {
    WAITING_FOR_APPROVAL(0L, "Waiting for approval"), APPROVED(1L, "Approved");

    private Long id;
    private String name;

    private PictureStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }
}
