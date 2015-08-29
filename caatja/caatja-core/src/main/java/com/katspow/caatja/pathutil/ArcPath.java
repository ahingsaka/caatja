package com.katspow.caatja.pathutil;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.math.matrix.Matrix;

/**
 * Circular path. Complete 360 degrees.
 *
 */
public class ArcPath extends PathSegment {
    
    /**
     * @name ArcPath
     * @memberOf CAAT.PathUtil
     * @extends CAAT.PathUtil.PathSegment
     * @constructor
     */
    public ArcPath() {
        super();
        this.points = new ArrayList<Pt>();
        this.points.add(new Pt());
        this.points.add(new Pt());
        
        this.newPosition = new Pt();
    }

    /**
     * A collection of CAAT.Math.Point objects which defines the arc (center, start, end)
     */
    private List<Pt> points;
    private int length=-1;
    
    /**
     * Defined clockwise or counterclockwise ?
     */
    private boolean cw=true; // should be clock wise traversed ?
    private Rectangle bbox=null;
    
    /**
     * spare point for calculations
     */
    private Pt newPosition;
    
    /**
     * Arc radius.
     */
    private double radius= 0;
    
    /**
     * Arc start angle.
     */
    private double startAngle = 0;
    
    /**
     * Arc end angle.
     */
    private double angle = 2*Math.PI;
    
    /**
     * is a relative or absolute arc ?
     */
    private boolean arcTo = false;
    
    public ArcPath setRadius(double radius) {
        this.radius = radius;
        return this;
    }
    
    public ArcPath setArcTo(boolean arcTo) {
        this.arcTo = arcTo;
        return this;
    }
    
    public ArcPath initialize(int x, int y, int r, Double angle ) {
        this.setInitialPosition( x, y );
        this.setFinalPosition( x+r, y );
        this.angle = angle != null ? angle : 2*Math.PI;
        return this;
    }
    
    @Override
    public ArcPath applyAsPath(Director director) {
        CaatjaContext2d ctx = director.ctx;
        if ( !this.arcTo ) {
            ctx.arc( this.points.get(0).x, this.points.get(0).y, this.radius, this.startAngle, this.angle + this.startAngle, this.cw );
        } else {
            ctx.arcTo( this.points.get(0).x, this.points.get(0).y, this.points.get(1).x, this.points.get(1).y, this.radius );
        }
        return this;
    }
    
    @Override
    public void setPoint(Pt point, int index) {
        if (index >= 0 && index < this.points.size()) {
            this.points.set(index, point);
        }
    }
    
    /**
     * An array of {CAAT.Point} composed of two points.
     * @param points {Array<CAAT.Point>}
     */
    @Override
    public ArcPath setPoints(List<Pt> points) {
        this.points = new ArrayList<Pt>();
        this.points.set(0, points.get(0));
        this.points.set(1,  points.get(1));
        this.updatePath(null);

        return this;
    }
    
