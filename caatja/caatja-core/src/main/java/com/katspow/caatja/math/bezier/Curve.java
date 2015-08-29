package com.katspow.caatja.math.bezier;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;

/**
 * Classes to solve and draw curves.
 * Curve is the superclass of
 *  + Bezier (quadric and cubic)
 *  + Catmull Rom
 *
 **/

/**
*
* Curve class is the base for all curve solvers available in CAAT.
*
* @constructor
*/
public class Curve {

    /**
     * A collection of CAAT.Math.Point objects.
     */
    public List<Pt> coordlist = null;
    
    /**
     * Minimun solver step.
     */
    public double k = 0.05;
    
    /**
     * Curve length.
     */
    private double length = -1;
    
    /**
     * If this segments belongs to an interactive path, the handlers will be this size.
     */
    public static int HANDLE_SIZE = 20;
    
    /**
     * Draw interactive handlers ?
     */
    public boolean drawHandles = true;

    // Add by me
    protected boolean cubic;

    /**
     * Paint the curve control points.
     * @param director {CAAT.Director}
     */
    public void paint(Director director) {
        
        if ( false==this.drawHandles ) {
            return;
        }

        List<Pt> cl= this.coordlist;
        CaatjaContext2d ctx = director.ctx;

        // control points
        ctx.save();
        ctx.beginPath();

        ctx.setStrokeStyle("#a0a0a0");
        ctx.moveTo(cl.get(0).x, cl.get(0).y);
        ctx.lineTo(cl.get(1).x, cl.get(1).y);
        ctx.stroke();
        if (this.cubic) {
            ctx.moveTo(cl.get(2).x, cl.get(2).y);
            ctx.lineTo(cl.get(3).x, cl.get(3).y);
            ctx.stroke();
        }

        ctx.setGlobalAlpha(.5);
        for (int i = 0; i < this.coordlist.size(); i++) {
            ctx.setFillStyle("#7f7f00");
            double w= Curve.HANDLE_SIZE/2;
            ctx.beginPath();
            ctx.arc( cl.get(i).x, cl.get(i).y, w, 0, 2*Math.PI, false );
            ctx.fill();
        }

        ctx.restore();
    }

    /**
     * Signal the curve has been modified and recalculate curve length.
     */
    public void update() {
        this.calcLength();
    }

    /**
     * This method must be overriden by subclasses. It is called whenever the curve must be solved for some time=t.
     * The t parameter must be in the range 0..1
     * @param point {CAAT.Point} to store curve solution for t.
     * @param t {number}
     * @return {CAAT.Point} the point parameter.
     */
    public Pt solve(Pt point, double t) {
        return null;
    }
    
    /**
     * Get an array of points defining the curve contour.
     * @param numSamples {number} number of segments to get.
     */
    public List<Pt> getContour(int numSamples) {
        List<Pt> contour= new ArrayList<Pt>();

        for(int i=0; i<=numSamples; i++ ) {
            Pt point= new Pt();
            this.solve( point, i/numSamples );
            contour.add(point);
        }

        return contour;
    }

    /**
     * Calculates a curve bounding box.
     *
     * @param rectangle {CAAT.Rectangle} a rectangle to hold the bounding box.
     * @return {CAAT.Rectangle} the rectangle parameter.
     */
    public Rectangle getBoundingBox(Rectangle rectangle) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        
     // thanks yodesoft.com for spotting the first point is out of the BB
        rectangle.setEmpty();
        rectangle.union( this.coordlist.get(0).x, this.coordlist.get(0).y );

        Pt pt = new Pt();
        for (double t = this.k; t <= 1 + this.k; t += this.k) {
            this.solve(pt, t);
            rectangle.union(pt.x, pt.y);
        }

        return rectangle;
    }

    /**
     * Calculate the curve length by incrementally solving the curve every substep=CAAT.Curve.k. This value defaults
     * to .05 so at least 20 iterations will be performed.
     *
     * @return {number} the approximate curve length.
     */
    public double calcLength() {
        double x1, y1;
        x1 = this.coordlist.get(0).x;
        y1 = this.coordlist.get(0).y;
        double llength = 0;
        Pt pt = new Pt();
        for (double t = this.k; t <= 1 + this.k; t += this.k) {
            this.solve(pt, t);
            llength += Math.sqrt((pt.x - x1) * (pt.x - x1) + (pt.y - y1) * (pt.y - y1));
            x1 = pt.x;
            y1 = pt.y;
        }

        this.length = llength;
        return llength;
    }

    /**
     * Return the cached curve length.
     * @return {number} the cached curve length.
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Return the first curve control point.
     * @param point {CAAT.Point}
     * @return {CAAT.Point}
     */
    public Pt endCurvePosition() {
        return this.coordlist.get(this.coordlist.size() - 1);
    }

    /**
     * Return the last curve control point.
     * @param point {CAAT.Point}
     * @return {CAAT.Point}
     */
    public Pt startCurvePosition() {
        return this.coordlist.get(0);
    }
    
    public void setPoints(List<Pt> points ) throws Exception {
    }

    public void setPoint(Pt point, int index ) {
        if ( index>=0 && index<this.coordlist.size() ) {
            this.coordlist.set(index, point);
        }
    }

    /**
    *
    * @param director <=CAAT.Director>
    */
    public void applyAsPath(Director director) {
        // TODO Auto-generated method stub
        
    }

}
