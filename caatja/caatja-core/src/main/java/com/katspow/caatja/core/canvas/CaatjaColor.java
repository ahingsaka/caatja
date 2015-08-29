package com.katspow.caatja.core.canvas;

public class CaatjaColor {
	
	private String strokeStyle;
	
	public CaatjaColor() {
	    
	}
	
	private CaatjaColor(String strokeStyle) {
		this.strokeStyle = strokeStyle;
	}
	
	public String getStrokeStyle() {
        return strokeStyle;
    }
	
	public static CaatjaColor valueOf(String style) {
	    return new CaatjaColor(style);
	}
}
