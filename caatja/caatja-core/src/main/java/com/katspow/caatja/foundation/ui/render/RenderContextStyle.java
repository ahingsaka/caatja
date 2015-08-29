package com.katspow.caatja.foundation.ui.render;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.ui.TextFont;

public class RenderContextStyle {
    
    // TODO Place here ?
    
    // TODO ?
    //var DEBUG=0;
    
    /**
    *
    * Current applied rendering context information.
    *
    * @constructor
    * @param ctx
    * @return {*}
    */
    public RenderContextStyle(CaatjaContext2d ctx) {
        this.ctx = ctx;
    }

    public CaatjaContext2d ctx;
    
    public int defaultFS;
    public String font        = null;
    public int fontSize ;
    public String fill        = null;
    public String stroke      = null;
    private boolean filled;
    private boolean stroked ;
    public int strokeSize;
    private boolean italic;
    private boolean bold;
    public String alignment;
    public int tabSize;
    public boolean shadow      ;
    public int shadowBlur  ;
    public String shadowColor = null;

    public String sfont       = null;

    public RenderContextStyle chain       = null;

    public RenderContextStyle setDefault (RenderContextStyle... defaultStyles ) {
        this.defaultFS  =   24;
        this.font       =   "Arial";
        this.fontSize   =   this.defaultFS;
        this.fill       =   "#000";
        this.stroke     =   "#f00";
        this.filled     =   true;
        this.stroked    =   false;
        this.strokeSize =   1;
        this.italic     =   false;
        this.bold       =   false;
        this.alignment  =   "left";
        this.tabSize    =   75;
        this.shadow     =   false;
        this.shadowBlur =   0;
        this.shadowColor=   "#000";

        for (RenderContextStyle style : defaultStyles) {
            // FIXME How to do this ?
//            if ( defaultStyles.hasOwnProperty(style) ) {
//                this[style]= style;
//            }
        }

        this.__setFont();

        return this;
    }

    public RenderContextStyle setStyle (RenderContextStyle... styles ) {
        if (styles!= null ) {
            for (RenderContextStyle style : styles) {
             // FIXME How to do this ?
//                this[style]= style;
            }
            
        }
        return this;
    }

    public RenderContextStyle applyStyle () {
        this.__setFont();

        return this;
    }

    public RenderContextStyle clone ( ) {
        RenderContextStyle c= new RenderContextStyle( this.ctx );
        
        // FIXME How ?
//        for( var pr in this ) {
//            if ( this.hasOwnProperty(pr) ) {
//                c[pr]= this[pr];
//            }
//        }
        
        /*
        c.defaultFS  =   this.defaultFS;
        c.font       =   this.font;
        c.fontSize   =   this.fontSize;
        c.fill       =   this.fill;
        c.stroke     =   this.stroke;
        c.filled     =   this.filled;
        c.stroked    =   this.stroked;
        c.strokeSize =   this.strokeSize;
        c.italic     =   this.italic;
        c.bold       =   this.bold;
        c.alignment  =   this.alignment;
        c.tabSize    =   this.tabSize;
        */

        // FIXME How to do this ?
//        while( this.chain ) {
//            me= me.chain;
//            for( var pr in me ) {
//                if ( c[pr]===null  && me.hasOwnProperty(pr) ) {
//                    c[pr]= me[pr];
//                }
//            }
//        }

        c.__setFont();

        return c;
    }

    /**
     * TODO I don't think return type is correct 
     * @param prop
     * @return
     */
    public String __getProperty (String prop ) {
        
        // FIXME How to do this ?
//        var me= this;
//        var res;
//        do {
//            res= me[prop];
//            if ( res!=null ) {
//                return res;
//            }
//            me= me.chain;
//        } while( me );

        return null;
    }
    
    public void image(CaatjaContext2d ctx ) {
        this.__setShadow( ctx );
    }

    public void text (CaatjaContext2d  ctx, String text, double x,double y ) {
        
        this.__setShadow( ctx );
        
        // FIXME
        ctx.setFont(new TextFont(null, this.__getProperty("sfont")));

        if ( this.filled ) {
            this.__fillText( ctx,text,x,y );
        }
        if ( this.stroked ) {
            this.__strokeText( ctx,text,x,y );
        }
    }
    
    public void __setShadow(CaatjaContext2d ctx ) {
        if ( this.__getProperty("shadow" ) != null) {
            ctx.setShadowBlur(Double.parseDouble(this.__getProperty("shadowBlur")));
            ctx.setShadowColor(this.__getProperty("shadowColor"));
        }
    }

    public void __fillText ( CaatjaContext2d ctx, String text, double x, double y ) {
        ctx.setFillStyle(this.__getProperty("fill"));
        ctx.fillText( text, x, y );
    }

    public void __strokeText (CaatjaContext2d  ctx,String text, double x,double y ) {
        ctx.setStrokeStyle(this.__getProperty("stroke"));
        // TODO Check
        ctx.setLineWidth(Double.parseDouble(this.__getProperty("strokeSize")));
        ctx.beginPath();
        ctx.strokeText( text, x, y );
    }

    public void __setFont () {
        String italic= this.__getProperty("italic");
        String bold= this.__getProperty("bold");
        String fontSize= this.__getProperty("fontSize");
        String font= this.__getProperty("font");

        this.sfont= (italic != null ? "italic " : "") +
            (bold != null ? "bold " : "") +
            fontSize + "px " +
            font;

        this.ctx.setFont(new TextFont(null, this.__getProperty("sfont")));
    }

    public void setBold (boolean bool ) {
        if ( bool!=this.bold ) {
            this.bold= bool;
            this.__setFont();
        }
    }

    public void setItalic (boolean bool ) {
        if ( bool!=this.italic ) {
            this.italic= bool;
            this.__setFont();
        }
    }

    public void setStroked (boolean bool ) {
        this.stroked= bool;
    }

    public void setFilled (boolean bool ) {
        this.filled= bool;
    }

    public double getTabPos (double x ) {
        // TODO Check
        double ts= Double.parseDouble(this.__getProperty("tabSize"));
        return (((int)(x/ts)>>0)+1)*ts;
    }

    public void setFillStyle (String style ) {
        this.fill= style;
    }

    public void setStrokeStyle (String style ) {
        this.stroke= style;
    }

    public void setStrokeSize (int size ) {
        this.strokeSize= size;
    }

    public void setAlignment(String alignment ) {
        this.alignment= alignment;
    }

    public void setFontSize (int size ) {
        if ( size!=this.fontSize ) {
            this.fontSize= size;
            this.__setFont();
        }
    }
    
    
}
