package com.katspow.caatja.modules.texturepacker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.image.Image;
import com.katspow.caatja.math.Pt;

/**
 * FIXME 
 *
 */
public class TexturePage {

    public TexturePage() {
        this(1024, 1024);
    }

    public TexturePage(int w, int h) {
        this.width = w;
        this.height = h;
        this.images = new ArrayList<Image>();
    }

    public int width = 1024;
    public int height = 1024;
//    public WebGLRenderingContext gl = null;
//    public WebGLTexture texture = null;
    public boolean allowImagesInvertion = false;
    public int padding = 4;
    public TextureScanMap scan = null;
    public List<Image> images = null;
    public String criteria = "area";

//    public void initialize(WebGLRenderingContext gl) {
//        this.gl = gl;
//        this.texture = gl.createTexture();
//
//        gl.bindTexture(gl.TEXTURE_2D, this.texture);
//        gl.enable(gl.BLEND);
//        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
//
//        Uint8Array uarr = WebGLUtils.createArrayOfUInt8(this.width * this.height * 4);
//        // FIXME TODO
//        // for (int jj = 0; jj < 4*this.width*this.height; ) {
//        // uarr[jj++]=0;
//        // uarr[jj++]=0;
//        // uarr[jj++]=0;
//        // uarr[jj++]=0;
//        // }
//        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, this.width, this.height, 0, gl.RGBA, gl.UNSIGNED_BYTE, uarr);
//
//        gl.enable(gl.BLEND);
//
//        for (int i = 0; i < this.images.size(); i++) {
//
//            Image img = this.images.get(i);
//            if (img.inverted) {
//                // FIXME TODO
//                // img = ImageUtil.rotate(img, -90d);
//            }
//
//            // FIXME TODO
//            // gl.texSubImage2D(gl.TEXTURE_2D, 0, this.images.get(i).__tx,
//            // this.images.get(i).__ty, gl.RGBA,
//            // gl.UNSIGNED_BYTE, img.imageElement);
//        }
//    }

    public void create(Map<String, Image> imagesCache) {

        List<Image> images = new ArrayList<Image>();

        for (Image img : imagesCache.values()) {
            images.add(img);
        }

        this.createFromImages(images);

        // this.gl= gl;
        // this.texture = gl.createTexture();
        //
        // gl.bindTexture(gl.TEXTURE_2D, this.texture);
        // gl.enable( gl.BLEND );
        // gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
        //
        // Uint8Array uarr=
        // WebGLUtils.createArrayOfUInt8(this.width*this.height*4);
        // // FIXME TODO
        // // for (int jj = 0; jj < 4*this.width*this.height; ) {
        // // uarr[jj++]=0;
        // // uarr[jj++]=0;
        // // uarr[jj++]=0;
        // // uarr[jj++]=0;
        // // }
        // gl.texImage2D(
        // gl.TEXTURE_2D,
        // 0,
        // gl.RGBA,
        // this.width,
        // this.height,
        // 0,
        // gl.RGBA,
        // gl.UNSIGNED_BYTE,
        // uarr);
        //
        // int i;
        //
        // // poner las imagenes normalizadas en alto o ancho.
        // // por defecto en alto.
        // // ordenar imagenes: 1� mas altas, y a igual altura, 1� mas anchas.
        //
        // // FIXME TODO
        // // imagesCache.sort( function(a,b) {
        // // var ah= a.image.height;
        // // var bh= b.image.height;
        // // if (!(bh-ah)) {
        // // return b.image.width-a.image.width;
        // // } else {
        // // return bh-ah;
        // // }
        // // });
        //
        //
        // for (Image img : imagesCache.values()) {
        // if ( img.__texturePage == null) {
        // Image cimg = this.normalizeSize(img);
        //
        // int w= cimg.getWidth();
        // int h= cimg.getHeight();
        // int mod;
        //
        // // dejamos un poco de espacio para que las texturas no se pisen.
        // // coordenadas normalizadas 0..1 dan problemas cuando las texturas no
        // est�n
        // // alineadas a posici�n mod 4,8...
        // mod= w%4;
        // // TODO Check
        // if ( mod == 0 ) {mod=4;}
        // if ( w+mod<=this.width ) {
        // w+=mod;
        // }
        // mod= h%4;
        // // TODO Check
        // if ( mod == 0) {mod=4;}
        // if ( h+mod<=this.height ) {
        // h+=mod;
        // }
        //
        // Pt where= this.scan.whereFitsChunk( w, h );
        //
        // if ( null!=where ) {
        // this.images.add( img );
        //
        // // FIXME TODO
        // // gl.texSubImage2D(gl.TEXTURE_2D, 0, where.x, where.y, gl.RGBA,
        // gl.UNSIGNED_BYTE, cimg);
        //
        // img.__tx= where.x;
        // img.__ty= where.y;
        // img.__u= where.x / this.width;
        // img.__v= where.y / this.height;
        // img.__u1= (where.x+cimg.getWidth()) / this.width;
        // img.__v1= (where.y+cimg.getHeight()) / this.height;
        // img.__texturePage= this;
        // img.__w= cimg.getWidth();
        // img.__h= cimg.getHeight();
        // img.inverted= cimg.inverted;
        //
        // //this.scan.substract(where.x,where.y,cimg.width,cimg.height);
        // this.scan.substract((int)where.x,(int)where.y,w,h);
        // } else {
        // CAAT.log("Imagen " + img.getSrc() + " de tama�o " + img.getWidth() +
        // img.getHeight() + " no cabe.");
        // }
        // }
        //
        // }
        //
        // gl.enable( gl.BLEND );
    }

    public void clear() {
        this.createFromImages(new ArrayList<Image>());
    }

