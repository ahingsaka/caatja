package com.katspow.caatjagwt.client;

import java.util.Map;

import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.data.ChallengeResponse;
import org.restlet.client.data.ChallengeScheme;
import org.restlet.client.data.MediaType;
import org.restlet.client.ext.json.JsonRepresentation;
import org.restlet.client.resource.ClientResource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaImageLoaderCallback;
import com.katspow.caatja.core.image.CaatjaPreloader;

/**
 * A REST implementation for the image loader.
 * It uses a basic authentication, so login and password MUST be provided in constructor. 
 * 
 * @author Ahingsaka
 *
 */
public class CaatjaGwtRestImageLoader implements CaatjaImageLoader {
    
    private String url;
    
    private String appName;

    private String login;

    private String pwd;
    
    public CaatjaGwtRestImageLoader(String url, String appName, String login, String pwd) {
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
            public void handle(Request request, Response response) {
                
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                JSONValue value;
                
                try {
                    
                    value = rep.getValue();
                    JSONArray array = value.isArray();
                    
                    if (array != null) {
                        
                        for (int j = 0; j < array.size(); j++) {
                            JSONValue jsonValue = array.get(j);
                            
                            JSONObject object = jsonValue.isObject();
                            
                            for (String key : object.keySet()) {
                                JSONValue val = object.get(key);
                                
                                CaatjaGwtImage image = new CaatjaGwtImage();
                                String data = val.toString();
                                
                                // Remove " at the beginning and at the end (why in hell are they added ?)
                                String finishedData = data.substring(1, data.length() - 1);
                                
                                image.loadData(finishedData);
                                preloader.getCaatjaImages().put(key, image);
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
