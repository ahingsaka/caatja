package com.katspow.caatja.math.matrix;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.matrix.transform.context.TransformRenderingContext;
import com.katspow.caatja.math.matrix.transform.context.TransformRenderingContextClamp;
import com.katspow.caatja.math.matrix.transform.context.TransformRenderingContextNoClamp;
import com.katspow.caatja.math.matrix.transform.contextset.TransformRenderingContextSet;
import com.katspow.caatja.math.matrix.transform.contextset.TransformRenderingContextSetClamp;
import com.katspow.caatja.math.matrix.transform.contextset.TransformRenderingContextSetNoClamp;

/**
 * Manages every Actor affine transformations. Take into account that Canvas'
 * renderingContext computes postive rotation clockwise, so hacks to handle it
 * properly are hardcoded.
 * 
 * Contained classes are CAAT.Matrix and CAAT.MatrixStack.
 * 
 * 
 * 
 **/
public class Matrix {

    /**
     * An array of 9 numbers.
     */
    public double[] matrix;

    public static void setCoordinateClamping(boolean clamp) {
        if (clamp) {
            transformRenderingContext = new TransformRenderingContextClamp();
            transformRenderingContextSet = new TransformRenderingContextSetClamp();
        } else {
            transformRenderingContext = new TransformRenderingContextNoClamp();
            transformRenderingContextSet = new TransformRenderingContextSetNoClamp();
        }
    }


    /**
     * 2D affinetransform matrix represeantation. It includes matrices for
     * <ul>
     * <li>Rotation by any anchor point
     * <li>translation
     * <li>scale by any anchor point
     * </ul>
     * 
     */
    public Matrix() {
        this.matrix = new double[] 
              { 1.0, 0.0, 0.0, 
                0.0, 1.0, 0.0, 
                0.0, 0.0, 1.0 };
        
        // TODO 
//        if ( typeof Float32Array!=="undefined" ) {
//            this.matrix= new Float32Array(this.matrix);
//        }
    }

    /**
     * Transform a point by this matrix. The parameter point will be modified
     * with the transformation values.
     * 
     * @param point
     *            {CAAT.Point}.
     * @return {CAAT.Point} the parameter point.
     */
    public Pt transformCoord(Pt point) {
        double x = point.x;
        double y = point.y;
        
        double[] tm= this.matrix;

        point.x = x * tm[0] + y * tm[1] + tm[2];
        point.y = x * tm[3] + y * tm[4] + tm[5];

        return point;
    }

    /**
     * Create a new rotation matrix and set it up for the specified angle in
     * radians.
     * 
     * @param angle
     *            {number}
     * @return {CAAT.Matrix} a matrix object.
     * 
     * @static
     */
    public static Matrix rotate(double angle) {
        Matrix m = new Matrix();
        m.setRotation(angle);
        return m;
    }

    public Matrix setRotation(double angle) {

        this.identity();
        
        double[] tm= this.matrix;
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        
        tm[0] = c;
        tm[1] = -s;
        tm[3] = s;
        tm[4] = c;

        return this;
    }


    /**
     * Create a scale matrix.
     * 
     * @param scalex
     *            {number} x scale magnitude.
     * @param scaley
     *            {number} y scale magnitude.
     * 
     * @return {CAAT.Matrix} a matrix object.
     * 
     * @static
     */
    public static Matrix scale(double scalex, double scaley) {
        Matrix m = new Matrix();

        m.matrix[0] = scalex;
        m.matrix[4] = scaley;

        return m;
    }

    public Matrix setScale(double scalex, double scaley) {
        this.identity();

        this.matrix[0] = scalex;
        this.matrix[4] = scaley;

        return this;
    }

    /**
     * Create a translation matrix.
     * 
     * @param x
     *            {number} x translation magnitude.
     * @param y
     *            {number} y translation magnitude.
     * 
     * @return {CAAT.Matrix} a matrix object.
     * @static
     * 
     */
    public static Matrix translate(double x, double y) {
        Matrix m = new Matrix();

        m.matrix[2] = x;
        m.matrix[5] = y;

        return m;
    }

    /**
     * Sets this matrix as a translation matrix.
     * 
     * @param x
     * @param y
     */
    public Matrix setTranslate(double x, double y) {
        this.identity();
        
        this.matrix[2] = x;
        this.matrix[5] = y;

        return this;
    }

