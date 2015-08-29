package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.ActorContainer;

public class StarActor extends ActorContainer {

    /**
     * This actor draws stars.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public StarActor() {
        super();
        this.compositeOp= "source-over";
    }

    /**
     * Number of star peaks.
     */
    public int nPeaks = 0;
    
    /**
     * Maximum radius.
     */
    public double maxRadius = 0;
    
    /**
     * Minimum radius.
     */
    public double minRadius = 0;
    
    /**
     * Staring angle in radians.
     */
    public double initialAngle = 0;
    
    /**
     * Draw the star with this composite operation.
     */
    public String compositeOp =    null;
    double lineWidth = 1;
    String lineCap = null;
    String lineJoin = null;
    Integer miterLimit = null;
    
    /**
    *
    * @param l {number>0}
    */
   public StarActor setLineWidth (double l)  {
       this.lineWidth= l;
       return this;
   }
   /**
    *
    * @param lc {string{butt|round|square}}
    */
   public StarActor setLineCap (String lc)   {
       this.lineCap= lc;
       return this;
   }
   /**
    *
    * @param lj {string{bevel|round|miter}}
    */
   public StarActor setLineJoin (String lj)  {
       this.lineJoin= lj;
       return this;
   }
   /**
    *
    * @param ml {integer>0}
    */
   public StarActor setMiterLimit (int ml)    {
       this.miterLimit= ml;
       return this;
   }
   public String getLineCap () {
       return this.lineCap;
   }
   public String getLineJoin ()    {
       return this.lineJoin;
   }
   public Integer getMiterLimit ()  {
       return this.miterLimit;
   }
   public double getLineWidth ()   {
       return this.lineWidth;
   }

    /**
     * Sets whether the star will be color filled.
     * @param filled {boolean}
     * @deprecated
     */
    @Deprecated
    public StarActor setFilled(boolean filled) {
        return this;
    }

    /**
     * Sets whether the star will be outlined.
     * @param outlined {boolean}
     * @deprecated
     */
    @Deprecated
    public StarActor setOutlined(boolean outlined) {
        return this;
    }
    
    /**
     * Sets the composite operation to apply on shape drawing.
     * @param compositeOp an string with a valid canvas rendering context string describing compositeOps.
     * @return this
     */
    public StarActor setCompositeOp(String compositeOp) {
        this.compositeOp = compositeOp;
        return this;
    }
    
    /**
     * 
     * @param angle {number} number in radians.
     */
    public StarActor setInitialAngle(double angle) {
        this.initialAngle= angle;
        return this;
    }

    /**
     * Initialize the star values.
     * <p>
     * The star actor will be of size 2*maxRadius.
     *
     * @param nPeaks {number} number of star points.
     * @param maxRadius {number} maximum star radius
     * @param minRadius {number} minimum star radius
     *
     * @return this
     */
    public StarActor initialize(int nPeaks, double maxRadius, double minRadius) {
        this.setSize(2 * maxRadius, 2 * maxRadius);

        this.nPeaks = nPeaks;
        this.maxRadius = maxRadius;
        this.minRadius = minRadius;

        return this;
    }

    /**
     * Paint the star.
     *
     * @param director {CAAT.Director}
     * @param timer {number}
     */
    public void paint(Director director, double timer) {

        CaatjaContext2d ctx = director.ctx;
        double centerX = this.width / 2;
        double centerY = this.height / 2;
        double r1 = this.maxRadius;
        double r2 = this.minRadius;
        double ix = centerX + r1 * Math.cos(this.initialAngle);
        double iy = centerY + r1 * Math.sin(this.initialAngle);
        
        ctx.setLineWidth(this.lineWidth);
        if ( this.lineCap != null) {
            ctx.setLineCap(this.lineCap);
        }
        if ( this.lineJoin != null)    {
            ctx.setLineJoin(this.lineJoin);
        }
        if ( this.miterLimit != null)  {
            ctx.setMiterLimit(this.miterLimit);
        }
        
        ctx.setGlobalCompositeOperation(this.compositeOp);
        ctx.beginPath();

        ctx.moveTo(ix, iy);

        for (int i = 1; i < this.nPeaks * 2; i++) {
            double angleStar = Math.PI / this.nPeaks * i + this.initialAngle;
            double rr = (i % 2 == 0) ? r1 : r2;
            double x = centerX + rr * Math.cos(angleStar);
            double y = centerY + rr * Math.sin(angleStar);
            ctx.lineTo(x, y);
        }
        
        ctx.lineTo(centerX + r1 * Math.cos(this.initialAngle), centerY + r1 * Math.sin(this.initialAngle));

        ctx.closePath();
        
        if (this.fillStyle != null) {
            ctx.setFillStyle(this.fillStyle);
            ctx.fill();
        }

        if (this.strokeStyle != null) {
            ctx.setStrokeStyle(this.strokeStyle);
            ctx.stroke();
        }
    }
    
    // Add by me
    
    @Override
    public StarActor enableEvents(boolean enable) {
        return (StarActor) super.enableEvents(enable);
    }
    @Override
    public StarActor setFillStyle(String fillStyle) {
        return (StarActor) super.setFillStyle(fillStyle);
    }
    @Override
    public StarActor setSize(double w, double h) {
        return (StarActor) super.setSize(w, h);
    }
    @Override
    public StarActor setLocation(double x, double y) {
        return (StarActor) super.setLocation(x, y);
    }
    @Override
    public StarActor setStringStrokeStyle(String strokeStyle) {
        return (StarActor) super.setStringStrokeStyle(strokeStyle);
    }
    @Override
    public StarActor setAlpha(double alpha) {
        return (StarActor) super.setAlpha(alpha);
    }
    @Override
    public StarActor setBounds(double x, double y, double w, double h) {
        return (StarActor) super.setBounds(x, y, w, h);
    }
    
}
