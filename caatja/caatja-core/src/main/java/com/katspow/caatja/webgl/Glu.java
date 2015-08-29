package com.katspow.caatja.webgl;

import com.katspow.caatja.math.matrix.Matrix3;

public class Glu {

    /**
     * Create a perspective matrix.
     *
     * @param fovy
     * @param aspect
     * @param znear
     * @param zfar
     * @param viewportHeight
     */
    public static Matrix3 makePerspective(double fovy, double aspect, double znear, double zfar, double viewportHeight) {
        double ymax = znear * Math.tan(fovy * Math.PI / 360.0);
        double ymin = -ymax;
        double xmin = ymin * aspect;
        double xmax = ymax * aspect;

        return makeFrustum(xmin, xmax, ymin, ymax, znear, zfar, viewportHeight);
    }

    /**
     * Create a matrix for a frustum.
     *
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param znear
     * @param zfar
     * @param viewportHeight
     */
    public static Matrix3 makeFrustum(double left, double right, double bottom, double top, double znear, double zfar,
            double viewportHeight) {
        double X = 2 * znear / (right - left);
        double Y = 2 * znear / (top - bottom);
        double A = (right + left) / (right - left);
        double B = (top + bottom) / (top - bottom);
        double C = -(zfar + znear) / (zfar - znear);
        double D = -2 * zfar * znear / (zfar - znear);

        return new Matrix3().initWithMatrix(new double[][] { { X, 0, A, -viewportHeight / 2 },
                { 0, -Y, B, viewportHeight / 2 }, { 0, 0, C, D }, { 0, 0, -1, 0 } });
    }

    /**
     * Create an orthogonal projection matrix.
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param znear
     * @param zfar
     */
    public static Matrix3 makeOrtho(double left, double right, double bottom, double top, double znear, double zfar) {
        double tx = - (right + left) / (right - left);
        double ty = - (right + left) / (right - left);
        double tz = -(zfar + znear) / (zfar - znear);

        return new Matrix3().initWithMatrix(new double[][] { { 2 / (right - left), 0, 0, tx },
                { 0, 2 / (top - bottom), 0, ty }, { 0, 0, -2 / (zfar - znear), tz }, { 0, 0, 0, 1 } });
    }

}
