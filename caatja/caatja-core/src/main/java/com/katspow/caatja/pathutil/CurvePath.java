package com.katspow.caatja.pathutil;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.bezier.Bezier;
import com.katspow.caatja.math.bezier.Curve;

/**
 * @name CurvePath
 * @memberOf CAAT.PathUtil
 * @extends CAAT.PathUtil.PathSegment
 * @constructor
 */
public class CurvePath extends Path {

    /**
     * This class defines a Bezier cubic or quadric path segment.
     * 
     * @constructor
     * @extends CAAT.PathSegment
     * @implements CAAT.PathSegment
     */
    public CurvePath() {
        super();
        this.newPosition = new Pt(0,0,0);
    }

    /**
     * A CAAT.Math.Curve instance.
     */
    public Curve curve = null;
    
    /**
     * spare holder for getPosition coordinate return.
     * @type {CAAT.Math.Point}
     */
    private Pt newPosition = null;
    
    @Override
    public CurvePath applyAsPath(Director director) {
        this.curve.applyAsPath(director);
        return this;
    }    
    
    @Override
    public void setPoint(Pt point, int index) {
        if ( this.curve != null) {
            this.curve.setPoint(point,index);
        }
    }
    
    @Override
    public CurvePath setPoints(List<Pt> points) {
        Bezier curve = new Bezier();
        try {
            curve.setPoints(points);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.curve = curve;
        return this;
    }

    /**
     * Set the pathSegment as a CAAT.Bezier quadric instance. Parameters are
     * quadric coordinates control points.
     * 
     * @param p0x
     *            {number}
     * @param p0y
     *            {number}
     * @param p1x
     *            {number}
     * @param p1y
     *            {number}
     * @param p2x
     *            {number}
     * @param p2y
     *            {number}
     * @return this
     */
    public CurvePath setQuadric(double p0x, double p0y, double p1x, double p1y, double p2x, double p2y) {
        Bezier curve = new Bezier();
        curve.setQuadric(p0x, p0y, p1x, p1y, p2x, p2y);
        this.curve = curve;
        
        // TODO Check
        this.updatePath(null);
        
        return this;
    }

    /**
     * Set the pathSegment as a CAAT.Bezier cubic instance. Parameters are cubic
     * coordinates control points.
     * 
     * @param p0x
     *            {number}
     * @param p0y
     *            {number}
     * @param p1x
     *            {number}
     * @param p1y
     *            {number}
     * @param p2x
     *            {number}
     * @param p2y
     *            {number}
     * @param p3x
     *            {number}
     * @param p3y
     *            {number}
     * @return this
     */
    public CurvePath setCubic(double p0x, double p0y, double p1x, double p1y, double p2x, double p2y, double p3x,
            double p3y) {
        Bezier curve = new Bezier();
        curve.setCubic(p0x, p0y, p1x, p1y, p2x, p2y, p3x, p3y);
        this.curve = curve;
        
        // TODO Check
        this.updatePath(null);

        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public CurvePath updatePath(Pt point) {
        this.curve.update();
        this.length= (int) this.curve.getLength();
        this.curve.getBoundingBox(this.bbox);
         return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Pt getPosition(double time) {
        if (time > 1 || time < 0) {
            time %= 1;
        }
        if (time < 0) {
            time = 1 + time;
        }

        this.curve.solve(this.newPosition, time);

        return this.newPosition;
    }

    // TODO Double or int ???
    /**
     * Gets the coordinate on the path relative to the path length.
     * @param iLength {number} the length at which the coordinate will be taken from.
     * @return {CAAT.Point} a CAAT.Point instance with the coordinate on the path corresponding to the
     * iLenght parameter relative to segment's length.
     */
    public Pt getPositionFromLength(double iLength) {
        this.curve.solve(this.newPosition, iLength / this.length);
        return this.newPosition;
    }

    /**
     * Get path segment's first point's x coordinate.
     * @return {number}
     */
    public double initialPositionX() {
        return this.curve.coordlist.get(0).x;
    }

    /**
     * Get path segment's last point's y coordinate.
     * @return {number}
     */
    public double finalPositionX() {
        return this.curve.coordlist.get(this.curve.coordlist.size() - 1).x;
    }

    /**
     * @inheritDoc
     * @param director {CAAT.Director}
     * @param bDrawHandles {boolean}
     */
    @Override
    public void paint(Director director, Boolean bDrawHandles) {
        this.curve.drawHandles = bDrawHandles;
        director.ctx.setStrokeStyle(this.color);
        // TODO Different from source
        this.curve.paint(director);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int numControlPoints() {
        return this.curve.coordlist.size();
    }

    /**
     * @inheritDoc
     * @param index
     */
    @Override
    public Pt getControlPoint(int index) {
        return this.curve.coordlist.get(index);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Pt endCurvePosition() {
        return this.curve.endCurvePosition();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Pt startCurvePosition() {
        return this.curve.startCurvePosition();
    }

    /**
     * @inheritDoc
     * @param iSize
     */
    @Override
    public List<Pt> getContour(int iSize) {
        List<Pt> contour = new ArrayList<Pt>();
        for (int i = 0; i <= iSize; i++) {
            contour.add(new Pt().set((double) i / iSize, this.getPosition((double) i / iSize).y));
        }

        return contour;
    }

    // Add by me
    @Override
    public CurvePath setColor(String color) {
        return (CurvePath) super.setColor(color);
    }
    
    @Override
    public CurvePath endPath() {
        // Do nothing ?
        return this;
    }

    @Override
    public CurvePath setParent(Path parent) {
        return (CurvePath) super.setParent(parent);
    }
    
    

}