    public ArcPath setClockWise(Boolean cw) {
        this.cw = cw != null ? cw : true;
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
    public ArcPath setInitialPosition(int x, int y) {
        for (int i = 0, l = this.points.size(); i < l; i++) {
            this.points.get(0).x = x;
            this.points.get(0).y = y;
        }

        return this;
    }
    
    /**
     * Set a rectangle from points[0] to (finalX, finalY)
     * @param finalX {number}
     * @param finalY {number}
     */
    public ArcPath setFinalPosition(int finalX, int finalY) {
        this.points.get(1).x = finalX;
        this.points.get(1).y = finalY;

        this.updatePath(this.points.get(1));
        return this;
    }
    
    /**
     * An arc starts and ends in the same point.
     */
    @Override
    public Pt endCurvePosition() {
        return this.points.get(0);
    }

    /**
     * @inheritsDoc
     */
    @Override
    public Pt startCurvePosition() {
        return this.points.get(0);
    }

    /**
     * @inheritsDoc
     */
    @Override
    public Pt getPosition(double time) {
        if (time > 1 || time < 0) {
            time %= 1;
        }
        if (time < 0) {
            time = 1 + time;
        }

        if (-1 == this.length) {
            this.newPosition.set( this.points.get(0).x, this.points.get(0).y );
        } else {

            double angle= this.angle * time * (this.cw ? 1 : -1) + this.startAngle;

            this.newPosition.set(
                    this.points.get(0).x + this.radius * Math.cos(angle),
                    this.points.get(0).y + this.radius * Math.sin(angle)
            );
        }

        return this.newPosition;
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
    public void paint(Director director, boolean bDrawHandles) {

        CaatjaContext2d ctx = director.ctx;

        ctx.save();

        ctx.setStrokeStyle(this.color);
        
        ctx.beginPath();
        if ( !this.arcTo ) {
            ctx.arc( this.points.get(0).x, this.points.get(0).y, this.radius, this.startAngle, this.startAngle + this.angle, this.cw );
        } else {
            ctx.arcTo( this.points.get(0).x, this.points.get(0).y, this.points.get(0).x, this.points.get(0).y, this.radius );
        }
        ctx.stroke();

        if (bDrawHandles) {
            ctx.setGlobalAlpha(0.5);
            ctx.setFillStyle("#7f7f00");

            for (int i = 0; i < this.points.size(); i++) {
                this.drawHandle(ctx, this.points.get(i).x, this.points.get(i).y);
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
    
    /**
     * @inheritsDoc
     */
    @Override
    public List<Pt> getContour(int iSize) {
        List<Pt> contour = new ArrayList<Pt>();

        for (int i = 0; i < iSize; i++) {
            contour.add(new Pt(
                    this.points.get(0).x + this.radius * Math.cos( i*Math.PI/(iSize/2) ),
                    this.points.get(0).y + this.radius * Math.sin( i*Math.PI/(iSize/2) )));
        }

        return contour;
    }
    
    public Pt getPositionFromLength (int iLength) {
        double ratio= iLength / this.length * (this.cw ? 1: -1);
        return this.getPosition( ratio );
        /*
        this.newPosition.set(
            this.points[0].x + this.radius * Math.cos( 2*Math.PI * ratio ),
            this.points[0].y + this.radius * Math.sin( 2*Math.PI * ratio )
            );
        return this.newPosition;*/
    }
    
    /**
     * TODO Check equals
     */
    @Override
    public ArcPath updatePath(Pt point) {
        // just move the circle, not modify radius.
        if ( this.points.get(1).equals(point) ) {

            if ( !this.arcTo ) {
                this.radius= Math.sqrt(
                    ( this.points.get(0).x - this.points.get(1).x ) * ( this.points.get(0).x - this.points.get(1).x ) +
                    ( this.points.get(0).y - this.points.get(1).y ) * ( this.points.get(0).y - this.points.get(1).y )
                );
            }

            this.length= (int) (this.angle * this.radius);
            this.startAngle= Math.atan2( (this.points.get(1).y-this.points.get(0).y) , (this.points.get(1).x-this.points.get(0).x) );

        } else if (this.points.get(0).equals(point)) {
            this.points.get(1).set(
                this.points.get(0).x + this.radius * Math.cos( this.startAngle ),
                this.points.get(0).y + this.radius * Math.sin( this.startAngle )
            );
        }

        this.bbox.setEmpty();
        this.bbox.x= this.points.get(0).x - this.radius;
        this.bbox.y= this.points.get(0).y - this.radius;
        this.bbox.x1= this.points.get(0).x + this.radius;
        this.bbox.y1= this.points.get(0).y + this.radius;
        this.bbox.width= 2*this.radius;
        this.bbox.height= 2*this.radius;

        return this;
    }

    @Override
    public PathSegment endPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PathSegment transform(Matrix matrix) {
        // TODO Auto-generated method stub
        return null;
    }

}