    public void update(boolean invert, int padding, int width, int height) {
        this.allowImagesInvertion = invert;
        this.padding = padding;

        if (width < 100) {
            width = 100;
        }
        if (height < 100) {
            height = 100;
        }

        this.width = width;
        this.height = height;

        this.createFromImages(this.images);
    }

    public void createFromImages (List<Image> images ) {

        int i;

        this.scan=   new TextureScanMap( this.width, this.height );
        this.images= new ArrayList<Image>();

        if ( this.allowImagesInvertion ) {
            for( i=0; i<images.size(); i++ ) {
                images.get(i).inverted= this.allowImagesInvertion && images.get(i).getHeight()<images.get(i).getWidth();
            }
        }
        
        Collections.sort(images, new Comparator<Image>() {
            @Override
            public int compare(Image a, Image b) {
                 int aarea = a.getWidth()*a.getHeight();
                 int barea = b.getWidth()*b.getHeight();

                if (criteria.equals("width")) {
                    return a.getWidth()<b.getWidth() ? 1 : a.getWidth()>b.getWidth() ? -1 : 0;
                } else if (criteria.equals("height")) {
                    return a.getHeight()<b.getHeight() ? 1 : a.getHeight()>b.getHeight() ? -1 : 0;
                }
                return aarea<barea ? 1 : aarea>barea ? -1 : 0;
            }
        });

        for( i=0; i<images.size(); i++ ) {
            Image img=  images.get(i);
            this.packImage(img);
        }
    }

    public void addImage(Image image, boolean invert, int padding) {
        this.allowImagesInvertion = invert;
        this.padding = padding;
        this.images.add(image);
        // TODO Check if it works
        // this.createFromImages(Array.prototype.slice.call(this.images));
        this.createFromImages(this.images);
    }

    public void endCreation() {
//        WebGLRenderingContext gl = this.gl;
//
//        // gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST );
//        // gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST );
//        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
//        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);
//        gl.generateMipmap(gl.TEXTURE_2D);
    }

    public void deletePage() {

        for (Image img : images) {
            img.__texturePage = null;
            img.__u = null;
            img.__v = null;
        }

//        this.gl.deleteTexture(this.texture);
    }

    public CaatjaCanvas toCanvas(CaatjaCanvas canvass, boolean outline) {

        if (canvass == null) {
            canvass = Caatja.createCanvas();
        }

        canvass.setCoordinateSpaceWidth(this.width);
        canvass.setCoordinateSpaceHeight(this.height);
        CaatjaContext2d ctxx = canvass.getContext2d();
        ctxx.setFillStyle("rgba(0,0,0,0)");
        ctxx.fillRect(0, 0, this.width, this.height);

        for (int i = 0; i < this.images.size(); i++) {
            // FIXME TODO
            // ctxx.drawImage(
            // !this.images.get(i).inverted ? this.images.get(i) :
            // ImageUtil.rotate(
            // this.images.get(i), 90d),
            // this.images.get(i).__tx, this.images.get(i).__ty);
            if (outline) {
                ctxx.setStrokeStyle("red");
                ctxx.strokeRect(this.images.get(i).__tx, this.images.get(i).__ty, this.images.get(i).__w,
                        this.images.get(i).__h);
            }

            if (this.images.get(i).__gridC != null && this.images.get(i).__gridR != null) {
                for (int t = 0; t < this.images.get(i).__gridR; t++) {
                    for (int u = 0; u < this.images.get(i).__gridC; u++) {
                        ctxx.setStrokeStyle("blue");
                        ctxx.strokeRect(this.images.get(i).__tx + u * this.images.get(i).__w
                                / this.images.get(i).__gridC, this.images.get(i).__ty + t * this.images.get(i).__h
                                / this.images.get(i).__gridR, this.images.get(i).__w / this.images.get(i).__gridC,
                                this.images.get(i).__h / this.images.get(i).__gridR);
                    }
                }
            }
        }

        if (outline) {
            ctxx.setStrokeStyle("red");
            ctxx.strokeRect(0, 0, this.width, this.height);
        }

        return canvass;
    }

    public void packImage(Image img) {
        int newWidth, newHeight;
        if (img.inverted) {
            newWidth = img.getHeight();
            newHeight = img.getWidth();
        } else {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        }

        int w = newWidth;
        int h = newHeight;

        int mod;

        // dejamos un poco de espacio para que las texturas no se pisen.
        // coordenadas normalizadas 0..1 dan problemas cuando las texturas no
        // est‡n
        // alineadas a posici—n mod 4,8...
        if (w != 0 && this.padding != 0) {
            mod = this.padding;
            // mod= (w/this.padding)>>0;
            // if ( !mod ) {mod=this.padding;}
            if (w + mod <= this.width) {
                w += mod;
            }
        }
        if (h != 0 && this.padding != 0) {
            mod = this.padding;
            // mod= (h/this.padding)>>0;
            // if ( !mod ) {mod=this.padding;}
            if (h + mod <= this.height) {
                h += mod;
            }
        }

        Pt where = this.scan.whereFitsChunk(w, h);
        if (null != where) {
            this.images.add(img);

            img.__tx = where.x;
            img.__ty = where.y;
            img.__u = where.x / this.width;
            img.__v = where.y / this.height;
            img.__u1 = (where.x + newWidth) / this.width;
            img.__v1 = (where.y + newHeight) / this.height;
            img.__texturePage = this;
            img.__w = newWidth;
            img.__h = newHeight;

            this.scan.substract((int) where.x, (int) where.y, w, h);
        } else {
            CAAT.log("Imagen " + img.getSrc() + " de tama–o " + img.getWidth() + img.getHeight() + " no cabe.");
        }
    }

    public void changeHeuristic(String criteria) {
        this.criteria = criteria;
    }
}
