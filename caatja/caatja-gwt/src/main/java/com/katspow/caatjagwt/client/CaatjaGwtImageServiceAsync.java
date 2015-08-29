package com.katspow.caatjagwt.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * Move to another package ?
 * 
 * @author ahingsaka
 */
public interface CaatjaGwtImageServiceAsync {

    void getImages(HashMap<String, String> imagesToLoad, AsyncCallback<HashMap<String, String>> callback);

}
