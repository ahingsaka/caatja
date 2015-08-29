package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.FontOrSpriteImage;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.modules.font.Font;
import com.katspow.caatja.modules.font.FontData;
import com.katspow.caatja.pathutil.Path;

public class TextActor extends Actor {

    /**
     * TextActor draws text on screen. The text can be drawn directly on screen or make if follow a
     * path defined by an instance of <code>CAAT.Path</code>.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     *
     */
    public TextActor() {
        super();
        
        // Add by me
        this.font = new FontOrSpriteImage();
        this.font.font = new TextFont(10, "px", "sans-serif");
        
        this.textAlign = "left";
        this.outline = true;
        this.outlineColor = "black";
        this.clip= false;
        
    }
    
    public static final int TRAVERSE_PATH_FORWARD= 1;
    public static final int TRAVERSE_PATH_BACKWARD= -1;

    /**
     * a valid canvas rendering context font description. Default font will be "10px sans-serif".
     */
    public FontOrSpriteImage font;
    
    /**
     * Font info. Calculated in CAAT.
     */
    public FontData fontData =          null;
    
    /**
     * a valid canvas rendering context textAlign string. Any of:
     *   start, end, left, right, center.
     * defaults to "left".
     */
    public String textAlign;
    
    /**
     * a valid canvas rendering context textBaseLine string. Any of:
     *   top, hanging, middle, alphabetic, ideographic, bottom.
     * defaults to "top".
     */
    public String textBaseline = "top"; 
                                 
    /**
     * a boolean indicating whether the text should be filled.
     */
    private boolean fill = true;
    
    /**
     * text fill color
     */
    private CaatjaColor textFillStyle = CaatjaColor.valueOf("#eee"); // text fill color
    
    /**
     * a string with the text to draw.
     */
    private String text = null;
    
    /**
     * calculated text width in pixels.
     */
    public double textWidth = 0;
    
    /**
     * calculated text height in pixels.
     */
    public double textHeight = 0;
    
    /**
     * a boolean indicating whether the text should be outlined. not all browsers support it.
     */
    public boolean outline = false;
    
    /**
     * a valid color description string.
     */
    public String outlineColor;
    
    /**
     * text's stroke line width.
     */
    public double lineWidth =       1;
    
    /**
     * a CAAT.PathUtil.Path which will be traversed by the text.
     */
    private Path path = null;
    
    /**
     * A CAAT.Behavior.Interpolator to apply to the path traversal.
     */
    private Interpolator pathInterpolator = null;
    
    /**
     * time to be taken to traverse the path. ms.
     */
    private double pathDuration = 10000;
    
    /**
     * traverse the path forward (1) or backwards (-1).
     */
    public int sign = 1;
    
    /**
     * Set the text to be filled. The default Filling style will be set by calling setFillStyle method.
     * Default value is true.
     * @param fill {boolean} a boolean indicating whether the text will be filled.
     * @return this;
     */
    public TextActor setFill(boolean fill) {
        this.stopCacheAsBitmap();
        this.fill = fill;
        return this;
    }
    
    public TextActor setLineWidth(int lineWidth) {
        this.stopCacheAsBitmap();
        this.lineWidth = lineWidth;
        return this;
    }
    
    public TextActor setTextFillStyle(String style ) {
        this.stopCacheAsBitmap();
        this.textFillStyle= CaatjaColor.valueOf(style);
        return this;
    }
    
    // Add by me
    public TextActor setTextFillStyle(CaatjaColor style) {
        this.stopCacheAsBitmap();
        this.textFillStyle= style;
        return this;
    }

    /**
     * Sets whether the text will be outlined.
     * @param outline {boolean} a boolean indicating whether the text will be outlined.
     * @return this;
     */
    public TextActor setOutline(boolean outline) {
        this.stopCacheAsBitmap();
        this.outline = outline;
        return this;
    }
    
    public TextActor setPathTraverseDirection(int direction) {
        this.sign= direction;
        return this;
    }

    /**
     * Defines text's outline color.
     *
     * @param color {string} sets a valid canvas context color.
     * @return this.
     */
    public TextActor setOutlineColor(String color) {
        this.stopCacheAsBitmap();
        this.outlineColor = color;
        return this;
    }
    
    /**
     * Set the text to be shown by the actor.
     * @param sText a string with the text to be shwon.
     * @return this
     */
    public TextActor setText(String sText) {
        this.stopCacheAsBitmap();
        this.text = sText;
        
        if ( null==this.text || this.text.equals("")) {
            this.width= this.height= 0;
        }
        this.calcTextSize(CAAT.currentDirector);
        
        this.invalidate();

        return this;
    }
    
