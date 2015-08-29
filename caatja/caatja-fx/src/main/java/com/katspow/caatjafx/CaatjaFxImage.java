package com.katspow.caatjafx;

import java.io.ByteArrayInputStream;

import javax.xml.bind.DatatypeConverter;

import javafx.scene.image.Image;

import com.katspow.caatja.core.canvas.CaatjaImage;

public class CaatjaFxImage extends CaatjaImage {
	
	public Image image;

	@Override
	public int getHeight() {
		return (int) image.getHeight();
	}

	@Override
	public String getSrc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth() {
		return (int) image.getWidth();
	}

	@Override
	public void loadData(String data) {
	    byte[] binaryData = DatatypeConverter.parseBase64Binary(data);
	    ByteArrayInputStream bais = new ByteArrayInputStream(binaryData);
	    image = new Image(bais);
	}

}
