package com.example.polina.adminapp.Features;

public class AttendedClientsFeature {
    private boolean attendedClients = false;

    public AttendedClientsFeature() {}

    public AttendedClientsFeature(boolean attendedClientsShowingPermission) {
        setAttendedClients(attendedClientsShowingPermission);
    }

    public void activateFeature() {
        setAttendedClients(true);
    }

    private void setAttendedClients(boolean attendedClientsShowingPermission) {
        this.attendedClients = attendedClientsShowingPermission;
    }

    public boolean isFeatureActivated() {
        return attendedClients;
    }
}