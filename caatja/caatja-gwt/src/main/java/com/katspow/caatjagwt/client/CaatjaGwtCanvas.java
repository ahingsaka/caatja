package com.katspow.caatjagwt.client;

import com.google.gwt.canvas.client.Canvas;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class CaatjaGwtCanvas implements CaatjaCanvas {
	
	public Canvas canvas;
    private CaatjaGwtContext2d caatjaGwtContext2d;
	
	public CaatjaGwtCanvas() {
		canvas = Canvas.createIfSupported();
		caatjaGwtContext2d = new CaatjaGwtContext2d();
		caatjaGwtContext2d.setContext2d(canvas.getContext2d());
	}

	public CaatjaGwtCanvas(int width, int height) {
		canvas = Canvas.createIfSupported();
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		
		caatjaGwtContext2d = new CaatjaGwtContext2d();
        caatjaGwtContext2d.setContext2d(canvas.getContext2d());
	}

	@Override
	public void setCoordinateSpaceWidth(int width) {
		canvas.setCoordinateSpaceWidth(width);
	}

	@Override
	public void setCoordinateSpaceHeight(int height) {
		canvas.setCoordinateSpaceHeight(height);
	}

	@Override
	public CaatjaContext2d getContext2d() {
		return caatjaGwtContext2d;
	}

	@Override
	public int getCoordinateSpaceWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	@Override
	public int getCoordinateSpaceHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	@Override
	public String toDataUrl(String str) {
		return canvas.toDataUrl(str);
	}

}
