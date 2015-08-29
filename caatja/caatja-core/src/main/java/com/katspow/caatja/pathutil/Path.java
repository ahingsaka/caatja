package com.katspow.caatja.pathutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.PathListener;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.math.bezier.Bezier;
import com.katspow.caatja.math.bezier.CatmullRom;
import com.katspow.caatja.math.bezier.Curve;
import com.katspow.caatja.math.matrix.Matrix;

public class Path extends PathSegment {
    
    /**
     * This class the top most abstraction of path related classes in CAAT. It defines a path composes un
     * an unlimited number of path segments including CAAT.Path instances.
     * <p>
     * Every operation of the CAAT.PathSegment interface is performed for every path segment. In example,
     * the method <code>getLength</code> will contain the sum of every path segment's length.
     * <p>
     * An example of CAAT.Path will be as follows:

     * <code>
     * path.beginPath(x,y).<br>
     * &nbsp;&nbsp;addLineTo(x1,y1).<br>
     * &nbsp;&nbsp;addLineTo(x2,y2).<br>
     * &nbsp;&nbsp;addQuadricTo(...).<br>
     * &nbsp;&nbsp;addCubicTo(...).<br>
     * &nbsp;&nbsp;endPath();<br>
     * </code>
     * <p>
     * This code creates a path composed of four chained segments, starting at (x,y) and having each
     * segment starting where the previous one ended.
     * <p>
     * This class is intended to wrap the other kind of path segment classes when just a one segmented
     * path is to be defined. The methods <code>setLinear, setCubic and setQuadrid</code> will make
     * a CAAT.Path instance to be defined by just one segment.
     *
     * @constructor
     * @extends CAAT.PathSegment
     * @implements CAAT.PathSegment
     */
    public Path() {
        super();
        this.newPosition = new Pt(0,0,0);
        this.pathSegments = new ArrayList<Path>();
        
        this.behaviorList= new ArrayList<BaseBehavior>();
        this.matrix=        new Matrix();
        this.tmpMatrix=     new Matrix();
    }

    /**
     * A collection of PathSegments.
     * @type {Array.<CAAT.PathUtil.PathSegment>}
     */
    public ArrayList<Path> pathSegments; // a collection of CAAT.PathSegment instances.
    
    /**
     * For each path segment in this path, the normalized calculated duration.
     * precomputed segment duration relative to segment legnth/path length
     */
    private List<Double> pathSegmentDurationTime; // precomputed segment duration relative to segment legnth/path length
    
    /**
     * For each path segment in this path, the normalized calculated start time.
     * precomputed segment start time relative to segment legnth/path length and duration.
     */
    private List<Double> pathSegmentStartTime;  // precomputed segment start time relative to segment legnth/path length and duration.

    /**
     * spare CAAT.Math.Point to return calculated values in the path.
     */
    private Pt newPosition; // spare CAAT.Point.
    
    /**
     * path length (sum of every segment length)
     */
    private Double pathLength = -1d; // path length (sum of every segment length)

    /**
     * starting path x position
     */
    private double beginPathX = -1;
    
    /**
     * starting path y position
     */
    private double beginPathY = -1;

    /*
    last path coordinates position (using when building the path).
     */
    public double trackPathX = -1;
    public double trackPathY = -1;
    
    /*
        needed to drag control points.
     */
    private double ax = -1;
    private double ay = -1;
    private List<Pt> points = new ArrayList<Pt>();
    
    /**
     * Is this path interactive ?. If so, controls points can be moved with a CAAT.Foundation.UI.PathActor.
     */
    private boolean interactive = true;
    
    /**
     * A list of behaviors to apply to this path.
     * A path can be affine transformed to create a different path.
     */
    public List<BaseBehavior> behaviorList =              null;

    /** rotation behavior info **/
    
    /**
     * Path rotation angle.
     */
    public double rb_angle=                   0;
    
    /**
     * Path rotation x anchor.
     */
    public double rb_rotateAnchorX=           .5;
    
    /**
     * Path rotation y anchor.
     */
    public double rb_rotateAnchorY=           .5;

    /** scale behavior info **/
    
    /**
     * Path X scale.
     */
    public double sb_scaleX=                  1;
    
    /**
     * Path Y scale.
     */
    public double sb_scaleY=                  1;
    
    /**
     * Path scale X anchor.
     */
    public Double sb_scaleAnchorX=            .5;
    
    /**
     * Path scale Y anchor.
     */
    public Double sb_scaleAnchorY=            .5;
    
    /**
     * Path translation anchor X.
     */
    public double  tAnchorX=                   0;
    
    /**
     * Path translation anchor Y.
     */
    public double tAnchorY=                   0;

    /** translate behavior info **/
    
    /**
     * Path translation X.
     */
    public int tb_x=                       0;
    
    /**
     * Path translation Y.
     */
    public int tb_y=                       0;

    /** behavior affine transformation matrix **/
    
    /**
     * Path behaviors matrix.
     */
    public Matrix matrix=                     null;
    
    /**
     * Spare calculation matrix.
     */
    public Matrix tmpMatrix=                  null;

