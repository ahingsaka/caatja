package com.katspow.caatja.foundation.ui.document;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.ui.UI;
import com.katspow.caatja.modules.font.FontData;

public class DocumentLine {
    
    /**
     * This class represents a document line.
     * It contains a collection of DocumentElement objects.
     * @return {*}
     * @constructor
     */
    public DocumentLine(FontData defaultFontMetrics) {
        this.elements= new ArrayList<DocumentElement>();
        this.defaultFontMetrics= defaultFontMetrics;
    }
    
    List<DocumentElement> elements    = null;
    public double width       = 0;
    public double height      = 0;
    public double defaultHeight = 0;  // default line height in case it is empty.
    public double y           = 0;
    public double x           = 0;
    public String alignment   = null;
    
    public double baselinePos = 0;
    
    // Add by me
    private FontData defaultFontMetrics;
    
    public void addElement (DocumentElement element ) {
        this.width= Math.max( this.width, element.x + element.width );
        this.height= Math.max( this.height, element.height );
        this.elements.add( element );
        this.alignment= element.style.__getProperty("alignment");
    }

    public void addElementImage (DocumentElementImage element ) {
        this.width= Math.max( this.width, element.x + element.width );
        this.height= Math.max( this.height, element.height );
        this.elements.add( element );
    }

    public double getHeight () {
        return this.height;
    }

    public void setY (double y ) {
        this.y= y;
    }

    public double getY () {
        return this.y;
    }

    public void paint (CaatjaContext2d ctx ) {
        ctx.save();
        ctx.translate(this.x,this.y + this.baselinePos );

        for( int i=0; i<this.elements.size(); i++ ) {
            this.elements.get(i).paint(ctx);
        }

        ctx.restore();

    }

    public void setAlignment (double width ) {
        if ( this.alignment.equals("center") ) {
            this.x= (width - this.width)/2;
        } else if ( this.alignment.equals("right") ) {
            this.x= width - this.width;
        } else if ( this.alignment.equals("justify") ) {

            // justify: only when text overflows further than document's 80% width
            if ( this.width / width > UI.JUSTIFY_RATIO && this.elements.size()>1) {
                double remaining= width - this.width;
                
                int forEachElement= (int)(remaining/(this.elements.size()-1))|0;
                for( int j=1; j<this.elements.size() ; j++ ) {
                    this.elements.get(j).x+= j*forEachElement;
                }

                remaining= remaining - forEachElement*this.elements.size() + 1;
                for( int j=this.elements.size()-1; j>=remaining; j-- ) {
                    this.elements.get(j).x+= (j-remaining);
                }
            }
        }
    }
    
    public double adjustHeight() {
        // TODO
//        Font biggestFont=null;
//        var biggestImage=null;
//
//        for( int i=0; i<this.elements.size(); i+=1 ) {
//            DocumentElement elem= this.elements.get(i);
//
//            FontData fm= elem.getFontMetrics();
//            if ( null!=fm ) {           // gest a fontMetrics, is a DocumentElementText (text)
//                if ( biggestFont == null) {
//                    biggestFont= fm;
//                } else {
//                    if ( fm.ascent > biggestFont.ascent ) {
//                        biggestFont= fm;
//                    }
//                }
//            } else {                    // no FontMetrics, it is an image.
//                if (!biggestImage) {
//                    biggestImage= elem;
//                } else {
//                    if ( elem.getHeight() > elem.getHeight() ) {
//                        biggestImage= elem;
//                    }
//                }
//            }
//        }
//
//        this.baselinePos= Math.max(
//                biggestFont ? biggestFont.ascent : this.defaultFontMetrics.ascent,
//                biggestImage ? biggestImage.getHeight() : this.defaultFontMetrics.ascent );
//            this.height= this.baselinePos + (biggestFont!=null ? biggestFont.descent : this.defaultFontMetrics.descent );
//
//        for( int i=0; i<this.elements.size(); i++ ) {
//            this.elements.get(i).setYPosition( this.baselinePos );
//        }

        return this.height;
    }
    
    /**
     * Every element is positioned at line's baseline.
     * @param x
     * @param y
     * @private
     */
    public DocumentElement __getElementAt(double x, double y ) {
        for( int i=0; i<this.elements.size(); i++ ) {
            DocumentElement elem= this.elements.get(i);
            if ( elem.contains(x,y) ) {
                return elem;
            }
        }

        return null;
    }

}
