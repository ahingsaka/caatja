package com.katspow.caatjagwt.client;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;

public class CaatjaGwtCanvasPixelArray implements CaatjaCanvasPixelArray {
	
	private CanvasPixelArray canvasPixelArray;

	@Override
	public void set(int i, int r) {
		canvasPixelArray.set(i, r);
	}

	@Override
	public int get(int i) {
		return canvasPixelArray.get(i);
	}
	
	public void setCanvasPixelArray(CanvasPixelArray canvasPixelArray) {
		this.canvasPixelArray = canvasPixelArray;
	}

}