    /**
     * Original Path�s path segments points.
     */
    public List<Pt> pathPoints=                 null;

    /**
     * Path bounding box width.
     */
    public int width=                      0;
    
    /**
     * Path bounding box height.
     */
    public int height=                     0;
    
    /**
     * Path bounding box X position.
     */
    public double clipOffsetX             =   0;
    
    /**
     * Path bounding box Y position.
     */
    public double clipOffsetY             =   0;
    
    /**
     * Is this path closed ?
     */
    public boolean closed                  =   false;
    
    // Add by me
    public List<PathListener> pathListener;
    private List<Pt> point = new ArrayList<Pt>();
    
    /**
     * Apply this path as a Canvas context path.
     * You must explicitly call context.beginPath
     * @param director
     * @return {*}
     */
    @Override
    public Path applyAsPath(Director director) {
        CaatjaContext2d ctx= director.ctx;
        
        director.modelViewMatrix.transformRenderingContext( ctx );
//        ctx.beginPath();
        ctx.setGlobalCompositeOperation("source-out");
        ctx.moveTo(
            this.getFirstPathSegment().startCurvePosition().x,
            this.getFirstPathSegment().startCurvePosition().y
        );
        
        for (Path path : this.pathSegments) {
            path.applyAsPath(director);
        }
        
        ctx.setGlobalCompositeOperation("source-over");
        return this;
    }

    /**
     * Set whether this path should paint handles for every control point.
     * @param interactive {boolean}.
     */
    public Path setInteractive(boolean interactive) {
        this.interactive = interactive;
        return this;
    }
    
    public Path getFirstPathSegment() {
        return this.pathSegments.size() > 0 ?
            this.pathSegments.get(0) :
            null;
    }
    
    public Path getLastPathSegment() {
        return this.pathSegments.size() > 0 ?
            this.pathSegments.get(this.pathSegments.size()-1 ) :
            null;
    }

    /**
     * Return the last point of the last path segment of this compound path.
     * @return {CAAT.Point}
     */
    @Override
    public Pt endCurvePosition() {
        if ( this.pathSegments.size() > 0) {
            return this.pathSegments.get(this.pathSegments.size() - 1).endCurvePosition();
        } else {
            return new Pt().set( this.beginPathX, this.beginPathY );
        }
    }

    /**
     * Return the first point of the first path segment of this compound path.
     * @return {CAAT.Point}
     */
    @Override
    public Pt startCurvePosition() {
        return this.pathSegments.get(0).startCurvePosition();
    }
    
    /**
     * Return the last path segment added to this path.
     * @return {CAAT.PathSegment}
     */
    public PathSegment getCurrentPathSegment() {
        return this.pathSegments.get(this.pathSegments.size()-1);
    }

    /**
     * Set the path to be composed by a single LinearPath segment.
     * @param x0 {number}
     * @param y0 {number}
     * @param x1 {number}
     * @param y1 {number}
     * @return this
     */
    public Path setLinear(double x0, double y0, double x1, double y1) {
        this.pathSegments.clear();
        this.beginPath(x0, y0);
        this.addLineTo(x1, y1, null);
        this.endPath();

        return this;
    }
    
    /**
     * Set this path to be composed by a single Quadric Bezier path segment.
     * @param x0 {number}
     * @param y0 {number}
     * @param x1 {number}
     * @param y1 {number}
     * @param x2 {number}
     * @param y2 {number}
     * @return this
     */
    public Path setQuadric(double x0, double y0, double x1, double y1, double x2, double y2) {
        this.beginPath(x0, y0);
        this.addQuadricTo(x1, y1, x2, y2, null);
        this.endPath();

        return this;
    }

    /**
     * Sets this path to be composed by a single Cubic Bezier path segment.
     * @param x0 {number}
     * @param y0 {number}
     * @param x1 {number}
     * @param y1 {number}
     * @param x2 {number}
     * @param y2 {number}
     * @param x3 {number}
     * @param y3 {number}
     *
     * @return this
     */
    public Path setCubic(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        this.beginPath(x0, y0);
        this.addCubicTo(x1, y1, x2, y2, x3, y3, null);
        this.endPath();

        return this;
    }
    
    public Path setRectangle(double x0,double y0,double x1,double y1) {
        this.beginPath(x0,y0);
        this.addRectangleTo(x1,y1, null, null);
        this.endPath();

        return this;
    }
    
    public Path setCatmullRom(List<Pt> points, boolean closed ) {
        
        // TODO Check if it works
        List<Pt> clones = new ArrayList<Pt>();
        for (Pt pt : points) {
            clones.add(pt.clone());
        }
        
        if ( closed ) {
            clones.set(0, clones.get(clones.size()-1));
            clones.add(clones.get(1));
            clones.add(clones.get(2));
        } else {
            clones.set(0, clones.get(clones.size()-1));
            clones.add(clones.get(clones.size() - 1));
        }

        for( int i=1; i<points.size()-2; i++ ) {

            CurvePath segment= new CurvePath().setColor("#000").setParent(this);
            CatmullRom cm= new CatmullRom();
            cm.setCurve(
                points.get( i-1 ),
                points.get( i ),
                points.get( i+1 ),
                points.get( i+2 )
            );
            segment.curve= cm;
            this.pathSegments.add(segment);
        }
        return this;
    }

