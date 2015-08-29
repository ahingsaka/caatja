package com.katspow.caatja.foundation.ui.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.foundation.ui.document.DocumentElementImage;
import com.katspow.caatja.foundation.ui.document.DocumentElementText;
import com.katspow.caatja.foundation.ui.document.DocumentLine;
import com.katspow.caatja.modules.font.Font;

public class RenderContext {
    
    /**
     * This class keeps track of styles, images, and the current applied style.
     * @constructor
     * @return {*}
     */
    public RenderContext() {
        this.text = "";
    }
    
    public double x           =   0;
    public double y           =   0;
    public Double width       =   0d;
    public String text        =   null;

    public RenderContextStyle crcs        =   null;   // current rendering context style
    public Stack<RenderContextStyle> rcs         =   null;   // rendering content styles stack

    public Map<String, RenderContextStyle> styles      =   null;
    public Map<String, SpriteImage> images      =   null;

    public List<DocumentLine> lines       =   null;

    public int documentHeight  = 0;
    
    // TODO Change type
    public Stack<Object> anchorStack     = null;
    
    // Add by me
    public DocumentLine currentLine;
    public CaatjaContext2d ctx;

    public void __nextLine () {
        this.x= 0;
        // FIXME
        //this.currentLine= new DocumentLine(Font.getFontMetrics(this.crcs.sfont));
        this.lines.add( this.currentLine );
    }

    /**
     *
     * @param image {CAAT.SpriteImage}
     * @param r {number=}
     * @param c {number=}
     * @private
     */
    public void __image (SpriteImage image,int r, int c ) {


        int image_width;

        // TODO Check cond
        if ( r != 0 && c != 0) {
            image_width= (int) image.getWidth();
        } else {
            image_width= image.getWrappedImageWidth();
        }

        // la imagen cabe en este sitio.
        if ( this.width != null) {
            if ( image_width + this.x > this.width && this.x>0 ) {
                this.__nextLine();
            }
        }

        this.currentLine.addElementImage( new DocumentElementImage( 
                this.x, 
                image, 
                r, 
                c,
                this.crcs.clone(),
                this.__getCurrentAnchor()) );

        this.x+= image_width;
    }

    public void __text () {

        if ( this.text.length()==0 ) {
            return;
        }

        double text_width= this.ctx.measureTextWidth(this.text);

        // la palabra cabe en este sitio.
        if ( this.width != null) {
            if ( text_width + this.x > this.width && this.x>0 ) {
                this.__nextLine();
            }
        }

        //this.crcs.text( this.text, this.x, this.y );
        this.currentLine.addElement( new DocumentElementText(
            this.text,
            this.x,
            text_width,
            0, // Double.parseDouble(this.crcs.__getProperty("fontSize")), calculated later
            this.crcs.clone(),
            this.__getCurrentAnchor()) ) ;

        this.x+= text_width;

        this.text="";
    }

    public void fchar(String _char ) {

        if ( _char.equals(' ') ) {

            this.__text();

            this.x+= this.ctx.measureTextWidth(_char);
            if ( this.width != null) {
                if ( this.x > this.width ) {
                    this.__nextLine();
                }
            }
        } else {
            this.text+= _char;
        }
    }

    public void end () {
        if ( this.text.length()>0 ) {
            this.__text();
        }

        int y=0;
        int lastLineEstimatedDescent= 0;
        for( int i=0; i<this.lines.size(); i++ ) {
            double inc= this.lines.get(i).getHeight();

            if ( inc==0 ) {
                // lineas vacias al menos tienen tama�o del estilo por defecto
                inc= this.styles.get("default").fontSize;
            }
            y+= inc;

            /**
             * add the estimated descent of the last text line to document height's.
             * the descent is estimated to be a 20% of font's height.
             */
            if ( i==this.lines.size()-1 ) {
                lastLineEstimatedDescent= (int)(inc*.25)>>0;
            }

            this.lines.get(i).setY(y);
        }

        this.documentHeight= y + lastLineEstimatedDescent;
    }

    public double getDocumentHeight () {
        return this.documentHeight;
    }
    
    public Object __getCurrentAnchor() {
        if ( this.anchorStack.size() > 0) {
            return this.anchorStack.get( this.anchorStack.size()-1 );
        }

        return null;
    }

    public void __resetAppliedStyles () {
        this.rcs= new Stack<RenderContextStyle>();
        this.__pushDefaultStyles();
    }

