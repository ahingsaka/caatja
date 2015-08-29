package com.katspow.caatja.core.canvas;

public interface CaatjaCanvas {

	void setCoordinateSpaceWidth(int width);

	void setCoordinateSpaceHeight(int height);

	CaatjaContext2d getContext2d();

	int getCoordinateSpaceWidth();

	int getCoordinateSpaceHeight();

	String toDataUrl(String str);

}
