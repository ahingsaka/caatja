package com.katspow.caatjafx;

import java.util.HashMap;
import java.util.Map;

import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.core.image.CaatjaPreloader;

public class CaatjaFxImageLoader implements CaatjaImageLoader {

	@Override
	public void loadImages(CaatjaPreloader preloader, CaatjaImageLoaderCallback loadingCallback) {
		// TODO Auto-generated method stub
		
		CaatjaFxImageService service = new CaatjaFxImageServiceImpl();
		
		HashMap<String, String> images = service.getImages((HashMap<String, String>) preloader.getImages());

		for (Map.Entry<String, String> entry : images.entrySet()) {
            CaatjaFxImage image = new CaatjaFxImage();
            image.loadData(entry.getValue());
            preloader.getCaatjaImages().put(entry.getKey(), image);
        }
        
        try {
            loadingCallback.onFinishedLoading();
        } catch (Exception e) {
            loadingCallback.onError(e);
        }
	}

}
