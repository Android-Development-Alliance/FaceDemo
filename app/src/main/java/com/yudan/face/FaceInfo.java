package com.yudan.face;

import android.graphics.Bitmap;


public class FaceInfo {
    String faceName;
    Bitmap faceResource;

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public Bitmap getFaceResource() {
        return faceResource;
    }

    public void setFaceResource(Bitmap faceResource) {
        this.faceResource = faceResource;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "faceName='" + faceName + '\'' +
                ", faceResource=" + faceResource +
                '}';
    }
}
