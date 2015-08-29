package com.katspow.caatjagwt.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.core.image.CaatjaPreloader;

public class CaatjaGwtImageLoader implements CaatjaImageLoader {

    @Override
    public void loadImages(final CaatjaPreloader preloader, final CaatjaImageLoaderCallback loadingCallback) {
        
        // Make GWT async call
        CaatjaGwtImageServiceAsync service = (CaatjaGwtImageServiceAsync) GWT.create(CaatjaGwtImageService.class);
        
        
        AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String,String>>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onSuccess(HashMap<String, String> result) {

                for (Map.Entry<String, String> entry : result.entrySet()) {
                    CaatjaGwtImage image = new CaatjaGwtImage();
                    image.loadData(entry.getValue());
                    preloader.getCaatjaImages().put(entry.getKey(), image);
                }
                
                try {
                    loadingCallback.onFinishedLoading();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        };
        
        service.getImages((HashMap<String, String>) preloader.getImages(), callback);
        
        
//        Map<String, CaatjaImage> caatjaImages = new HashMap<String, CaatjaImage>();
//        
//        for (Map.Entry<String, CaatjaImage> entry : caatjaImages.entrySet()) {
//            String data = entry.getValue().getBase64Image();
//            
//            CaatjaGwtImage caatjaGwtImage = new CaatjaGwtImage();
//            caatjaGwtImage.loadData(data);
//            
//            caatjaImages.put(entry.getKey(), caatjaGwtImage);
//        }
//        preloader.setCaatjaImages(caatjaImages);
        
    }

}
