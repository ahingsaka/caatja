package com.katspow.caatjafx;

import com.katspow.caatja.core.image.CaatjaPreloader;

public class CaatjaFxPreloader extends CaatjaPreloader {

	@Override
	public void addImage(String name, String path) {
		images.put(name, path);
	}

}
