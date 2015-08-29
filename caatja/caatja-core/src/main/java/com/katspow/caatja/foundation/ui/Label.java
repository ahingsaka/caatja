package com.katspow.caatja.foundation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.foundation.ui.UI.ALIGNMENT;
import com.katspow.caatja.foundation.ui.document.DocumentElement;
import com.katspow.caatja.foundation.ui.document.DocumentLine;
import com.katspow.caatja.foundation.ui.render.RenderContext;

/**
 * @name Label
 * @memberOf CAAT.Foundation.UI
 * @extends CAAT.Foundation.Actor
 * @constructor
 */
public class Label extends Actor {
    
    /**
     * This object represents a label object.
     * A label is a complex presentation object which is able to:
     * <li>define comples styles
     * <li>contains multiline text
     * <li>keep track of per-line baseline regardless of fonts used.
     * <li>Mix images and text.
     * <li>Layout text and images in a fixed width or by parsing a free-flowing document
     * <li>Add anchoring capabilities.
     *
     * @return {*}
     * @constructor
     */
    public Label() {
        this.rc = new RenderContext();
        this.lines = new ArrayList<DocumentLine>();
        this.styles = new HashMap<String, Object>();
        this.images = new HashMap<String, SpriteImage>();
        
        // Add by me
        setMouseClickListener(new MouseListener() {
            public void call(CAATMouseEvent e) {
                if (clickCallback != null) {
                    DocumentElement elem = __getDocumentElementAt(e.x, e.y);
                    if (elem.getLink() != null) {
                        clickCallback.call(elem.getLink());
                    }
                }
            }
        });
        
        setMouseExitListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                Caatja.setCursor("default");
            }
        });
        
        setMouseMoveListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                DocumentElement elem= __getDocumentElementAt(e.x, e.y);
                if ( elem != null && elem.getLink() != null) {
                    Caatja.setCursor( "pointer");
                } else {
                    Caatja.setCursor( "default");
                }
            }
        });
    }
    
    /**
     * This Label document�s horizontal alignment.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager}
     * @private
     */
    public ALIGNMENT halignment  =   UI.ALIGNMENT.LEFT;
    
    /**
     * This Label document�s vertical alignment.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager}
     * @private
     */
    public ALIGNMENT valignment  =   UI.ALIGNMENT.TOP;
    
    /**
     * This label text.
     * @type {string}
     * @private
     */
    private String text        =   null;
    
    /**
     * This label document�s render context
     * @type {RenderContext}
     * @private
     */
    private RenderContext rc          =   null;

    /**
     * Styles object.
     * @private
     */
    private Map<String, Object> styles      =   null;

    /**
     * Calculated document width.
     * @private
     */
    private double documentWidth   = 0;
    
    /**
     * Calculated document Height.
     * @private
     */
    private double documentHeight  = 0;
    
    /**
     * Document x position.
     * @private
     */
    private double documentX       = 0;
    
    /**
     * Document y position.
     * @private
     */
    private double documentY       = 0;

    /**
     * Does this label document flow ?
     * @private
     */
    private boolean reflow      =   true;

    /**
     * Collection of text lines calculated for the label.
     * @private
     */
    private List<DocumentLine> lines       =   null;   // calculated elements lines...

    /**
     * Collection of image objects in this label�s document.
     * @private
     */
    public Map<String, SpriteImage> images      =   null;
    
    /**
     * Registered callback to notify on anchor click event.
     * @private
     */
    public ClickCallback clickCallback   = null;

    // FIXME Type
    public Label setStyle (String name, Object styleData ) {
        this.styles.put( name, styleData);
        return this;
    }

    public Label addImage (String name,SpriteImage spriteImage ) {
        this.images.put( name, spriteImage);
        return this;
    }

    @Override
    public Label setSize (double w, double h) {
        super.setSize(w, h );
        this.setText( this.text, this.width );
        return this;
    }

    @Override
    public Label setBounds (double x, double y, double w, double h ) {
        super.setBounds(x,y,w,h );
        this.setText( this.text, this.width );
        return this;
    }

    public Label setText (String _text, Double width ) {

        if ( null==_text ) {
           return null;
        }

        Cache cached= this.cached;
        // TODO Check
        if ( cached.getValue() > 0 ) {
            this.stopCacheAsBitmap();
        }

        this.documentWidth= 0;
        this.documentHeight= 0;

        this.text= _text;

        int i, l;
        String text, tag;
        int tag_closes_at_pos;
        char _char;
        CaatjaContext2d ctx= CAAT.currentDirector.ctx;
        ctx.save();

        text= this.text;
        
        i=0;
        l=text.length();

        this.rc.start( ctx, this.styles, this.images, width );

        while( i<l ) {
            _char= text.charAt(i);

            if ( _char=='\\' ) {
                i+=1;
                this.rc.fchar( text.charAt(i) + "");
                i+=1;

            } else if ( _char=='<' ) {   // try an enhancement.

                // try finding another '>' and see whether it matches a tag
                tag_closes_at_pos= text.indexOf(">", i+1);
                if ( -1!=tag_closes_at_pos ) {
                    tag= text.substring( i+1, tag_closes_at_pos-i-1 );
                    if ( tag.indexOf("<")!=-1 ) {
                        this.rc.fchar( _char + "");
                        i+=1;
                    } else {
                        this.rc.setTag( tag );
                        i= tag_closes_at_pos+1;
                    }
                }
            } else {
                this.rc.fchar( _char + "");
                i+= 1;
            }
        }

        this.rc.end();
        this.lines= this.rc.lines;

        this.__calculateDocumentDimension( width== null ? 0 : width );
        this.setLinesAlignment();

        ctx.restore();

        this.setPreferredSize( this.documentWidth, this.documentHeight );
        this.invalidateLayout();
        
        this.setDocumentPosition();

        // TODO Check
        if ( cached.getValue() > 0) {
            this.cacheAsBitmap(0d,cached);
        }

        return this;
    }

    public Label setVerticalAlignment (ALIGNMENT align ) {
        this.valignment= align;
        this.setDocumentPosition();
        return this;
    }

    public Label setHorizontalAlignment (ALIGNMENT align ) {
        this.halignment= align;
        this.setDocumentPosition();
        return this;
    }
    
    public void setDocumentPosition() {
        double xo=0, yo=0;

        if ( this.valignment==UI.ALIGNMENT.CENTER ) {
            yo= (this.height - this.documentHeight )/2;
        } else if ( this.valignment==UI.ALIGNMENT.BOTTOM ) {
            yo= this.height - this.documentHeight;
        }

        if ( this.halignment==UI.ALIGNMENT.CENTER ) {
            xo= (this.width - this.documentWidth )/2;
        } else if ( this.halignment==UI.ALIGNMENT.RIGHT ) {
            xo= this.width - this.documentWidth;
        }

        this.documentX= xo;
        this.documentY= yo;
    }

    public Label __calculateDocumentDimension (double suggestedWidth ) {
        int i;
        int y = 0;

        this.documentWidth= 0;
        this.documentHeight= 0;
        for( i=0; i<this.lines.size(); i++ ) {
            this.lines.get(i).y =y;
            this.documentWidth= Math.max( this.documentWidth, this.lines.get(i).width );
            this.documentHeight+= this.lines.get(i).adjustHeight();
            y+= this.lines.get(i).getHeight();
        }

        this.documentWidth= Math.max( this.documentWidth, suggestedWidth );

        return this;
    }

    public void setLinesAlignment () {
        for( int i=0; i<this.lines.size(); i++ ) {
            this.lines.get(i).setAlignment( this.documentWidth );
        }
    }

    @Override
    public void paint(Director director, double time ) {

        CaatjaContext2d ctx= director.ctx;

        // TODO Check
        if ( this.cached  == Cache.NO) {

            ctx.save();

            ctx.setTextBaseline("alphabetic");
            ctx.translate( this.documentX, this.documentY );

            for( int i=0; i<this.lines.size(); i++ ) {
                DocumentLine line= this.lines.get(i);
                line.paint( director.ctx );

                if ( UI.DEBUG ) {
                    ctx.strokeRect( line.x, line.y, line.width, line.height );
                }
            }

            ctx.restore();
        } else {
            if ( this.backgroundImage != null) {
                this.backgroundImage.paint(director,time,0,0);
            }
        }
    }
    
    public DocumentElement __getDocumentElementAt(double x, double y ) {

        x-= this.documentX;
        y-= this.documentY;

        for( int i=0; i<this.lines.size(); i++ ) {
            DocumentLine line= this.lines.get(i);

            if ( line.x<=x && line.y<=y && line.x+line.width>=x && line.y+line.height>=y ) {
                return line.__getElementAt( x - line.x, y - line.y );
            }
        }

        return null;
    }

    // TODO Remove
//    @Override
//    public void mouseExit(CAATMouseEvent mouseEvent) {
//    	Caatja.setCursor("default");
//    }
    
    // TODO Remove
//    @Override
//    public void mouseMove(CAATMouseEvent e) throws Exception {
//        DocumentElement elem= this.__getDocumentElementAt(e.x, e.y);
//        if ( elem != null && elem.getLink() != null) {
//        	Caatja.setCursor( "pointer");
//        } else {
//        	Caatja.setCursor( "default");
//        }
//    }
    
//    // TODO Clean    
//    @Override
//    public void mouseClick(CAATMouseEvent e) throws Exception {
//        if ( this.clickCallback != null ) {
//            DocumentElement elem= this.__getDocumentElementAt(e.x, e.y);
//            if ( elem.getLink() != null) {
//                this.clickCallback.call( elem.getLink() );
//            }
//        }
//    }
    
    public Label setClickCallback(ClickCallback callback ) {
        this.clickCallback= callback;
        return this;
    }
    
    

}
