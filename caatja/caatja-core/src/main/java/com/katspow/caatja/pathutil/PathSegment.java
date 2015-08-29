package com.katspow.caatja.pathutil;

import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.math.bezier.Curve;
import com.katspow.caatja.math.matrix.Matrix;

/**
 * @author  Hyperandroid  ||  http://hyperandroid.com/
 *
 * These classes encapsulate different kinds of paths.
 * LinearPath, defines an straight line path, just 2 points.
 * CurvePath, defines a path based on a Curve. Curves can be bezier quadric/cubic and catmull-rom.
 * Path, is a general purpose class, which composes a path of different path segments (Linear or Curve paths).
 *
 * A path, has an interpolator which stablishes the way the path is traversed (accelerating, by
 * easing functions, etc.). Normally, interpolators will be defined by CAAT,Interpolator instances, but
 * general Paths could be used as well.
 *
 **/

/**
 * This is the abstract class that every path segment must conform to.
 * <p>
 * It is implemented by all path segment types, ie:
 * <ul>
 *  <li>LinearPath
 *  <li>CurvePath, base for all curves: quadric and cubic bezier.
 *  <li>Path. A path built of different PathSegment implementations.
 * </ul>
 *
 * @constructor
 */
public abstract class PathSegment {
    
    /**
     * Color to draw the segment.
     */
    public String color = "#000";
    
    /**
     * Segment length.
     */
    public int length = 0;
    
    /**
     * Segment bounding box.
     */
    public Rectangle bbox;
    
    /**
     * Path this segment belongs to.
     */
    public Path parent;
    
    public PathSegment() {
        this.bbox= new Rectangle();
    }
    
    /**
     * Set a PathSegment's parent
     * @param parent
     */
    public PathSegment setParent(Path parent) {
        this.parent = parent;
        return this;
    }
    
    public PathSegment setColor(String color) {
        if (color != null) {
            this.color = color;
        }
        
        return this;
    }

    /**
     * Get path's last coordinate.
     * @return {CAAT.Point}
     */
    public abstract Pt endCurvePosition();

    /**
     * Get path's starting coordinate.
     * @return {CAAT.Point}
     */
    public abstract Pt startCurvePosition();
    
    /**
     * Set this path segment's points information.
     * @param points {Array<CAAT.Point>}
     */
    public abstract PathSegment setPoints(List<Pt> points );
    
    /**
     * Set a point from this path segment.
     * @param point {CAAT.Point}
     * @param index {integer} a point index.
     */
    public abstract void setPoint(Pt point, int index );

    /**
     * Get a coordinate on path.
     * The parameter time is normalized, that is, its values range from zero to one.
     * zero will mean <code>startCurvePosition</code> and one will be <code>endCurvePosition</code>. Other values
     * will be a position on the path relative to the path length. if the value is greater that 1, if will be set
     * to modulus 1.
     * @param time a float with a value between zero and 1 inclusive both.
     *
     * @return {CAAT.Point}
     */
    public abstract Pt getPosition(double time);

    /**
     * Gets Path length.
     * @return {number}
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Gets the path bounding box (or the rectangle that contains the whole path).
     * @param rectangle a CAAT.Rectangle instance with the bounding box.
     * @return {CAAT.Rectangle}
     */
    public Rectangle getBoundingBox() {
        return this.bbox;
    }

    /**
     * Gets the number of control points needed to create the path.
     * Each PathSegment type can have different control points.
     * @return {number} an integer with the number of control points.
     */
    public abstract int numControlPoints();

    /**
     * Gets CAAT.Point instance with the 2d position of a control point.
     * @param index an integer indicating the desired control point coordinate.
     * @return {CAAT.Point}
     */
    public abstract Pt getControlPoint(int index);

    /**
     * Instruments the path has finished building, and that no more segments will be added to it.
     * You could later add more PathSegments and <code>endPath</code> must be called again.
     */
    public abstract PathSegment endPath();

    /**
     * Gets a polyline describing the path contour. The contour will be defined by as mush as iSize segments.
     * @param iSize an integer indicating the number of segments of the contour polyline.
     *
     * @return {[CAAT.Point]}
     */
    public abstract List<Pt> getContour(int iSize);
    
    /**
     * Recalculate internal path structures.
     */
    public abstract PathSegment updatePath(Pt point);
    
    /**
     * Draw this path using RenderingContext2D drawing primitives.
     * The intention is to set a path or pathsegment as a clipping region.
     * @param director TODO
     */
    public abstract PathSegment applyAsPath(Director director);
    
    /**
     * Transform this path with the given affinetransform matrix.
     * @param matrix
     */
    public abstract PathSegment transform(Matrix matrix);
    
    public void drawHandle(CaatjaContext2d ctx, double x, double y ) {
//         int w = Curve.HANDLE_SIZE/2;
//        ctx.fillRect( x-w, y-w, w*2, w*2 );
        
        ctx.beginPath();
        ctx.arc(
            x,
            y,
            Curve.HANDLE_SIZE/2,
            0,
            2*Math.PI,
            false) ;
        ctx.fill();
    }
}
