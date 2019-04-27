package com.example.polina.adminapp.Features;

public class ServerFeature {
    private boolean serverConnection = false;

    public ServerFeature() {}

    public ServerFeature(boolean serverConnectionPermission) {
        setLectureEdition(serverConnectionPermission);
    }

    public void activateFeature() {
        setLectureEdition(true);
    }

    private void setLectureEdition(boolean serverConnectionPermission) {
        this.serverConnection = serverConnectionPermission;
    }

    public boolean isFeatureActivated() {
        return serverConnection;
    }
}
