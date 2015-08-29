package com.katspow.caatja.modules.image.util;

//import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaCanvasPixelArray;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.math.matrix.Matrix;
//import com.google.gwt.canvas.dom.client.ImageData;

public class ImageUtil {

    public static CaatjaCanvas createAlphaSpriteSheet(double maxAlpha, double minAlpha, int sheetSize, CaatjaImage image, String bg_fill_style) {

        if (maxAlpha < minAlpha) {
            double t = maxAlpha;
            maxAlpha = minAlpha;
            minAlpha = t;
        }

        CaatjaCanvas canvas = Caatja.createCanvas();;
        canvas.setCoordinateSpaceWidth(image.getWidth());
        canvas.setCoordinateSpaceHeight(image.getHeight() * sheetSize);
        CaatjaContext2d ctx = canvas.getContext2d();
        ctx.setFillStyle("rgba(255,255,255,0)");
        ctx.clearRect(0, 0, image.getWidth(), image.getHeight() * sheetSize);
        
        ctx.setFillStyle(bg_fill_style != null ? bg_fill_style : "rgba(255,255,255,0)");
        ctx.fillRect(0,0,image.getWidth(),image.getHeight()*sheetSize);

        int i;
        for (i = 0; i < sheetSize; i++) {
            ctx.setGlobalAlpha( 1-(maxAlpha-minAlpha)/sheetSize*(i+1));
            ctx.drawImage(image, 0, i * image.getHeight());
        }

        return canvas;
    }
    
    /**
     * Creates a rotated canvas image element.
     * @param img
     */
    public static CaatjaCanvas rotate(CaatjaImage image, Double angle) {

        if (angle == null) {
            // TODO Change return type
            // return image;
            return null;
        }

        CaatjaCanvas canvas = Caatja.createCanvas();;
        canvas.setCoordinateSpaceWidth(image.getWidth());
        canvas.setCoordinateSpaceHeight(image.getHeight());
        CaatjaContext2d ctx= canvas.getContext2d();
        ctx.setGlobalAlpha(1);
        ctx.setFillStyle("rgba(0,0,0,0)");
        ctx.clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());

        Matrix m = new Matrix();
        m.multiply(new Matrix().setTranslate(canvas.getCoordinateSpaceWidth() / (double) 2,
                canvas.getCoordinateSpaceWidth() / (double) 2));
        m.multiply( new Matrix().setRotation( angle*Math.PI/180 ) );
        m.multiply(new Matrix().setTranslate(-canvas.getCoordinateSpaceWidth() / (double) 2,
                -canvas.getCoordinateSpaceWidth() / 2));
        m.transformRenderingContext(ctx);
        ctx.drawImage(image, 0, 0);

