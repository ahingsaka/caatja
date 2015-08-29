package com.katspow.caatja.modules.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.foundation.image.SpriteImageHelper;
import com.katspow.caatja.foundation.ui.TextFont;

/**
 * See LICENSE file.
 *
 * This class generates an in-memory image with the representation of a drawn list of characters.
 * 
 * FIXME All
 **/
public class Font {
    
    public static FontData getFontMetrics(TextFont font ) {
        FontData ret;
        if ( CAAT.CSS_TEXT_METRICS ) {
            try {
                ret= getFontMetricsCSS( font.getSize() + font.getUnit() + " " + font.getFontFamily());
                return ret;
            } catch(Exception e) {

            }
        }

        return getFontMetricsNoCSS(font.getSize() + font.getUnit() + " " + font.getFontFamily());
    }
    
    public static FontData getFontMetricsNoCSS(String font ) {

        // TODO Regexp !
//        String re= "/(\d+)p[x|t]\s*/i";
        String re = "";
//        var res= re.exec( font );
        String[] res = new String[] {};
        
        int height;

        // TODO Check
        if ( res.length == 0) {
            height= 32;     // no px or pt value in font. assume 32.)
        } else {
            height= Integer.parseInt(res[1])|0;
        }

        double ascent= height-1;
        int h= (int)(height + height *.2)|0;
        
        return new FontData(h, ascent, h - ascent);
    };
    
    // TODO
//    private  offset( elem ) {
//
//        var box, docElem, body, win, clientTop, clientLeft, scrollTop, scrollLeft, top, left;
//        var doc= elem && elem.ownerDocument;
//        var docElem = doc.documentElement;
//
//        box = elem.getBoundingClientRect();
//        //win = getWindow( doc );
//
//        body= document.body;
//        win= doc.nodeType === 9 ? doc.defaultView || doc.parentWindow : false;
//
//        clientTop  = docElem.clientTop  || body.clientTop  || 0;
//        clientLeft = docElem.clientLeft || body.clientLeft || 0;
//        scrollTop  = win.pageYOffset || docElem.scrollTop;
//        scrollLeft = win.pageXOffset || docElem.scrollLeft;
//        top  = box.top  + scrollTop  - clientTop;
//        left = box.left + scrollLeft - clientLeft;
//
//        return { top: top, left: left };
//    }
    
    /**
     * Totally ripped from:
     *
     * jQuery (offset function)
     * Daniel Earwicker: http://stackoverflow.com/questions/1134586/how-can-you-find-the-height-of-text-on-an-html-canvas
     *
     * @param font
     * @return {*}
     */
    public static FontData getFontMetricsCSS(String font ) {

        try {
            // FIXME
//            var text = document.createElement("span");
//            text.style.font = font;
//            text.innerHTML = "Hg";
//
//            var block = document.createElement("div");
//            block.style.display = "inline-block";
//            block.style.width = "1px";
//            block.style.heigh = "0px";
//
//            var div = document.createElement("div");
//            div.appendChild(text);
//            div.appendChild(block);
//
//
//            var body = document.body;
//            body.appendChild(div);

            try {

                FontData result = new FontData();

                // FIXME
//                block.style.verticalAlign = "baseline";
//                result.ascent = offset(block).top - offset(text).top;
//
//                result.ascent= Math.ceil(result.ascent);
//                result.height= Math.ceil(result.height);
//
//                result.descent = result.height - result.ascent;

                return result;

            } finally {
                // FIXME
//                body.removeChild( div );
            }
        } catch (Exception e) {
            return null;
        }
    };
    
    public static final int UNKNOWN_CHAR_WIDTH= 10;
    
    public int fontSize    =   10;
    public String fontSizeUnit=   "px";
    public String font        =   "Sans-Serif";
    public String fontStyle   =   "";
    public String fillStyle   =   "#fff";
    public String strokeStyle =   null;
    public double strokeSize  =   1;
    public int padding     =   0;
    public CaatjaCanvas image       =   null;
    public Map<Character, CharMapData> charMap     =   null;
    
    // Add by me
    public double ascent;
    public double descent;
    
    public class CharMapData {
        public CharMapData(int x, double width, double height) {
            this.x = x;
            this.width = width;
            this.height = height;
        }
        
        int x;
        double width;
        double height;
        
    }
    
    public double height      =   0;

    public Font setPadding (int padding ) {
        this.padding= padding;
        return this;
    }

    public Font setFontStyle (String style ) {
        this.fontStyle= style;
        return this;
    }
    
    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    public Font setFontSize (int fontSize ) {
        this.fontSize=      fontSize;
        this.fontSizeUnit=  "px";
        return this;
    }

    public Font setFont (String font ) {
        this.font= font;
        return this;
    }

    public Font setFillStyle (String style ) {
        this.fillStyle= style;
        return this;
    }

    public Font setStrokeStyle (String style ) {
        this.strokeStyle= style;
        return this;
    }
    
    public Font createDefault(int padding ) {
        String str="";
        for( int i=32; i<128; i++ ) {
            str= str+ (char) i;
        }

        return this.create( str, padding );
    }

