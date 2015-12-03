package com.abahety.instagramclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abahety on 12/1/15.
 * This class is pojo to hold JSON object of interested items from instagram
 * popular media api
 */
public class PopularMedia {

    private String imageUrl;
    private String caption;
    private String userName;

    private static String Error_Tag = "JsonParseError";

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public String getUserName() {
        return userName;
    }

    // Decodes json into PopularMedia model object
    public static PopularMedia fromJson(JSONObject jsonObject) {
        PopularMedia popularMedia = new PopularMedia();
        // Deserialize json into object fields
        try {
            popularMedia.imageUrl = getImageUrlFromJsonObject(jsonObject);
            popularMedia.caption = getCaptionFromJsonObject(jsonObject);
            popularMedia.userName = getUsernameFromJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return popularMedia;
    }

    // Decodes array of json results into PopularMedia model objects
    public static ArrayList<PopularMedia> fromJson(JSONArray jsonArray) {
        ArrayList<PopularMedia> medias = new ArrayList<PopularMedia>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject mediaJson = null;
            try {
                mediaJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            PopularMedia media = PopularMedia.fromJson(mediaJson);
            if (media != null) {
                medias.add(media);
            }
        }
        return medias;
    }


    // parse caption text key object => caption => text
    private static String getCaptionFromJsonObject(JSONObject json) throws JSONException {

            if (json.has("caption")) {
                JSONObject caption = json.getJSONObject("caption");
                if(caption!=null){
                    return caption.getString("text");
                }
            }

        return null;

    }

    // parse caption text key object => caption => text
    private static String getImageUrlFromJsonObject(JSONObject json) throws JSONException{

            if (json.has("caption")) {
                JSONObject images = json.getJSONObject("images");
                if(images!=null){
                    JSONObject standardRes = images.getJSONObject("standard_resolution");
                    if(standardRes!=null){
                        return standardRes.getString("url");
                    }
                }
            }

        return null;

    }

    // parse caption text key object => caption => text
    private static String getUsernameFromJsonObject(JSONObject json) throws JSONException{

            if (json.has("caption")) {
                JSONObject user = json.getJSONObject("user");
                if(user!=null){
                    return user.getString("username");
                }
            }

        return null;

    }


    @Override
    public String toString() {
        return caption;
    }
}
