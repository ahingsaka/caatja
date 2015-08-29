package com.katspow.caatja.foundation.image;

//import com.google.gwt.dom.client.ImageElement;
import com.katspow.caatja.modules.texturepacker.TexturePage;

/**
 * Add by me
 * 
 * FIXME TODO
 * Find way to merge image and canvas ?
 * 
 */
@Deprecated
public class Image {

//    public ImageElement imageElement;

    public boolean inverted;

    public TexturePage __texturePage;
    
    public double __du;
    
    public double __dv;
    
    public double __tx;
    
    public double __ty;

    public Double __u;

    public Double __v;

    public double __u1;

    public double __v1;

    public int __w;

    public int __h;

    public Integer __gridC;

    public Integer __gridR;
    
    public int getWidth() {
//        return imageElement.getWidth();
    	return -1;
    }
    
    public int getHeight() {
//        return imageElement.getHeight();
    	return -1;
    }
    
//    public Image(ImageElement imageElement) {
//        this.imageElement = imageElement;
//    }

    public String getSrc() {
    	return null;
//        return imageElement.getSrc();
    }

}
