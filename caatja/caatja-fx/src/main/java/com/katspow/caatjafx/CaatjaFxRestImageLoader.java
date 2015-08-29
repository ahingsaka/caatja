package com.katspow.caatjafx;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.core.image.CaatjaPreloader;

public class CaatjaFxRestImageLoader implements CaatjaImageLoader {

    private String url;

    private String appName;

    private String login;

    private String pwd;

    public CaatjaFxRestImageLoader(String url, String appName, String login, String pwd) {
        this.url = url;
        this.appName = appName;
        this.login = login;
        this.pwd = pwd;
    }

    @Override
    public void loadImages(final CaatjaPreloader preloader, final CaatjaImageLoaderCallback callback) {
        Map<String, String> images = preloader.getImages();

        ClientResource cr = new ClientResource(url);
        cr.addQueryParameter("appName", appName);

        for (Map.Entry<String, String> entry : images.entrySet()) {
            cr.addQueryParameter(entry.getKey(), entry.getValue());
        }

        ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, login, pwd);
        cr.setChallengeResponse(authentication);

        cr.setOnResponse(new Uniform() {
            @SuppressWarnings("unchecked")
            public void handle(Request request, Response response) {
                try {
                    JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                    JSONArray array = rep.getJsonArray();
                    
                    if (array != null) {
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            
                            object.keys();
                            
                            for (Iterator<String> key = object.keys(); key.hasNext();) {
                                String next = key.next();
                                String val = object.getString(next);
                                
                                CaatjaFxImage image = new CaatjaFxImage();
                                image.loadData(val);
                                preloader.getCaatjaImages().put(next, image);
                            }
                        }
                        
                        callback.onFinishedLoading();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cr.get(MediaType.APPLICATION_JSON);
    }

}
