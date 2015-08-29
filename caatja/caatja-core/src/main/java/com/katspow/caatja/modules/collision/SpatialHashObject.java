package com.katspow.caatja.modules.collision;

public class SpatialHashObject {
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public boolean isRectangular() {
        return rectangular;
    }
    public void setRectangular(boolean rectangular) {
        this.rectangular = rectangular;
    }
    
    public SpatialHashObject(String id, int x, int y, int width, int height, boolean rectangular) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rectangular = rectangular;
    }

    String id;
    int x;
    int y;
    int width;
    int height;
    boolean rectangular;

}
