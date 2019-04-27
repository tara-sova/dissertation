package com.example.polina.adminapp.Features;

public class LectureFeature {
    private boolean lectureEdition = false;

    public LectureFeature() {}

    public LectureFeature(boolean lectureEditionPermission) {
        setLectureEdition(lectureEditionPermission);
    }

    public void activateFeature() {
        setLectureEdition(true);
    }

    private void setLectureEdition(boolean lectureEditionPermission) {
        this.lectureEdition = lectureEditionPermission;
    }

    public boolean isFeatureActivated() {
        return lectureEdition;
    }
}
