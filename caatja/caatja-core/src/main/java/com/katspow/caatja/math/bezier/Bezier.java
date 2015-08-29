package com.katspow.caatja.math.bezier;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

public class Bezier extends Curve {

    /**
     * Bezier quadric and cubic curves implementation.
     *
     * @constructor
     * @extends CAAT.Curve
     */
    public Bezier() {
        super();
        // Add by me
        this.cubic = false;
    }

    /**
     * TODO Check !
     * This curbe is cubic or quadratic bezier ?
     */
    // cubic: false;
    
    @Override
    public void applyAsPath(Director director) {
        
        List<Pt> cc = this.coordlist;
        
        if ( this.cubic ) {
            director.ctx.bezierCurveTo(
                cc.get(1).x,
                cc.get(1).y,
                cc.get(2).x,
                cc.get(2).y,
                cc.get(3).x,
                cc.get(3).y
            );
        } else {
            director.ctx.quadraticCurveTo(
                cc.get(1).x,
                cc.get(1).y,
                cc.get(2).x,
                cc.get(2).y
            );
        }
        // TODO No return ?
//        return this;
    }
    
    public boolean isQuadric() {
        return !this.cubic;
    }
    
    public boolean isCubic() {
        return this.cubic;
    }
    
    /**
     * Set this curve as a cubic bezier defined by the given four control points.
     * @param cp0x {number}
     * @param cp0y {number}
     * @param cp1x {number}
     * @param cp1y {number}
     * @param cp2x {number}
     * @param cp2y {number}
     * @param cp3x {number}
     * @param cp3y {number}
     */
    public Bezier setCubic(double cp0x, double cp0y, double cp1x, double cp1y, double cp2x, double cp2y, double cp3x,
            double cp3y) {

        this.coordlist = new ArrayList<Pt>();

        this.coordlist.add(new Pt().set(cp0x, cp0y));
        this.coordlist.add(new Pt().set(cp1x, cp1y));
        this.coordlist.add(new Pt().set(cp2x, cp2y));
        this.coordlist.add(new Pt().set(cp3x, cp3y));

        this.cubic = true;
        this.update();
        
        return this;
    }

    /**
     * Set this curve as a quadric bezier defined by the three control points.
     * @param cp0x {number}
     * @param cp0y {number}
     * @param cp1x {number}
     * @param cp1y {number}
     * @param cp2x {number}
     * @param cp2y {number}
     */
    public Bezier setQuadric(double cp0x, double cp0y, double cp1x, double cp1y, double cp2x, double cp2y) {

        this.coordlist = new ArrayList<Pt>();

        this.coordlist.add(new Pt().set(cp0x, cp0y));
        this.coordlist.add(new Pt().set(cp1x, cp1y));
        this.coordlist.add(new Pt().set(cp2x, cp2y));

        this.cubic = false;
        this.update();
        
        return this;
    }
    
    public void setPoints(List<Pt> points ) throws Exception {
        if ( points.size()==3 ) {
            this.coordlist= points;
            this.cubic= false;
            this.update();
        } else if (points.size()==4 ) {
            this.coordlist= points;
            this.cubic= true;
            this.update();
        } else {
            throw new Exception("points must be an array of 3 or 4 CAAT.Point instances.");
        }

        // TODO No return ?
//        return this;
    }

    /**
     * Paint this curve.
     * @param director {CAAT.Director}
     */
    public void paint(Director director) {
        if (this.cubic) {
            this.paintCubic(director);
        } else {
            this.paintCuadric(director);
        }

        super.paint(director);

    }

    /**
     * Paint this quadric Bezier curve. Each time the curve is drawn it will be solved again from 0 to 1 with
     * CAAT.Bezier.k increments.
     *
     * @param director {CAAT.Director}
     * @private
     */
    public void paintCuadric(Director director) {
        double x1, y1;
        x1 = this.coordlist.get(0).x;
        y1 = this.coordlist.get(0).y;

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

    }

    /**
     * Paint this cubic Bezier curve. Each time the curve is drawn it will be solved again from 0 to 1 with
     * CAAT.Bezier.k increments.
     *
     * @param director {CAAT.Director}
     * @private
     */
    public void paintCubic(Director director) {

        double x1, y1;
        x1 = this.coordlist.get(0).x;
        y1 = this.coordlist.get(0).y;

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
    }

    /**
     * Solves the curve for any given parameter t.
     * @param point {CAAT.Point} the point to store the solved value on the curve.
     * @param t {number} a number in the range 0..1
     */
    public Pt solve(Pt point, double t) {
        if (this.cubic) {
            return this.solveCubic(point, t);
        } else {
            return this.solveQuadric(point, t);
        }
    }

    /**
     * Solves a cubic Bezier.
     * @param point {CAAT.Point} the point to store the solved value on the curve.
     * @param t {number} the value to solve the curve for.
     */
    public Pt solveCubic(Pt point, double t) {

        double t2 = t * t;
        double t3 = t * t2;
        
        List<Pt> cl = this.coordlist;
        Pt cl0 = cl.get(0);
        Pt cl1 = cl.get(1);
        Pt cl2 = cl.get(2);
        Pt cl3 = cl.get(3);
        
        point.x = (cl0.x + t * (-cl0.x * 3 + t * (3 * cl0.x - cl0.x * t))) + t
                * (3 * cl1.x + t * (-6 * cl1.x + cl1.x * 3 * t)) + t2
                * (cl2.x * 3 - cl2.x * 3 * t) + cl3.x * t3;

        point.y = (cl0.y + t * (-cl0.y * 3 + t * (3 * cl0.y - cl0.y * t))) + t
                * (3 * cl1.y + t * (-6 * cl1.y + cl1.y * 3 * t)) + t2
                * (cl2.y * 3 - cl2.y * 3 * t) + cl3.y * t3;

        return point;
    }

    /**
     * Solves a quadric Bezier.
     * @param point {CAAT.Point} the point to store the solved value on the curve.
     * @param t {number} the value to solve the curve for.
     */
    public Pt solveQuadric(Pt point, double t) {
        List<Pt> cl = this.coordlist;
        Pt cl0 = cl.get(0);
        Pt cl1 = cl.get(1);
        Pt cl2 = cl.get(2);
        double t1 = 1 - t;
        
        point.x = t1 * t1 * cl0.x + 2 * t1 * t * cl1.x + t * t
                * cl2.x;
        point.y = t1 * t1 * cl0.y + 2 * t1 * t * cl1.y + t * t
                * cl2.y;

        return point;
    }
}
