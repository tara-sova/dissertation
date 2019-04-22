package com.example.polina.adminapp;

public class ClientFeature {
    private boolean beAttended = false;

    public ClientFeature() {}

    public ClientFeature(boolean beAttendedActionPermission) {
        setBeAttended(beAttendedActionPermission);
    }

    public void activateFeature() {
        setBeAttended(true);
    }

    private void setBeAttended(boolean beAttendedActionPermission) {
        this.beAttended = beAttendedActionPermission;
    }

    public boolean isFeatureActivated() {
        return beAttended;
    }
}

