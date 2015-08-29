package com.katspow.caatjagwt.client;

import java.util.List;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.CanvasPattern;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.dom.client.ImageElement;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaGradient;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.core.canvas.CaatjaImageData;
import com.katspow.caatja.core.canvas.CaatjaPattern;
import com.katspow.caatja.foundation.ui.TextFont;

public class CaatjaGwtContext2d implements CaatjaContext2d {
	
	private Context2d context2d;
	
	public void setContext2d(Context2d context2d) {
		this.context2d = context2d;
	}

	@Override
	public void fillRect(double i, double j, double width, double height) {
		context2d.fillRect(i, j, width, height);
	}

	@Override
	public void setFillStyle(CaatjaColor fillStrokeStyle) {
	    
	    if (fillStrokeStyle instanceof CaatjaGradient) {
	        CaatjaGradient caatjaGradient = (CaatjaGradient) fillStrokeStyle;
            CanvasGradient linearGradient = context2d.createLinearGradient(caatjaGradient.getX0(), caatjaGradient.getY0(), caatjaGradient.getX1(),
                    caatjaGradient.getY1());
            
            List<Double> colorStopValues = caatjaGradient.getColorStopValues();
            for (int i = 0; i <=  colorStopValues.size() - 1; i++) {
                Double colorStopValue = colorStopValues.get(i);
                String colorStopColor = caatjaGradient.getColorStopColors().get(i);
                
                linearGradient.addColorStop(colorStopValue, colorStopColor);
            }
            
            context2d.setFillStyle(linearGradient);
	        
	    } else if (fillStrokeStyle instanceof CaatjaPattern) {
	        
	        CaatjaPattern caatjaPattern = (CaatjaPattern) fillStrokeStyle;
            CaatjaImage caatjaImage = caatjaPattern.getImageElement();
            CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
            ImageElement imageElement = caatjaGwtImage.imageElement;
            
            String repetition = caatjaPattern.getRepetition();
	        if (imageElement != null) {
                CanvasPattern canvasPattern = context2d.createPattern(imageElement, repetition);
                
                context2d.setFillStyle(canvasPattern);
                
	        } else {
	            CaatjaCanvas canvasElement = caatjaPattern.getCaatjaCanvas();
	            CanvasPattern canvasPattern = context2d.createPattern(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), repetition);
	            
	            context2d.setFillStyle(canvasPattern);
	        }
	        
	    } else {
	        context2d.setFillStyle(fillStrokeStyle.getStrokeStyle());
	    }
	    
//		context2d.setFillStyle(fillStrokeStyle);
	}

	@Override
	public void save() {
		context2d.save();
	}

	@Override
	public void beginPath() {
		context2d.beginPath();
	}

	@Override
	public void moveTo(double i, double d) {
		context2d.moveTo(i, d);
	}

	@Override
	public void lineTo(double i, double d) {
		context2d.lineTo(i, d);
	}

	@Override
	public void setStrokeStyle(String strokeStyle) {
		context2d.setStrokeStyle(strokeStyle);
	}

	@Override
	public void stroke() {
		context2d.stroke();
	}

	@Override
	public void restore() {
		context2d.restore();
	}

	@Override
	public void strokeRect(double d, double e, double i, double j) {
		context2d.strokeRect(d, e, i, j);
	}

	@Override
	public void closePath() {
		context2d.closePath();
	}

	@Override
	public void setGlobalAlpha(double frameAlpha) {
		context2d.setGlobalAlpha(frameAlpha);
	}

	@Override
	public void rect(int i, int j, double width, double height) {
		context2d.rect(i, j, width, height);
	}

	@Override
	public void clip() {
		context2d.clip();
	}

	@Override
	public void setTransform(double d, double e, double f, double g, double h,
			double i) {
		context2d.setTransform(d, e, f, g, h, i);
	}

	@Override
	public void translate(double i, double j) {
		context2d.translate(i, j);
	}

	@Override
	public void scale(double i, double j) {
		context2d.scale(i, j);
	}

	@Override
	public void setTextBaseline(String baseline) {
		context2d.setTextBaseline(baseline);
	}

	@Override
	public void setLineWidth(double lineWidth) {
		context2d.setLineWidth(lineWidth);
	}

	@Override
	public void setGlobalCompositeOperation(String compositeOp) {
		context2d.setGlobalCompositeOperation(compositeOp);
	}

	@Override
	public void arc(double d, double e, double f, double i, double g, boolean b) {
		context2d.arc(d, e, f, i, g, b);
	}

	@Override
	public void fill() {
		context2d.fill();
	}

	@Override
	public void setStrokeStyle(CaatjaColor strokeStyle) {
	    
	    if (strokeStyle instanceof CaatjaGradient) {
            CaatjaGradient caatjaGradient = (CaatjaGradient) strokeStyle;
            CanvasGradient linearGradient = context2d.createLinearGradient(caatjaGradient.getX0(), caatjaGradient.getY0(), caatjaGradient.getX1(),
                    caatjaGradient.getY1());
            
            List<Double> colorStopValues = caatjaGradient.getColorStopValues();
            for (int i = 0; i <=  colorStopValues.size() - 1; i++) {
                Double colorStopValue = colorStopValues.get(i);
                String colorStopColor = caatjaGradient.getColorStopColors().get(i);
                
                linearGradient.addColorStop(colorStopValue, colorStopColor);
            }
            
            context2d.setStrokeStyle(linearGradient);
            
        } else if (strokeStyle instanceof CaatjaPattern) {
            
            CaatjaPattern caatjaPattern = (CaatjaPattern) strokeStyle;
            CaatjaImage caatjaImage = caatjaPattern.getImageElement();
            CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
            ImageElement imageElement = caatjaGwtImage.imageElement;
            
            String repetition = caatjaPattern.getRepetition();
            if (imageElement != null) {
                CanvasPattern canvasPattern = context2d.createPattern(imageElement, repetition);
                
                context2d.setStrokeStyle(canvasPattern);
                
            } else {
                CaatjaCanvas canvasElement = caatjaPattern.getCaatjaCanvas();
                CanvasPattern canvasPattern = context2d.createPattern(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), repetition);
                
                context2d.setStrokeStyle(canvasPattern);
            }
            
        } else {
            context2d.setStrokeStyle(strokeStyle.getStrokeStyle());
        }
	    
