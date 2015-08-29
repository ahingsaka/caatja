package com.katspow.caatja.modules.image.imageprocess;

//import com.google.gwt.canvas.dom.client.CanvasPixelArray;
//import com.google.gwt.canvas.dom.client.ImageData;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.core.canvas.CaatjaPattern;
import com.katspow.caatja.foundation.Director;

/**
 * This file contains some image processing effects. Currently contains the
 * following out-of-the-box effects:
 * 
 * + IMPlasma: creates a plasma texture. The plasma is generated out of a color
 * ramp (see color.js file) + IMBump: creates a realtime bump-mapping from a
 * given image. It supports multiple light sources as well as different light
 * colors. + IMRotoZoom: produces a roto zoom effect out of a given square sized
 * image. Image must be 2^n in size.
 * 
 * This class must be used as fillStyle for an actor or any element that will be
 * painted in a canvas context.
 * 
 */

/**
 * ImageProcessor is a class to encapsulate image processing operations. These image processing
 * manipulates individual image pixels and from an array of pixels builds an image which can
 * be used as a pattern or image.
 * <p>
 * This class pre-creates a canvas of the given dimensions and extracts an imageData object to
 * hold the pixel manipulation.
 *
 * @constructor
 */
public class ImageProcessor {

    CaatjaCanvas canvas = null;
    CaatjaContext2d ctx = null;
    public int width = 0;
    public int height = 0;
    CaatjaImageData imageData = null;
    CaatjaCanvasPixelArray bufferImage = null;

    /**
     * Grabs an image pixels.
     *
     * @param image {HTMLImageElement}
     * @return {ImageData} returns an ImageData object with the image representation or null in
     * case the pixels can not be grabbed.
     *
     * @static
     */
    public CaatjaImageData grabPixels(CaatjaImage image) {

        CaatjaCanvas canvas = Caatja.createCanvas();;

        if (canvas != null) {
            canvas.setCoordinateSpaceWidth(image.getWidth());
            canvas.setCoordinateSpaceHeight(image.getHeight());
            CaatjaContext2d ctx = canvas.getContext2d();
            ctx.drawImage(image, 0, 0);

            CaatjaImageData imageData = null;
            try {
                imageData = ctx.getImageData(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
                return imageData;
            } catch (Exception e) {
                CAAT.log("error pixelgrabbing." + image.getSrc());
                return null;
            }
        }
        return null;
    }

    /**
     * Helper method to create an array.
     *
     * @param size {number} integer number of elements in the array.
     * @param initValue {number} initial array values.
     *
     * @return {[]} an array of 'initialValue' elements.
     *
     * @static
     */
    public static int[] makeArray(int size, int initValue) {
        int[] a = new int[size];

        for (int i = 0; i < size; i++) {
            a[i] = initValue;
        }

        return a;
    }

    /**
     * Helper method to create a bidimensional array.
     *
     * @param size {number} number of array rows.
     * @param size2 {number} number of array columns.
     * @param initvalue array initial values.
     *
     * @return {[]} a bidimensional array of 'initvalue' elements.
     *
     * @static
     *
     */
    public static int[][] makeArray2D(int size, int size2, int initvalue) {
        int[][] a = new int[size][size2];

        for (int i = 0; i < size; i++) {
            a[i] = (makeArray(size2, initvalue));
        }

        return a;
    }

    /**
     * Initializes and creates an offscreen Canvas object. It also creates an ImageData object and
     * initializes the internal bufferImage attribute to imageData's data.
     * @param width {number} canvas width.
     * @param height {number} canvas height.
     * @return this
     */
    public ImageProcessor initialize(int width, int height) {

        this.width = width;
        this.height = height;

        this.canvas = Caatja.createCanvas();

        if (this.canvas != null) {
            this.canvas.setCoordinateSpaceWidth(width);
            this.canvas.setCoordinateSpaceHeight(height);
            this.ctx = this.canvas.getContext2d();
            this.imageData = this.ctx.getImageData(0, 0, width, height);
            this.bufferImage = this.imageData.getData();
        }

        return this;
    }

    /**
     * Clear this ImageData object to the given color components.
     * @param r {number} red color component 0..255.
     * @param g {number} green color component 0..255.
     * @param b {number} blue color component 0..255.
     * @param a {number} alpha color component 0..255.
     * @return this
     */
    public ImageProcessor clear(int r, int g, int b, int a) {
        
        if ( null==this.imageData ) {
            return this;
        }
        
        CaatjaCanvasPixelArray data = this.imageData.getData();
        for (int i = 0; i < this.width * this.height; i++) {
            data.set(i * 4 + 0, r);
            data.set(i * 4 + 1, g);
            data.set(i * 4 + 2, b);
            data.set(i * 4 + 3, a);
        }
        
        // TODO Useless ?
//        this.imageData.data= data;
        
        return this;
    }

    /**
     * Get this ImageData.
     * @return {ImageData}
     */
    public CaatjaImageData getImageData() {
        return this.ctx.getImageData(0, 0, this.width, this.height);
    }

    /**
     * Sets canvas pixels to be the applied effect. After process pixels, this method must be called
     * to show the result of such processing.
     * @param director {CAAT.Director}
     * @param time {number}
     * @return this
     */
    public ImageProcessor apply(Director director, double time) {
        if (null != this.imageData) {
            // TODO Useless ?
//            this.imageData.data= this.bufferImage;
            this.ctx.putImageData(this.imageData, 0, 0);
        }
        return this;
    }

    /**
     * Returns the offscreen canvas.
     * @return {HTMLCanvasElement}
     */
    public CaatjaCanvas getCanvas() {
        return this.canvas;
    }

    /**
     * Creates a pattern that will make this ImageProcessor object suitable as a fillStyle value.
     * This effect can be drawn too as an image by calling: canvas_context.drawImage methods.
     * @param type {string} the pattern type. if no value is supplied 'repeat' will be used.
     * @return CanvasPattern.
     */
    public CaatjaPattern createPattern(String type) {
        return this.ctx.createPattern(this.canvas, type != null ? type : "repeat");
    }
    
    /**
     * Paint this ImageProcessor object result.
     * @param director {CAAT.Director}.
     * @param time {number} scene time.
     */
    public void paint(Director director, double time ) {
        if ( null!=this.canvas ) {
            CaatjaContext2d ctx= director.ctx;
            ctx.drawImage( this.getCanvas(), 0, 0 );
        }
    }

}
