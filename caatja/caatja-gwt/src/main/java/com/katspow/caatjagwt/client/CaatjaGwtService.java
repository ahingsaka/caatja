package com.katspow.caatjagwt.client;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaService;

public class CaatjaGwtService implements CaatjaService {

	@Override
	public CaatjaCanvas createCanvas() {
		CaatjaGwtCanvas caatjaCanvas = new CaatjaGwtCanvas();
		return caatjaCanvas;
	}

	@Override
	public CaatjaCanvas createCanvas(int width, int height) {
		CaatjaGwtCanvas caatjaCanvas = new CaatjaGwtCanvas(width, height);
		return caatjaCanvas;
	}

}
