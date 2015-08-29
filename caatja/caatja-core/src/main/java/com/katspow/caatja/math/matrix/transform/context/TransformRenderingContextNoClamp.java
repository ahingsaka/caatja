package com.katspow.caatja.math.matrix.transform.context;

import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class TransformRenderingContextNoClamp implements TransformRenderingContext {

    @Override
    public void call(double[] m, CaatjaContext2d ctx) {
        ctx.transform( m[0], m[3], m[1], m[4], m[2], m[5] );
    }


}
