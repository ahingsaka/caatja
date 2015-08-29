package com.katspow.caatja.modules.image.imageprocess;

//import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.modules.colorutil.Color;

// TODO Fix exception
/**
 * Creates an additive plasma wave image.
 *
 * @constructor
 * @extends CAAT.ImageProcessor
 *
 */
public class IMPlasma extends ImageProcessor {
    
    public IMPlasma() {
        super();
    }
    
    int[] wavetable;
    int[][] m_colorMap;
    double spd1= 1;
    double spd2= 2;
    double spd3= 3;
    double spd4= 4;
    int pos1= 0;
    int pos2= 0;
    int pos3= 0;
    int pos4= 0;
    int tpos1= 0;
    int tpos2= 0;
    int tpos3= 0;
    int tpos4= 0;
    int m_colorMapSize= 256;
    int i1= 0;
    int i2= 0;
    int i3= 0;
    int i4= 0;
    boolean b1= false;
    boolean b2= false;
    boolean b3= false;
    boolean b4= false;

    int[] color = new int[] {0xffffffff, 0xffff00ff, 0xffffff00, 0xff00ff00, 0xffff0000, 0xff0000ff, 0xff000000};

    /**
     * Initialize the plasma image processor.
     * <p>
     * This image processor creates a color ramp of 256 elements from the colors of the parameter 'colors'.
     * Be aware of color definition since the alpha values count to create the ramp.
     *
     * @param width {number}
     * @param height {number}
     * @param colors {Array.<number>} an array of color values.
     *
     * @return this
     */
    public IMPlasma initialize (int width, int height, int[] colors) {
        super.initialize(width,height);

        this.wavetable= new int[256];
        for (int x=0; x<256; x++)   {
            this.wavetable[x] = (int)( Math.floor(32 * (1 + Math.cos(x*2 * Math.PI / 256))) );
        }

        this.pos1=(int) Math.floor(255*Math.random());
        this.pos2=(int) Math.floor(255*Math.random());
        this.pos3=(int) Math.floor(255*Math.random());
        this.pos4=(int) Math.floor(255*Math.random());

        this.m_colorMap= Color.makeRGBColorRamp(
                colors!=null ? colors : this.color,
                256,
                Color.RAMP_CHANNEL_RGBA_ARRAY );

        this.setB();

        return this;
    }
    
    /**
     * Initialize internal plasma structures. Calling repeatedly this method will make the plasma
     * look different.
     */
    public void setB () {

        this.b1= Math.random()>.5;
        this.b2= Math.random()>.5;
        this.b3= Math.random()>.5;
        this.b4= Math.random()>.5;

        this.spd1= Math.floor((Math.random()*3+1)*(Math.random()<.5?1:-1));
        this.spd2= Math.floor((Math.random()*3+1)*(Math.random()<.5?1:-1));
        this.spd3= Math.floor((Math.random()*3+1)*(Math.random()<.5?1:-1));
        this.spd4= Math.floor((Math.random()*3+1)*(Math.random()<.5?1:-1));

        this.i1= (int) Math.floor((Math.random()*2.4+1)*(Math.random()<.5?1:-1));
        this.i2= (int) Math.floor((Math.random()*2.4+1)*(Math.random()<.5?1:-1));
        this.i3= (int) Math.floor((Math.random()*2.4+1)*(Math.random()<.5?1:-1));
        this.i4= (int) Math.floor((Math.random()*2.4+1)*(Math.random()<.5?1:-1));
    }
    
    /**
     * Apply image processing to create the plasma and call superclass's apply to make the result
     * visible.
     * @param director {CAAT.Director}
     * @param time {number}
     *
     * @return this
     */
    public IMPlasma apply (Director director, double time) {

        int v = 0;
        this.tpos1 = this.pos1;
        this.tpos2 = this.pos2;
        
        CaatjaCanvasPixelArray bi= this.bufferImage;
        int[][] cm = this.m_colorMap;
        int[] wt = this.wavetable;
        int z;
        int[] cmz;

        for (int x=0; x<this.height; x++) {
            this.tpos3 = this.pos3;
            this.tpos4 = this.pos4;

            for(int y=0; y<this.width; y++) {
                // mix at will, or at your own risk.
                int o1= this.tpos1+this.tpos2+this.tpos3;
                int o2= this.tpos2+this.tpos3-this.tpos1;
                int o3= this.tpos3+this.tpos4-this.tpos2;
                int o4= this.tpos4+this.tpos1-this.tpos2;

                // set different directions. again, change at will.
                if ( this.b1 ) o1= -o1;
                if ( this.b2 ) o2= -o2;
                if ( this.b3 ) o3= -o3;
                if ( this.b4 ) o4= -o4;

                z = (int) Math.floor(wt[o1 & 255] + wt[o2 & 255] + wt[o3 & 255] + wt[o4 & 255]);
                cmz= cm[z];
                
                try {
                    bi.set(v++, cmz[0]);
                    bi.set(v++, cmz[1]);
                    bi.set(v++, cmz[2]);
                    bi.set(v++, cmz[3]);
                } catch (Exception e) {
                    CAAT.log("error");
                }
                
                this.tpos3 += this.i1;
                this.tpos3&=255;
                this.tpos4 += this.i2;
                this.tpos4&=255;
            }

            this.tpos1 += this.i3;
            this.tpos1&=255;
            this.tpos2 += this.i4;
            this.tpos2&=255;
        }

        this.pos1 += this.spd1;
        this.pos2 -= this.spd2;
        this.pos3 += this.spd3;
        this.pos4 -= this.spd4;
        this.pos1&=255;
        this.pos3&=255;
        this.pos2&=255;
        this.pos4&=255;

        return (IMPlasma) super.apply(director,time);
    }

}
