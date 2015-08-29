package com.katspow.caatja.math.matrix;

import com.katspow.caatja.math.Pt;

public class Matrix3 {

    /**
     * An Array of 4 Array of 4 numbers.
     */
    private double[][] matrix;
    
    /**
     * An array of 16 numbers.
     */
    private double[] fmatrix;

    /**
     * 
     * Define a matrix to hold three dimensional affine transforms.
     * 
     * @constructor
     */
    public Matrix3() {
        this.matrix = new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

        this.fmatrix = new double[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
    }
    
    public Pt transformCoord (Pt point) {
        double x= point.x;
        double y= point.y;
        double z= point.z;

        point.x= x*this.matrix[0][0] + y*this.matrix[0][1] + z*this.matrix[0][2] + this.matrix[0][3];
        point.y= x*this.matrix[1][0] + y*this.matrix[1][1] + z*this.matrix[1][2] + this.matrix[1][3];
        point.z= x*this.matrix[2][0] + y*this.matrix[2][1] + z*this.matrix[2][2] + this.matrix[2][3];

        return point;
    }
    public Matrix3 initialize (double x0,double y0,double z0,double  x1,double y1,double z1,double  x2,double y2,double z2 ) {
        this.identity( );
        this.matrix[0][0]= x0;
        this.matrix[0][1]= y0;
        this.matrix[0][2]= z0;

        this.matrix[1][0]= x1;
        this.matrix[1][1]= y1;
        this.matrix[1][2]= z1;

        this.matrix[2][0]= x2;
        this.matrix[2][1]= y2;
        this.matrix[2][2]= z2;

        return this;
    }
    public Matrix3 initWithMatrix (double[][] matrixData) {
        this.matrix= matrixData;
        return this;
    }
    
    public double[] flatten() {
        double[] d= this.fmatrix;
        double[][] s= this.matrix;
        d[ 0]= s[0][0];
        d[ 1]= s[1][0];
        d[ 2]= s[2][0];
        d[ 3]= s[3][0];

        d[ 4]= s[0][1];
        d[ 5]= s[1][1];
        d[ 6]= s[2][1];
        d[ 7]= s[2][1];

        d[ 8]= s[0][2];
        d[ 9]= s[1][2];
        d[10]= s[2][2];
        d[11]= s[3][2];

        d[12]= s[0][3];
        d[13]= s[1][3];
        d[14]= s[2][3];
        d[15]= s[3][3];
        
        return this.fmatrix;
    }

    /**
     * Set this matrix to identity matrix.
     * @return this
     */
    public Matrix3 identity () {
        for( int i=0; i<4; i++ ) {
            for( int j=0; j<4; j++ ) {
                this.matrix[i][j]= (i==j) ? 1.0 : 0.0;
            }
        }

        return this;
    }
    /**
     * Get this matri'x internal representation data. The bakced structure is a 4x4 array of number.
     */
    public double[][] getMatrix () {
        return this.matrix;
    }
    /**
     * Multiply this matrix by a created rotation matrix. The rotation matrix is set up to rotate around
     * xy axis.
     *
     * @param xy {Number} radians to rotate.
     *
     * @return this
     */
    public Matrix3 rotateXY (double xy ) {
        return this.rotate( xy, 0, 0 );
    }
    /**
     * Multiply this matrix by a created rotation matrix. The rotation matrix is set up to rotate around
     * xz axis.
     *
     * @param xz {Number} radians to rotate.
     *
     * @return this
     */
    public Matrix3 rotateXZ (double xz ) {
        return this.rotate( 0, xz, 0 );
    }
    /**
     * Multiply this matrix by a created rotation matrix. The rotation matrix is set up to rotate aroind
     * yz axis.
     *
     * @param yz {Number} radians to rotate.
     *
     * @return this
     */
    public Matrix3 rotateYZ (double yz ) {
        return this.rotate( 0, 0, yz );
    }
    /**
     * 
     * @param xy
     * @param xz
     * @param yz
     */
    public Matrix3 setRotate (double xy,double xz,double yz ) {
        Matrix3 m= this.rotate(xy,xz,yz);
        this.copy(m);
        return this;
    }
    /**
     * Creates a matrix to represent arbitrary rotations around the given planes.
     * @param xy {number} radians to rotate around xy plane.
     * @param xz {number} radians to rotate around xz plane.
     * @param yz {number} radians to rotate around yz plane.
     *
     * @return {Matrix3} a newly allocated matrix.
     * @static
     */
    public Matrix3 rotate (double xy,double xz,double yz ) {
        Matrix3 res=new Matrix3();
        Matrix3 m;
        double s, c;

        if (xy!=0) {
            m =new Matrix3( );
            s=Math.sin(xy);
            c=Math.cos(xy);
            m.matrix[1][1]=c;
            m.matrix[1][2]=-s;
            m.matrix[2][1]=s;
            m.matrix[2][2]=c;
            res.multiply(m);
        }

        if (xz!=0) {
            m =new Matrix3( );
            s=Math.sin(xz);
            c=Math.cos(xz);
            m.matrix[0][0]=c;
            m.matrix[0][2]=-s;
            m.matrix[2][0]=s;
            m.matrix[2][2]=c;
            res.multiply(m);
        }

        if (yz!=0) {
            m =new Matrix3( );
            s=Math.sin(yz);
            c=Math.cos(yz);
            m.matrix[0][0]=c;
            m.matrix[0][1]=-s;
            m.matrix[1][0]=s;
            m.matrix[1][1]=c;
            res.multiply(m);
        }

        return res;
    }
    /**
     * Creates a new matrix being a copy of this matrix.
     * @return {Matrix3} a newly allocated matrix object.
     */
    public Matrix3 getClone ()   {
        Matrix3 m= new Matrix3( );
        m.copy(this);
        return m;
    }
    /**
     * Multiplies this matrix by another matrix.
     *
     * @param n {Matrix3} a Matrix3 object.
     * @return this
     */
    public Matrix3 multiply (Matrix3 m ) {
        Matrix3 n= this.getClone( );

        double[][] nm= n.matrix;
        double n00= nm[0][0];
        double n01= nm[0][1];
        double n02= nm[0][2];
        double n03= nm[0][3];

        double n10= nm[1][0];
        double n11= nm[1][1];
        double n12= nm[1][2];
        double n13= nm[1][3];

        double n20= nm[2][0];
        double n21= nm[2][1];
        double n22= nm[2][2];
        double n23= nm[2][3];

        double n30= nm[3][0];
        double n31= nm[3][1];
        double n32= nm[3][2];
        double n33= nm[3][3];

        double[][] mm= m.matrix;
        double m00= mm[0][0];
        double m01= mm[0][1];
        double m02= mm[0][2];
        double m03= mm[0][3];

        double m10= mm[1][0];
        double m11= mm[1][1];
        double m12= mm[1][2];
        double m13= mm[1][3];

        double m20= mm[2][0];
        double m21= mm[2][1];
        double m22= mm[2][2];
        double m23= mm[2][3];

        double m30= mm[3][0];
        double m31= mm[3][1];
        double m32= mm[3][2];
        double m33= mm[3][3];

        this.matrix[0][0] = n00*m00 + n01*m10 + n02*m20 + n03*m30;
        this.matrix[0][1] = n00*m01 + n01*m11 + n02*m21 + n03*m31;
        this.matrix[0][2] = n00*m02 + n01*m12 + n02*m22 + n03*m32;
        this.matrix[0][3] = n00*m03 + n01*m13 + n02*m23 + n03*m33;

        this.matrix[1][0] = n10*m00 + n11*m10 + n12*m20 + n13*m30;
        this.matrix[1][1] = n10*m01 + n11*m11 + n12*m21 + n13*m31;
        this.matrix[1][2] = n10*m02 + n11*m12 + n12*m22 + n13*m32;
        this.matrix[1][3] = n10*m03 + n11*m13 + n12*m23 + n13*m33;

        this.matrix[2][0] = n20*m00 + n21*m10 + n22*m20 + n23*m30;
        this.matrix[2][1] = n20*m01 + n21*m11 + n22*m21 + n23*m31;
        this.matrix[2][2] = n20*m02 + n21*m12 + n22*m22 + n23*m32;
        this.matrix[2][3] = n20*m03 + n21*m13 + n22*m23 + n23*m33;

        return this;
    }
    /**
     * Pre multiplies this matrix by a given matrix.
     *
     * @param m {Matrix3} a Matrix3 object.
     *
     * @return this
     */
    public Matrix3 premultiply (Matrix3 m ) {
        Matrix3 n= this.getClone( );

        double[][] nm= n.matrix;
        double n00= nm[0][0];
        double n01= nm[0][1];
        double n02= nm[0][2];
        double n03= nm[0][3];

        double n10= nm[1][0];
        double n11= nm[1][1];
        double n12= nm[1][2];
        double n13= nm[1][3];

        double n20= nm[2][0];
        double n21= nm[2][1];
        double n22= nm[2][2];
        double n23= nm[2][3];

        double n30= nm[3][0];
        double n31= nm[3][1];
        double n32= nm[3][2];
        double n33= nm[3][3];

        double[][] mm= m.matrix;
        double m00= mm[0][0];
        double m01= mm[0][1];
        double m02= mm[0][2];
        double m03= mm[0][3];

        double m10= mm[1][0];
        double m11= mm[1][1];
        double m12= mm[1][2];
        double m13= mm[1][3];

        double m20= mm[2][0];
        double m21= mm[2][1];
        double m22= mm[2][2];
        double m23= mm[2][3];

        double m30= mm[3][0];
        double m31= mm[3][1];
        double m32= mm[3][2];
        double m33= mm[3][3];

        this.matrix[0][0] = n00*m00 + n01*m10 + n02*m20;
        this.matrix[0][1] = n00*m01 + n01*m11 + n02*m21;
        this.matrix[0][2] = n00*m02 + n01*m12 + n02*m22;
        this.matrix[0][3] = n00*m03 + n01*m13 + n02*m23 + n03;
        this.matrix[1][0] = n10*m00 + n11*m10 + n12*m20;
        this.matrix[1][1] = n10*m01 + n11*m11 + n12*m21;
        this.matrix[1][2] = n10*m02 + n11*m12 + n12*m22;
        this.matrix[1][3] = n10*m03 + n11*m13 + n12*m23 + n13;
        this.matrix[2][0] = n20*m00 + n21*m10 + n22*m20;
        this.matrix[2][1] = n20*m01 + n21*m11 + n22*m21;
        this.matrix[2][2] = n20*m02 + n21*m12 + n22*m22;
        this.matrix[2][3] = n20*m03 + n21*m13 + n22*m23 + n23;

        return this;
    }
    /**
     * Set this matrix translation values to be the given parameters.
     *
     * @param x {number} x component of translation point.
     * @param y {number} y component of translation point.
     * @param z {number} z component of translation point.
     *
     * @return this
     */
    public Matrix3 setTranslate (double x,double y,double z ) {
        this.identity();
        this.matrix[0][3]=x;
        this.matrix[1][3]=y;
        this.matrix[2][3]=z;
        return this;
    }
    /**
     * Create a translation matrix.
     * @param x {number}
     * @param y {number}
     * @param z {number}
     * @return {Matrix3} a new matrix.
     */
    public Matrix3 translate (double x, double y, double z ) {
        Matrix3 m= new Matrix3();
        m.setTranslate( x,y,z );
        return m;
    }
    public Matrix3 setScale (double sx,double sy,double sz ) {
        this.identity();
        this.matrix[0][0]= sx;
        this.matrix[1][1]= sy;
        this.matrix[2][2]= sz;
        return this;
    }
    
    public Matrix3 scale (double sx,double sy,double sz ) {
        Matrix3 m= new Matrix3();
        m.setScale(sx,sy,sz);
        return m;
    }
    /**
     * Set this matrix as the rotation matrix around the given axes.
     * @param xy {number} radians of rotation around z axis.
     * @param xz {number} radians of rotation around y axis.
     * @param yz {number} radians of rotation around x axis.
     *
     * @return this
     */
    public Matrix3 rotateModelView (double xy,double xz,double yz ) {
        double sxy= Math.sin( xy );
        double sxz= Math.sin( xz );
        double syz= Math.sin( yz );
        double cxy= Math.cos( xy );
        double cxz= Math.cos( xz );
        double cyz= Math.cos( yz );

        this.matrix[0][0]= cxz*cxy;
        this.matrix[0][1]= -cxz*sxy;
        this.matrix[0][2]= sxz;
        this.matrix[0][3]= 0;
        this.matrix[1][0]= syz*sxz*cxy+sxy*cyz;
        this.matrix[1][1]= cyz*cxy-syz*sxz*sxy;
        this.matrix[1][2]= -syz*cxz;
        this.matrix[1][3]= 0;
        this.matrix[2][0]= syz*sxy-cyz*sxz*cxy;
        this.matrix[2][1]= cyz*sxz*sxy+syz*cxy;
        this.matrix[2][2]= cyz*cxz;
        this.matrix[2][3]= 0;
        this.matrix[3][0]= 0;
        this.matrix[3][1]= 0;
        this.matrix[3][2]= 0;
        this.matrix[3][3]= 1;

        return this;
    }
    /**
     * Copy a given matrix values into this one's.
     * @param m {Matrix} a matrix
     *
     * @return this
     */
    public Matrix3 copy(Matrix3 m) {
        for( int i=0; i<4; i++ ) {
            for( int j=0; j<4; j++ ) {
                this.matrix[i][j]= m.matrix[i][j];
            }
        }

        return this;
    }
    /**
     * Calculate this matrix's determinant.
     * @return {number} matrix determinant.
     */
    public double calculateDeterminant () {

        double[][] mm= this.matrix;
        double m11= mm[0][0], m12= mm[0][1], m13= mm[0][2], m14= mm[0][3],
            m21= mm[1][0], m22= mm[1][1], m23= mm[1][2], m24= mm[1][3],
            m31= mm[2][0], m32= mm[2][1], m33= mm[2][2], m34= mm[2][3],
            m41= mm[3][0], m42= mm[3][1], m43= mm[3][2], m44= mm[3][3];

        return  m14 * m22 * m33 * m41 +
                m12 * m24 * m33 * m41 +
                m14 * m23 * m31 * m42 +
                m13 * m24 * m31 * m42 +

                m13 * m21 * m34 * m42 +
                m11 * m23 * m34 * m42 +
                m14 * m21 * m32 * m43 +
                m11 * m24 * m32 * m43 +

                m13 * m22 * m31 * m44 +
                m12 * m23 * m31 * m44 +
                m12 * m21 * m33 * m44 +
                m11 * m22 * m33 * m44 +

                m14 * m23 * m32 * m41 -
                m13 * m24 * m32 * m41 -
                m13 * m22 * m34 * m41 -
                m12 * m23 * m34 * m41 -

                m14 * m21 * m33 * m42 -
                m11 * m24 * m33 * m42 -
                m14 * m22 * m31 * m43 -
                m12 * m24 * m31 * m43 -

                m12 * m21 * m34 * m43 -
                m11 * m22 * m34 * m43 -
                m13 * m21 * m32 * m44 -
                m11 * m23 * m32 * m44;
    }
    /**
     * Return a new matrix which is this matrix's inverse matrix.
     * @return {Matrix3} a new matrix.
     */
    public Matrix3 getInverse() {
        double[][] mm = this.matrix;
        double m11 = mm[0][0], m12 = mm[0][1], m13 = mm[0][2], m14 = mm[0][3],
            m21 = mm[1][0], m22 = mm[1][1], m23 = mm[1][2], m24 = mm[1][3],
            m31 = mm[2][0], m32 = mm[2][1], m33 = mm[2][2], m34 = mm[2][3],
            m41 = mm[3][0], m42 = mm[3][1], m43 = mm[3][2], m44 = mm[3][3];
        
        Matrix3 m2 = new Matrix3();
        m2.matrix[0][0]= m23*m34*m42 + m24*m32*m43 + m22*m33*m44 - m24*m33*m42 - m22*m34*m43 - m23*m32*m44;
        m2.matrix[0][1]= m14*m33*m42 + m12*m34*m43 + m13*m32*m44 - m12*m33*m44 - m13*m34*m42 - m14*m32*m43;
        m2.matrix[0][2]= m13*m24*m42 + m12*m23*m44 + m14*m22*m43 - m12*m24*m43 - m13*m22*m44 - m14*m23*m42;
        m2.matrix[0][3]= m14*m23*m32 + m12*m24*m33 + m13*m22*m34 - m13*m24*m32 - m14*m22*m33 - m12*m23*m34;

        m2.matrix[1][0]= m24*m33*m41 + m21*m34*m43 + m23*m31*m44 - m23*m34*m41 - m24*m31*m43 - m21*m33*m44;
        m2.matrix[1][1]= m13*m34*m41 + m14*m31*m43 + m11*m33*m44 - m14*m33*m41 - m11*m34*m43 - m13*m31*m44;
        m2.matrix[1][2]= m14*m23*m41 + m11*m24*m43 + m13*m21*m44 - m13*m24*m41 - m14*m21*m43 - m11*m23*m44;
        m2.matrix[1][3]= m13*m24*m31 + m14*m21*m33 + m11*m23*m34 - m14*m23*m31 - m11*m24*m33 - m13*m21*m34;

        m2.matrix[2][0]= m22*m34*m41 + m24*m31*m42 + m21*m32*m44 - m24*m32*m41 - m21*m34*m42 - m22*m31*m44;
        m2.matrix[2][1]= m14*m32*m41 + m11*m34*m42 + m12*m31*m44 - m11*m32*m44 - m12*m34*m41 - m14*m31*m42;
        m2.matrix[2][2]= m13*m24*m41 + m14*m21*m42 + m11*m22*m44 - m14*m22*m41 - m11*m24*m42 - m12*m21*m44;
        m2.matrix[2][3]= m14*m22*m31 + m11*m24*m32 + m12*m21*m34 - m11*m22*m34 - m12*m24*m31 - m14*m21*m32;

        m2.matrix[3][0]= m23*m32*m41 + m21*m33*m42 + m22*m31*m43 - m22*m33*m41 - m23*m31*m42 - m21*m32*m43;
        m2.matrix[3][1]= m12*m33*m41 + m13*m31*m42 + m11*m32*m43 - m13*m32*m41 - m11*m33*m42 - m12*m31*m43;
        m2.matrix[3][2]= m13*m22*m41 + m11*m23*m42 + m12*m21*m43 - m11*m22*m43 - m12*m23*m41 - m13*m21*m42;
        m2.matrix[3][3]= m12*m23*m31 + m13*m21*m32 + m11*m22*m33 - m13*m22*m31 - m11*m23*m32 - m12*m21*m33;
        
        return m2.multiplyScalar( 1/this.calculateDeterminant() );
    }
    /**
     * Multiply this matrix by a scalar.
     * @param scalar {number} scalar value
     *
     * @return this
     */
    public Matrix3 multiplyScalar(double scalar) {
        int i, j;

        for( i=0; i<4; i++ ) {
            for( j=0; j<4; j++ ) {
                this.matrix[i][j]*=scalar;
            }
        }

        return this;
    }


}