    public TextActor setTextAlign(String align) {
        this.textAlign = align;
        this.__setLocation();
        return this;
    }
    
    /**
     * Sets text alignment
     * @param align
     * @deprecated use setTextAlign
     */
    @Deprecated
    public TextActor setAlign(String align) {
        return setTextAlign(align);
    }

    /**
     * Set text baseline.
     * @param baseline
     */
    public TextActor setTextBaseline(String baseline) {
        this.stopCacheAsBitmap();
        this.textBaseline = baseline;
        return this;
    }
    
    public TextActor setBaseline(String baseline ) {
        this.stopCacheAsBitmap();
        return this.setTextBaseline(baseline);
    }

    /**
     * Sets the font to be applied for the text.
     * @param font a string with a valid canvas rendering context font description.
     * @return this
     */
    public TextActor setFont(TextFont font) {
        
        this.stopCacheAsBitmap();
        
        if (font == null) {
            font = new TextFont(10, "px", "sans-serif");
        }
        
        // Replaced by method below
//        if ( font instanceof CAAT.Font ) {
//            font= font.setAsSpriteImage();
//        }

        this.font.font = font;
        
        this.__calcFontData();
        this.calcTextSize(CAAT.director.get(0));

        return this;
    }
    
    // Add by me
    public TextActor setFont(Font font) {
        
        this.stopCacheAsBitmap();
        
        if (font == null) {
            this.font.font = new TextFont(10, "px", "sans-serif");
        }
        
        font.setAsSpriteImage();
       
        // TODO ?
//    } else if (font instanceof CAAT.SpriteImage ) {
//        CAAT.log("WARN: setFont will no more accept a CAAT.SpriteImage as argument.");
//    }
        
        this.font.bigFont = font;
        
        this.__calcFontData();
        this.calcTextSize(CAAT.director.get(0));
        
        return this;
    }
    
    // Add by me
    private double lx;
    private double ly;
    
    public TextActor setLocation (double x, double y) {
        this.lx= x;
        this.ly= y;
        this.__setLocation();
        return this;
    }

    public TextActor setPosition (double x, double y ) {
        this.lx= x;
        this.ly= y;
        this.__setLocation();
        return this;
    }

    public TextActor setBounds (double x,double y,double w,double h ) {
        this.lx= x;
        this.ly= y;
        this.setSize(w,h);
        this.__setLocation();
        return this;
    }

    public TextActor setSize (double w,double h ) {
        super.setSize(w,h);
        this.__setLocation();
        return this;
    }

    /**
     * @private
     */
    public TextActor __setLocation () {
        double nx, ny;
        if ( this.textAlign.equals("center")) {
            nx= this.lx - this.width/2;
        } else if ( this.textAlign.equals("right") || this.textAlign.equals("end")) {
            nx= this.lx - this.width;
        } else {
            nx= this.lx;
        }
        
        if ( this.textBaseline.equals("bottom" )) {
            ny= this.ly - this.height;
        } else if ( this.textBaseline.equals("middle" )) {
            ny= this.ly - this.height/2;
        } else if ( this.textBaseline.equals("alphabetic" )) {
            ny= this.ly - this.fontData.ascent;
        } else {
            ny= this.ly;
        }

        return (TextActor) super.setLocation( nx, ny );
    }
    
    public Actor centerAt(double x, double y) {
        this.textAlign="left";
        this.textBaseline = "top";
        return super.centerAt(x, y );
    }
    
