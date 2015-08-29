package com.katspow.caatja.math.matrix;

import java.util.Stack;

public class MatrixStack {

    private Stack<Matrix> stack;
    private Stack<Integer> saved;

    /**
     * Implementation of a matrix stack. Each CAAT.Actor instance contains a MatrixStack to hold of its affine
     * transformations. The Canvas rendering context will be fed with this matrix stack values to keep a homogeneous
     * transformation process.
     *
     * @constructor
     */
    public MatrixStack() {
        this.stack = new Stack<Matrix>();
        this.saved = new Stack<Integer>();
    }

    /**
     * Add a matrix to the transformation stack.
     * @return 
     * @return this
     */
    public MatrixStack pushMatrix(Matrix matrix) {
        this.stack.push(matrix);
        return this;
    }

    /**
     * Remove the last matrix from this stack.
     * @return {CAAT.Matrix} the poped matrix.
     */
    public Matrix popMatrix() {
        return this.stack.pop();
    }

    /**
     * Create a restoration point of pushed matrices.
     * @return this
     */
    public MatrixStack save() {
        this.saved.push(this.stack.size());
        return this;
    }

    /**
     * Restore from the last restoration point set.
     * @return 
     * @return this
     */
    public MatrixStack restore() {
        Integer pos = this.saved.pop();
        while (this.stack.size() != pos) {
            this.popMatrix();
        }
        
        return this;
    }

    /**
     * Return the concatenation (multiplication) matrix of all the matrices contained in this stack.
     * @return {CAAT.Matrix} a new matrix.
     */
    public Matrix getMatrix() {
        Matrix matrix= new Matrix();

        for( int i=0; i<this.stack.size(); i++ ) {
            Matrix matrixStack= this.stack.get(i);
            matrix.multiply( matrixStack );
        }

        return matrix;
    }
    

}
