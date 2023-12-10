package com.mehboob.myshadi.model.profilemodel;

import java.util.List;

public class Images {
    private List<String> images;

    public Images(List<String> images) {
        this.images = images;
    }

    public Images() {
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
