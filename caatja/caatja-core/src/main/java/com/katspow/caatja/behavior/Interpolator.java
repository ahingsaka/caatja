package com.katspow.caatja.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

/**
 * @author  Hyperandroid  ||  http://hyperandroid.com/
 *
 * Interpolator actor will draw interpolators on screen.
 *
 **/
public class Interpolator {
    
 // a coordinate holder for not building a new CAAT.Point for each interpolation call.
    protected Pt interpolated;
    
 // the size of the interpolation draw on screen in pixels.
    private int paintScale = 90;

    /**
     * a CAAT.Interpolator is a function which transforms a value into another but with some constraints:
     *
     * <ul>
     * <li>The input values must be between 0 and 1.
     * <li>Output values will be between 0 and 1.
     * <li>Every Interpolator has at least an entering boolean parameter called pingpong. if set to true, the Interpolator
     * will set values from 0..1 and back from 1..0. So half the time for each range.
     * </ul>
     *
     * <p>
     * CAAt.Interpolator is defined by a createXXXX method which sets up an internal getPosition(time)
     * function. You could set as an Interpolator up any object which exposes a method getPosition(time)
     * and returns a CAAT.Point or an object of the form {x:{number}, y:{number}}.
     * <p>
     * In the return value, the x attribute's value will be the same value as that of the time parameter,
     * and y attribute will hold a value between 0 and 1 with the resulting value of applying the
     * interpolation function for the time parameter.
     *
     * <p>
     * For am exponential interpolation, the getPosition function would look like this:
     * <code>function getPosition(time) { return { x:time, y: Math.pow(time,2) }Ê}</code>.
     * meaning that for time=0.5, a value of 0,5*0,5 should use instead.
     *
     * <p>
     * For a visual understanding of interpolators see tutorial 4 interpolators, or play with technical
     * demo 1 where a SpriteActor moves along a path and the way it does can be modified by every
     * out-of-the-box interpolator.
     *
     * @constructor
     *
     */
    private Interpolator() {
        this.interpolated = new Pt();
    }
    
    // Should be overriden by subclasses
    public Pt getPosition(double time) {
        return null;
    }
    
    private class LinearInterpolator extends Interpolator {

        private Boolean bPingPong;
        private Boolean bInverse;

        public LinearInterpolator(Boolean bPingPong, Boolean bInverse) {
            this.bPingPong = bPingPong;
            this.bInverse = bInverse;
        }
        
        /**
         * Linear and inverse linear interpolation function.
         * @param time {number}
         */
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            if ( bInverse!=null && bInverse ) {
                time= 1-time;
            }