    /**
     * Copy into this matrix the given matrix values.
     * 
     * @param matrix
     *            {CAAT.Matrix}
     * @return
     * @return this
     */
    public Matrix copy(Matrix matrix) {
        // matrix = matrix.matrix;

        double[] tmatrix = this.matrix;
        tmatrix[0] = matrix.matrix[0];
        tmatrix[1] = matrix.matrix[1];
        tmatrix[2] = matrix.matrix[2];
        tmatrix[3] = matrix.matrix[3];
        tmatrix[4] = matrix.matrix[4];
        tmatrix[5] = matrix.matrix[5];
        tmatrix[6] = matrix.matrix[6];
        tmatrix[7] = matrix.matrix[7];
        tmatrix[8] = matrix.matrix[8];

        return this;
    }

    /**
     * Set this matrix to the identity matrix.
     * 
     * @return
     * @return this
     */
    public Matrix identity() {

        double[] m = this.matrix;
        m[0] = 1.0;
        m[1] = 0.0;
        m[2] = 0.0;

        m[3] = 0.0;
        m[4] = 1.0;
        m[5] = 0.0;

        m[6] = 0.0;
        m[7] = 0.0;
        m[8] = 1.0;

        return this;
    }

    /**
     * Multiply this matrix by a given matrix.
     * 
     * @param m
     *            {CAAT.Matrix}
     * @return
     * @return this
     */
    public Matrix multiply(Matrix m) {
        
        double[] tm = this.matrix;
        double[] mm = m.matrix;

        double tm0= tm[0];
        double tm1= tm[1];
        double tm2= tm[2];
        double tm3= tm[3];
        double tm4= tm[4];
        double tm5= tm[5];
        double tm6= tm[6];
        double tm7= tm[7];
        double tm8= tm[8];

        double mm0= mm[0];
        double mm1= mm[1];
        double mm2= mm[2];
        double mm3= mm[3];
        double mm4= mm[4];
        double mm5= mm[5];
        double mm6= mm[6];
        double mm7= mm[7];
        double mm8= mm[8];

        tm[0]= tm0*mm0 + tm1*mm3 + tm2*mm6;
        tm[1]= tm0*mm1 + tm1*mm4 + tm2*mm7;
        tm[2]= tm0*mm2 + tm1*mm5 + tm2*mm8;
        tm[3]= tm3*mm0 + tm4*mm3 + tm5*mm6;
        tm[4]= tm3*mm1 + tm4*mm4 + tm5*mm7;
        tm[5]= tm3*mm2 + tm4*mm5 + tm5*mm8;
        tm[6]= tm6*mm0 + tm7*mm3 + tm8*mm6;
        tm[7]= tm6*mm1 + tm7*mm4 + tm8*mm7;
        tm[8]= tm6*mm2 + tm7*mm5 + tm8*mm8;

        return this;
    }

    /**
     * Premultiply this matrix by a given matrix.
     * 
     * @param m
     *            {CAAT.Matrix}
     * @return
     * @return this
     */
    public Matrix premultiply(Matrix m) {

        double m00 = m.matrix[0] * this.matrix[0] + m.matrix[1] * this.matrix[3] + m.matrix[2] * this.matrix[6];
        double m01 = m.matrix[0] * this.matrix[1] + m.matrix[1] * this.matrix[4] + m.matrix[2] * this.matrix[7];
        double m02 = m.matrix[0] * this.matrix[2] + m.matrix[1] * this.matrix[5] + m.matrix[2] * this.matrix[8];

        double m10 = m.matrix[3] * this.matrix[0] + m.matrix[4] * this.matrix[3] + m.matrix[5] * this.matrix[6];
        double m11 = m.matrix[3] * this.matrix[1] + m.matrix[4] * this.matrix[4] + m.matrix[5] * this.matrix[7];
        double m12 = m.matrix[3] * this.matrix[2] + m.matrix[4] * this.matrix[5] + m.matrix[5] * this.matrix[8];

        double m20 = m.matrix[6] * this.matrix[0] + m.matrix[7] * this.matrix[3] + m.matrix[8] * this.matrix[6];
        double m21 = m.matrix[6] * this.matrix[1] + m.matrix[7] * this.matrix[4] + m.matrix[8] * this.matrix[7];
        double m22 = m.matrix[6] * this.matrix[2] + m.matrix[7] * this.matrix[5] + m.matrix[8] * this.matrix[8];

        this.matrix[0] = m00;
        this.matrix[1] = m01;
        this.matrix[2] = m02;

        this.matrix[3] = m10;
        this.matrix[4] = m11;
        this.matrix[5] = m12;

        this.matrix[6] = m20;
        this.matrix[7] = m21;
        this.matrix[8] = m22;

        return this;
    }
    
