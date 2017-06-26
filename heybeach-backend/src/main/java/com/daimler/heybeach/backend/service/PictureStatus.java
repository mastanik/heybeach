package com.daimler.heybeach.backend.service;

public enum PictureStatus {
    WAITING_FOR_APPROVAL(0L, "Waiting for approval"),
    APPROVED(1L, "Approved"),
    READY_FOR_MARKETPLACE(2L, "Ready to be published to marketplace"),
    AVAILABLE_IN_MARKETPLACE(3L, "Available in marketplace"),
    DISAPPROVED_FROM_MARKETPLACE(4L, "Disapproved from marketplace");

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

    public static PictureStatus getById(Long id) {
        for (PictureStatus status : PictureStatus.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Bad id provided");
    }
}
