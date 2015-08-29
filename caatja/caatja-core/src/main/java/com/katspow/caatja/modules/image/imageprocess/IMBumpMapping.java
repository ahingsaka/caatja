package com.katspow.caatja.modules.image.imageprocess;

import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.math.Pt;

/**
 * This class creates a bumpy effect from a given image. The effect can be applied by different lights
 * each of which can bump the image with a different color. The lights will have an additive color
 * effect on affected pixels.
 *
 * @constructor
 * @extends CAAT.ImageProcessor
 */
public class IMBumpMapping extends ImageProcessor {

    public IMBumpMapping() {
        super();
    }

    // bump
    int[][] m_avgX;
    int[][] m_avgY;
    int[] m_tt;
    int[][] phong;

    int m_radius = 75;

    int[][] m_lightcolor;
    boolean bcolor = false;
    public Pt[] lightPosition;

    /**
     * Initializes internal bump effect data.
     *
     * @param image {HTMLImageElement}
     * @param radius {number} lights radius.
     *
     * @private
     */
    public void prepareBump(CaatjaImage image, Integer radius) {
        int i, j;

        this.m_radius = (radius != null ? radius : 75);

        CaatjaImageData imageData = this.grabPixels(image);

        this.m_tt = makeArray(this.height, 0);
        for (i = 0; i < this.height; i++) {
            this.m_tt[i] = this.width * i;
        }

        this.m_avgX = makeArray2D(this.height, this.width, 0);
        this.m_avgY = makeArray2D(this.height, this.width, 0);

        int[][] bump = makeArray2D(this.height, this.width, 0);

        if (null == imageData) {
            return;
        }

        CaatjaCanvasPixelArray sourceImagePixels = imageData.getData();

        for (i = 0; i < this.height; i++) {
            for (j = 0; j < this.width; j++) {
                int pos = (i * this.width + j) * 4;
                bump[i][j] = sourceImagePixels.get(pos) + sourceImagePixels.get(pos + 1)
                        + sourceImagePixels.get(pos + 2);
            }
        }

        bump = this.soften(bump);

        for (int x = 1; x < this.width - 1; x++) {
            for (int y = 1; y < this.height - 1; y++) {
                this.m_avgX[y][x] = (int) Math.floor(bump[y][x + 1] - bump[y][x - 1]);
                this.m_avgY[y][x] = (int) Math.floor(bump[y + 1][x] - bump[y - 1][x]);
            }
        }

        bump = null;
    }

    /**
     * Soften source images extracted data on prepareBump method.
     * @param bump bidimensional array of black and white source image version.
     * @return bidimensional array with softened version of source image's b&w representation.
     */
    public int[][] soften(int[][] bump) {
        int temp;
        int[][] sbump = makeArray2D(this.height, this.width, 0);

        for (int j = 0; j < this.width; j++) {
            for (int i = 0; i < this.height; i++) {
                temp = (bump[i][j]);
                temp += (bump[(i + 1) % this.height][j]);
                temp += (bump[(i + this.height - 1) % this.height][j]);
                temp += (bump[i][(j + 1) % this.width]);
                temp += (bump[i][(j + this.width - 1) % this.width]);
                temp += (bump[(i + 1) % this.height][(j + 1) % this.width]);
                temp += (bump[(i + this.height - 1) % this.height][(j + this.width - 1) % this.width]);
                temp += (bump[(i + this.height - 1) % this.height][(j + 1) % this.width]);
                temp += (bump[(i + 1) % this.height][(j + this.width - 1) % this.width]);
                temp /= 9;
                sbump[i][j] = temp / 3;
            }
        }

        return sbump;
    }

    /**
     * Create a phong image to apply bump effect.
     * @private
     */
    public void calculatePhong() {
        this.phong = makeArray2D(this.m_radius, this.m_radius, 0);

        int i, j;
        double z;
        for (i = 0; i < this.m_radius; i++) {
            for (j = 0; j < this.m_radius; j++) {
                int x = j / this.m_radius;
                int y = i / this.m_radius;
                z = (1 - Math.sqrt(x * x + y * y)) * .8;
                if (z < 0) {
                    z = 0;
                }
                this.phong[i][j] = (int) Math.floor(z * 255);
            }
        }
    }

