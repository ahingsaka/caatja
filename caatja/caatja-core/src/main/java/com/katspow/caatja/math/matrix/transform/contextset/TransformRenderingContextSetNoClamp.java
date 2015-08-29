package com.katspow.caatja.math.matrix.transform.contextset;

import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class TransformRenderingContextSetNoClamp implements TransformRenderingContextSet {

    @Override
    public void call(double[] m, CaatjaContext2d ctx) {
        ctx.setTransform( m[0], m[3], m[1], m[4], m[2], m[5] );
    }

}
