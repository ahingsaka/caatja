package com.katspow.caatja.modules.colorutil;

/**
 * Helper classes for color manipulation.
 **/
/**
 * Class with color utilities.
 *
 * @constructor
 */
public class Color {
    
    /**
     * HSV to RGB color conversion
     * <p>
     * H runs from 0 to 360 degrees<br>
     * S and V run from 0 to 100
     * <p>
     * Ported from the excellent java algorithm by Eugene Vishnevsky at:
     * http://www.cs.rit.edu/~ncs/color/t_convert.html
     *
     * @static
     */
    public RGB hsvToRgb(int h,int s,int v)
    {
        int r, g, b;
        int i;
        int f, p, q, t;

        // Make sure our arguments stay in-range
        h = Math.max(0, Math.min(360, h));
        s = Math.max(0, Math.min(100, s));
        v = Math.max(0, Math.min(100, v));

        // We accept saturation and value arguments from 0 to 100 because that's
        // how Photoshop represents those values. Internally, however, the
        // saturation and value are calculated from a range of 0 to 1. We make
        // That conversion here.
        s /= 100;
        v /= 100;

        if(s == 0) {
            // Achromatic (grey)
            r = g = b = v;
            return new RGB(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
        }

        h /= 60; // sector 0 to 5
        
        // TODO Check cast
        i = (int) Math.floor(h);
        f = h - i; // factorial part of h
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));

        switch(i) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;

            case 1:
                r = q;
                g = v;
                b = p;
                break;

            case 2:
                r = p;
                g = v;
                b = t;
                break;

            case 3:
                r = p;
                g = q;
                b = v;
                break;

            case 4:
                r = t;
                g = p;
                b = v;
                break;

            default: // case 5:
                r = v;
                g = p;
                b = q;
        }

        return new RGB(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }
    
    public static final int RAMP_RGBA = 0;
    public static final int RAMP_RGB = 1;
    public static final int RAMP_CHANNEL_RGB = 2;
    public static final int RAMP_CHANNEL_RGBA = 3;
    public static final int RAMP_CHANNEL_RGB_ARRAY = 4;
    public static final int RAMP_CHANNEL_RGBA_ARRAY = 5;
    
    /**
     * Interpolate the color between two given colors. The return value will be a calculated color
     * among the two given initial colors which corresponds to the 'step'th color of the 'nsteps'
     * calculated colors.
     * @param r0 {number} initial color red component.
     * @param g0 {number} initial color green component.
     * @param b0 {number} initial color blue component.
     * @param r1 {number} final color red component.
     * @param g1 {number} final color green component.
     * @param b1 {number} final color blue component.
     * @param nsteps {number} number of colors to calculate including the two given colors. If 16 is passed as value,
     * 14 colors plus the two initial ones will be calculated.
     * @param step {number} return this color index of all the calculated colors.
     *
     * TODO Check return ...
     * @return { {r{number}, g{number}, b{number}} } return an object with the new calculated color components.
     * @static
     */
    public static int[] interpolate(int r0, int g0, int b0, int r1, int g1, int b1, int nsteps, int step) {
        if (step <= 0) {
            return new int[] { r0, g0, b0 };

        } else if (step >= nsteps) {
            return new int[] { r1, g1, b1 };
        }

        int r = (r0 + (r1 - r0) / nsteps * step) >> 0;
        int g = (g0 + (g1 - g0) / nsteps * step) >> 0;
        int b = (b0 + (b1 - b0) / nsteps * step) >> 0;

        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }
        if (g > 255) {
            g = 255;
        } else if (g < 0) {
            g = 0;
        }
        if (b > 255) {
            b = 255;
        } else if (b < 0) {
            b = 0;
        }

        return new int[] { r, g, b };

    }
    
    /**
     * Generate a ramp of colors from an array of given colors.
     * @param fromColorsArray {[number]} an array of colors. each color is defined by an integer number from which
     * color components will be extracted. Be aware of the alpha component since it will also be interpolated for
     * new colors.
     * @param rampSize {number} number of colors to produce.
     * @param returnType {CAAT.ColorUtils.RampEnumeration} a value of CAAT.ColorUtils.RampEnumeration enumeration.
     *
     * @return { [{number},{number},{number},{number}] } an array of integers each of which represents a color of
     * the calculated color ramp.
     *
     * @static
     */
    public static int[][] makeRGBColorRamp(int[] fromColorsArray, int rampSize, int returnType ) {

        // TODO Check sizes of ramp
        int nc=     fromColorsArray.length-1;
        int chunk=  rampSize/nc;
        int[][] ramp = new int[nc][chunk];

        for( int i=0; i<nc; i++ ) {
            int c= fromColorsArray[i];
            int a0= (c>>24)&0xff;
            int r0= (c&0xff0000)>>16;
            int g0= (c&0xff00)>>8;
            int b0= c&0xff;

            int c1= fromColorsArray[i+1];
            int a1= (c1>>24)&0xff;
            int r1= (c1&0xff0000)>>16;
            int g1= (c1&0xff00)>>8;
            int b1= c1&0xff;

            double da= (a1-a0)/chunk;
            double dr= (r1-r0)/chunk;
            double dg= (g1-g0)/chunk;
            double db= (b1-b0)/chunk;

            for( int j=0; j<chunk; j++ ) {
                int na= (int) (a0+da*j)>>0;
            int nr= (int) (r0+dr*j)>>0;
            int ng= (int) (g0+dg*j)>>0;
            int nb= (int) (b0+db*j)>>0;
                
                switch( returnType ) {
                    case RAMP_RGBA:
                        // FIXME cannot push this ...
                        //ramp.push( "argb("+na+","+nr+","+ng+","+nb+")" );
                        break;
                    case RAMP_RGB:
                        // FIXME cannot push this ...
                        //ramp.push( "rgb("+nr+","+ng+","+nb+")" );
                        break;
                    case RAMP_CHANNEL_RGB:
                        ramp[i] = new int[] {(0xff000000 | nr<<16 | ng<<8 | nb )};
                        break;
                    case RAMP_CHANNEL_RGBA:
                        ramp[i] = new int[] {( na<<24 | nr<<16 | ng<<8 | nb )};
                        break;
                    case RAMP_CHANNEL_RGBA_ARRAY:
                        ramp[i] = new int[] { nr, ng, nb, na };
                        break;
                    case RAMP_CHANNEL_RGB_ARRAY:
                        ramp[i] = new int[] { nr, ng, nb };
                        break;
                }
            }
        }

        return ramp;

    }

}