    /**
     * Calculates the text dimension in pixels and stores the values in textWidth and textHeight
     * attributes.
     * If Actor's width and height were not set, the Actor's dimension will be set to these values.
     * @param director a CAAT.Director instance.
     * @return this
     */
    public TextActor calcTextSize(Director director) {
        
        if ( this.text == null || this.text.equals("") ) {
            this.textWidth= 0;
            this.textHeight= 0;
            return this;
        }
        
        if ( director.glEnabled ) {
            return this;
        }
        
        // FIXME should be replaced by instanceof
        if ( this.font.spriteImage != null) {
            this.textWidth= this.font.spriteImage.stringWidth( this.text );
            this.textHeight=this.font.spriteImage.stringHeight();
            this.width= this.textWidth;
            this.height= this.textHeight;
            
            // FIXME TODO Whats going on ?
//            this.fontData= this.font.getFontData();
            int as= (int)(this.font.spriteImage.singleHeight *.8)>>0;
            this.fontData= new FontData(this.font.spriteImage.singleHeight, as, this.font.spriteImage.singleHeight - as);
            
            return this;
        } else if (this.font.bigFont != null ){
            // FIXME should be replaced by instanceof
            this.textWidth= this.font.bigFont.stringWidth( this.text );
            // FIXME
//            this.textHeight=this.font.bigFont.stringHeight();
//            this.width= this.textWidth;
//            this.height= this.textHeight;
//            this.fontData= this.font.bigFont.getFontData();
            return this;
        }
        
        CaatjaContext2d ctx= director.ctx;
        
        ctx.save();
        ctx.setFont(this.font.font);

        this.textWidth= ctx.measureTextWidth( this.text );
        if (this.width == 0) {
            this.width = this.textWidth;
        }

        //try {
//            int pos = this.font.font.indexOf("px");
//
//            if (-1 == pos) {
//                pos = this.font.font.indexOf("pt");
//            }
//            if (-1 == pos) {
//                // no pt or px, so guess a size: 32. why not ?
//                this.textHeight = 32;
//            } else {
//                String s = this.font.font.substring(0, pos);
//                this.textHeight = Integer.parseInt(s, 10);
//            }
//            
//            this.__calcFontData();
//
//            // needed to calculate the descent.
//            // no context.getDescent(font) WTF !!!
////            this.textHeight += (int) (this.textHeight / 4) >> 0;
//            this.setSize( this.textWidth, this.textHeight );

//        } catch(Exception e) {
//            this.textHeight=20; // default height;
//        }
//        if ( this.height==0 ) {
//            this.height= this.textHeight;
//        }
            
        this.textHeight= this.fontData.height;
        this.setSize( this.textWidth, this.textHeight );

        ctx.restore();
        
        return this;
    }
    
    public FontData __calcFontData() {
        // TODO Check
        this.fontData= Font.getFontMetrics( this.font.font );
        
        // Add by me
        return this.fontData;
    }

    /**
     * Custom paint method for TextActor instances.
     * If the path attribute is set, the text will be drawn traversing the path.
     *
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     */
    public void paint(Director director, double time) {
        
        if (this.text == null) {
            return;
        }
        
        super.paint(director, time);
        
        // FIXME Remove comment when cache as bitmap is done
//        if ( this.cached ) {
            // cacheAsBitmap sets this actor's background image as a representation of itself.
            // So if after drawing the background it was cached, we're done.
//            return;
//        }

        if (null == this.text) {
            return;
        }
        
        if ( this.textWidth==0 || this.textHeight==0 ) {
            this.calcTextSize(director);
        }

        CaatjaContext2d ctx = director.ctx;
        
        // Change by me : font.spriteImage and no return type
        if (font.spriteImage != null || this.font.bigFont != null) {
            this.drawSpriteText(director,time);
        }

        if (null != this.font) {
            ctx.setFont(this.font.font);
        }

        // TODO FIXME should be replaced by alphabetic
        if (null != this.textBaseline) {
            ctx.setTextBaseline(this.textBaseline);
        }
        
        /**
         * always draw text with middle or bottom, top is buggy in FF.
         * @type {String}
         */
//        ctx.setTextBaseline(TextBaseline.ALPHABETIC);

        if (null == this.path) {

            if (null != this.textAlign) {
                ctx.setTextAlign(this.textAlign);
            }

            double tx = 0;
            if ("center".equals(textAlign)) {
                tx = (int) (this.width / 2) | 0;
            } else if ("right".equals(textAlign)) {
                tx = this.width;
            }

            if (this.fill) {
                if (null != this.textFillStyle) {
                    ctx.setFillStyle(this.textFillStyle);
                }
                // FIXME TODO
                ctx.fillText(this.text, tx, 0);
//                ctx.fillText(this.text, tx, this.fontData.ascent);
            }

            if (this.outline) {
                if (null != this.outlineColor) {
                    ctx.setStrokeStyle(this.outlineColor);
                }

                ctx.setLineWidth(this.lineWidth);
                ctx.beginPath();
                ctx.fillText(this.text, tx, 0);
             // FIXME TODO
                ctx.strokeText(this.text, tx, 0);
//                ctx.strokeText(this.text, tx, this.fontData.ascent);
            }
        } else {
            this.drawOnPath(director, time);
        }
    }

