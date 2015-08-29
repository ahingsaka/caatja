package com.katspow.caatja.pathutil;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

/**
 * @name RectPath
 * @memberOf CAAT.PathUtil
 * @extends CAAT.PathUtil.PathSegment
 * @constructor
 */
public class RectPath extends Path {
    
    /**
     * A collection of Points.
     * @type {Array.<CAAT.Math.Point>}
     */
    private List<Pt> points;
    
    public int length=             -1;
    
    /**
     * Traverse this path clockwise or counterclockwise (false).
     */
    public boolean cw=                 true;   // should be clock wise traversed ?
    
    /**
     * spare point for calculations
     */
    private Pt newPosition; // spare point for calculations
    
    // Add by me
    private int[] lengths;

    public RectPath() {
        super();
        this.points = new ArrayList<Pt>();
        this.points.add( new Pt() );
        this.points.add( new Pt() );
        this.points.add( new Pt() );
        this.points.add( new Pt() );
        this.points.add( new Pt() );
        
        this.newPosition = new Pt();
        this.lengths = new int[]{0, 0, 0, 0};
    }
    
    @Override
    public RectPath applyAsPath(Director director) {
        CaatjaContext2d ctx = director.ctx;
//        ctx.rect( this.bbox.x, this.bbox.y, this.bbox.width, this.bbox.height );
        if ( this.cw ) {
            ctx.lineTo( this.points.get(0).x, this.points.get(0).y );
            ctx.lineTo( this.points.get(1).x, this.points.get(1).y );
            ctx.lineTo( this.points.get(2).x, this.points.get(2).y );
            ctx.lineTo( this.points.get(3).x, this.points.get(3).y );
            ctx.lineTo( this.points.get(4).x, this.points.get(4).y );
        } else {
            ctx.lineTo( this.points.get(4).x, this.points.get(4).y );
            ctx.lineTo( this.points.get(3).x, this.points.get(3).y );
            ctx.lineTo( this.points.get(2).x, this.points.get(2).y );
            ctx.lineTo( this.points.get(1).x, this.points.get(1).y );
            ctx.lineTo( this.points.get(0).x, this.points.get(0).y );
        }
        return this;
    }

    @Override
    public void setPoint(Pt point, int index) {
        if ( index>=0 && index<this.points.size() ) {
            this.points.set(index, point);
        }
    }

    /**
     * An array of {CAAT.Point} composed of two points.
     * @param points {Array<CAAT.Point>}
     */
    @Override
    public RectPath setPoints(List<Pt> points) {
        this.points.clear();
        this.points.add( points.get(0) );
        this.points.add( new Pt().set(points.get(1).x, points.get(0).y) );
        this.points.add( points.get(1) );
        this.points.add( new Pt().set(points.get(0).x, points.get(1).y) );
        this.points.add( points.get(0).clone() );
        // TODO Check
        this.updatePath(null);

        return this;
    }
    
    public RectPath setClockWise(Boolean cw) {
        this.cw= cw!=null ? cw : true;
        return this;
    }
    
    public boolean isClockWise() {
        return this.cw;
    }
    
    /**
     * Set this path segment's starting position.
     * This method should not be called again after setFinalPosition has been called.
     * @param x {number}
     * @param y {number}
     */
    public RectPath setInitialPosition(double x, double y )   {
        for (Pt pt : this.points) {
            pt.x = x;
            pt.y = y;
        }

        return this;
    }
    /**
     * Set a rectangle from points[0] to (finalX, finalY)
     * @param finalX {number}
     * @param finalY {number}
     */
    public RectPath setFinalPosition(double finalX,double finalY )   {
        this.points.get(2).x= finalX;
        this.points.get(2).y= finalY;

        this.points.get(1).x= finalX;
        this.points.get(1).y= this.points.get(0).y;

        this.points.get(3).x= this.points.get(0).x;
        this.points.get(3).y= finalY;
        
        this.points.get(4).x= this.points.get(0).x;
        this.points.get(4).y= this.points.get(0).y;

     // TODO Check
        this.updatePath(null);
        return this;
    }

    @Override
    public Pt endCurvePosition() {
        return this.points.get(4);
    }

    @Override
    public Pt startCurvePosition() {
        return this.points.get(0);
    }

