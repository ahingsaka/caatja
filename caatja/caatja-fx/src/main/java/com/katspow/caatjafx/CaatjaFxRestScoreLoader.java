package com.katspow.caatjafx;

import java.util.Iterator;

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

import com.katspow.caatja.modules.data.CaatjaHighScores;
import com.katspow.caatja.modules.data.CaatjaScore;
import com.katspow.caatja.modules.data.CaatjaScoreLoader;
import com.katspow.caatja.modules.data.CaatjaScoreLoaderCallback;

public class CaatjaFxRestScoreLoader implements CaatjaScoreLoader {

    private String url;

    private String appName;

    private String login;

    private String pwd;
    
    public CaatjaFxRestScoreLoader(String url, String appName, String login, String pwd) {
        this.url = url;
        this.appName = appName;
        this.login = login;
        this.pwd = pwd;
    }

    @Override
    public void loadScores(final CaatjaHighScores highscores, final CaatjaScoreLoaderCallback callback) {
        
        ClientResource cr = new ClientResource(url);
        cr.addQueryParameter("appName", appName);
        
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
                                
                                CaatjaScore score = new CaatjaScore(next, Integer.parseInt(val));
                                highscores.addScore(score);
                            }
                        }
                        
                        callback.onFinishedLoading(highscores);
                    }

				} catch (Exception e) {
					callback.onError(e);
				}
            }
        });

        cr.get(MediaType.APPLICATION_JSON);
        
    }

}
