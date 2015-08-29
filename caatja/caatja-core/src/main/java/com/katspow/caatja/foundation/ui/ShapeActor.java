package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;

public class ShapeActor extends ActorContainer {

    /**
     * This Actor draws common shapes, concretely Circles and rectangles.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public ShapeActor() {
        super();
        this.compositeOp = "source-over";

        /**
         * Thanks Svend Dutz and Thomas Karolski for noticing this call was not
         * performed by default, so if no explicit call to setShape was made,
         * nothing would be drawn.
         */
        this.setShape(Shape.CIRCLE);
    }
    
    /**
     * Define this actor shape: rectangle or circle
     */
    private Shape shape = Shape.CIRCLE; // shape type. One of the constant SHAPE_* values
    
    /**
     * Set this shape composite operation when drawing it.
     */
    public String compositeOp; // a valid canvas rendering context string describing compositeOps.
    
    /**
     * Stroke the shape with this line width.
     */
    int lineWidth = 1;
    
    /**
     * Stroke the shape with this line cap.
     */
    String lineCap = null;
    
    /**
     * Stroke the shape with this line Join.
     */
    String lineJoin = null;
    
    /**
     * Stroke the shape with this line mitter limit.
     */
    Integer miterLimit = null;
    
    public enum Shape {
        CIRCLE, RECTANGLE;
    }
    
    /**
     * 
     * @param l {number>0}
     */
    public ShapeActor setLineWidth (int l)  {
        this.lineWidth= l;
        return this;
    }
    /**
     *
     * @param lc {string{butt|round|square}}
     */
    public ShapeActor setLineCap (String lc)   {
        this.lineCap= lc;
        return this;
    }
    /**
     *
     * @param lj {string{bevel|round|miter}}
     */
    public ShapeActor setLineJoin (String lj)  {
        this.lineJoin= lj;
        return this;
    }
    /**
     *
     * @param ml {integer>0}
     */
    public ShapeActor setMiterLimit (int ml)    {
        this.miterLimit= ml;
        return this;
    }

    public String getLineCap() {
        return this.lineCap;
    }

    public String getLineJoin() {
        return this.lineJoin;
    }

    public int getMiterLimit() {
        return this.miterLimit;
    }

    public int getLineWidth() {
        return this.lineWidth;
    }

    /**
     * Sets shape type. No check for parameter validity is performed. Set paint
     * method according to the shape.
     * 
     * @param iShape
     *            an integer with any of the SHAPE_* constants.
     * @return this
     */
    public ShapeActor setShape(Shape shape) {
        this.shape = shape;
        return this;
    }
    
    /**
     * Sets the composite operation to apply on shape drawing.
     * @param compositeOp an string with a valid canvas rendering context string describing compositeOps.
     * @return this
     */
    public ShapeActor setCompositeOp(String compositeOp) {
        this.compositeOp = compositeOp;
        return this;
    }

    /**
     * Draws the shape.
     * Applies the values of fillStype, strokeStyle, compositeOp, etc.
     *
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     */
    public void paint(Director director, double time) {
        if (shape == Shape.CIRCLE) {
            paintCircle(director, time);
        } else if (shape == Shape.RECTANGLE) {
            paintRectangle(director, time);
        }
    }
    
    /**
     * @private
     * Draws a circle.
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     */
    private void paintCircle(Director director, double time) {

        if (this.cached.getValue() > 0) {
            super.paint(director, time);
            return;
        }

        CaatjaContext2d ctx = director.ctx;

        ctx.setLineWidth(this.lineWidth);

        ctx.setGlobalCompositeOperation(this.compositeOp);
        if (null != this.fillStyle) {
            ctx.setFillStyle(this.fillStyle);
            ctx.beginPath();
            ctx.arc(this.width / 2, this.height / 2, Math.min(this.width, this.height) / 2 - this.lineWidth / 2, 0,
                    2 * Math.PI, false);
            ctx.fill();
        }

        if (null != this.strokeStyle) {
            ctx.setStrokeStyle(this.strokeStyle);
            ctx.beginPath();
            ctx.arc(this.width / 2, this.height / 2, Math.min(this.width, this.height) / 2 - this.lineWidth / 2, 0,
                    2 * Math.PI, false);
            ctx.stroke();
        }
    }
    
    /**
    *
    * Private
    * Draws a Rectangle.
    *
    * @param director a valid CAAT.Director instance.
    * @param time an integer with the Scene time the Actor is being drawn.
    */
    public void paintRectangle (Director director, double time) {
        
        if ( this.cached.getValue() > 0 ) {
            super.paint(director, time );
            return;
        }
        
        CaatjaContext2d ctx= director.ctx;
        
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
        if ( null!=this.fillStyle ) {
            ctx.setFillStyle(this.fillStyle);
            ctx.beginPath();
            ctx.fillRect(0,0,this.width,this.height);
            ctx.fill();
        }

        if ( null!=this.strokeStyle ) {
            ctx.setStrokeStyle(this.strokeStyle);
            ctx.beginPath();
            ctx.strokeRect(0,0,this.width,this.height);
            ctx.stroke();
        }
    }

    // Add by me
    @Override
    public ShapeActor setBounds(double x, double y, double w, double h) {
        return (ShapeActor) super.setBounds(x, y, w, h);
    }

    @Override
    public ShapeActor setFillStyle(String fillStyle) {
        return (ShapeActor) super.setFillStyle(fillStyle);
    }

    @Override
    public ShapeActor addBehavior(BaseBehavior behaviour) {
        return (ShapeActor) super.addBehavior(behaviour);
    }

    @Override
    public ShapeActor setSize(double w, double h) {
        return (ShapeActor) super.setSize(w, h);
    }

    @Override
    public ShapeActor setLocation(double x, double y) {
        return (ShapeActor) super.setLocation(x, y);
    }

    @Override
    public ShapeActor setRotationAnchored(double angle, Double rx, Double ry) {
        return (ShapeActor) super.setRotationAnchored(angle, rx, ry);
    }

    @Override
    public ShapeActor setStringStrokeStyle(String strokeStyle) {
        return (ShapeActor) super.setStringStrokeStyle(strokeStyle);
    }

    @Override
    public ShapeActor setAlpha(double alpha) {
        return (ShapeActor) super.setAlpha(alpha);
    }

    @Override
    public ShapeActor enableEvents(boolean enable) {
        return (ShapeActor) super.enableEvents(enable);
    }
    @Override
    public ShapeActor setFillStrokeStyle(CaatjaColor style) {
        return (ShapeActor) super.setFillStrokeStyle(style);
    }
    @Override
    public ShapeActor setStrokeStyle(CaatjaColor style) {
        return (ShapeActor) super.setStrokeStyle(style);
    }
    @Override
    public ShapeActor setPosition(double x, double y) {
        return (ShapeActor) super.setPosition(x, y);
    }
    
}
