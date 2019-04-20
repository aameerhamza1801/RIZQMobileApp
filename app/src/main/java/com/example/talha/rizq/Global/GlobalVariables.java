package com.example.talha.rizq.Global;

import android.app.Application;

public class GlobalVariables extends Application {
    private Boolean notificationIsActive;
    private Boolean addedNewEvent;

    public Boolean getAddedNewEvent() {
        return addedNewEvent;
    }

    public void setAddedNewEvent(Boolean addedNewEvent) {
        this.addedNewEvent = addedNewEvent;
    }

    public Boolean getNotificationIsActive() {
        return notificationIsActive;
    }

    public void setNotificationIsActive(Boolean notificationIsActive) {
        this.notificationIsActive = notificationIsActive;
    }
}
