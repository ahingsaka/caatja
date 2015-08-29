package com.katspow.caatja.pathutil;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

/**
 * @name LinearPath
 * @memberOf CAAT.PathUtil
 * @extends CAAT.PathUtil.PathSegment
 * @constructor
 */
public class LinearPath extends Path {
    

    /**
     * Straight line segment path between two given points.
     *
     * @constructor
     * @extends CAAT.PathSegment
     */
    public LinearPath() {
        super();
        this.points = new ArrayList<Pt>();
        this.points.add(new Pt());
        this.points.add(new Pt());
        
        this.newPosition=       new Pt(0,0,0); 
    }
    
    /**
     * A collection of points.
     * @type {Array.<CAAT.Math.Point>}
     */
    private List<Pt> points;
    
    /**
     * spare holder for getPosition coordinate return.
     */
    private Pt newPosition;
    
    @Override
    public LinearPath applyAsPath(Director director) {
        // Fixed: Thanks https://github.com/roed
        director.ctx.lineTo( this.points.get(1).x, this.points.get(1).y );
        return this;
    }

    @Override
    public void setPoint(Pt point, int index) {
        if ( index==0 ) {
            this.points.set(0, point);
        } else if ( index==1 ) {
            this.points.set(1, point);
        }
    }

    @Override
    public LinearPath updatePath(Pt point) {
        double x= this.points.get(1).x - this.points.get(0).x;
        double y= this.points.get(1).y - this.points.get(0).y;
        this.length= (int) Math.sqrt( x*x+y*y );

        this.bbox.setEmpty();
        this.bbox.union( this.points.get(0).x, this.points.get(0).y );
        this.bbox.union( this.points.get(1).x, this.points.get(1).y );

        return this;
    }
    
    @Override
    public LinearPath setPoints(List<Pt> points) {
        this.points.set(0, points.get(0));
        this.points.set(1, points.get(1));
        
        // TODO Check
        this.updatePath(null);
        return this;
    }

    /**
     * Set this path segment's starting position.
     * @param x {number}
     * @param y {number}
     */
    public LinearPath setInitialPosition(double x, double y) {
        this.points.get(0).x = x;
        this.points.get(0).y = y;
        this.newPosition.set(x, y);
        return this;
    }
    
    /**
     * Set this path segment's ending position.
     * @param finalX {number}
     * @param finalY {number}
     */
    public LinearPath setFinalPosition(double finalX, double finalY) {
        this.points.get(1).x = finalX;
        this.points.get(1).y = finalY;
        return this;
    }
    
    /**
     * @inheritDoc
     */
    public Pt endCurvePosition () {
        return this.points.get(1);
    }
    
    /**
     * @inheritsDoc
     */
    public Pt startCurvePosition () {
        return this.points.get(0);
    }
    
    /**
     * @inheritsDoc
     */
    public Pt getPosition (double time) {

        if ( time>1 || time<0 ) {
            time%=1;
        }
        if ( time<0 ) {
            time= 1+time;
        }

        this.newPosition.set(
                    (this.points.get(0).x+(this.points.get(1).x-this.points.get(0).x)*time),
                    (this.points.get(0).y+(this.points.get(1).y-this.points.get(0).y)*time) );

        return this.newPosition;
    }
    
    public Pt getPositionFromLength(double len ) {
        return this.getPosition( len/this.length );
    }
   
    /**
     * Returns initial path segment point's x coordinate.
     * @return {number}
     */
    public double initialPositionX () {
        return this.points.get(0).x;
    }
    
    /**
     * Returns final path segment point's x coordinate.
     * @return {number}
     */
    public double finalPositionX () {
        return this.points.get(1).x;
    }
    
    /**
     * Draws this path segment on screen. Optionally it can draw handles for every control point, in
     * this case, start and ending path segment points.
     * @param director {CAAT.Director}
     * @param bDrawHandles {boolean}
     */
    @Override
    public void paint (Director director, Boolean bDrawHandles) {
        
        CaatjaContext2d ctx= director.ctx;
        
        ctx.save();
        
        ctx.setStrokeStyle(this.color);
        ctx.beginPath();
        ctx.moveTo( this.points.get(0).x, this.points.get(0).y );
        ctx.lineTo( this.points.get(1).x, this.points.get(1).y );
        ctx.stroke();
        
        if ( bDrawHandles ) {
            ctx.setGlobalAlpha(.5);
            ctx.setFillStyle("#7f7f00");
            ctx.beginPath();
            this.drawHandle( ctx, this.points.get(0).x, this.points.get(0).y );
            this.drawHandle( ctx, this.points.get(1).x, this.points.get(1).y );
//            ctx.arc(
//                    this.points.get(0).x,
//                    this.points.get(0).y,
//                    Curve.HANDLE_SIZE/2,
//                    0,
//                    2*Math.PI,
//                    false) ;
//            ctx.arc(
//                    this.points.get(1).x,
//                    this.points.get(1).y,
//                    Curve.HANDLE_SIZE/2,
//                    0,
//                    2*Math.PI,
//                    false) ;
//            ctx.fill();
        }
        
        ctx.restore();
    }
    
    /**
     * Get the number of control points. For this type of path segment, start and
     * ending path segment points. Defaults to 2.
     * @return {number}
     */
    public int numControlPoints () {
        return 2;
    }
    
    /**
     * @inheritsDoc
     */
    public Pt getControlPoint(int index) {
        if ( 0==index ) {
            return this.points.get(0);
        } else if (1==index) {
            return this.points.get(1);
        }
        
        // add by me
        return null;
    }
    
    /**
     * @inheritsDoc
     */
    public List<Pt> getContour (int iSize) {
        
        List<Pt> contour= new ArrayList<Pt>();

        contour.add( this.getPosition(0).clone() );
        contour.add( this.getPosition(1).clone() );

        return contour;
    }

    @Override
    public LinearPath endPath() {
        // Do nothing ?
        return this;
    }

    // Add by me
    @Override
    public LinearPath setColor(String color) {
        return (LinearPath) super.setColor(color);
    }
    
    
}
