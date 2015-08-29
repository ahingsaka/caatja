package com.katspow.caatja.foundation.ui;

import java.util.List;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Pt;

/**
 * Interpolator actor will draw interpolators on screen.
 *
 *
 */
/**
 * @name InterpolatorActor
 * @memberOf CAAT.Foundation.UI
 * @extends CAAT.Foundation.Actor
 * @constructor
 */
public class InterpolatorActor extends ActorContainer {

    /**
     * This actor class draws an interpolator function by caching an interpolator contour as a polyline.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public InterpolatorActor() {
        super();
    }

    /**
     * The interpolator instance to draw.
     * @type {CAAT.Behavior.Interpolator}
     */
    private Interpolator interpolator = null;
    
    /**
     * This interpolator�s contour.
     * @type {Array.<CAAT.Math.Point>}
     */
    private List<Pt> contour = null;
    
    /**
     * Number of samples to calculate a contour.
     */
    private int S = 50;
    
    /**
     * padding when drawing the interpolator.
     */
    private double gap = 5;
    
    /**
     * Sets a padding border size. By default is 5 pixels.
     * @param gap {number} border size in pixels.
     * @return this
     */
    public InterpolatorActor setGap(double gap) {
        this.gap = gap;
        return this;
    }

    /**
     * Sets the CAAT.Interpolator instance to draw.
     *
     * @param interpolator a CAAT.Interpolator instance.
     * @param size an integer indicating the number of polyline segments so draw to show the CAAT.Interpolator
     * instance.
     * @return this
     */
    public InterpolatorActor setInterpolator(Interpolator interpolator, Integer size) {
        this.interpolator = interpolator;
        
        if (size != null) {
            this.contour = interpolator.getContour(size);
        } else {
            this.contour = interpolator.getContour(this.S);
        }
        
        return this;
    }
    
    // Add by me
    public InterpolatorActor setInterpolator(Interpolator interpolator) {
        return setInterpolator(interpolator, null);
    }
            

    /**
     * Paint this actor.
     * @param director {CAAT.Director}
     * @param time {number} scene time.
     */
    public void paint(Director director, double time) {

        super.paint(director, time);
        
        if ( this.backgroundImage != null) {
            // TODO No returning object ?
            return;
        }

        if (this.interpolator != null) {

            CaatjaContext2d canvas = director.ctx;
            
            double xs= (this.width-2*this.gap);
            double ys= (this.height-2*this.gap);
            
            canvas.beginPath();
            canvas.moveTo(
                    this.gap + xs * this.contour.get(0).x,
                    -this.gap + this.height - ys * this.contour.get(0).y );

            for (int i = 1; i < this.contour.size(); i++) {
                canvas.lineTo(
                        this.gap + xs * this.contour.get(i).x,
                        -this.gap + this.height - ys * this.contour.get(i).y);
            }

            // Add by me
            if (strokeStyle != null) {
                canvas.setStrokeStyle(this.strokeStyle);
            }
            
            canvas.stroke();
        }
    }
    
    /**
     * Return the represented interpolator.
     * @return {CAAT.Interpolator}
     */
    public Interpolator getInterpolator() {
        return interpolator;
    }
    
    // Add by me
    @Override
    public InterpolatorActor setBounds(double x, double y, double w, double h) {
        return (InterpolatorActor) super.setBounds(x, y, w, h);
    }

    @Override
    public InterpolatorActor setStringStrokeStyle(String strokeStyle) {
        return (InterpolatorActor) super.setStringStrokeStyle(strokeStyle);
    }

    @Override
    public InterpolatorActor setFillStyle(String fillStyle) {
        return (InterpolatorActor) super.setFillStyle(fillStyle);
    }
    

}
