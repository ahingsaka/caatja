package com.katspow.caatja.core.canvas;

import com.katspow.caatja.foundation.ui.TextFont;


public interface CaatjaContext2d {
	
	void fillRect(double i, double j, double width, double height);

	void setFillStyle(CaatjaColor fillStrokeStyle);

	void save();

	void beginPath();

	void moveTo(double x, double y);

	void lineTo(double x, double y);

	void setStrokeStyle(String string);

	void stroke();

	void restore();

	void strokeRect(double d, double e, double i, double j);

	void closePath();

	void setGlobalAlpha(double frameAlpha);

	void rect(int i, int j, double width, double height);

	void clip();

	void setTransform(double d, double e, double f, double g, double h, double i);

	void translate(double i, double j);

	void scale(double i, double j);

	void setTextBaseline(String string);

	void setLineWidth(double lineWidth);

	void setGlobalCompositeOperation(String compositeOp);

	void arc(double d, double e, double f, double i, double g, boolean b);

	void fill();

	void setStrokeStyle(CaatjaColor strokeStyle);

	void setLineCap(String lineCap);

	void setLineJoin(String lineJoin);

	void setMiterLimit(double miterLimit);

	void setFont(TextFont font);

	void setShadowBlur(double parseDouble);

	void setShadowColor(String color);

	void setFillStyle(String style);

	void fillText(String text, double x, double y);

	void strokeText(String text, double x, double y);

	double measureTextWidth(String text);

	void drawImage(CaatjaImage imageElement, double x, double d);
	
	void drawImage(CaatjaImage imageElement, double x, double y, double width,
			double height, double i, double j, double width2, double height2);

	void drawImage(CaatjaImage imageElement, double offsetX, double offsetY,
			double width, double height);
        
	void drawImage(CaatjaCanvas canvasElement, double offsetX, double offsetY,
			double width, double height);

	void drawImage(CaatjaCanvas canvasElement, double offsetX, double offsetY);

	
	void drawImage(CaatjaCanvas canvasElement, int x, int y, double w,
			int height, int x2, int y2, double w2, int height2);

	void transform(double d, double e, double f, double g, double h, double i);
	
	void clearRect(double i, double j, double coordinateSpaceWidth,
			double coordinateSpaceHeight);

	CaatjaImageData getImageData(int i, int j, double coordinateSpaceWidth,
			double coordinateSpaceHeight);

	void putImageData(CaatjaImageData imageData, int i, int j);

	// TODO Is it really used ?
	CaatjaPattern createPattern(CaatjaCanvas canvasElement, String string);

	CaatjaPattern createPattern(CaatjaImage image, String string);
	
	String getFont();

	void setTextAlign(String textAlign);

	void rotate(double angle);

	void arcTo(double x, double y, double x2, double y2, double radius);

	void bezierCurveTo(double x, double y, double x2, double y2, double x3,
			double y3);

	void quadraticCurveTo(double x, double y, double x2, double y2);

    CaatjaGradient createLinearGradient(double i, double j, double width, double k);


}
