package com.katspow.caatja.core.canvas;

public class CaatjaPattern extends CaatjaColor {
    
    private CaatjaCanvas caatjaCanvas;
    private CaatjaImage imageElement; 
    private String repetition;
    
    public CaatjaImage getImageElement() {
        return imageElement;
    }
    public void setImageElement(CaatjaImage imageElement) {
        this.imageElement = imageElement;
    }
    public String getRepetition() {
        return repetition;
    }
    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }
    
    public CaatjaCanvas getCaatjaCanvas() {
        return caatjaCanvas;
    }
    
    public void setCaatjaCanvas(CaatjaCanvas caatjaCanvas) {
        this.caatjaCanvas = caatjaCanvas;
    }

}