//		context2d.setStrokeStyle(strokeStyle);
	}

	@Override
	public void setLineCap(String lineCap) {
		context2d.setLineCap(lineCap);
	}

	@Override
	public void setLineJoin(String lineJoin) {
		context2d.setLineJoin(lineJoin);
	}

	@Override
	public void setMiterLimit(double miterLimit) {
		context2d.setMiterLimit(miterLimit);
	}

	@Override
	public void setFont(TextFont font) {
		context2d.setFont(font.getSize() + font.getUnit() + " " + font.getFontFamily());
	}

	@Override
	public void setShadowBlur(double parseDouble) {
		context2d.setShadowBlur(parseDouble);
	}

	@Override
	public void setShadowColor(String color) {
		context2d.setShadowColor(color);
	}

	@Override
	public void setFillStyle(String style) {
		context2d.setFillStyle(style);
	}

	@Override
	public void fillText(String text, double x, double y) {
		context2d.fillText(text, x, y);
	}

	@Override
	public void strokeText(String text, double x, double y) {
		context2d.strokeText(text, x, y);
	}

	@Override
	public double measureTextWidth(String text) {
		return context2d.measureText(text).getWidth();
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double x, double d) {
	    CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
	    ImageElement imageElement = caatjaGwtImage.imageElement;
	    context2d.drawImage(imageElement, x, d);
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double x, double y,
			double width, double height, double i, double j, double width2,
			double height2) {
		try {
			CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
			ImageElement imageElement = caatjaGwtImage.imageElement;
			context2d.drawImage(imageElement, x, y, width, height, i, j,
					width2, height2);
		} catch (Exception e) {
			// TODO
		}
	}

	@Override
	public void drawImage(CaatjaImage caatjaImage, double offsetX,
			double offsetY, double width, double height) {
	    CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
        ImageElement imageElement = caatjaGwtImage.imageElement;
		context2d.drawImage(imageElement, offsetX, offsetY, width, height);
	}

	@Override
	public void drawImage(CaatjaCanvas canvasElement, double offsetX,
			double offsetY, double width, double height) {
		context2d.drawImage(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), offsetX, offsetY, width, height);
	}

	@Override
	public void drawImage(CaatjaCanvas canvasElement, double offsetX,
			double offsetY) {
		context2d.drawImage(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), offsetX, offsetY);
	}

    @Override
    public void drawImage(CaatjaCanvas canvasElement, int x, int y, double w, int height, int x2, int y2, double w2,
            int height2) {
        context2d.drawImage(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), x, y, w, height, x2, y2, w2, height2);
    }

	@Override
	public void transform(double d, double e, double f, double g, double h,
			double i) {
	    context2d.transform(d, e, f, g, h, i);
	}

	@Override
	public void clearRect(double i, double j, double coordinateSpaceWidth,
			double coordinateSpaceHeight) {
	    context2d.clearRect(i, j, coordinateSpaceWidth, coordinateSpaceHeight);
	}

	@Override
	public CaatjaImageData getImageData(int i, int j, double coordinateSpaceWidth,
			double coordinateSpaceHeight) {
		ImageData imageData = context2d.getImageData(i, j, coordinateSpaceWidth, coordinateSpaceHeight);
		CaatjaGwtImageData caatjaImageData = new CaatjaGwtImageData();
		caatjaImageData.setImageData(imageData);
		
		return caatjaImageData;
	}

	@Override
	public void putImageData(CaatjaImageData imageData, int i, int j) {
		CaatjaGwtImageData caatjaGwtImageData = (CaatjaGwtImageData) imageData.getData();
	    context2d.putImageData(caatjaGwtImageData.getImageData(), i, j);
	}

	@Override
	public CaatjaPattern createPattern(CaatjaCanvas canvasElement,
			String repetition) {
	    
	    CaatjaPattern caatjaPattern = new CaatjaPattern();
	    caatjaPattern.setCaatjaCanvas(canvasElement);
	    caatjaPattern.setRepetition(repetition);
	    
	    
//		return context2d.createPattern(((CaatjaGwtCanvas)canvasElement).canvas.getCanvasElement(), repetition);
	    return caatjaPattern;
	}
	
    @Override
    public CaatjaPattern createPattern(CaatjaImage caatjaImage, String repetition) {
        
        CaatjaPattern caatjaPattern = new CaatjaPattern();
        CaatjaGwtImage caatjaGwtImage = (CaatjaGwtImage) caatjaImage;
        
        caatjaPattern.setImageElement(caatjaGwtImage);
        caatjaPattern.setRepetition(repetition);
        
//        return context2d.createPattern(imageElement, repetition);
        return caatjaPattern;
    }

	@Override
	public String getFont() {
		return context2d.getFont();
	}

	@Override
	public void setTextAlign(String textAlign) {
	    context2d.setTextAlign(textAlign);
	}

	@Override
	public void rotate(double angle) {
	    context2d.rotate(angle);
	}

	@Override
	public void arcTo(double x, double y, double x2, double y2, double radius) {
	    context2d.arcTo(x, y, x2, y2, radius);
	}

	@Override
	public void bezierCurveTo(double x, double y, double x2, double y2,
			double x3, double y3) {
	    context2d.bezierCurveTo(x, y, x2, y2, x3, y3);
	}

	@Override
	public void quadraticCurveTo(double x, double y, double x2, double y2) {
	    context2d.quadraticCurveTo(x, y, x2, y2);
	}

    @Override
    public CaatjaGradient createLinearGradient(double i, double j, double width, double k) {
    	
    	CaatjaGradient caatjaGradient = new CaatjaGradient(i, j, width, k);
    	
//        return context2d.createLinearGradient(i, j, width, k);
    	return caatjaGradient;
    }

}
