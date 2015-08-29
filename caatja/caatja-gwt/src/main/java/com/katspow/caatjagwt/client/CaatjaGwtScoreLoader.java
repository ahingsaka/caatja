package com.katspow.caatjagwt.client;

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
import com.katspow.caatja.modules.data.CaatjaHighScores;
import com.katspow.caatja.modules.data.CaatjaScore;
import com.katspow.caatja.modules.data.CaatjaScoreLoader;
import com.katspow.caatja.modules.data.CaatjaScoreLoaderCallback;

/**
 * Should be a local db access
 * 
 * @author Ahingsaka
 *
 */
public class CaatjaGwtScoreLoader implements CaatjaScoreLoader {

    private String url;

    private String appName;

    private String login;

    private String pwd;

    public CaatjaGwtScoreLoader(String url, String appName, String login, String pwd) {
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
                                String data = val.toString();

                                // Remove " at the beginning and at the end (why
                                // in hell are they added ?)
                                String finishedData = data.substring(1, data.length() - 1);

                                CaatjaScore score = new CaatjaScore(key, Integer.parseInt(finishedData));
                                highscores.addScore(score);

                            }
                        }

                        callback.onFinishedLoading(highscores);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        cr.get(MediaType.APPLICATION_JSON);
    }

}
