package com.katspow.caatja.math.matrix.transform.contextset;

import com.katspow.caatja.core.canvas.CaatjaContext2d;

public interface TransformRenderingContextSet {
    
    public void call(double[] m, CaatjaContext2d ctx);

}
