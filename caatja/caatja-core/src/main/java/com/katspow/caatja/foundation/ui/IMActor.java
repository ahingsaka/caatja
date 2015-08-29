package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.modules.image.imageprocess.ImageProcessor;

/**
 * An actor suitable to draw an ImageProcessor instance.
 */
/**
 * @name IMActor
 * @memberOf CAAT.Foundation.UI
 * @extends CAAT.Foundation.Actor
 * @constructor
 */
public class IMActor extends ActorContainer {

    /**
     * This Actor will show the result of an image processing operation.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public IMActor() {
        super();
    }

    /**
     * Image processing interface.
     * @type { }
     */
    public ImageProcessor imageProcessor = null;
    
    /**
     * Calculate another image processing frame every this milliseconds.
     */
    public double changeTime = 100;
    
    /**
     * Last scene time this actor calculated a frame.
     */
    public double lastApplicationTime = -1;

    /**
     * Set the image processor.
     *
     * @param im {CAAT.ImageProcessor} a CAAT.ImageProcessor instance.
     */
    public IMActor setImageProcessor(ImageProcessor im) {
        this.imageProcessor = im;
        return this;
    }

    /**
     * Call image processor to update image every time milliseconds.
     * 
     * @param time
     *            an integer indicating milliseconds to elapse before updating
     *            the frame.
     */
    public IMActor setImageProcessingTime(double time) {
        this.changeTime = time;
        return this;
    }

    public void paint(Director director, double time) {
        if (time - this.lastApplicationTime > this.changeTime) {
            this.imageProcessor.apply(director, time);
            this.lastApplicationTime = time;
        }

        CaatjaContext2d ctx = director.ctx;
        this.imageProcessor.paint( director, time );
    }

    @Override
    public IMActor setBounds(double x, double y, double w, double h) {
        return (IMActor) super.setBounds(x, y, w, h);
    }
    

}
