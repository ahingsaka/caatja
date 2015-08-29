package com.katspow.caatjagwt.client;

import com.google.gwt.canvas.dom.client.ImageData;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaImageData;

public class CaatjaGwtImageData implements CaatjaImageData {
	
	private ImageData imageData;

	@Override
	public CaatjaCanvasPixelArray getData() {
		CaatjaGwtCanvasPixelArray caatjaGwtCanvasPixelArray = new CaatjaGwtCanvasPixelArray();
		caatjaGwtCanvasPixelArray.setCanvasPixelArray(imageData.getData());
		return caatjaGwtCanvasPixelArray;
	}

	@Override
	public int getWidth() {
		return imageData.getWidth();
	}

	@Override
	public int getHeight() {
		return imageData.getHeight();
	}
	
	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}
	
	public ImageData getImageData() {
		return imageData;
	}

}
