package com.katspow.caatja.foundation.actor;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.pathutil.Path;

public class ImageActor extends ActorContainer {

    /**
     * 
     * This class shows a simple image on screen. It can be flipped by calling
     * the method <code>
     * setImageTransformation</code>, and offseted, that is, translated from
     * actors 0,0 position by calling the methods
     * <code>setOffsetX( {float} ) and setOffsetY( {float} )</code>.
     * 
     * @constructor
     * @extends CAAT.ActorContainer
     * 
     */
    @Deprecated
    public ImageActor() {
        super();
        this.glEnabled = true;
    }

    // Add by me
    private CaatjaCanvas canvasElement = null;

    private CaatjaImage image = null;
    public Tr transformation = Tr.NONE; // any of the Tr* constants.

    // constants used to determine how to draw the sprite image)
    public enum Tr {
        NONE(0), FLIP_HORIZONTAL(1), FLIP_VERTICAL(2), FLIP_ALL(3), FIXED_TO_SIZE(4);

        private int val;

        private Tr(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    public double offsetX = 0;
    public double offsetY = 0;

    /**
     * Set horizontal displacement to draw image. Positive values means drawing
     * the image more to the right.
     * 
     * @param x
     *            {number}
     * @return this
     */
    public ImageActor setOffsetX(double x) {
        this.offsetX = (int) x | 0;
        return this;
    }

    /**
     * Set vertical displacement to draw image. Positive values means drawing
     * the image more to the bottom.
     * 
     * @param y
     *            {number}
     * @return this
     */
    public ImageActor setOffsetY(double y) {
        this.offsetY = (int) y | 0;
        return this;
    }

    public ImageActor setOffset(double x, double y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}

    /**
     * Set the image to draw. If this CAAT.ImageActor has not set dimension, the
     * actor will be equal size to the image.
     * 
     * @param image
     *            {HTMLImageElement}
     * @return this
     */
    public ImageActor setImage(CaatjaImage image) {
        this.image = image;
        if (image != null && (this.width == 0 || this.height == 0)) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
        return this;
    }

    // Add by me
    // TODO Check if it works
    public ImageActor setCanvas(CaatjaCanvas canvas) {
        this.canvasElement = canvas;
        if (this.width == 0 || this.height == 0) {
            this.width = canvas.getCoordinateSpaceWidth();
            this.height = canvas.getCoordinateSpaceHeight();
        }

        return this;
    }

    /**
     * Set the transformation to apply to the image. Any value of
     * <ul>
     * <li>TR_NONE
     * <li>TR_FLIP_HORIZONTAL
     * <li>TR_FLIP_VERTICAL
     * <li>TR_FLIP_ALL
     * </ul>
     * 
     * @param transformation
     *            {number} an integer indicating one of the previous values.
     * @return this
     */
    public ImageActor setImageTransformation(Tr transformation) {
        this.transformation = transformation;
        
        // TODO Cannot make this in Java ...
//        switch(this.transformation)	{
//        case FLIP_HORIZONTAL:
//            //paintInvertedH( ctx);
//            paint= paintInvertedH;
//            break;
//        case FLIP_VERTICAL:
//            //paintInvertedV( ctx);
//            paint= paintInvertedV;
//            break;
//        case FLIP_ALL:
//            //paintInvertedHV( ctx);
//            paint= paintInvertedHV;
//            break;
//        case FIXED_TO_SIZE:
//            paint= paintFixed;
//    }
        
        return this;
    }
    
    // TODO Check
    public void paintFixed(Director director, double time) {
    	if (canvasElement != null) {
    		director.ctx.drawImage(this.canvasElement,this.offsetX,this.offsetY,this.width,this.height);
    	} else if (image != null) {
    		director.ctx.drawImage(this.image,this.offsetX,this.offsetY,this.width,this.height);
    	} else {
    	    CaatjaContext2d ctx= director.ctx;
            ctx.setFillStyle(this.fillStyle);
            ctx.fillRect(0,0,this.width,this.height);
    	}
    }

    /**
     * Draws the image.
     * 
     * @param director
     *            the Director object instance that contains the Scene the Actor
     *            is in.
     * @param time
     *            an integer indicating the Scene time when the bounding box is
     *            to be drawn.
     */
    public void paint(Director director, double time) {

        CaatjaContext2d ctx = director.ctx;
        // TODO no need since the switch handles all situations
//        ctx.drawImage(this.canvasElement, this.offsetX, this.offsetY);

        // TODO Remove this and use setImageTransformation ?
        // drawn at 0,0 because they're already affine-transformed.
        switch (this.transformation) {
        case FLIP_HORIZONTAL:
            this.paintInvertedH(director, time);
            break;
        case FLIP_VERTICAL:
            this.paintInvertedV(director, time);
            break;
        case FLIP_ALL:
            this.paintInvertedHV(director, time);
            break;
        case FIXED_TO_SIZE:
        	this.paintFixed(director, time);
        	break;
        default:
            if (canvasElement == null) {
                ctx.drawImage(this.image, this.offsetX, this.offsetY);
            } else {
                ctx.drawImage(this.canvasElement, this.offsetX, this.offsetY, this.width, this.height);
            }
        }
    }

    public boolean paintActorGL(Director director, double time) {
        if (null == this.image) {
            // TODO Check return type
            return true;
        }

        return super.paintActorGL(director, time);
    }

    public void paintInvertedH(Director director, double time) {
    	
        CaatjaContext2d ctx= director.ctx;

        ctx.save();
        ctx.translate(this.width, 0);
        ctx.scale(-1, 1);

        if (canvasElement == null) {
            ctx.drawImage(this.image, this.offsetX, this.offsetY);
        } else {
            ctx.drawImage(this.canvasElement, this.offsetX, this.offsetY);
        }

        ctx.restore();
    }

    public void paintInvertedV(Director director, double time) {
    	
        CaatjaContext2d ctx= director.ctx;
    	
        ctx.save();
        ctx.translate(0, this.height);
        ctx.scale(1, -1);

        if (canvasElement == null) {
            ctx.drawImage(this.image, this.offsetX, this.offsetY);
        } else {
            ctx.drawImage(this.canvasElement, this.offsetX, this.offsetY);
        }

        ctx.restore();
    }

    public void paintInvertedHV(Director director, double time) {
    	
        CaatjaContext2d ctx= director.ctx;
    	
        ctx.save();
        ctx.translate(0, this.height);
        ctx.scale(1, -1);
        ctx.translate(this.width, 0);
        ctx.scale(-1, 1);

        if (canvasElement == null) {
            ctx.drawImage(this.image, this.offsetX, this.offsetY);
        } else {
            ctx.drawImage(this.canvasElement, this.offsetX, this.offsetY);
        }

        ctx.restore();
    }

    /**
     * 
     * @param uvBuffer
     *            {Float32Array}
     * @param uvIndex
     *            {Number}
     *            
     * FIXME
     */
//    public void setUV(Float32Array uvBuffer, int uvIndex) {
//
//        int index = uvIndex;
//
//        CaatjaImage im = this.image;
//
//        if (im.__texturePage == null) {
//            return;
//        }
//
//        double u = im.__u;
//        double v = im.__v;
//        double u1 = im.__u1;
//        double v1 = im.__v1;
//        if (this.offsetX > 0 || this.offsetY > 0) {
//            double w = this.width;
//            double h = this.height;
//
//            TexturePage tp = im.__texturePage;
//            u = (im.__tx - this.offsetX) / tp.width;
//            v = (im.__ty - this.offsetY) / tp.height;
//            u1 = u + w / tp.width;
//            v1 = v + h / tp.height;
//        }
//
//        if (im.inverted) {
//            uvBuffer.set(index++, (float) u1);
//            uvBuffer.set(index++, (float) v);
//
//            uvBuffer.set(index++, (float) u1);
//            uvBuffer.set(index++, (float) v1);
//
//            uvBuffer.set(index++, (float) u);
//            uvBuffer.set(index++, (float) v1);
//
//            uvBuffer.set(index++, (float) u);
//            uvBuffer.set(index++, (float) v);
//        } else {
//            uvBuffer.set(index++, (float) u);
//            uvBuffer.set(index++, (float) v);
//
//            uvBuffer.set(index++, (float) u1);
//            uvBuffer.set(index++, (float) v);
//
//            uvBuffer.set(index++, (float) u1);
//            uvBuffer.set(index++, (float) v1);
//
//            uvBuffer.set(index++, (float) u);
//            uvBuffer.set(index++, (float) v1);
//        }
//
//    }

    public boolean glNeedsFlush(Director director) {
        if (this.image.__texturePage != director.currentTexturePage) {
            return true;
        }
        if (this.frameAlpha != director.currentOpacity) {
            return true;
        }
        return false;
    }

    // Add by me
    @Override
    public ImageActor setBounds(double x, double y, double w, double h) {
        return (ImageActor) super.setBounds(x, y, w, h);
    }

    @Override
    public ImageActor setClip(boolean clip, Path path) {
        return (ImageActor) super.setClip(clip, path);
    }

    @Override
    public ImageActor setLocation(double x, double y) {
        return (ImageActor) super.setLocation(x, y);
    }

}
