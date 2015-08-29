package com.katspow.caatjafx;

import javafx.scene.canvas.Canvas;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class CaatjaFxCanvas implements CaatjaCanvas {
	
	public Canvas canvas;
	public CaatjaFxContext2d caatjaFxContext2d;
	
	public CaatjaFxCanvas() {
		canvas = new Canvas();
		caatjaFxContext2d = new CaatjaFxContext2d();
		caatjaFxContext2d.setContext(canvas.getGraphicsContext2D());
	}
	
	public CaatjaFxCanvas(int width, int height) {
		canvas = new Canvas(width, height);
		caatjaFxContext2d = new CaatjaFxContext2d();
		caatjaFxContext2d.setContext(canvas.getGraphicsContext2D());
	}
	
	@Override
	public CaatjaContext2d getContext2d() {
		return caatjaFxContext2d;
	}

	@Override
	public int getCoordinateSpaceHeight() {
		return (int) canvas.getHeight();
	}

	@Override
	public int getCoordinateSpaceWidth() {
		return (int) canvas.getWidth();
	}

	@Override
	public void setCoordinateSpaceHeight(int height) {
		canvas.setHeight(height);
	}

	@Override
	public void setCoordinateSpaceWidth(int width) {
		canvas.setWidth(width);
	}

	@Override
	public String toDataUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
