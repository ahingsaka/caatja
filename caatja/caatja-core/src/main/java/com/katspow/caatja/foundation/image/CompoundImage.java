package com.katspow.caatja.foundation.image;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.modules.texturepacker.TexturePage;

/**
 * @author  Hyperandroid  ||  http://hyperandroid.com/
 *
 * TODO: allow set of margins, spacing, etc. to define subimages. 
 *
 **/

/**
 * This class is exclusively used by SpriteActor. This class is deprecated since the base CAAT.Actor
 * now is able to draw images.
 * 
 * A CompoundImage is an sprite sheet. It encapsulates an Image and treates and
 * references it as a two dimensional array of row by columns sub-images. The
 * access form will be sequential so if defined a CompoundImage of more than one
 * row, the subimages will be referenced by an index ranging from 0 to
 * rows*columns-1. Each sumimage will be of size (image.width/columns) by
 * (image.height/rows).
 * 
 * <p>
 * It is able to draw its sub-images in the following ways:
 * <ul>
 * <li>no transformed (default)
 * <li>flipped horizontally
 * <li>flipped vertically
 * <li>flipped both vertical and horizontally
 * </ul>
 * 
 * <p>
 * It is supposed to be used in conjunction with <code>CAAT.SpriteActor</code>
 * instances.
 * 
 * @constructor
 * 
 */
@Deprecated
public class CompoundImage {

    public static int TR_NONE = 0; // constants used to determine how to draw the sprite image,
    public static int TR_FLIP_HORIZONTAL = 1;
    public static int TR_FLIP_VERTICAL = 2;
    public static int TR_FLIP_ALL = 3;

    public CaatjaImage image = null;
    private int rows = 0;
    private int cols = 0;
    private int width = 0;
    private int height = 0;
    public double singleWidth = 0;
    public double singleHeight = 0;
    
    public double[][] xyCache =                    null;
    
    public CompoundImage() {
    	// Cannot do this in Java
    	// this.paint = this.paintN;
    }

    /**
     * Initialize a grid of subimages out of a given image.
     * 
     * @param image
     *            {HTMLImageElement|Image} an image object.
     * @param rows
     *            {number} number of rows.
     * @param cols
     *            {number} number of columns
     * 
     * @return this
     */
    public CompoundImage initialize(CaatjaImage image, int rows, int cols) {
        this.image = image;
        this.rows = rows;
        this.cols = cols;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.singleWidth = Math.floor((double) this.width / cols);
        this.singleHeight = Math.floor((double) this.height / rows);
        
        this.xyCache= new double[rows*cols][];

        int i,sx0,sy0;
        
        if ( image.__texturePage != null) {
            image.__du= this.singleWidth/image.__texturePage.width;
            image.__dv= this.singleHeight/image.__texturePage.height;


            double w= this.singleWidth;
            double h= this.singleHeight;
            int mod= this.cols;
            if ( image.inverted) {
                double t= w;
                w= h;
                h= t;
                mod= this.rows;
            }

            double xt= this.image.__tx;
            double yt= this.image.__ty;

            TexturePage tp= this.image.__texturePage;

            for( i=0; i<rows*cols; i++ ) {


                int c = ((i%mod)>>0);
                int r= ((i/mod)>>0);

                double u= xt+c*w;  // esquina izq x
                double v= yt+r*h;

                double u1= u+w;
                double v1= v+h;

                /*
                var du= image.__du;
                var dv= image.__dv;
                var mod= this.cols;
                if ( image.inverted) {
                    var t= du;
                    du= dv;
                    dv= t;
                    mod= this.rows;
                }

                sx0= ((i%mod)>>0)*du;
                sy0= ((i/mod)>>0)*dv;

                var u= image.__u+sx0;
                var v= image.__v+sy0;

                var u1= u+du;
                var v1= v+dv;
                */

                this.xyCache[i] = new double[]{u/tp.width,v/tp.height,u1/tp.width,v1/tp.height,u,v,u1,v1};
            }

        } else {
            
            
            for( i=0; i<rows*cols; i++ ) {
                sx0= (int) (((i%this.cols)|0)*this.singleWidth);
                sy0= (int) (((i/this.cols)|0)*this.singleHeight);

                this.xyCache[i] = new double[]{sx0,sy0};
            }
        }
        
        
        return this;
    }

    /**
     * Draws the subimage pointed by imageIndex horizontally inverted.
     * 
     * @param canvas
     *            a canvas context.
     * @param imageIndex
     *            {number} a subimage index.
     * @param x
     *            {number} x position in canvas to draw the image.
     * @param y
     *            {number} y position in canvas to draw the image.
     * @return
     * 
     * @return this
     */
    public CompoundImage paintInvertedH(CaatjaContext2d canvas, int imageIndex, double x, double y) {
        
//        double sx0 = ((imageIndex%this.cols)|0)*this.singleWidth;
//        double sy0 = ((imageIndex/this.cols)|0)*this.singleHeight;

        canvas.save();
        canvas.translate(((int)(.5+x)|0)+this.singleWidth, (int)(.5+y)|0 );
        canvas.scale(-1, 1);

        canvas.drawImage(this.image, this.xyCache[imageIndex][0], this.xyCache[imageIndex][1], this.singleWidth, this.singleHeight, 0, 0, this.singleWidth,
                this.singleHeight);

        canvas.restore();

        return this;
    }

