package com.katspow.caatja.foundation.ui.document;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.foundation.ui.UI;
import com.katspow.caatja.foundation.ui.render.RenderContextStyle;

/**
 * TODO Check inheritance
 *
 */
public class DocumentElementImage extends DocumentElement {
    
    public DocumentElementImage(double x, SpriteImage image, int r, int c, RenderContextStyle style, Object anchor ) {
        
        super(anchor, style);
        
        this.x= x;
        this.image= image;
        this.row= r;
        this.column= c;
        this.width= image.getWidth();
        this.height= image.getHeight();

        if ( this.image instanceof SpriteImage ) {
            this.spriteIndex= r*image.columns+c;
//            this.paint= this.paintSI;
        }
    }
    
    public double x       ;
    public SpriteImage image   = null;
    public int row     ;
    public int column  ;
    public int spriteIndex;
    
    public void paint (CaatjaContext2d ctx ) {
        // Change by me
        if ( this.image instanceof SpriteImage ) {
           paintSI(ctx);   
        } else {        
            this.style.image( ctx );
            // TODO Check
            ctx.drawImage( this.image.image, this.x, -this.height+1);
            if ( UI.DEBUG ) {
                ctx.strokeRect( this.x, -this.height+1, this.width, this.height );
            }
        }
    }

    public void paintSI (CaatjaContext2d ctx ) {
        this.style.image( ctx );
        this.image.setSpriteIndex( this.spriteIndex );
        
        // Add by me
        Director d = new Director(false);
        d.ctx = ctx;
        
        // TODO Check restriction
        this.image.paint( d, 0d, (int) this.x,  (int) -this.height+1 );
        
        if ( UI.DEBUG ) {
            ctx.strokeRect( this.x, -this.height+1, this.width, this.height );
        }
    }

    public double getHeight () {
        return this.image instanceof SpriteImage ? this.image.singleHeight : this.image.height;
    }
    
    // TODO
    public Object getFontMetrics () {
        return null;
    }

    public boolean contains (double x, double y) {
        return x>=this.x && x<=this.x+this.width && y>=this.y && y<this.y + this.height;
    }

    public void setYPosition (double baseline ) {
        this.y= baseline - this.height + 1;
    }

}