    /**
     * Generates a bump image.
     * @param dstPixels {ImageData.data} destinarion pixel array to store the calculated image.
     */
    public void drawColored(CaatjaCanvasPixelArray dstPixels) {
        int i, j, k;
        for (i = 0; i < this.height; i++) {
            for (j = 0; j < this.width; j++) {

                int rrr = 0;
                int ggg = 0;
                int bbb = 0;

                for (k = 0; k < this.m_lightcolor.length; k++) {
                    
                    int lx = (int) this.lightPosition[k].x;
                    int ly = (int) this.lightPosition[k].y;
                    
                    int dx = (int) Math.floor(Math.abs(this.m_avgX[i][j] - j + lx));
                    int dy = (int) Math.floor(Math.abs(this.m_avgY[i][j] - i + ly));

                    if (dx >= this.m_radius) {
                        dx = this.m_radius - 1;
                    }
                    if (dy >= this.m_radius) {
                        dy = this.m_radius - 1;
                    }

                    int c = this.phong[dx][dy];
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if (c >= 0) {// oscurecer
                        r = (this.m_lightcolor[k][0] * c / 128);
                        g = (this.m_lightcolor[k][1] * c / 128);
                        b = (this.m_lightcolor[k][2] * c / 128);
                    } else { // blanquear.
                        c = 128 + c;
                        int rr = (this.m_lightcolor[k][0]);
                        int gg = (this.m_lightcolor[k][1]);
                        int bb = (this.m_lightcolor[k][2]);

                        r = (int) Math.floor(rr + (255 - rr) * c / 128);
                        g = (int) Math.floor(gg + (255 - gg) * c / 128);
                        b = (int) Math.floor(bb + (255 - bb) * c / 128);
                    }

                    rrr += r;
                    ggg += g;
                    bbb += b;
                }

                if (rrr > 255) {
                    rrr = 255;
                }
                if (ggg > 255) {
                    ggg = 255;
                }
                if (bbb > 255) {
                    bbb = 255;
                }

                int pos = (j + this.m_tt[i]) * 4;
                dstPixels.set(pos, rrr);
                dstPixels.set(pos + 1, ggg);
                dstPixels.set(pos + 2, bbb);
                dstPixels.set(pos + 3, 255);
            }
        }
    }

    /**
     * Sets lights color.
     * 
     * @param colors_rgb_array
     *            an array of arrays. Each internal array has three integers
     *            defining an RGB color. ie: [ [ 255,0,0 ], [ 0,255,0 ] ]
     * @return this
     */
    public IMBumpMapping setLightColors(int[][] colors_rgb_array) {
        this.m_lightcolor = colors_rgb_array;
        this.lightPosition = new Pt[m_lightcolor.length];
        for (int i = 0; i < this.m_lightcolor.length; i++) {
            double x= this.width*Math.random();
            double y= this.height*Math.random();
            this.lightPosition[i] = new Pt(x, y);
        }
        return this;
    }

    /**
     * Initialize the bump image processor.
     * @param image {HTMLImageElement} source image to bump.
     * @param radius {number} light radius.
     */
    public IMBumpMapping initialize(CaatjaImage image, int radius) {
        super.initialize(image.getWidth(), image.getHeight());

        this.setLightColors(new int[][] { { 255, 128, 0 }, { 0, 0, 255 } });

        this.prepareBump(image, radius);
        this.calculatePhong();

        return this;
    }

    /**
     * Set a light position.
     * @param lightIndex {number} light index to position.
     * @param x {number} light x coordinate.
     * @param y {number} light y coordinate.
     * @return this
     */
    public IMBumpMapping setLightPosition(int lightIndex, int x, int y) {
        this.lightPosition[lightIndex].set(x, y);
        return this;
    }

    /**
     * Applies the bump effect and makes it visible on the canvas surface.
     * @param director {CAAT.Director}
     * @param time {number}
     */
    public IMBumpMapping apply(Director director, double time) {
        this.drawColored(this.bufferImage);
        return (IMBumpMapping) super.apply(director, time);
    }

}
