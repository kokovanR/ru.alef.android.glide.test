package ru.test.alef;

import android.os.Parcelable;

public class Photo {

    private String image_url;

    public Photo() {

    }
    public Photo(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}