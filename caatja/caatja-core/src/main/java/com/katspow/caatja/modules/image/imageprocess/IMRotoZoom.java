package com.katspow.caatja.modules.image.imageprocess;

//import com.google.gwt.canvas.dom.client.CanvasPixelArray;
//import com.google.gwt.canvas.dom.client.ImageData;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.foundation.Director;

/**
 * This class creates an image processing Rotozoom effect.
 *
 * @constructor
 * @extends CAAT.ImageProcessor
 */
public class IMRotoZoom extends ImageProcessor {

    public IMRotoZoom() {
        super();
    }

    int m_alignv = 1;
    int m_alignh = 1;
    int distortion = 2;
    int mask = 0;
    int shift = 0;
    CaatjaImageData sourceImageData = null; // pattern to fill area with.

    /**
     * Initialize the rotozoom.
     * @param width {number}
     * @param height {number}
     * @param patternImage {HTMLImageElement} image to tile with.
     *
     * @return this
     */
    public IMRotoZoom initialize(int width, int height, CaatjaImage patternImage) {
        super.initialize(width, height);

        this.clear(255, 128, 0, 255);

        this.sourceImageData = this.grabPixels(patternImage);

        if (null != this.sourceImageData) {
            // patternImage must be 2^n sized.
            switch (this.sourceImageData.getWidth()) {
            case 1024:
                this.mask = 1023;
                this.shift = 10;
                break;
            case 512:
                this.mask = 511;
                this.shift = 9;
                break;
            case 256:
                this.mask = 255;
                this.shift = 8;
                break;
            case 128:
                this.mask = 127;
                this.shift = 7;
                break;
            case 64:
                this.mask = 63;
                this.shift = 6;
                break;
            case 32:
                this.mask = 31;
                this.shift = 5;
                break;
            case 16:
                this.mask = 15;
                this.shift = 4;
                break;
            case 8:
                this.mask = 7;
                this.shift = 3;
                break;
            }
        }

        this.setCenter();

        return this;
    }

    /**
     * Performs the process of tiling rotozoom.
     * @param director {CAAT.Director}
     * @param time {number}
     *
     * @private
     */
    public void rotoZoom(Director director, double time) {

        double timer = Caatja.getTime();

        double angle = Math.PI * 2 * Math.cos(timer * 0.0001);
        int distance = (int) (600 + 550 * Math.sin(timer * 0.0002));

        int dist = this.distortion;

        int off = 0;
        double ddx = Math.floor(Math.cos(angle) * distance);
        double ddy = Math.floor(Math.sin(angle) * distance);

        int hh = 0, ww = 0;

        switch (this.m_alignh) {
        case 0:
            hh = 0;
            break;
        case 1:
            hh = (this.height >> 1);
            break;
        case 2:
            hh = this.height - 1;
            break;
        }

        switch (this.m_alignv) {
        case 0:
            ww = 0;
            break;
        case 1:
            ww = (this.width >> 1);
            break;
        case 2:
            ww = this.width - 1;
            break;
        }

        int i = (int)(((this.width >> 1) << 8) - ddx * ww + ddy * hh) & 0xffff;
        int j = (int)(((this.height >> 1) << 8) - ddy * ww - ddx * hh) & 0xffff;

        int srcwidth = this.sourceImageData.getWidth();
        int srcheight = this.sourceImageData.getHeight();
        CaatjaCanvasPixelArray srcdata = this.sourceImageData.getData();
        CaatjaCanvasPixelArray bi = this.bufferImage;
        int dstoff;
        int addx;
        int addy;

        while (off < this.width * this.height * 4) {
            addx = i;
            addy = j;

            for (int m = 0; m < this.width; m++) {
                dstoff = ((addy >> this.shift) & this.mask) * srcwidth + ((addx >> this.shift) & this.mask);
                dstoff <<= 2;

                bi.set(off++, srcdata.get(dstoff++));
                bi.set(off++, srcdata.get(dstoff++));
                bi.set(off++, srcdata.get(dstoff++));
                bi.set(off++, srcdata.get(dstoff++));

                addx += ddx;
                addy += ddy;

            }

            dist += this.distortion;
            i -= ddy;
            j += ddx - dist;
        }
    }

    /**
     * Perform and apply the rotozoom effect.
     * @param director {CAAT.Director}
     * @param time {number}
     * @return this
     */
    public IMRotoZoom apply(Director director, double time) {
        if (null != this.sourceImageData) {
            this.rotoZoom(director, time);
        }
        return (IMRotoZoom) super.apply(director, time);
    }

    /**
     * Change the effect's rotation anchor. Call this method repeatedly to make the effect look
     * different.
     */
    public void setCenter() {
        double d = Math.random();
        if (d < .33) {
            this.m_alignv = 0;
        } else if (d < .66) {
            this.m_alignv = 1;
        } else {
            this.m_alignv = 2;
        }

        d = Math.random();
        if (d < .33) {
            this.m_alignh = 0;
        } else if (d < .66) {
            this.m_alignh = 1;
        } else {
            this.m_alignh = 2;
        }
    }

}
