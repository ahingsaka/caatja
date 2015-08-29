package com.katspow.caatja.math.bezier;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

public class CatmullRom extends Curve {

    /**
     * CatmullRom curves solver implementation.
     * <p>
     * This object manages one single catmull rom segment, that is 4 points.
     * A complete spline should be managed with CAAT.Path.setCatmullRom with a complete list of points.
     *
     * @constructor
     * @extends CAAT.Curve
     */
    public CatmullRom() {
        super();
    }

    /**
     * Set curve control points.
     * 
     * @param p0
     *            <CAAT.Point>
     * @param p1
     *            <CAAT.Point>
     * @param p2
     *            <CAAT.Point>
     * @param p3
     *            <CAAT.Point>
     */
    public void setCurve(Pt p0,Pt p1,Pt p2,Pt p3 ) {

        this.coordlist = new ArrayList<Pt>();

        this.coordlist.add(p0);
        this.coordlist.add(p1);
        this.coordlist.add(p2);
        this.coordlist.add(p3);

        this.cubic = true;
        this.update();
        
        // TODO No return ?
//        return this;
    }

    /**
     * Paint the contour by solving again the entire curve.
     * @param director {CAAT.Director}
     */
    public void paint(Director director) {

        double x1, y1;
        
     // Catmull rom solves from point 1 !!!
        
        x1 = this.coordlist.get(1).x;
        y1 = this.coordlist.get(1).y;

        CaatjaContext2d ctx = director.ctx;

        ctx.save();
        ctx.beginPath();
        ctx.moveTo(x1, y1);

        Pt point = new Pt();

        for (double t = this.k; t <= 1 + this.k; t += this.k) {
            this.solve(point, t);
            ctx.lineTo(point.x, point.y);
        }

        ctx.stroke();
        ctx.restore();

        super.paint(director);
    }

    /**
     * Solves the curve for any given parameter t.
     * @param point {CAAT.Point} the point to store the solved value on the curve.
     * @param t {number} a number in the range 0..1
     */
    public Pt solve(Pt point, double t) {

        List<Pt> c = this.coordlist;

        // Handy from CAKE. Thanks.
        double af = ((-t + 2) * t - 1) * t * 0.5;
        double bf = (((3 * t - 5) * t) * t + 2) * 0.5;
        double cf = ((-3 * t + 4) * t + 1) * t * 0.5;
        double df = ((t - 1) * t * t) * 0.5;

        point.x= c.get(0).x * af + c.get(1).x * bf + c.get(2).x * cf + c.get(3).x * df;
        point.y= c.get(0).y * af + c.get(1).y * bf + c.get(2).y * cf + c.get(3).y * df;

        return point;

    }

    @Override
    public void applyAsPath(Director director) {
        CaatjaContext2d ctx= director.ctx;

        Pt point= new Pt();

        for(double t=this.k;t<=1+this.k;t+=this.k){
            this.solve(point,t);
            ctx.lineTo(point.x,point.y);
        }

        // TODO No return ?
//        return this;
    }
    
    /**
     * Return the first curve control point.
     * @return {CAAT.Point}
     */
    @Override
    public Pt endCurvePosition() {
        return this.coordlist.get(this.coordlist.size()-2 );
    }

    /**
     * Return the last curve control point.
     * @return {CAAT.Point}
     */
    @Override
    public Pt startCurvePosition() {
        return this.coordlist.get( 1 );
    }

    
    
}