    public void __pushDefaultStyles () {
        this.crcs= new RenderContextStyle(this.ctx).setDefault( this.styles.get("default") );
        this.rcs.push( this.crcs );
    }

    public void __pushStyle (RenderContextStyle style ) {
        RenderContextStyle pcrcs= this.crcs;
        this.crcs= new RenderContextStyle(this.ctx);
        this.crcs.chain= pcrcs;
        this.crcs.setStyle( style );
        this.crcs.applyStyle( );

        this.rcs.push( this.crcs );
    }

    public void __popStyle () {
        // make sure you don't remove default style.
        if ( this.rcs.size()>1 ) {
            this.rcs.pop();
            this.crcs= this.rcs.get( this.rcs.size()-1 );
            this.crcs.applyStyle();
        }
    }
    
    public void __popAnchor() {
        if ( this.anchorStack.size()> 0 ) {
            this.anchorStack.pop();
        }
    }

    public void __pushAnchor(Object anchor ) {
        this.anchorStack.push( anchor );
    }

    public void start (CaatjaContext2d ctx, Map styles, Map<String, SpriteImage> images, Double width ) {
        this.x=0;
        this.y=0;
        this.width= width!= null ? width : 0;
        this.ctx= ctx;
        this.lines= new ArrayList<DocumentLine>();
        this.styles= styles;
        this.images= images;
        this.anchorStack = new Stack<Object>();

        this.__resetAppliedStyles();
        this.__nextLine();

    }

    public void setTag  (String tag ) {

        String[] pairs;
        RenderContextStyle style;

        this.__text();

        tag= tag.toLowerCase();
        if ( tag.equals("b" )) {
            this.crcs.setBold( true );
        } else if ( tag.equals("/b" )) {
            this.crcs.setBold( false );
        } else if ( tag.equals("i" )) {
            this.crcs.setItalic( true );
        } else if ( tag.equals("/i" )) {
            this.crcs.setItalic( false );
        } else if ( tag.equals("stroked" ) ){
            this.crcs.setStroked( true );
        } else if ( tag.equals("/stroked" )) {
            this.crcs.setStroked( false );
        } else if ( tag.equals("filled" ) ){
            this.crcs.setFilled( true );
        } else if ( tag.equals("/filled" )) {
            this.crcs.setFilled( false );
        } else if ( tag.equals("tab" )) {
            this.x= this.crcs.getTabPos( this.x );
        } else if ( tag.equals("br" )) {
            this.__nextLine(); 
        } else if ( tag.equals("/a")) {
            this.__popAnchor();
        } else if ( tag.equals("/style" )) {
            if ( this.rcs.size()>1 ) {
                this.__popStyle();
            } else {
                /**
                 * underflow pop de estilos. eres un cachondo.
                 */
            }
        } else {
            if ( tag.indexOf("fillcolor")==0 ) {
                pairs= tag.split("=");
                this.crcs.setFillStyle( pairs[1] );
            } else if ( tag.indexOf("strokecolor")==0 ) {
                pairs= tag.split("=");
                this.crcs.setStrokeStyle( pairs[1] );
            } else if ( tag.indexOf("strokesize")==0 ) {
                pairs= tag.split("=");
                this.crcs.setStrokeSize( Integer.parseInt(pairs[1])|0 );
            } else if ( tag.indexOf("fontsize")==0 ) {
                pairs= tag.split("=");
                this.crcs.setFontSize( Integer.parseInt(pairs[1])|0 );
            } else if ( tag.indexOf("style")==0 ) {
                pairs= tag.split("=");
                style= this.styles.get( pairs[1] );
                if ( style != null) {
                    this.__pushStyle( style );
                }
            } else if ( tag.indexOf("image")==0) {
                pairs= tag.split("=")[1].split(",");
                String image= pairs[0];
                if ( this.images.get(image) != null) {
                    int r= 0, c=0;
                    if ( pairs.length>=3 ) {
                        r= Integer.parseInt(pairs[1])|0;
                        c= Integer.parseInt(pairs[2])|0;
                    }
                    // FIXME
//                    this.__image( this.images[image], r, c );
                }
            } else if ( tag.indexOf("a=") == 0 ) {
                pairs= tag.split("=");
                this.__pushAnchor( pairs[1] );
            }
        }
    }

}