    /**
     * Add a CAAT.PathSegment instance to this path.
     * @param pathSegment {CAAT.PathSegment}
     * @return this
     * 
     */
    public Path addSegment(Path pathSegment) {
        pathSegment.setParent(this);
        this.pathSegments.add(pathSegment);
        return this;
    }
    
    public Path addArcTo(int x1, int y1,int x2, int y2, int radius, boolean cw, String color ) {
        ArcPath r= new ArcPath();
        r.setArcTo(true);
        r.setRadius( radius );
        r.setInitialPosition( x1,y1).
            setFinalPosition( x2,y2 );


        r.setParent( this );
        r.setColor( color );

        // FIXME
//        this.pathSegments.add(r);

        return this;
    }
    
    public Path addRectangleTo(double x1, double y1, Boolean cw, String color ) {
        RectPath r= new RectPath();
        r.setPoints(Arrays.asList(
                this.endCurvePosition(),
                new Pt().set(x1,y1)
            ));

        r.setClockWise(cw);
        r.setColor(color);
        r.setParent(this);

        this.pathSegments.add(r);

        return this;
    }

    /**
     * Add a Quadric Bezier path segment to this path.
     * The segment starts in the current last path coordinate.
     * @param px1 {number}
     * @param py1 {number}
     * @param px2 {number}
     * @param py2 {number}
     * @param color {color=}. optional parameter. determines the color to draw the segment with (if
     *         being drawn by a CAAT.PathActor).
     *
     * @return this
     */
    public Path addQuadricTo(double px1, double py1, double px2, double py2, String color) {
        Bezier bezier = new Bezier();
        
        try {
            bezier.setPoints(Arrays.asList(
                        this.endCurvePosition(),
                        new Pt().set(px1,py1),
                        new Pt().set(px2,py2)
                    ));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        this.trackPathX = px2;
        this.trackPathY = py2;

        CurvePath segment = new CurvePath().setColor(color).setParent(this);
        segment.curve = bezier;

        this.pathSegments.add(segment);

        return this;
    }
    
    // Add by me
    public Path addQuadricTo(double px1, double py1, double px2, double py2) {
        return addQuadricTo(px1, py1, px2, py2, null);
    }

    /**
     * Add a Cubic Bezier segment to this path.
     * The segment starts in the current last path coordinate.
     * @param px1 {number}
     * @param py1 {number}
     * @param px2 {number}
     * @param py2 {number}
     * @param px3 {number}
     * @param py3 {number}
     * @param color {color=}. optional parameter. determines the color to draw the segment with (if
     *         being drawn by a CAAT.PathActor).
     *
     * @return this
     */
    public Path addCubicTo(double px1, double py1, double px2, double py2, double px3, double py3, String color) {
        Bezier bezier = new Bezier();
        
        try {
            bezier.setPoints(Arrays.asList(
                        this.endCurvePosition(),
                        new Pt().set(px1,py1),
                        new Pt().set(px2,py2),
                        new Pt().set(px3,py3)
                    ));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        this.trackPathX = px3;
        this.trackPathY = py3;

        CurvePath segment = new CurvePath().setColor(color).setParent(this);
        segment.curve = bezier;

        // TODO Check if pathSegment is curve : look demo15
        this.pathSegments.add(segment);
        return this;
    }
    
    // Add by me
    public Path addCubicTo(double px1, double py1, double px2, double py2, double px3, double py3) {
        return addCubicTo(px1, py1, px2, py2, px3, py3, null);
    }

    /**
     * Add a Catmull-Rom segment to this path.
     * The segment starts in the current last path coordinate.
     * @param px1 {number}
     * @param py1 {number}
     * @param px2 {number}
     * @param py2 {number}
     * @param px3 {number}
     * @param py3 {number}
     * @param color {color=}. optional parameter. determines the color to draw the segment with (if
     *         being drawn by a CAAT.PathActor).
     *
     * @return this
     */
    public Path addCatmullTo(double px1, double py1, double px2, double py2, double px3, double py3, String color) {
        
        // FIXME no setColor method
        //CatmullRom curve = new CatmullRom().setColor(color);
        CatmullRom curve = new CatmullRom();
        // FIXME call has changed
//        curve.setCurve(this.trackPathX, this.trackPathY, px1, py1, px2, py2, px3, py3);
        this.trackPathX = px3;
        this.trackPathY = py3;

        CurvePath segment = new CurvePath().setParent(this);
        segment.curve = curve;

        this.pathSegments.add(segment);
        return this;
    }

    /**
     * Adds a line segment to this path.
     * The segment starts in the current last path coordinate.
     * @param px1 {number}
     * @param py1 {number}
     * @param color {color=}. optional parameter. determines the color to draw the segment with (if
     *         being drawn by a CAAT.PathActor).
     *
     * @return this
     */
    public Path addLineTo(double px1, double py1, String color) {
        LinearPath segment = new LinearPath().setColor(color);
        segment.setPoints( Arrays.asList(
                            this.endCurvePosition(),
                            new Pt().set(px1,py1)
                        ));
        
        segment.setParent(this);

        this.trackPathX = px1;
        this.trackPathY = py1;

        this.pathSegments.add(segment);
        return this;
    }
    
    // Add by me
    public Path addLineTo(double px1, double py1) {
        return addLineTo(px1, py1, null);
    }

    /**
     * Set the path's starting point. The method startCurvePosition will return this coordinate.
     * <p>
     * If a call to any method of the form <code>add<Segment>To</code> is called before this calling
     * this method, they will assume to start at -1,-1 and probably you'll get the wrong path.
     * @param px0 {number}
     * @param py0 {number}
     *
     * @return this
     */
    public Path beginPath(double px0, double py0) {
        this.trackPathX = px0;
        this.trackPathY = py0;
        this.beginPathX = px0;
        this.beginPathY = py0;
        return this;
    }

    /**
     * <del>Close the path by adding a line path segment from the current last path
     * coordinate to startCurvePosition coordinate</del>.
     * <p>
     * This method closes a path by setting its last path segment's last control point
     * to be the first path segment's first control point.
     * <p>
     *     This method also sets the path as finished, and calculates all path's information
     *     such as length and bounding box.
     *
     * @return this
     */
    public Path closePath() {
        this.getLastPathSegment().setPoint(
                this.getFirstPathSegment().startCurvePosition(),
                this.getLastPathSegment().numControlPoints()-1 );
        
        this.trackPathX = this.beginPathX;
        this.trackPathY = this.beginPathY;
        
        this.closed= true;

        this.endPath();
        return this;
    }
    
    /**
     * Finishes the process of building the path. It involves calculating each path segments length
     * and proportional length related to a normalized path length of 1.
     * It also sets current paths length.
     * These calculi are needed to traverse the path appropriately.
     * <p>
     * This method must be called explicitly, except when closing a path (that is, calling the
     * method closePath) which calls this method as well.
     *
     * @return this
     */
    @Override
    public Path endPath() {
        this.pathSegmentStartTime= new ArrayList<Double>();
        this.pathSegmentDurationTime= new ArrayList<Double>();
        
        // TODO Check
        this.updatePath(null);

//        this.pathLength=0d;
//        for( int i=0; i<this.pathSegments.size(); i++) {
//            this.pathLength+= this.pathSegments.get(i).getLength();
//            this.pathSegmentStartTime.add(0d);
//            this.pathSegmentDurationTime.add(0d);
//        }
//
//        for( int i=0; i<this.pathSegments.size(); i++) {
//            
//            // TODO Check for 0
//            if (this.pathLength != null && this.pathLength != 0) {
//                this.pathSegmentDurationTime.set(i, this.pathSegments.get(i).getLength()/this.pathLength);
//            } else {
//                this.pathSegmentDurationTime.set(i, 0d);
//            }
//            
//            if ( i>0 ) {
//                this.pathSegmentStartTime.set(i, this.pathSegmentStartTime.get(i-1)+this.pathSegmentDurationTime.get(i-1));
//            } else {
//                this.pathSegmentStartTime.set(0, 0d);
//            }
//
//            this.pathSegments.get(i).endPath();
//        }
        
        return this;
    }
    
    /**
     * This method, returns a CAAT.Foundation.Point instance indicating a coordinate in the path.
     * The returned coordinate is the corresponding to normalizing the path's length to 1,
     * and then finding what path segment and what coordinate in that path segment corresponds
     * for the input time parameter.
     * <p>
     * The parameter time must be a value ranging 0..1.
     * If not constrained to these values, the parameter will be modulus 1, and then, if less
     * than 0, be normalized to 1+time, so that the value always ranges from 0 to 1.
     * <p>
     * This method is needed when traversing the path throughout a CAAT.Interpolator instance.
     *
     *
     * @param time {number} a value between 0 and 1 both inclusive. 0 will return path's starting coordinate.
     * 1 will return path's end coordinate.
     * @param open_contour {boolean=} treat this path as an open contour. It is intended for
     * open paths, and interpolators which give values above 1. see tutorial 7.1.
     * @link{../../documentation/tutorials/t7-1.html}
     *
     * @return {CAAT.Foundation.Point}
     */
//    @Override
    public Pt getPosition(double time, boolean open_contour) {
        
        if (open_contour && (time>=1 || time<=0) ) {

            Pt p0,p1;
            double ratio, angle;

            if ( time>=1 ) {
                // these values could be cached.
                p0= this.__getPositionImpl( .999 );
                p1= this.endCurvePosition();

                angle= Math.atan2( p1.y - p0.y, p1.x - p0.x );
                ratio= time%1;


            } else {
                // these values could be cached.
                p0= this.__getPositionImpl( .001 );
                p1= this.startCurvePosition();

                angle= Math.atan2( p1.y - p0.y, p1.x - p0.x );
                ratio= -time;
            }

            Pt np= this.newPosition;
            double length= this.getLength();

            np.x = p1.x + (ratio * length)*Math.cos(angle);
            np.y = p1.y + (ratio * length)*Math.sin(angle);


            return np;
        }

        return this.__getPositionImpl(time);
        
//        if (time > 1 || time < 0) {
//            time %= 1;
//        }
//        if (time < 0) {
//            time = 1 + time;
//        }
//
////        boolean found= false;
////        for (int i = 0; i < this.pathSegments.size(); i++) {
////            if (this.pathSegmentStartTime.get(i) <= time
////                    && time <= this.pathSegmentStartTime.get(i) + this.pathSegmentDurationTime.get(i)) {
////                
////                // TODO FIXME Not working ???
//////                if (this.pathSegmentStartTime.get(i) != null && this.pathSegmentStartTime.get(i) != 0) {
//////                    time = (time - this.pathSegmentStartTime.get(i)) / this.pathSegmentDurationTime.get(i);
//////                } else {
//////                    time = 0;
//////                }
////                
////                time = (time - this.pathSegmentStartTime.get(i)) / this.pathSegmentDurationTime.get(i);
////                Pt pointInPath = this.pathSegments.get(i).getPosition(time);
////                this.newPosition.x = pointInPath.x;
////                this.newPosition.y = pointInPath.y;
////                found= true;
////                break;
////            }
////        }
////
////        return found ? this.newPosition : this.endCurvePosition();
//        
//        ArrayList<Path> ps = this.pathSegments;
//        List<Double> psst = this.pathSegmentStartTime;
//        List<Double> psdt = this.pathSegmentDurationTime;
//        int l = 0;
//        int r = ps.size();
//        int m;
//        Pt np = this.newPosition;
//        Double psstv;
//        while( l!=r ) {
//
//            m= ((r+l)/2)|0;
//            psstv= psst.get(m);
//            if ( psstv<=time && time<=psstv+psdt.get(m)) {
//                // TODO Check 
//                time= psdt.get(m) != null && psdt.get(m) != 0 ?
//                        (time-psstv)/psdt.get(m) :
//                        0;
//                        
//                // Clamp this segment's time to a maximum since it is relative
//                // to the path.
//                // thanks https://github.com/donaldducky for spotting.
//                if (time > 1) {
//                    time = 1;
//                } else if (time < 0) {
//                    time = 0;
//                }
//
//                Pt pointInPath= ps.get(m).getPosition(time);
//                np.x= pointInPath.x;
//                np.y= pointInPath.y;
//                return np;
//            } else if ( time<psstv ) {
//                r= m;
//            } else /*if ( time>=psstv )*/ {
//                l= m+1;
//            }
//        }
//        return this.endCurvePosition();
        
    }
    
    private Pt __getPositionImpl(double time) {

        if ( time>1 || time<0 ) {
            time%=1;
        }
        if ( time<0 ) {
            time= 1+time;
        }

        ArrayList<Path> ps = this.pathSegments;
        List<Double> psst = this.pathSegmentStartTime;
        List<Double> psdt = this.pathSegmentDurationTime;
        int l = 0;
        int r = ps.size();
        int m;
        Pt np = this.newPosition;
        Double psstv;
        while( l!=r ) {

            m= ((r+l)/2)|0;
            psstv= psst.get(m);
            if ( psstv<=time && time<=psstv+psdt.get(m)) {
                // TODO Check 
                time= psdt.get(m) != null && psdt.get(m) != 0 ?
                        (time-psstv)/psdt.get(m) :
                        0;
                        
                // Clamp this segment's time to a maximum since it is relative
                // to the path.
                // thanks https://github.com/donaldducky for spotting.
                if (time > 1) {
                    time = 1;
                } else if (time < 0) {
                    time = 0;
                }

                Pt pointInPath= ps.get(m).getPosition(time);
                np.x= pointInPath.x;
                np.y= pointInPath.y;
                return np;
            } else if ( time<psstv ) {
                r= m;
            } else /*if ( time>=psstv )*/ {
                l= m+1;
            }
        }
        return this.endCurvePosition();

    }
    
    // Add by me
    @Override
    public Pt getPosition(double time) {
        return getPosition(time, false);
    }

    // TODO Check double or int ?
    /**
     * Analogously to the method getPosition, this method returns a CAAT.Point instance with
     * the coordinate on the path that corresponds to the given length. The input length is
     * related to path's length.
     *
     * @param iLength {number} a float with the target length.
     * @return {CAAT.Point}
     */
    public Pt getPositionFromLength(double iLength) {

        iLength %= this.getLength();
        if (iLength < 0) {
            iLength += this.getLength();
        }

        int accLength = 0;

        for (int i = 0; i < this.pathSegments.size(); i++) {
            if (accLength <= iLength && iLength <= this.pathSegments.get(i).getLength() + accLength) {
                iLength -= accLength;
                Pt pointInPath = this.pathSegments.get(i).getPositionFromLength(iLength);
                this.newPosition.x = pointInPath.x;
                this.newPosition.y = pointInPath.y;
                break;
            }
            accLength += this.pathSegments.get(i).getLength();
        }

        return this.newPosition;
    }

    /**
     * Paints the path.
     * This method is called by CAAT.PathActor instances.
     * If the path is set as interactive (by default) path segment will draw curve modification
     * handles as well.
     *
     * @param director {CAAT.Director} a CAAT.Director instance.
     * 
     */
    public void paint(Director director, Boolean interactive) {
        for (int i = 0; i < this.pathSegments.size(); i++) {
            this.pathSegments.get(i).paint(director, this.interactive);
        }
    }

    /**
     * Method invoked when a CAAT.PathActor stops dragging a control point.
     */
    public void release() {
        this.ax = -1;
        this.ay = -1;
    }
    
    public boolean isEmpty() {
        return this.pathSegments.isEmpty();
    }

    /**
     * Returns an integer with the number of path segments that conform this path.
     * @return {number}
     */
    public int getNumSegments() {
        return this.pathSegments.size();
    }

    /**
     * Gets a CAAT.PathSegment instance.
     * @param index {number} the index of the desired CAAT.PathSegment.
     * @return CAAT.PathSegment
     */
    public Path getSegment(int index) {
        return this.pathSegments.get(index);
    }
    
    @Override
    public int numControlPoints() {
        return this.points.size();
    }
    
    @Override
    public Pt getControlPoint(int index) {
        return this.points.get(index);
    }
    
    // Add by me
    @Override
    public Path updatePath(Pt point) {
        return updatePath(point, null);
    }

    /**
     * Indicates that some path control point has changed, and that the path must recalculate
     * its internal data, ie: length and bbox.
     */
//    @Override
    public Path updatePath(Pt point, PathActorUpdateCallback callback) {
        int i, j;
        this.length=0;
        this.bbox.setEmpty();
        this.points = new ArrayList<Pt>();
        
        double xmin= Double.MAX_VALUE, ymin= Double.MAX_VALUE;
        for(i=0; i<this.pathSegments.size(); i++ ) {
            this.pathSegments.get(i).updatePath(point);
            this.length+= this.pathSegments.get(i).getLength();
            this.bbox.unionRectangle(this.pathSegments.get(i).bbox);

            for( j=0; j<this.pathSegments.get(i).numControlPoints(); j++ ) {
//                this.points.add( this.pathSegments.get(i).getControlPoint( j ) );
                Pt pt= this.pathSegments.get(i).getControlPoint( j );
                this.points.add( pt );
                if ( pt.x < xmin ) {
                    xmin= pt.x;
                }
                if ( pt.y < ymin ) {
                    ymin= pt.y;
                }
            }
        }
        
        this.clipOffsetX= -xmin;
        this.clipOffsetY= -ymin;
        
        this.width= (int) this.bbox.width;
        this.height= (int) this.bbox.height;
        // TODO int or double ?
        this.setLocation((int) this.bbox.x, (int) this.bbox.y );

        this.pathSegmentStartTime=      new ArrayList<Double>();;
        this.pathSegmentDurationTime=   new ArrayList<Double>();;
        
        for( i=0; i<this.pathSegments.size(); i++) {
            this.pathSegmentStartTime.add(0d);
            this.pathSegmentDurationTime.add(0d);
        }

        for( i=0; i<this.pathSegments.size(); i++) {
            // TODO Check condition
            this.pathSegmentDurationTime.set(i, this.getLength() > 0 ? this.pathSegments.get(i).getLength()/this.getLength() : 0);
            if ( i>0 ) {
                this.pathSegmentStartTime.set(i, this.pathSegmentStartTime.get(i-1)+this.pathSegmentDurationTime.get(i-1));
            } else {
                this.pathSegmentStartTime.set(0, 0d);
            }

            this.pathSegments.get(i).endPath();
        }

        this.extractPathPoints();
        
        if (callback!= null ) {
            callback.call(this);
        }

        return this;
    }

    /**
     * Sent by a CAAT.PathActor instance object to try to drag a path's control point.
     * @param x {number}
     * @param y {number}
     */
    public void press(double x, double y) {
        double HS = (double) Curve.HANDLE_SIZE / 2;
        for (int i = 0; i < this.pathSegments.size(); i++) {
            for (int j = 0; j < this.pathSegments.get(i).numControlPoints(); j++) {
                Pt point = this.pathSegments.get(i).getControlPoint(j);
                if (x >= point.x - HS && y >= point.y - HS && x < point.x + HS && y < point.y + HS) {

                    // TODO Check
                    this.point = Arrays.asList(point);
                    return;
                }
            }
        }
        this.point = null;
    }

    /**
     * Drags a path's control point.
     * If the method press has not set needed internal data to drag a control point, this
     * method will do nothing, regardless the user is dragging on the CAAT.PathActor delegate.
     * @param x {number}
     * @param y {number}
     */
    public void drag(double x, double y, PathActorUpdateCallback callback) {
        
        if (!this.interactive) {
            return;
        }
        
        if (null == this.point) {
            return;
        }

        if (-1 == this.ax || -1 == this.ay) {
            this.ax = x;
            this.ay = y;
        }

        // TODO Check
//        for (int i = 0; i < this.point.size(); i++) {
            this.point.get(0).x += x - this.ax;
            this.point.get(0).y += y - this.ay;
//        }

        this.ax = x;
        this.ay = y;

        // TODO Check get(0)
        this.updatePath(this.point.get(0), callback);
    }

    /**
     * Returns a collection of CAAT.Point objects which conform a path's contour.
     * @param iSize {number}. Number of samples for each path segment.
     * @return {[CAAT.Point]}
     */
    @Override
    public List<Pt> getContour(int iSize) {
        List<Pt> contour = new ArrayList<Pt>();

        // Add by me
        if (iSize > 0) {

            for (int i = 0; i <= iSize; i++) {
                contour.add(new Pt().set((double) i / iSize, this.getPosition((double) i / iSize).y, 0));
            }

        }

        return contour;
    }

    /**
     * Reposition this path points.
     * This operation will only take place if the supplied points array equals in size to
     * this path's already set points.
     * @param points {Array<CAAT.Point>}
     */
    @Override
    public Path setPoints(List<Pt> points) {
        if ( this.points.size()==points.size() ) {
            for( int i=0; i<points.size(); i++ ) {
                this.points.get(i).x= points.get(i).x;
                this.points.get(i).y= points.get(i).y;
            }
        }
        return this;
    }

    /**
     * Set a point from this path.
     * @param point {CAAT.Point}
     * @param index {integer} a point index.
     */
    @Override
    public void setPoint(Pt point, int index) {
        if ( index>=0 && index<this.points.size() ) {
            this.points.get(index).x= point.x;
            this.points.get(index).y= point.y;
        }
        
        // TODO No return ??
//        return this;
    }
    
    /**
     * Removes all behaviors from an Actor.
     * @return this
     */
    public Path emptyBehaviorList () {
        this.behaviorList.clear();
        return this;
    }

    public Path extractPathPoints () {
        if ( this.pathPoints == null) {
            int i;
            this.pathPoints = new ArrayList<Pt>();
            for ( i=0; i<this.numControlPoints(); i++ ) {
                this.pathPoints.add( this.getControlPoint(i).clone() );
            }
        }

        return this;
    }

    /**
     * Add a Behavior to the Actor.
     * An Actor accepts an undefined number of Behaviors.
     *
     * @param behavior {CAAT.Behavior} a CAAT.Behavior instance
     * @return this
     */
    public Path addBehavior (BaseBehavior behavior )  {
        this.behaviorList.add(behavior);
//        this.extractPathPoints();
        return this;
    }
    /**
     * Remove a Behavior from the Actor.
     * If the Behavior is not present at the actor behavior collection nothing happends.
     *
     * @param behavior {CAAT.Behavior} a CAAT.Behavior instance.
     */
    public Path removeBehaviour (BaseBehavior behavior ) {
        this.behaviorList.remove(behavior);
        return this;
    }
    /**
     * Remove a Behavior with id param as behavior identifier from this actor.
     * This function will remove ALL behavior instances with the given id.
     *
     * @param id {number} an integer.
     * return this;
     */
    public Path removeBehaviorById (String id ) {
        for (BaseBehavior behavior : this.behaviorList) {
            if (behavior.id.equals(id)) {
                this.behaviorList.remove(behavior);
            }
        }
        
        return this;

    }

    public Path applyBehaviors (double time) {
//        if (this.behaviorList.size() > 0) {
            
            for (BaseBehavior behavior : this.behaviorList) {
                // FIXME TODO Warning, it will be a BIG change : First priority !!!
                // add an Entity that will contain x, y, width, height
//                behavior.apply(time, this);
            }
            
            /** calculate behavior affine transform matrix **/
            this.setATMatrix();

            for (int i = 0; i < this.numControlPoints(); i++) {
                this.setPoint(
                    this.matrix.transformCoord(
                        this.pathPoints.get(i).clone().translate( this.clipOffsetX, this.clipOffsetY)), i);
            }
//        }

        return this;
    }

    public Path setATMatrix () {
        this.matrix.identity();

        Matrix m= this.tmpMatrix.identity();
        double[] mm= this.matrix.matrix;
        double c,s,_m00,_m01,_m10,_m11;
        double mm0, mm1, mm2, mm3, mm4, mm5;

//        mm[2]+= this.tb_x;
//        mm[5]+= this.tb_y;
        
        Rectangle bbox= this.bbox;
        double bbw= bbox.width  ;
        double bbh= bbox.height ;
        double bbx= bbox.x;
        double bby= bbox.y;
        
        mm0= 1;
        mm1= 0;
        mm3= 0;
        mm4= 1;

        mm2= this.tb_x - bbx - this.tAnchorX * bbw;
        mm5= this.tb_y - bby - this.tAnchorY * bbh;

        // TODO Check cond
        if ( this.rb_angle != 0) {
            
            double rbx= (this.rb_rotateAnchorX*bbw + bbx);
            double rby= (this.rb_rotateAnchorY*bbh + bby);

            mm2+= mm0*rbx + mm1*rby;
            mm5+= mm3*rbx + mm4*rby;

            c= Math.cos( this.rb_angle );
            s= Math.sin( this.rb_angle);
            _m00= mm0;
            _m01= mm1;
            _m10= mm3;
            _m11= mm4;
            mm0=  _m00*c + _m01*s;
            mm1= -_m00*s + _m01*c;
            mm3=  _m10*c + _m11*s;
            mm4= -_m10*s + _m11*c;

            mm2+= -mm0*rbx - mm1*rby;
            mm5+= -mm3*rbx - mm4*rby;
            
//            mm[2]+= mm[0]*this.rb_rotateAnchorX + mm[1]*this.rb_rotateAnchorY;
//            mm[5]+= mm[3]*this.rb_rotateAnchorX + mm[4]*this.rb_rotateAnchorY;
//
//            this.matrix.multiply( m.setRotation( this.rb_angle ) );
//
//            mm[2]+= -mm[0]*this.rb_rotateAnchorX - mm[1]*this.rb_rotateAnchorY;
//            mm[5]+= -mm[3]*this.rb_rotateAnchorX - mm[4]*this.rb_rotateAnchorY;
        }

        if ( this.sb_scaleX!=1 || this.sb_scaleY!=1 && (this.sb_scaleAnchorX != null || this.sb_scaleAnchorY != null )) {
            
            double sbx= (this.sb_scaleAnchorX*bbw + bbx);
            double sby= (this.sb_scaleAnchorY*bbh + bby);

            mm2+= mm0*sbx + mm1*sby;
            mm5+= mm3*sbx + mm4*sby;

            mm0= mm0*this.sb_scaleX;
            mm1= mm1*this.sb_scaleY;
            mm3= mm3*this.sb_scaleX;
            mm4= mm4*this.sb_scaleY;

            mm2+= -mm0*sbx - mm1*sby;
            mm5+= -mm3*sbx - mm4*sby;
            
//            mm[2]+= mm[0]*this.sb_scaleAnchorX + mm[1]*this.sb_scaleAnchorY;
//            mm[5]+= mm[3]*this.sb_scaleAnchorX + mm[4]*this.sb_scaleAnchorY;
//
//            mm[0]= mm[0]*this.sb_scaleX;
//            mm[1]= mm[1]*this.sb_scaleY;
//            mm[3]= mm[3]*this.sb_scaleX;
//            mm[4]= mm[4]*this.sb_scaleY;
//
//            mm[2]+= -mm[0]*this.sb_scaleAnchorX- mm[1]*this.sb_scaleAnchorY;
//            mm[5]+= -mm[3]*this.sb_scaleAnchorX - mm[4]*this.sb_scaleAnchorY;
        }
        
        mm[0]= mm0;
        mm[1]= mm1;
        mm[2]= mm2;
        mm[3]= mm3;
        mm[4]= mm4;
        mm[5]= mm5;

        return this;

    }

    public Path setRotationAnchored (double angle, double rx, double ry ) {
        this.rb_angle=          angle;
        this.rb_rotateAnchorX=  rx;
        this.rb_rotateAnchorY=  ry;
        return this;
    }
    
    public void setRotationAnchor (double ax, double ay ) {
        this.rb_rotateAnchorX= ax;
        this.rb_rotateAnchorY= ay;
    }

    public void setRotation (double angle ) {
        this.rb_angle= angle;
    }

    public Path setScaleAnchored (double scaleX,double scaleY,double sx,double sy ) {
        this.sb_scaleX= scaleX;
        this.sb_scaleAnchorX= sx;
        this.sb_scaleY= scaleY;
        this.sb_scaleAnchorY= sy;
        return this;
    }
    
    public Path setScale (double sx,double sy ) {
        this.sb_scaleX= sx;
        this.sb_scaleY= sy;
        return this;
    }

    public Path setScaleAnchor (double ax, double ay ) {
        this.sb_scaleAnchorX= ax;
        this.sb_scaleAnchorY= ay;
        return this;
    }

    public Path setPositionAnchor (double ax,double ay ) {
        this.tAnchorX= ax;
        this.tAnchorY= ay;
        return this;
    }

    public Path setPositionAnchored (int x, int y, int ax, int ay ) {
        this.tb_x= x;
        this.tb_y= y;
        this.tAnchorX= ax;
        this.tAnchorY= ay;
        return this;
    }

    public Path setPosition (int x,int y ) {
        this.tb_x= x;
        this.tb_y= y;
        return this;
    }

    public Path setLocation (int x, int y ) {
        this.tb_x= x;
        this.tb_y= y;
        return this;
    }
    
    public Path flatten(int npatches, boolean closed ) {
        Pt point= this.getPositionFromLength(0);
        Path path= new Path().beginPath( point.x, point.y );
        for( int i=0; i<npatches; i++ ) {
            point= this.getPositionFromLength(i/npatches*this.length);
            path.addLineTo( point.x, point.y  );
        }
        if ( closed) {
            path.closePath();
        } else {
            path.endPath();
        }

        return path;
    }

    @Override
    public PathSegment transform(Matrix matrix) {
        // TODO Auto-generated method stub
        return null;
    }

}