            return this.interpolated.set(orgTime,time);
        }
    }
    
    /**
     * Set a linear interpolation function.
     *
     * @param bPingPong {boolean}
     * @param bInverse {boolean} will values will be from 1 to 0 instead of 0 to 1 ?.
     */
    public static Interpolator createLinearInterpolator (Boolean bPingPong, Boolean bInverse) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new LinearInterpolator(bPingPong, bInverse);
    }
    
    private class BackOutInterpolator extends Interpolator {
        
        private Boolean bPingPong;
        
        public BackOutInterpolator(Boolean bPingPong) {
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            time = time - 1;
            double overshoot= 1.70158;

            return this.interpolated.set(
                    orgTime,
                    time * time * ((overshoot + 1) * time + overshoot) + 1);
        }
    }
    
    public static Interpolator createBackOutInterpolator (boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new BackOutInterpolator(bPingPong);
    }
    
    private class ExponentialInInterpolator extends Interpolator {
        
        Boolean bPingPong;
        int exponent;
        
        public ExponentialInInterpolator(int exponent, Boolean bPingPong) {
            this.bPingPong = bPingPong;
            this.exponent = exponent;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }
            return this.interpolated.set(orgTime,Math.pow(time,exponent));
        }
    }
    
    /**
     * Set an exponential interpolator function. The function to apply will be Math.pow(time,exponent).
     * This function starts with 0 and ends in values of 1.
     *
     * @param exponent {number} exponent of the function.
     * @param bPingPong {boolean}
     */
    public static Interpolator createExponentialInInterpolator (int exponent, boolean  bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ExponentialInInterpolator(exponent, bPingPong);
    }
    
    private class ExponentialOutInterpolator extends Interpolator {
        
        Boolean bPingPong;
        int exponent;
        
        public ExponentialOutInterpolator(int exponent, boolean bPingPong) {
            this.bPingPong = bPingPong;
            this.exponent = exponent;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }
            return this.interpolated.set(orgTime,1-Math.pow(1-time,exponent));
        }
    }
    
    /**
     * Set an exponential interpolator function. The function to apply will be 1-Math.pow(time,exponent).
     * This function starts with 1 and ends in values of 0.
     *
     * @param exponent {number} exponent of the function.
     * @param bPingPong {boolean}
     */
    public static Interpolator createExponentialOutInterpolator (int exponent,boolean  bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ExponentialOutInterpolator(exponent, bPingPong);
    }
    
    private class ExponentialInOutInterpolator extends Interpolator {
        
        int exponent;
        Boolean bPingPong;
        
        public ExponentialInOutInterpolator(int exponent, Boolean bPingPong) {
            this.exponent = exponent;
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }
            if ( time*2<1 ) {
                return this.interpolated.set(orgTime,Math.pow(time*2,exponent)/2);
            }
            
            return this.interpolated.set(orgTime,1-Math.abs(Math.pow(time*2-2,exponent))/2);
        }
    }
    
    /**
     * Set an exponential interpolator function. Two functions will apply:
     * Math.pow(time*2,exponent)/2 for the first half of the function (t<0.5) and
     * 1-Math.abs(Math.pow(time*2-2,exponent))/2 for the second half (t>=.5)
     * This function starts with 0 and goes to values of 1 and ends with values of 0.
     *
     * @param exponent {number} exponent of the function.
     * @param bPingPong {boolean}
     */
    public static Interpolator createExponentialInOutInterpolator (int exponent,boolean  bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ExponentialInOutInterpolator(exponent, bPingPong);
    }
    
    
    private class QuadricBezierInterpolator extends Interpolator {
        
        Pt p0;
        Pt p1;
        Pt p2;
        Boolean bPingPong;
        
        public QuadricBezierInterpolator(Pt p0, Pt p1, Pt p2, Boolean bPingPong) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            time= (1-time)*(1-time)*p0.y + 2*(1-time)*time*p1.y + time*time*p2.y;

            return this.interpolated.set( orgTime, time );
        }
    }
    
    /**
     * Creates a Quadric bezier curbe as interpolator.
     *
     * @param p0,p1,p2 CAAT.Point instances.
     * @param bPingPong a boolean indicating if the interpolator must ping-pong.
     */
    public static Interpolator createQuadricBezierInterpolator (Pt p0,Pt p1,Pt p2,boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new QuadricBezierInterpolator(p0, p1, p2, bPingPong);
    }
    
    /**
     * Creates a Cubic bezier curbe as interpolator.
     *
     * @param p0,p1,p2,p3 CAAT.Point instances.
     * @param bPingPong a boolean indicating if the interpolator must ping-pong.
     */
    private class CubicBezierInterpolator extends Interpolator {
        
        Pt p0;
        Pt p1;
        Pt p2;
        Pt p3;
        
        Boolean bPingPong;
        
        public CubicBezierInterpolator(Pt p0, Pt p1, Pt p2, Pt p3, Boolean bPingPong) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            double orgTime= time;

            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            double t2= time*time;
            double t3= time*t2;

            time = (p0.y + time * (-p0.y * 3 + time * (3 * p0.y -
                    p0.y * time))) + time * (3 * p1.y + time * (-6 * p1.y +
                    p1.y * 3 * time)) + t2 * (p2.y * 3 - p2.y * 3 * time) +
                    p3.y * t3;

            return this.interpolated.set( orgTime, time );
        }
    }
    
    /**
     * Creates a Cubic bezier curbe as interpolator.
     *
     * @param p0 {CAAT.Point} a CAAT.Point instance.
     * @param p1 {CAAT.Point} a CAAT.Point instance.
     * @param p2 {CAAT.Point} a CAAT.Point instance.
     * @param p3 {CAAT.Point} a CAAT.Point instance.
     * @param bPingPong {boolean} a boolean indicating if the interpolator must ping-pong.
     */
    public static Interpolator createCubicBezierInterpolator (Pt p0,Pt p1,Pt p2,Pt p3,boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new CubicBezierInterpolator(p0, p1, p2, p3, bPingPong);
    }
    
    private class ElasticOutInterpolator extends Interpolator {
        
        double amplitude;
        double p;
        Boolean bPingPong;
        
        public ElasticOutInterpolator(double amplitude, double p, Boolean bPingPong) {
            this.amplitude = amplitude;
            this.p = p;
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            if (time == 0) {
                return new Pt().set(0, 0);
            }
            if (time == 1) {
                return new Pt().set(1, 1);
            }

            double s = p/(2*Math.PI) * Math.asin (1/amplitude);
            return this.interpolated.set(
                    time,
                    (amplitude*Math.pow(2,-10*time) * Math.sin( (time-s)*(2*Math.PI)/p ) + 1 ) );
        }
    }
    
    public static Interpolator createElasticOutInterpolator (double amplitude,double p,boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ElasticOutInterpolator(amplitude, p, bPingPong);
    }
    
    private class ElasticInInterpolator extends Interpolator {
        
        double amplitude;
        double p;
        Boolean bPingPong;
        
        public ElasticInInterpolator(double amplitude, double p, Boolean bPingPong) {
            this.amplitude = amplitude;
            this.p = p;
            this.bPingPong = bPingPong;
        }

        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            if (time == 0) {
                return new Pt().set(0, 0);
            }
            if (time == 1) {
                return new Pt().set(1, 1);
            }

            double s = p/(2*Math.PI) * Math.asin (1/amplitude);
            return this.interpolated.set(
                    time,
                    -(amplitude*Math.pow(2,10*(time-=1)) * Math.sin( (time-s)*(2*Math.PI)/p ) ) );
        }
    }
    
    public static Interpolator createElasticInInterpolator (double amplitude, double p,boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ElasticInInterpolator(amplitude, p, bPingPong);
    }

    private class ElasticInOutInterpolator extends Interpolator {

        double amplitude;
        double p;
        Boolean bPingPong;
        
        public ElasticInOutInterpolator(double amplitude, double p, Boolean bPingPong) {
            this.amplitude = amplitude;
            this.p = p;
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            double s = p/(2*Math.PI) * Math.asin (1/amplitude);
            time*=2;
            if ( time<=1 ) {
                return this.interpolated.set(
                        time,
                        -0.5*(amplitude*Math.pow(2,10*(time-=1)) * Math.sin( (time-s)*(2*Math.PI)/p )));
            }

            return this.interpolated.set(
                    time,
                    1+0.5*(amplitude*Math.pow(2,-10*(time-=1)) * Math.sin( (time-s)*(2*Math.PI)/p )));
        }
    }
    
    
    public static Interpolator createElasticInOutInterpolator (double amplitude,double p,boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new ElasticInOutInterpolator(amplitude, p, bPingPong);
    }
    
    /**
     * @param time {number}
     * @private
     */
    public Pt bounce (double time) {
        if ((time /= 1) < (1 / 2.75)) { 
            return new Pt().set(time, 7.5625 * time * time);
            
        } else if (time < (2 / 2.75)) {
            return new Pt().set(time, 7.5625 * (time -= (1.5 / 2.75)) * time + 0.75);
            
        } else if (time < (2.5 / 2.75)) {
            return new Pt().set(time, 7.5625 * (time -= (2.25 / 2.75)) * time + 0.9375);
            
        } else {
            return new Pt().set(time, 7.5625*(time-=(2.625/2.75))*time+0.984375);
        }
    }
    
    private class BounceOutInterpolator extends Interpolator {
        
        Boolean bPingPong;
        
        public BounceOutInterpolator(Boolean bPingPong) {
            this.bPingPong = bPingPong;
        }
        
        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }
            return this.bounce(time);
        }
        
    }
    
    public static Interpolator createBounceOutInterpolator (boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new BounceOutInterpolator(bPingPong);
    }
    
    private class BounceInInterpolator extends Interpolator {
        
        boolean bPingPong;

        public BounceInInterpolator(boolean bPingPong) {
            this.bPingPong = bPingPong;
        }

        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }
            Pt r= this.bounce(1-time);
            r.y= 1-r.y;
            return r;
        }
        
    }
    
    public static Interpolator createBounceInInterpolator (boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new BounceInInterpolator(bPingPong);
    }
    
    private class BounceInOutInterpolator extends Interpolator {
        
        boolean bPingPong;
            
        public BounceInOutInterpolator(boolean bPingPong) {
            this.bPingPong = bPingPong;
        }

        @Override
        public Pt getPosition(double time) {
            if ( bPingPong ) {
                if ( time<0.5 ) {
                    time*=2;
                } else {
                    time= 1-(time-0.5)*2;
                }
            }

            Pt r;
            if (time < 0.5) {
                r= this.bounce(1 - time * 2);
                r.y= (1 - r.y)* 0.5;
                return r;
            }
            
            // TODO check if bPingPong is really needed
            //r= this.bounce(time * 2 - 1,bPingPong);
            r = this.bounce(time * 2 - 1);
            
            r.y= r.y* 0.5 + 0.5;
            return r;
        }
    }
    
    public static Interpolator createBounceInOutInterpolator (boolean bPingPong) {
    	Interpolator interpolator = new Interpolator();
        return interpolator.new BounceInOutInterpolator(bPingPong);
    }
    /**
     * Paints an interpolator on screen.
     * @param director {CAAT.Director} a CAAT.Director instance.
     * @param time {number} an integer indicating the scene time the Interpolator will be drawn at. This value is useless.
     */
    public void paint (Director director, double time) {

        CaatjaContext2d canvas= director.ctx;
        canvas.save();
        canvas.beginPath();

        canvas.moveTo( 0, this.getPosition(0).y * this.paintScale );

        for( int i=0; i<=this.paintScale; i++ ) {
            canvas.lineTo( i, this.getPosition((double) i/this.paintScale).y * this.paintScale );
        }

        canvas.setStrokeStyle("black");
        canvas.stroke();
        canvas.restore();
    }
    /**
     * Gets an array of coordinates which define the polyline of the intepolator's curve contour.
     * Values for both coordinates range from 0 to 1. 
     * @param iSize {number} an integer indicating the number of contour segments.
     * @return array {[CAAT.Point]} of object of the form {x:float, y:float}.
     */
    public List<Pt> getContour (int iSize) {
        List<Pt> contour = new ArrayList<Pt>();
        for( int i=0; i<=iSize; i++ ) {
            contour.add(new Pt().set((double) i/iSize, this.getPosition((double )i/iSize).y));
        }

        return contour;
    }
    
    //TODO
    public static Interpolator parse(Object obj ) {
//        String name= "create"+obj.type+"Interpolator";
        Interpolator interpolator= new Interpolator();
//        try {
//            interpolator[name].apply( interpolator, obj.params||[] );
//        } catch(Exception e) {
//            interpolator.createLinearInterpolator(false, false);
//        }

        return interpolator;
    }
    
    /**
     * Returns all possible interpolators in hashmap
     */
    @SuppressWarnings("serial")
    public static Map<String, Interpolator> enumerateInterpolators () {
        
        return new HashMap<String, Interpolator>() {{
            
            put("Linear pingpong=false, inverse=false", createLinearInterpolator(false, false));
            put("Linear pingpong=true, inverse=false", createLinearInterpolator(true,  false));
            
            put("BackOut pingpong=true, inverse=false", createBackOutInterpolator(false));
            put("BackOut pingpong=true, inverse=true", createBackOutInterpolator(true));
            
            put("Linear pingpong=false, inverse=true", createLinearInterpolator(false, true));
            put("Linear pingpong=true, inverse=true", createLinearInterpolator(true,  true));
            put("ExponentialIn pingpong=false, exponent=2", createExponentialInInterpolator(2, false));
            put("ExponentialOut pingpong=false, exponent=2", createExponentialOutInterpolator(2, false));
            put("ExponentialInOut pingpong=false, exponent=2", createExponentialInOutInterpolator( 2, false));
            put("ExponentialIn pingpong=true, exponent=2", createExponentialInInterpolator(    2, true));
            put("ExponentialOut pingpong=true, exponent=2", createExponentialOutInterpolator(   2, true));
            put("ExponentialInOut pingpong=true, exponent=2", createExponentialInOutInterpolator( 2, true));
            put("ExponentialIn pingpong=false, exponent=4", createExponentialInInterpolator(    4, false));
            put("ExponentialOut pingpong=false, exponent=4", createExponentialOutInterpolator(   4, false));
            put("ExponentialInOut pingpong=false, exponent=4", createExponentialInOutInterpolator( 4, false));
            put("ExponentialIn pingpong=true, exponent=4", createExponentialInInterpolator(    4, true));
            put("ExponentialOut pingpong=true, exponent=4", createExponentialOutInterpolator(   4, true));
            put("ExponentialInOut pingpong=true, exponent=4", createExponentialInOutInterpolator( 4, true));
            put("ExponentialIn pingpong=false, exponent=6", createExponentialInInterpolator(    6, false));
            put("ExponentialOut pingpong=false, exponent=6", createExponentialOutInterpolator(   6, false));
            put("ExponentialInOut pingpong=false, exponent=6", createExponentialInOutInterpolator( 6, false));
            put("ExponentialIn pingpong=true, exponent=6", createExponentialInInterpolator(    6, true));
            put("ExponentialOut pingpong=true, exponent=6", createExponentialOutInterpolator(   6, true));
            put("ExponentialInOut pingpong=true, exponent=6", createExponentialInOutInterpolator( 6, true));
            put("BounceIn pingpong=false", createBounceInInterpolator(false));
            put("BounceOut pingpong=false", createBounceOutInterpolator(false));
            put("BounceInOut pingpong=false", createBounceInOutInterpolator(false));
            put("BounceIn pingpong=true", createBounceInInterpolator(true));
            put("BounceOut pingpong=true", createBounceOutInterpolator(true));
            put("BounceInOut pingpong=true", createBounceInOutInterpolator(true));
            put("ElasticIn pingpong=false, amp=1.1, d=.4", createElasticInInterpolator(    1.1, 0.4, false));
            put("ElasticOut pingpong=false, amp=1.1, d=.4", createElasticOutInterpolator(   1.1, 0.4, false));
            put("ElasticInOut pingpong=false, amp=1.1, d=.4", createElasticInOutInterpolator( 1.1, 0.4, false));
            put("ElasticIn pingpong=true, amp=1.1, d=.4", createElasticInInterpolator(    1.1, 0.4, true));
            put("ElasticOut pingpong=true, amp=1.1, d=.4", createElasticOutInterpolator(   1.1, 0.4, true));
            put("ElasticInOut pingpong=true, amp=1.1, d=.4", createElasticInOutInterpolator( 1.1, 0.4, true));
            put("ElasticIn pingpong=false, amp=1.0, d=.2", createElasticInInterpolator(    1.0, 0.2, false));
            put("ElasticOut pingpong=false, amp=1.0, d=.2", createElasticOutInterpolator(   1.0, 0.2, false));
            put("ElasticInOut pingpong=false, amp=1.0, d=.2", createElasticInOutInterpolator( 1.0, 0.2, false));
            put("ElasticIn pingpong=true, amp=1.0, d=.2", createElasticInInterpolator(    1.0, 0.2, true));
            put("ElasticOut pingpong=true, amp=1.0, d=.2", createElasticOutInterpolator(   1.0, 0.2, true));
            put("ElasticInOut pingpong=true, amp=1.0, d=.2",  createElasticInOutInterpolator( 1.0, 0.2, true));
        }};
        
    }
    
   
}
