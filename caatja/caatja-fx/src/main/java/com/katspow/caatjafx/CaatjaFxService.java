package com.katspow.caatjafx;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaService;

public class CaatjaFxService implements CaatjaService {

	@Override
	public CaatjaCanvas createCanvas() {
		CaatjaFxCanvas caatjaCanvas = new CaatjaFxCanvas();
		return caatjaCanvas;
	}

	@Override
	public CaatjaCanvas createCanvas(int arg0, int arg1) {
		CaatjaFxCanvas caatjaCanvas = new CaatjaFxCanvas(arg0, arg1);
		return caatjaCanvas;
	}

}