    public Font create (String chars, Integer padding ) {
        
        if (padding == null) {
            padding = 0;
        }
        this.padding = padding;
        
        CaatjaCanvas canvas = Caatja.createCanvas();;
        canvas.setCoordinateSpaceWidth(1);
        canvas.setCoordinateSpaceHeight(1);
        CaatjaContext2d ctx= canvas.getContext2d();

        ctx.setTextBaseline("top");
        
        ctx.setFont(new TextFont(this.fontSize, this.fontSizeUnit, this.fontStyle));
//        ctx.setFont(this.fontStyle+" "+this.fontSize+""+this.fontSizeUnit+" "+ this.font);

        int textWidth= 0;
        List<Double> charWidth = new ArrayList<Double>() ;
        int i;
        int x;
        Character cchar;

        for( i=0; i<chars.length(); i++ ) {
            double cw= Math.max( 1, ((int) ctx.measureTextWidth( chars.charAt(i) + "")>>0) + 1) + 2 * padding;
            charWidth.add(cw);
            textWidth+= cw;
        }
        
        // FIXME
        //FontData fontMetrics= Font.getFontMetrics( ctx.getFont() );
        
        String baseline="alphabetic";
        double yoffset, canvasheight;
        
        // FIXME
//        canvasheight= fontMetrics.height;
//        this.ascent=  fontMetrics.ascent;
//        this.descent= fontMetrics.descent;
//        this.height=  fontMetrics.height;
//        yoffset=      fontMetrics.ascent;
        yoffset = 0;
        
        /*
        if ( !CAAT.CSS_TEXT_METRICS ) {
            baseline= "alphabetic";

            fontHeight= CAAT.Font.getFontHeightNoCSS( ctx.font );

            canvasheight= fontHeight.;
 
            yoffset= this.fontSize;

            this.height= canvasheight;
            this.ascent= this.fontSize;
            this.descent= this.height - this.ascent;
        } else {

            fontHeight= CAAT.Font.getFontHeight( ctx.font );
            baseline="alphabetic";
            canvasheight= fontHeight.height;
            yoffset= fontHeight.ascent;
            this.ascent= Math.ceil(fontHeight.ascent | 0 );
            this.descent= Math.ceil(fontHeight.descent | 0);
            this.height= this.ascent + this.descent;

        }
*/

        canvas.setCoordinateSpaceWidth(textWidth);
        canvas.setCoordinateSpaceHeight((int)(this.fontSize*1.5)>>0);
        ctx= canvas.getContext2d();

        //ctx.textBaseline= 'bottom';
        ctx.setTextBaseline(baseline);
        
        ctx.setFont(new TextFont(this.fontSize, this.fontSizeUnit, this.fontStyle));
//        ctx.setFont(this.fontStyle+" "+this.fontSize+""+this.fontSizeUnit+" "+ this.font);
        
        ctx.setFillStyle(this.fillStyle);
        ctx.setStrokeStyle(this.strokeStyle);

        this.charMap= new HashMap<Character, Font.CharMapData>();

        x=0;
        for( i=0; i<chars.length(); i++ ) {
            cchar= chars.charAt(i);
            ctx.fillText( cchar + "", x+padding, 0 );
            if ( this.strokeStyle != null) {
                ctx.beginPath();
                ctx.setLineWidth(this.strokeSize);
                ctx.strokeText( cchar + "", x+padding,  yoffset );
            }
            
            this.charMap.put(cchar, new CharMapData(x + padding, charWidth.get(i) - 2* padding, height));
            x+= charWidth.get(i);
        }

        this.image = canvas;
//        this.image= ImageUtil.optimize( canvas, 32, new Area(true, true, false, false));
//        this.height= this.image.getCoordinateSpaceHeight();

        return this;
    }
    
    public SpriteImage setAsSpriteImage() {
        Map<String, SpriteImageHelper> cm= new HashMap<String, SpriteImageHelper>();
        int _index= 0;
        for(  Character i : this.charMap.keySet() ) {
            Character _char = i;
            CharMapData charData= this.charMap.get(i);
            
            SpriteImageHelper spriteImageHelper = new SpriteImageHelper();
            spriteImageHelper.id = _index++;
            spriteImageHelper.height = (int) this.height;
            spriteImageHelper.xoffset = 0;
            spriteImageHelper.letter = _char;
            spriteImageHelper.yoffset = 0;
            spriteImageHelper.width = (int) charData.width;
            spriteImageHelper.xadvance = (int) charData.width;
            spriteImageHelper.x = charData.x;
            spriteImageHelper.y = 0;

        }
        
        // FIXME canvas not permitted for the moment
        //return new SpriteImage().initializeAsGlyphDesigner( this.image, cm );
        return new SpriteImage().initializeAsGlyphDesigner( null, cm );
    }


    public int stringWidth (String str ) {
        int i, l,  w=0;
        CharMapData c;

        for( i=0, l=str.length(); i<l; i++ ) {
            c= this.charMap.get(str.charAt(i));
            if ( c != null) {
                w+= c.width;
            } else {
                w+= UNKNOWN_CHAR_WIDTH;
            }
        }

        return w;
    }

    public void drawText (CaatjaContext2d ctx,String str, int x, int y ) {
        int i,l;
        double w;
        int height= this.image.getCoordinateSpaceHeight();
        CharMapData charInfo;

        for( i=0, l=str.length(); i<l; i++ ) {
            charInfo= this.charMap.get(str.charAt(i));
            if ( charInfo != null) {
                w= charInfo.width;
                // TODO Check
                if ( w>0 ) { //&& charInfo.height>0 ) {
                    ctx.drawImage(
                        this.image,
                        charInfo.x, 0,
                        w, height,
                        x, y,
                        w, height);
                }
                x+= w;
            } else {
                ctx.setStrokeStyle("#f00");
                ctx.strokeRect( x,y,UNKNOWN_CHAR_WIDTH,height );
                x+= UNKNOWN_CHAR_WIDTH;
            }
        }
    }

    public void save () {
        String str= "image/png";
        String strData= this.image.toDataUrl(str);
        // FIXME
//        Window.Location.replace(strData.replace( str, "image/octet-stream" ));
    }
    

}
