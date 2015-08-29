package com.katspow.caatjagwt.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.interfaces.CaatjaRootPanel;

public class CaatjaGwtRootPanel implements CaatjaRootPanel {

	@Override
	public void addCanvas(CaatjaCanvas canvas) {
		RootPanel.get().add(((CaatjaGwtCanvas) canvas).canvas);
	}

    @Override
    public void addCanvas(CaatjaCanvas canvas, int x, int y) {
        RootPanel.get().add(((CaatjaGwtCanvas) canvas).canvas, x, y);
    }

}
