package com.katspow.caatjagwt.client;

import com.katspow.caatja.core.image.CaatjaPreloader;

public class CaatjaGwtPreloader extends CaatjaPreloader {
    
    private String url = "http://localhost:4567/caatja";

    @Override
    public void addImage(final String name, String path) {
        
        images.put(name, path);
        
//        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url + "/" + path + "/" + type));
//
//        try {
//          Request request = builder.sendRequest(null, new RequestCallback() {
//            public void onError(Request request, Throwable exception) {
//               // Couldn't connect to server (could be timeout, SOP violation, etc.)     
//            }
//
//            public void onResponseReceived(Request request, Response response) {
//              if (200 == response.getStatusCode()) {
//                  // Process the response in response.getText()
//                  String text = response.getText();
//                  
//                  CaatjaGwtImage caatjaGwtImage = new CaatjaGwtImage();
//                  caatjaGwtImage.loadData(text);
//                  
//                  caatjaImages.put(name, caatjaGwtImage);
//                  
//              } else {
//                // Handle the error.  Can get the status text from response.getStatusText()
//                  System.out.println(response.getStatusText());
//              }
//            }       
//          });
//        } catch (RequestException e) {
//          // Couldn't connect to server      
//            e.printStackTrace();
//        }
        
        
    }

}
