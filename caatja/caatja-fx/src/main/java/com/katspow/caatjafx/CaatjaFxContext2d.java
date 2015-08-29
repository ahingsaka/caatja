package com.katspow.caatjafx;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaGradient;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.core.canvas.CaatjaPattern;
import com.katspow.caatja.foundation.ui.TextFont;

public class CaatjaFxContext2d implements CaatjaContext2d {
	
	private GraphicsContext context2d;

	public void setContext(GraphicsContext graphicsContext2D) {
		context2d = graphicsContext2D;
	}
	
	@Override
	public void arc(double arg0, double arg1, double arg2, double arg3,
			double arg4, boolean arg5) {
		context2d.arc(arg0, arg1, arg2, arg2, 180 * arg3 / Math.PI, 180 * arg4 / Math.PI);
	}

	@Override
	public void arcTo(double arg0, double arg1, double arg2, double arg3,
			double arg4) {
		context2d.arcTo(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void beginPath() {
		context2d.beginPath();
	}

	@Override
	public void bezierCurveTo(double arg0, double arg1, double arg2,
			double arg3, double arg4, double arg5) {
		context2d.bezierCurveTo(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void clearRect(double arg0, double arg1, double arg2, double arg3) {
		context2d.clearRect(arg0, arg1, arg2, arg3);
	}

	@Override
	public void clip() {
		context2d.clip();
	}

	@Override
	public void closePath() {
		context2d.closePath();
	}

	@Override
	public CaatjaGradient createLinearGradient(double i, double j,
			double width, double k) {
		CaatjaGradient caatjaGradient = new CaatjaGradient(i, j, width, k);
		return caatjaGradient;
	}

	// Move to caatja
	@Override
	public CaatjaPattern createPattern(CaatjaCanvas canvasElement, String repetition) {
		CaatjaPattern caatjaPattern = new CaatjaPattern();
		caatjaPattern.setCaatjaCanvas(canvasElement);
		caatjaPattern.setRepetition(repetition);
		return caatjaPattern;
	}

	// Move to caatja
	@Override
	public CaatjaPattern createPattern(CaatjaImage caatjaImage, String repetition) {
		CaatjaPattern caatjaPattern = new CaatjaPattern();
		CaatjaFxImage caatjaFxImage = (CaatjaFxImage) caatjaImage;
		caatjaPattern.setImageElement(caatjaFxImage);
        caatjaPattern.setRepetition(repetition);
        return caatjaPattern;
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double arg1, double arg2) {
		CaatjaFxImage caatjaFxImage = (CaatjaFxImage) caatjaImage;
		Image img = caatjaFxImage.image;
		context2d.drawImage(img, arg1, arg2);
	}

	@Override
	public void drawImage(CaatjaCanvas canvas, double x, double y) {
		// Not tested
	    CaatjaFxCanvas cfcx = (CaatjaFxCanvas) canvas;
        Canvas fxCanvas = cfcx.canvas;
        WritableImage image = new WritableImage((int) fxCanvas.getWidth(), (int) fxCanvas.getHeight());
        WritableImage snapshot = fxCanvas.snapshot(null, image);
        
        context2d.drawImage(snapshot, x, y);
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double arg1, double arg2,
			double arg3, double arg4) {
		CaatjaFxImage caatjaFxImage = (CaatjaFxImage) caatjaImage;
		Image img = caatjaFxImage.image;
		context2d.drawImage(img, arg1, arg2, arg3, arg4);
		
	}

	@Override
	public void drawImage(CaatjaCanvas canvas, final double offsetX, final double offsetY,
			final double width, final double height) {
	    CaatjaFxCanvas cfcx = (CaatjaFxCanvas) canvas;
	    Canvas fxCanvas = cfcx.canvas;
	    
	    WritableImage image = new WritableImage((int) width, (int) height);
	    
	    WritableImage snapshot = fxCanvas.snapshot(null, image);
	    context2d.drawImage(snapshot, offsetX, offsetY, width, height);
	    
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double arg1, double arg2,
			double arg3, double arg4, double arg5, double arg6, double arg7,
			double arg8) {
		CaatjaFxImage caatjaFxImage = (CaatjaFxImage) caatjaImage;
		Image img = caatjaFxImage.image;
		context2d.drawImage(img, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	@Override
	public void drawImage(CaatjaCanvas canvas, int x, int y, double w,
			int height, int x2, int y2, double w2, int height2) {
		// Not tested
	    CaatjaFxCanvas cfcx = (CaatjaFxCanvas) canvas;
        Canvas fxCanvas = cfcx.canvas;
        WritableImage image = new WritableImage((int) w, (int) height);
        WritableImage snapshot = fxCanvas.snapshot(null, image);
        
        context2d.drawImage(snapshot, x, y, w, height, x2, y2, w2, height2);
	    
	}

	@Override
	public void fill() {
		context2d.fill();
	}

	@Override
	public void fillRect(double arg0, double arg1, double arg2, double arg3) {
		context2d.fillRect(arg0, arg1, arg2, arg3);
	}

	@Override
	public void fillText(String arg0, double arg1, double arg2) {
		context2d.fillText(arg0, arg1, arg2);
	}

	@Override
	public String getFont() {
		// TODO Auto-generated method stub
	    Font font = context2d.getFont();
	    double size = font.getSize();
	    String family = font.getFamily();
	    TextFont tf = new TextFont((int) size, family);
	    
		return null;
	}

	@Override
	public CaatjaImageData getImageData(int arg0, int arg1, double arg2,
			double arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lineTo(double arg0, double arg1) {
		context2d.lineTo(arg0, arg1);
	}
	
	@Override
	public double measureTextWidth(String text) {
		// TODO Check
	    Font font = context2d.getFont();
	    Text t = new Text(text);
	    t.setFont(font);
	    t.snapshot(null, null);
	    double width = t.getLayoutBounds().getWidth();
		return width;
	}

	@Override
	public void moveTo(double arg0, double arg1) {
		context2d.moveTo(arg0, arg1);
	}

	@Override
	public void putImageData(CaatjaImageData imageData, int i, int j) {
		// TODO Auto-generated method stub
	    CaatjaFxImageData caatjaFxImageData = (CaatjaFxImageData) imageData.getData();
	}

	@Override
	public void quadraticCurveTo(double arg0, double arg1, double arg2,
			double arg3) {
		context2d.quadraticCurveTo(arg0, arg1, arg2, arg3);
	}

	@Override
	public void rect(int arg0, int arg1, double arg2, double arg3) {
		context2d.rect(arg0, arg1, arg2, arg3);
	}

	@Override
	public void restore() {
		context2d.restore();
	}

	@Override
	public void rotate(double arg0) {
		context2d.rotate(arg0);
	}

	@Override
	public void save() {
		context2d.save();
	}

	@Override
	public void scale(double arg0, double arg1) {
		context2d.scale(arg0, arg1);
	}

	@Override
	public void setFillStyle(CaatjaColor fillStrokeStyle) {
	    
	    // TODO Auto-generated method stub
	    if (fillStrokeStyle instanceof CaatjaGradient) {
	        
	        CaatjaGradient caatjaGradient = (CaatjaGradient) fillStrokeStyle;
	        
            List<Stop> stops = new ArrayList<Stop>();
            
            List<Double> colorStopValues = caatjaGradient.getColorStopValues();
            for (int i = 0; i <=  colorStopValues.size() - 1; i++) {
                Double colorStopValue = colorStopValues.get(i);
                String colorStopColor = caatjaGradient.getColorStopColors().get(i);
                
                Stop stop = new Stop(colorStopValue, Color.web(colorStopColor));
                stops.add(stop);
            }
            
            LinearGradient lg = new LinearGradient(caatjaGradient.getX0(), caatjaGradient.getY0(), caatjaGradient.getX1(), caatjaGradient.getY1(), false, CycleMethod.NO_CYCLE, stops);
            
            context2d.setFill(lg);
            
            
        } else if (fillStrokeStyle instanceof CaatjaPattern) {
            System.out.println("fill caatjapattern nyi");
            
            CaatjaPattern caatjaPattern = (CaatjaPattern) fillStrokeStyle;
            CaatjaImage caatjaImage = caatjaPattern.getImageElement();
            CaatjaFxImage caatjaGwtImage = (CaatjaFxImage) caatjaImage;
            Image imageElement = caatjaGwtImage.image;
            
            String repetition = caatjaPattern.getRepetition();
            if (imageElement != null) {
//                CanvasPattern canvasPattern = context2d.createPattern(imageElement, repetition);
//                context2d.setFillStyle(canvasPattern);
                
            } else {
//                CaatjaCanvas canvasElement = caatjaPattern.getCaatjaCanvas();
//                CanvasPattern canvasPattern = context2d.createPattern(((CaatjaFxCanvas)canvasElement).canvas.getCanvasElement(), repetition);
//                context2d.setFillStyle(canvasPattern);
            }
            
           
        } else {
            setFillStyle(fillStrokeStyle.getStrokeStyle());
        }
	    
	}

	@Override
	public void setFillStyle(String fillStyle) {
	    context2d.setFill(Color.web(fillStyle));
	}

	@Override
	public void setFont(TextFont tf) {
        // TODO Auto-generated method stub
	    Font font = new Font(tf.getFontFamily(), tf.getSize());
	    context2d.setFont(font);
	}

	@Override
	public void setGlobalAlpha(double arg0) {
		context2d.setGlobalAlpha(arg0);
	}

	@Override
	public void setGlobalCompositeOperation(String compositeOp) {
	    // TODO Auto-generated method stub
	    if ("source-over".equals(compositeOp)) {
	        context2d.setGlobalBlendMode(BlendMode.SRC_OVER);
	    } else if ("source-out".equals(compositeOp)) {
	        context2d.setGlobalBlendMode(BlendMode.EXCLUSION);
	    } else if ("lighter".equals(compositeOp)) {
	        context2d.setGlobalBlendMode(BlendMode.LIGHTEN);
	    } else if ("source-atop".equals(compositeOp)) {
	        context2d.setGlobalBlendMode(BlendMode.SRC_ATOP);
	    } else {
	        System.out.println("compositeOp not supported: " + compositeOp);
	    }
	    
	}

	@Override
	public void setLineCap(String arg0) {
		context2d.setLineCap(StrokeLineCap.valueOf(arg0.toUpperCase()));
	}

	@Override
	public void setLineJoin(String arg0) {
		context2d.setLineJoin(StrokeLineJoin.valueOf(arg0.toUpperCase()));
	}

	@Override
	public void setLineWidth(double arg0) {
		context2d.setLineWidth(arg0);
	}

	@Override
	public void setMiterLimit(double arg0) {
		context2d.setMiterLimit(arg0);
	}

	@Override
	public void setShadowBlur(double radius) {
		// Not tested
	    if (shadowColor != null) {
	        DropShadow ds = new DropShadow(radius, Color.web(shadowColor));
	        context2d.applyEffect(ds);
	    }
	}
	
	private String shadowColor;

	@Override
	public void setShadowColor(String color) {
		// Not tested
	    shadowColor = color;
	}

	@Override
	public void setStrokeStyle(String strokeStyle) {
	    context2d.setStroke(Color.web(strokeStyle));
	}

	@Override
	public void setStrokeStyle(CaatjaColor strokeStyle) {
		// TODO Auto-generated method stub
	    if (strokeStyle instanceof CaatjaGradient) {
	        
	        CaatjaGradient caatjaGradient = (CaatjaGradient) strokeStyle;
            
            List<Stop> stops = new ArrayList<Stop>();
            
            List<Double> colorStopValues = caatjaGradient.getColorStopValues();
            for (int i = 0; i <=  colorStopValues.size() - 1; i++) {
                Double colorStopValue = colorStopValues.get(i);
                String colorStopColor = caatjaGradient.getColorStopColors().get(i);
                
                Stop stop = new Stop(colorStopValue, Color.web(colorStopColor));
                stops.add(stop);
            }
            
            LinearGradient lg = new LinearGradient(caatjaGradient.getX0(), caatjaGradient.getY0(), caatjaGradient.getX1(), caatjaGradient.getY1(), false, CycleMethod.NO_CYCLE, stops);
	        
	        context2d.setStroke(lg);
	        
	    } else if (strokeStyle instanceof CaatjaPattern) {
	        System.out.println("stroke caatjapattern nyi");
	        
	        CaatjaPattern caatjaPattern = (CaatjaPattern) strokeStyle;
            CaatjaImage caatjaImage = caatjaPattern.getImageElement();
            CaatjaFxImage caatjaGwtImage = (CaatjaFxImage) caatjaImage;
            Image imageElement = caatjaGwtImage.image;
            
            String repetition = caatjaPattern.getRepetition();
            if (imageElement != null) {
//                CanvasPattern canvasPattern = context2d.createPattern(imageElement, repetition);
//                context2d.setStrokeStyle(canvasPattern);
                
            } else {
                CaatjaCanvas canvasElement = caatjaPattern.getCaatjaCanvas();
//                CanvasPattern canvasPattern = context2d.createPattern(((CaatjaFxCanvas)canvasElement).canvas, repetition);
//                context2d.setStrokeStyle(canvasPattern);
            }
	        
	        
	        
	    } else {
	        setStrokeStyle(strokeStyle.getStrokeStyle());
	    }
		
	}

	@Override
	public void setTextAlign(String arg0) {
		context2d.setTextAlign(TextAlignment.valueOf(arg0.toUpperCase()));
	}

	@Override
	public void setTextBaseline(String arg0) {
		context2d.setTextBaseline(VPos.valueOf(arg0.toUpperCase()));
	}

	@Override
	public void setTransform(double arg0, double arg1, double arg2,
			double arg3, double arg4, double arg5) {
		context2d.setTransform(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void stroke() {
		context2d.stroke();
	}

	@Override
	public void strokeRect(double arg0, double arg1, double arg2, double arg3) {
		context2d.strokeRect(arg0, arg1, arg2, arg3);
	}

	@Override
	public void strokeText(String arg0, double arg1, double arg2) {
		context2d.strokeText(arg0, arg1, arg2);
	}

	@Override
	public void transform(double arg0, double arg1, double arg2, double arg3,
			double arg4, double arg5) {
		context2d.transform(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void translate(double arg0, double arg1) {
		context2d.translate(arg0, arg1);
	}


}
