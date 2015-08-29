package com.katspow.caatja.core.image;

import java.util.HashMap;
import java.util.Map;

import com.katspow.caatja.core.canvas.CaatjaImage;

public abstract class CaatjaPreloader {
    
    public CaatjaPreloader() {
        this.caatjaImages = new HashMap<String, CaatjaImage>();
    }

    protected Map<String, String> images = new HashMap<String, String>();
    protected Map<String, CaatjaImage> caatjaImages;
    
    public abstract void addImage(String name, String path);
    
    public void setCaatjaImages(Map<String, CaatjaImage> caatjaImages) {
        this.caatjaImages = caatjaImages;
    }
    
    public Map<String, CaatjaImage> getCaatjaImages() {
        return caatjaImages;
    }
    
    public Map<String, String> getImages() {
        return images;
    }
    
    public void setImages(Map<String, String> images) {
        this.images = images;
    }

}