    /**
     * Private.
     * Draw the text traversing a path.
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     */
    private void drawOnPath(Director director, double time) {

        CaatjaContext2d ctx = director.ctx;
        
        if ( this.fill && null!=this.textFillStyle ) {
            ctx.setFillStyle(this.textFillStyle);
        }

        if ( this.outline && null!=this.outlineColor ) {
            ctx.setStrokeStyle(this.outlineColor);
        }

        double textWidth=this.sign * this.pathInterpolator.getPosition( (time%this.pathDuration)/this.pathDuration ).y * this.path.getLength() ;
        Pt p0 = new Pt(0,0,0);
        Pt p1 = new Pt(0,0,0);

        for (int i = 0; i < this.text.length(); i++) {
            String caracter = Character.toString(this.text.charAt(i));
            double charWidth = ctx.measureTextWidth(caracter);
            // guonjien: remove "+charWidth/2" since it destroys the kerning. and he's right!!!. thanks.
            double currentCurveLength= textWidth;

            p0 = this.path.getPositionFromLength(currentCurveLength).clone();
            p1 = this.path.getPositionFromLength(currentCurveLength - .1).clone();

            double angle = Math.atan2(p0.y - p1.y, p0.x - p1.x);

            ctx.save();

            if  (CAAT.CLAMP) {
                ctx.translate( (int)p0.x>>0, (int)p0.y>>0 );
            } else {
                ctx.translate(p0.x, p0.y);
            }
            
            ctx.rotate(angle);

            if (this.fill) {
                ctx.fillText(caracter, 0, 0);
            }
            
            if (this.outline) {
                ctx.beginPath();
                ctx.setLineWidth(this.lineWidth);
                ctx.strokeText(caracter, 0, 0);
            }

            ctx.restore();

            textWidth += charWidth;
        }
    }
    
    /**
     * Private.
     * Draw the text using a sprited font instead of a canvas font.
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     */
    private void drawSpriteText(Director director, double time) {
        if (null==this.path) {
            // Change by me
            this.font.bigFont.drawText( director.ctx, this.text, 0, 0);
        } else {
            this.drawSpriteTextOnPath(director, time);
        }
    }
    
    /**
     * Private.
     * Draw the text traversing a path using a sprited font.
     * @param director a valid CAAT.Director instance.
     * @param time an integer with the Scene time the Actor is being drawn.
     * 
     * TODO Change by me for font.spriteImage
     */
    private void drawSpriteTextOnPath(Director director, double time) {
        CaatjaContext2d context= director.ctx;

        double textWidth=this.sign * this.pathInterpolator.getPosition(
                (time%this.pathDuration)/this.pathDuration ).y * this.path.getLength() ;
        Pt p0= new Pt(0,0,0);
        Pt p1= new Pt(0,0,0);

        for( int i=0; i<this.text.length(); i++ ) {
            String character= this.text.charAt(i) + "";
            int charWidth= this.font.spriteImage.stringWidth(character);

            double pathLength= this.path.getLength();

            double currentCurveLength = charWidth/2 + textWidth;

            p0= this.path.getPositionFromLength(currentCurveLength).clone();
            p1= this.path.getPositionFromLength(currentCurveLength-0.1).clone();

            double angle= Math.atan2( p0.y-p1.y, p0.x-p1.x );

            context.save();

            if (CAAT.CLAMP) {
                context.translate( (int)p0.x|0, (int)p0.y|0 );
            } else {
                context.translate( p0.x, p0.y );
            }
            context.rotate( angle );
            
            int y = (int) (this.textBaseline.equals("bottom") ? 0 - this.font.spriteImage.getHeight() : 0);
            
            this.font.spriteImage.drawText(context,character, 0, y);

            context.restore();

            textWidth+= charWidth;
        }
    }

    /**
     * Set the path, interpolator and duration to draw the text on.
     * @param path a valid CAAT.Path instance.
     * @param interpolator a CAAT.Interpolator object. If not set, a Linear Interpolator will be used.
     * @param duration an integer indicating the time to take to traverse the path. Optional. 10000 ms
     * by default.
     * 
     */
    public TextActor setPath(Path path, Interpolator interpolator, Double duration) {
        this.path = path;
        
        if (interpolator == null) {
            this.pathInterpolator = Interpolator.createLinearInterpolator(false, false);
        } else {
            this.pathInterpolator = interpolator;
        }
        
        if (duration != null && duration > 0) {
            this.pathDuration = duration;
        } else {
            this.pathDuration = 10000;
        }
        
        /*
        parent could not be set by the time this method is called.
        so the actors bounds set is removed.
        the developer must ensure to call setbounds properly on actor.
         */
        this.mouseEnabled = false;
        
        return this;
    }
    
    // Add by me
    public TextActor setFillStrokeStyle(CaatjaColor style) {
        return (TextActor) super.setFillStrokeStyle(style);
    }
    
    public TextActor setFillStyle(String fillStyle) {
        return (TextActor) super.setFillStyle(fillStyle);
    }
    
    @Override
    public TextActor enableEvents(boolean enable) {
        return (TextActor) super.enableEvents(enable);
    }
    
    
}
