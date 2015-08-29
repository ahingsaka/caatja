package com.katspow.caatjagwt.client;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.katspow.caatja.core.canvas.CaatjaImage;

public class CaatjaGwtImage extends CaatjaImage {
    
    public ImageElement imageElement;

    @Override
    public int getWidth() {
        return imageElement.getWidth();
    }

    @Override
    public int getHeight() {
        return imageElement.getHeight();
    }

    @Override
    public String getSrc() {
        return imageElement.getSrc();
    }
    
    public void setImageElement(ImageElement imageElement) {
        this.imageElement = imageElement;
    }

    @Override
    public void loadData(String data) {
        String base64Data = "data:image/png;base64,"+data;
        final Image image = new Image();
        image.setUrl(base64Data);
        Element element = image.getElement();
        imageElement = ImageElement.as(element);
    }

}
