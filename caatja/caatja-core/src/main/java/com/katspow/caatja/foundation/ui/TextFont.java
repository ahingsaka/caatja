package com.katspow.caatja.foundation.ui;

public class TextFont {
    
    public TextFont(Integer size, String unit, String fontFamily) {
        this.size = size;
        this.unit = unit;
        this.fontFamily = fontFamily;
    }
    
    public TextFont(Integer size, String fontFamily) {
        this.size = size;
        this.fontFamily = fontFamily;
    }

    private Integer size;
    
    private String unit = "";
    
    private String fontFamily = "";

    public Integer getSize() {
        return size;
    }

    public String getUnit() {
        return unit;
    }

    public String getFontFamily() {
        return fontFamily;
    }
    

}
