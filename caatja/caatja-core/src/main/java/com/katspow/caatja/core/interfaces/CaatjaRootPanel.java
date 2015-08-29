package com.katspow.caatja.core.interfaces;

import com.katspow.caatja.core.canvas.CaatjaCanvas;

public interface CaatjaRootPanel {

    public void addCanvas(CaatjaCanvas canvas);

    public void addCanvas(CaatjaCanvas canvas, int x, int y);
}