        return canvas;
    }
    /**
     * Remove an image"s padding transparent border.
     * Transparent means that every scan pixel is alpha=0.
     * @param image
     * @param threshold {integer} any value below or equal to this will be optimized.
     * @param !areas { object{ top<boolean>, bottom<boolean>, left<boolean, right<boolean> }}
     */
    public CaatjaCanvas optimize (CaatjaImage image, int threshold, Area areas) {
        
        threshold>>=0;
        
        boolean atop=       true;
        boolean abottom=    true;
        boolean aleft=      true;
        boolean aright=     true;
        
        if (areas != null) {
            if (areas.top != null) {
                atop = areas.top;
            }
            if (areas.bottom != null) {
                abottom = areas.bottom;
            }
            if (areas.left != null) {
                aleft = areas.left;
            }
            if (areas.right != null) {
                aright = areas.right;
            }
        }
        
        CaatjaCanvas canvas= Caatja.createCanvas();
        canvas.setCoordinateSpaceWidth(image.getWidth());
        canvas.setCoordinateSpaceHeight(image.getHeight());
        CaatjaContext2d ctx= canvas.getContext2d();

        ctx.setFillStyle("rgba(0,0,0,0)");
        ctx.fillRect(0,0,image.getWidth(),image.getHeight());
        ctx.drawImage( image, 0, 0 );

        CaatjaImageData imageData= ctx.getImageData(0,0,image.getWidth(),image.getHeight());
        CaatjaCanvasPixelArray data= imageData.getData();

        int i,j;
        int miny= 0, maxy=canvas.getCoordinateSpaceHeight() - 1;
        int minx= 0, maxx=canvas.getCoordinateSpaceWidth() - 1;

        boolean alpha= false;
        
        if (atop) {
            for( i=0; i<canvas.getCoordinateSpaceHeight(); i++ ) {
                for( j=0; j<canvas.getCoordinateSpaceWidth(); j++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
    
                if ( alpha ) {
                    break;
                }
            }
            // i contiene el indice del ultimo scan que no es transparente total.
            miny= i;
        }

        if (abottom) {
            alpha= false;
            for( i=canvas.getCoordinateSpaceHeight()-1; i>=miny; i-- ) {
                for( j=0; j<canvas.getCoordinateSpaceWidth(); j++) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
    
                if ( alpha ) {
                    break;
                }
            }
            maxy= i;
        }

        if (aleft) {
            alpha= false;
            for( j=0; j<canvas.getCoordinateSpaceWidth(); j++ ) {
                for( i=miny; i<maxy; i++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
                if ( alpha ) {
                    break;
                }
            }
            minx= j;
        }

        if (aright) {
            alpha= false;
            for( j=canvas.getCoordinateSpaceWidth()-1; j>=minx; j-- ) {
                for( i=miny; i<maxy; i++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
                if ( alpha ) {
                    break;
                }
            }
            maxx= j;
        }

        if (0 == minx && 0 == miny && canvas.getCoordinateSpaceWidth() - 1 == maxx
                && canvas.getCoordinateSpaceHeight() - 1 == maxy) {
            return canvas;
        }

        int width = maxx - minx + 1;
        int height = maxy - miny + 1;
        CaatjaImageData id2 = ctx.getImageData(minx, miny, width, height);

        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        ctx = canvas.getContext2d();
        ctx.putImageData( id2, 0, 0 );

        return canvas;
    }
    
    // Add by me, find way to merge image and canvas
    public static CaatjaCanvas optimize (CaatjaCanvas image, int threshold, Area areas) {
        
        threshold>>=0;
        
        boolean atop=       true;
        boolean abottom=    true;
        boolean aleft=      true;
        boolean aright=     true;
        
        if (areas != null) {
            if (areas.top != null) {
                atop = areas.top;
            }
            if (areas.bottom != null) {
                abottom = areas.bottom;
            }
            if (areas.left != null) {
                aleft = areas.left;
            }
            if (areas.right != null) {
                aright = areas.right;
            }
        }
        
        CaatjaCanvas canvas= Caatja.createCanvas();
        canvas.setCoordinateSpaceWidth(image.getCoordinateSpaceWidth());
        canvas.setCoordinateSpaceHeight(image.getCoordinateSpaceHeight());
        CaatjaContext2d ctx= canvas.getContext2d();

        ctx.setFillStyle("rgba(0,0,0,0)");
        ctx.fillRect(0,0,image.getCoordinateSpaceWidth(),image.getCoordinateSpaceHeight());
        ctx.drawImage( image, 0, 0 );

        CaatjaImageData imageData= ctx.getImageData(0,0,image.getCoordinateSpaceWidth(),image.getCoordinateSpaceHeight());
        CaatjaCanvasPixelArray data= imageData.getData();

        int i,j;
        int miny= 0, maxy=canvas.getCoordinateSpaceHeight() - 1;
        int minx= 0, maxx=canvas.getCoordinateSpaceWidth() - 1;

        boolean alpha= false;
        
        if (atop) {
            for( i=0; i<canvas.getCoordinateSpaceHeight(); i++ ) {
                for( j=0; j<canvas.getCoordinateSpaceWidth(); j++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
    
                if ( alpha ) {
                    break;
                }
            }
            // i contiene el indice del ultimo scan que no es transparente total.
            miny= i;
        }

        if (abottom) {
            alpha= false;
            for( i=canvas.getCoordinateSpaceHeight()-1; i>=miny; i-- ) {
                for( j=0; j<canvas.getCoordinateSpaceWidth(); j++) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
    
                if ( alpha ) {
                    break;
                }
            }
            maxy= i;
        }

        if (aleft) {
            alpha= false;
            for( j=0; j<canvas.getCoordinateSpaceWidth(); j++ ) {
                for( i=miny; i<maxy; i++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
                if ( alpha ) {
                    break;
                }
            }
            minx= j;
        }

        if (aright) {
            alpha= false;
            for( j=canvas.getCoordinateSpaceWidth()-1; j>=minx; j-- ) {
                for( i=miny; i<maxy; i++ ) {
                    if (data.get(i * canvas.getCoordinateSpaceWidth() * 4 + 3 + j * 4) > threshold) {
                        alpha= true;
                        break;
                    }
                }
                if ( alpha ) {
                    break;
                }
            }
            maxx= j;
        }

        if (0 == minx && 0 == miny && canvas.getCoordinateSpaceWidth() - 1 == maxx
                && canvas.getCoordinateSpaceHeight() - 1 == maxy) {
            return canvas;
        }

        int width = maxx - minx + 1;
        int height = maxy - miny + 1;
        CaatjaImageData id2 = ctx.getImageData(minx, miny, width, height);

        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        ctx = canvas.getContext2d();
        ctx.putImageData( id2, 0, 0 );

        return canvas;
        
    }

    public static CaatjaCanvas createThumb(CaatjaImage image, Integer w, Integer h, boolean best_fit) {

        if (w == null) {
            w = 24;
        }

        if (h == null) {
            h = 24;
        }

        CaatjaCanvas canvas = Caatja.createCanvas();;
        canvas.setCoordinateSpaceWidth(w);
        canvas.setCoordinateSpaceHeight(h);
        CaatjaContext2d ctx = canvas.getContext2d();

        if (best_fit) {
            int max= Math.max( image.getWidth(), image.getHeight() );
            double ww = image.getWidth() / (double) max * w;
            double hh = image.getHeight() / (double) max * h;
            ctx.drawImage(image, (w - ww) / 2, (h - hh) / 2, ww, hh);
        } else {
            ctx.drawImage( image, 0, 0, w, h );
        }
        
        return canvas;
    }

}