    /**
     * Add by me
     */
    public Matrix getInverse() {
        return getInverse(null);
    }

    /**
     * Creates a new inverse matrix from this matrix.
     * 
     * @return {CAAT.Matrix} an inverse matrix.
     */
    public Matrix getInverse(Matrix out) {
        
        double[] tm = this.matrix;

        double m00 = tm[0];
        double m01 = tm[1];
        double m02 = tm[2];
        double m10 = tm[3];
        double m11 = tm[4];
        double m12 = tm[5];
        double m20 = tm[6];
        double m21 = tm[7];
        double m22 = tm[8];

        Matrix newMatrix = out;
        if (newMatrix == null) {
            newMatrix = new Matrix();
        }

        double determinant = m00 * (m11 * m22 - m21 * m12) - m10 * (m01 * m22 - m21 * m02) + m20
                * (m01 * m12 - m11 * m02);
        if (determinant == 0) {
            return null;
        }

        double[] m = newMatrix.matrix;

        m[0] = m11 * m22 - m12 * m21;
        m[1] = m02 * m21 - m01 * m22;
        m[2] = m01 * m12 - m02 * m11;

        m[3] = m12 * m20 - m10 * m22;
        m[4] = m00 * m22 - m02 * m20;
        m[5] = m02 * m10 - m00 * m12;

        m[6] = m10 * m21 - m11 * m20;
        m[7] = m01 * m20 - m00 * m21;
        m[8] = m00 * m11 - m01 * m10;

        newMatrix.multiplyScalar(1 / determinant);

        return newMatrix;

    }
    
    /**
     * Multiply this matrix by a scalar.
     * @param scalar {number} scalar value
     *
     * @return this
     */
    public Matrix multiplyScalar(double scalar) {
        int i;

        for( i=0; i<9; i++ ) {
            this.matrix[i]*=scalar;
        }

        return this;
    }
    
    public static TransformRenderingContextSet transformRenderingContextSet = new TransformRenderingContextSetClamp();
    
    public static TransformRenderingContext transformRenderingContext = new TransformRenderingContextClamp();
    
    /**
     * Note : slightly different from source
     * 
     * @param ctx
     */
   public Matrix transformRenderingContextSet(CaatjaContext2d ctx) {
       double[] m= this.matrix;
       transformRenderingContextSet.call(m, ctx);
//       ctx.setTransform( m[0], m[3], m[1], m[4], (int) m[2] >> 0, (int) m[5] >> 0);
       return this;
   }
    
    /**
     * Note : slightly different from source
     * 
     * @param ctx
     */
    public Matrix transformRenderingContext(CaatjaContext2d ctx) {
        double[] m= this.matrix;
        transformRenderingContext.call(m, ctx);
//        ctx.setTransform( m[0], m[3], m[1], m[4], (int) m[2] >> 0, (int) m[5] >> 0);
        return this;
    }
    
    public void setModelViewMatrix(double x, double y, double sx, double sy, double r  ) {
        double c, s, _m00, _m01, _m10, _m11;
        double mm0, mm1, mm2, mm3, mm4, mm5;
        double[] mm;

        mm = this.matrix;

        mm0 = 1;
        mm1 = 0;
        mm3 = 0;
        mm4 = 1;

        mm2 = x;
        mm5 = y;

        c = Math.cos(r);
        s = Math.sin(r);
        _m00 = mm0;
        _m01 = mm1;
        _m10 = mm3;
        _m11 = mm4;
        mm0 = _m00 * c + _m01 * s;
        mm1 = -_m00 * s + _m01 * c;
        mm3 = _m10 * c + _m11 * s;
        mm4 = -_m10 * s + _m11 * c;

        // FIXME
//        mm0 = mm0 * this.scaleX;
//        mm1 = mm1 * this.scaleY;
//        mm3 = mm3 * this.scaleX;
//        mm4 = mm4 * this.scaleY;

        mm[0] = mm0;
        mm[1] = mm1;
        mm[2] = mm2;
        mm[3] = mm3;
        mm[4] = mm4;
        mm[5] = mm5;
    }
}
