package com.abahety.instagramclient.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by abahety on 12/2/15.
 */
public class InstagramClient {
    private static String clientIdKey="client_id", clientId="e05c462ebd86446ea48a5af73769b602";
    private static String mediaPopularUrl = "https://api.instagram.com/v1/media/popular";
    private static InstagramClient instance = new InstagramClient();

    private static AsyncHttpClient client = new AsyncHttpClient();

    private InstagramClient(){
        // singleton instance as explained in async http library best practices
    }

    public static void getPopularMedia(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //add client id in request params
        params.put(clientIdKey,clientId);
        client.get(mediaPopularUrl, params, responseHandler);
    }


}