    @Override
    public Pt getPosition(double time) {
        if ( time>1 || time<0 ) {
            time%=1;
        }
        if ( time<0 ) {
            time= 1+time;
        }

        if ( -1==this.length ) {
            this.newPosition.set(0,0);
        } else {
            double w= this.bbox.width / this.length;
            double h= this.bbox.height / this.length;
            int accTime= 0;
            double[] times;
            int[] segments;
            int index= 0;

            if ( this.cw ) {
                segments= new int[]{0,1,2,3,4};
                times=  new double[]{w,h,w,h};
            } else {
                segments=  new int[]{4,3,2,1,0};
                times= new double[]{h,w,h,w};
            }

            while( index<times.length ) {
                if ( accTime+times[index]<time ) {
                    accTime+= times[index];
                    index++;
                } else {
                    break;
                }
            }
            time-=accTime;

            int p0= segments[index];
            int p1= segments[index+1];

            // index tiene el indice del segmento en tiempo.
            this.newPosition.set(
                    (this.points.get(p0).x + (this.points.get(p1).x - this.points.get(p0).x)*time/times[index]),
                    (this.points.get(p0).y + (this.points.get(p1).y - this.points.get(p0).y)*time/times[index]) );
        }

        return this.newPosition;
    }
    
    /**
     * Returns initial path segment point's x coordinate.
     * @return {number}
     */
    public double initialPositionX() {
        return this.points.get(0).x;
    }
    /**
     * Returns final path segment point's x coordinate.
     * @return {number}
     */
    public double finalPositionX() {
        return this.points.get(2).x;
    }
    
    /**
     * Draws this path segment on screen. Optionally it can draw handles for every control point, in
     * this case, start and ending path segment points.
     * @param director {CAAT.Director}
     * @param bDrawHandles {boolean}
     */
    public void paint(Director director, boolean bDrawHandles) {

        CaatjaContext2d ctx= director.ctx;

        ctx.save();

        ctx.setStrokeStyle(this.color);
        ctx.beginPath();
        ctx.strokeRect(
            this.bbox.x, this.bbox.y,
            this.bbox.width, this.bbox.height );

        if ( bDrawHandles ) {
            ctx.setGlobalAlpha(0.5);
            ctx.setFillStyle("#7f7f00");
            
            for (Pt pt : this.points) {
                this.drawHandle( ctx, pt.x, pt.y );
            }

        }

        ctx.restore();
    }

    /**
     * Get the number of control points. For this type of path segment, start and
     * ending path segment points. Defaults to 2.
     * @return {number}
     */
    @Override
    public int numControlPoints() {
        return this.points.size();
    }

    /**
     * @inheritsDoc
     */
    @Override
    public Pt getControlPoint(int index) {
        return this.points.get(index);
    }
    
    @Override
    public List<Pt> getContour(int iSize) {
        List<Pt> contour=  new ArrayList<Pt>();

        for( int i=0; i<this.points.size(); i++ ) {
            contour.add( this.points.get(i) );
        }

        return contour;
    }
    
    @Override
    public RectPath updatePath(Pt point) {
        if ( point != null) {
            if ( point.equals(this.points.get(0)) ) {
                this.points.get(1).y= point.y;
                this.points.get(3).x= point.x;
            } else if ( point.equals(this.points.get(1) )) {
                this.points.get(0).y= point.y;
                this.points.get(2).x= point.x;
            } else if ( point.equals(this.points.get(2))) {
                this.points.get(3).y= point.y;
                this.points.get(1).x= point.x;
            } else if ( point.equals(this.points.get(3))) {
                this.points.get(0).x= point.x;
                this.points.get(2).y= point.y;
            }
            
            this.points.get(4).x= this.points.get(0).x;
            this.points.get(4).y= this.points.get(0).y;
        }

        this.bbox.setEmpty();
        int minx= Integer.MAX_VALUE, miny= Integer.MAX_VALUE, maxx= -Integer.MIN_VALUE, maxy= -Integer.MIN_VALUE;
        for( int i=0; i<4; i++ ) {
            this.bbox.union( this.points.get(i).x, this.points.get(i).y );
        }

        this.length= (int) (2*this.bbox.width + 2*this.bbox.height);

        this.points.get(0).x= this.bbox.x;
        this.points.get(0).y= this.bbox.y;

        this.points.get(1).x= this.bbox.x+this.bbox.width;
        this.points.get(1).y= this.bbox.y;

        this.points.get(2).x= this.bbox.x + this.bbox.width;
        this.points.get(2).y= this.bbox.y + this.bbox.height;

        this.points.get(3).x= this.bbox.x;
        this.points.get(3).y= this.bbox.y + this.bbox.height;
        
        this.points.get(4).x= this.bbox.x;
        this.points.get(4).y= this.bbox.y;

        return this;
    }
    
    public Pt getPositionFromLength(int iLength ) {
        return this.getPosition( iLength / (this.bbox.width*2 + this.bbox.height*2) );
    }


    @Override
    public RectPath endPath() {
        // TODO Auto-generated method stub
        return null;
    }

}
