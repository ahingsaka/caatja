package com.katspow.caatja.foundation.ui.document;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.ui.UI;
import com.katspow.caatja.foundation.ui.render.RenderContextStyle;
import com.katspow.caatja.modules.font.Font;
import com.katspow.caatja.modules.font.FontData;

public class DocumentElementText extends DocumentElement {
    
    /**
     * This class represents a text in the document. The text will have applied the styles selected
     * when it was defined.
     * @param text
     * @param x
     * @param width
     * @param height
     * @param style
     * @param anchor
     * @return {*}
     * @constructor
     */
    public DocumentElementText(String text, double x, double width, double height, RenderContextStyle style, Object anchor) {
        super(anchor, style);
        
        this.x=         x;
        this.y=         0;
        this.width=     width;
        this.text=      text;
        this.style=     style;
        // FIXME
//        this.fm=        Font.getFontMetrics( style.sfont );
        this.height=    this.fm.height;
    }
    
    public String text    = null;
    public RenderContextStyle style   = null;
    public FontData fm      = null;
    public double bl      ;     // where baseline was set. current 0 in ctx.
    
    public void paint (CaatjaContext2d ctx ) {
        this.style.text( ctx, this.text, this.x, 0 );
        if ( UI.DEBUG ) {
            ctx.strokeRect( this.x, -this.fm.ascent, this.width, this.height);
        }
    }

    public double getHeight () {
        return this.fm.height;
    }

    // TODO
    public Object getFontMetrics () {
        return this.fm; //CAAT.Font.getFontMetrics( this.style.sfont);
    }

    public boolean contains (double x, double y) {
        return x>= this.x && x<=this.x+this.width &&
            y>= this.y && y<= this.y+this.height;
    }

    public void setYPosition (double baseline ) {
        this.bl= baseline;
        this.y= baseline - this.fm.ascent;
    }

}
