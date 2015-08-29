package com.katspow.caatja.math.matrix.transform.context;

import com.katspow.caatja.core.canvas.CaatjaContext2d;

public interface TransformRenderingContext {
    
    public void call(double[] m, CaatjaContext2d ctx);

}
