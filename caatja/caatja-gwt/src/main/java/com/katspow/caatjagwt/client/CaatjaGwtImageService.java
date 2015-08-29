package com.katspow.caatjagwt.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * FIXME Move to another package ?
 * @author ahingsaka
 *
 */
@RemoteServiceRelativePath("caatjaGwtService")
public interface CaatjaGwtImageService extends RemoteService {

    HashMap<String, String> getImages(HashMap<String, String> imagesToLoad);
    
}
