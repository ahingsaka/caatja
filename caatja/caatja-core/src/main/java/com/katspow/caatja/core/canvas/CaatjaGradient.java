package com.katspow.caatja.core.canvas;

import java.util.ArrayList;
import java.util.List;

public class CaatjaGradient extends CaatjaColor {

	private double x0;
	private double y0;
	private double x1;
	private double y1;
	private List<Double> colorStopValues;
	private List<String> colorStopColors;

	public CaatjaGradient(String strokeStyle) {
	}

	public CaatjaGradient(double i, double j, double width, double k) {
		this.x0 = i;
		this.y0 = j;
		this.x1 = width;
		this.y1 = k;
	}

	public void addColorStop(double colorStopValue, String colorStopColor) {
		if (this.colorStopValues == null) {
			this.colorStopValues = new ArrayList<Double>();
		}
		
		if (this.colorStopColors == null) {
			this.colorStopColors = new ArrayList<String>();
		}
		
		this.colorStopValues.add(colorStopValue);
		this.colorStopColors.add(colorStopColor);
		
	}
	
	public double getX0() {
        return x0;
    }
	
	public double getY0() {
        return y0;
    }
	
	public double getX1() {
        return x1;
    }
	
	public double getY1() {
        return y1;
    }
	
	public List<Double> getColorStopValues() {
        return colorStopValues;
    }
	
	public List<String> getColorStopColors() {
        return colorStopColors;
    }
	
}
