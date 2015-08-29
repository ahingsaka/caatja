package com.katspow.caatja.math.matrix.transform.context;

import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class TransformRenderingContextClamp implements TransformRenderingContext {

    @Override
    public void call(double[] m, CaatjaContext2d ctx) {
        ctx.transform( m[0], m[3], m[1], m[4], (int) m[2]>>0, (int) m[5]>>0 );
    }

}
