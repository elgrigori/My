package com.volunteer.actionservice.adapters.in.rest.representation;

public class ActionNotificationRequest {
    public Long actionId;
    public String title;
    public String message;

    public ActionNotificationRequest() {
    }

    public ActionNotificationRequest(Long actionId, String title, String message) {
        this.actionId = actionId;
        this.title = title;
        this.message = message;
    }
}