    /**
     * Draws the subimage pointed by imageIndex vertically inverted.
     * 
     * @param canvas
     *            a canvas context.
     * @param imageIndex
     *            {number} a subimage index.
     * @param x
     *            {number} x position in canvas to draw the image.
     * @param y
     *            {number} y position in canvas to draw the image.
     * @return
     * 
     * @return this
     */
    public CompoundImage paintInvertedV(CaatjaContext2d canvas, int imageIndex, double x, double y) {
//        double sx0 = ((imageIndex%this.cols)|0)*this.singleWidth;
//        double sy0 = ((imageIndex/this.cols)|0)*this.singleHeight;

        canvas.save();
        canvas.translate((int)(x+.5)|0, (int)(.5+y+this.singleHeight)|0 );
        canvas.scale(1, -1);

        canvas.drawImage(this.image, this.xyCache[imageIndex][0], this.xyCache[imageIndex][1], this.singleWidth, this.singleHeight, 0, 0, this.singleWidth,
                this.singleHeight);
        canvas.restore();

        return this;
    }

    /**
     * Draws the subimage pointed by imageIndex both horizontal and vertically
     * inverted.
     * 
     * @param canvas
     *            a canvas context.
     * @param imageIndex
     *            {number} a subimage index.
     * @param x
     *            {number} x position in canvas to draw the image.
     * @param y
     *            {number} y position in canvas to draw the image.
     * @return
     * 
     * @return this
     */
    public CompoundImage paintInvertedHV(CaatjaContext2d canvas, int imageIndex, double x, double y) {
//        double sx0 = ((imageIndex%this.cols)|0)*this.singleWidth;
//        double sy0 = ((imageIndex/this.cols)|0)*this.singleHeight;

        canvas.save();
        canvas.translate((int) (x+.5)|0, (int)(.5+y+this.singleHeight)|0 );
        canvas.scale(1, -1);
        canvas.translate(this.singleWidth, 0);
        canvas.scale(-1, 1);

        canvas.drawImage(this.image, this.xyCache[imageIndex][0], this.xyCache[imageIndex][1], this.singleWidth, this.singleHeight, 0, 0, this.singleWidth,
                this.singleHeight);

        canvas.restore();

        return this;
    }

    /**
     * Draws the subimage pointed by imageIndex.
     * 
     * @param canvas
     *            a canvas context.
     * @param imageIndex
     *            {number} a subimage index.
     * @param x
     *            {number} x position in canvas to draw the image.
     * @param y
     *            {number} y position in canvas to draw the image.
     * @return
     * 
     * @return this
     */
    public CompoundImage paintN(CaatjaContext2d canvas, int imageIndex, double x, double y) {

        canvas.drawImage(this.image, (int) this.xyCache[imageIndex][0] >> 0,
                (int) this.xyCache[imageIndex][1] >> 0, this.singleWidth, this.singleHeight, (int) x >> 0,
                (int) y >> 0, this.singleWidth, this.singleHeight);

        return this;
    }
    
    public CompoundImage paint(CaatjaContext2d canvas, int imageIndex, double x, double y) {
    	return this.paintN(canvas,imageIndex,x,y);
    }

    /**
     * Draws the subimage pointed by imageIndex scaled to the size of w and h.
     * @param canvas a canvas context.
     * @param imageIndex {number} a subimage index.
     * @param x {number} x position in canvas to draw the image.
     * @param y {number} y position in canvas to draw the image.
     * @param w {number} new width of the subimage.
     * @param h {number} new height of the subimage.
     * @return 
     *
     * @return this
     */
    public CompoundImage paintScaled(CaatjaContext2d canvas, int imageIndex, double x, double y, double w, double h) {
//        double sx0 = ((imageIndex%this.cols)|0)*this.singleWidth;
//        double sy0 = ((imageIndex/this.cols)|0)*this.singleHeight;
        canvas.drawImage( 
                this.image, 
                this.xyCache[imageIndex][0], this.xyCache[imageIndex][1], this.singleWidth, this.singleHeight,
                (int)(x+.5)|0, (int)(y+.5)|0, w, h );
        
        return this;
    }
    
    /**
     * Get the number of subimages in this compoundImage
     * @return {number}
     */
    public int getNumImages() {
        return this.rows * this.cols;
    }
    
    // FIXME
//    public void setUV(int imageIndex, Float32Array uvBuffer, int uvIndex ) {
//        CaatjaImage im= this.image;
//
//        if ( im.__texturePage == null ) {
//            return;
//        }
//
//        int index= uvIndex;
//
//        if ( im.inverted ) {
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][2]);
//            uvBuffer.set(index++,(float) this.xyCache[imageIndex][1]);
//
//            uvBuffer.set(index++,(float) this.xyCache[imageIndex][2]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][3]);
//
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][0]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][3]);
//
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][0]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][1]);
//        } else {
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][0]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][1]);
//
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][2]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][1]);
//
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][2]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][3]);
//
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][0]);
//            uvBuffer.set(index++, (float) this.xyCache[imageIndex][3]);
//        }
//
//        //director.uvIndex= index;
//    }

}
