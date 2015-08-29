package com.katspow.caatja.foundation.image;

public class SpriteImageHelper {

    public double x = 0;
    public double y = 0;
    public double width = 0;
    public double height = 0;

    public double u = 0;
    public double v = 0;
    public double u1 = 0;
    public double v1 = 0;
    
    // Add by me
    public Integer xoffset;
    public Integer yoffset;
    public Integer xadvance;
    public int id;
    public Character letter;
    
    public SpriteImageHelper() {
        
    }
    
    public SpriteImageHelper(double x, double y, double w, double h, int iw, int ih) {
        super();
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        
        this.setGL( x/iw, y/ih, (x+w-1)/iw, (y+h-1)/ih );
    }

    public SpriteImageHelper setGL(double d, double e, double f, double g) {
        this.u = d;
        this.v = e;
        this.u1 = f;
        this.v1 = g;
        return this;
    }

}
