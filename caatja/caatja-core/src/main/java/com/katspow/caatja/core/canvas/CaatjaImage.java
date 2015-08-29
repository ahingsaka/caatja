package com.katspow.caatja.core.canvas;

import com.katspow.caatja.modules.texturepacker.TexturePage;



/**
 * FIXME TODO
 * Find way to merge image and canvas ?
 *
 */
public abstract class CaatjaImage {
    
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
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract String getSrc();
    
    public abstract void loadData(String data);

}
