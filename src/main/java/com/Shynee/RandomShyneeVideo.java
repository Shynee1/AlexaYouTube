package com.Shynee;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomShyneeVideo {

    String pageToken = "null";

    List<JSONObject> videos = new ArrayList<>();
    Random rand = new Random();
    public String videoTitle;

    public String getShyneeVideo() {

        String url = null;

        boolean hasError = false;
        commentHandler();
        while (!pageToken.equalsIgnoreCase("stop")){
            if (!commentHandler()){
                hasError = true;
                break;
            }
        }
        if (!hasError) {
            if (!(videos.size() == 0)) {
                int randomInt = rand.nextInt(videos.size());
                JSONObject snippet = videos.get(randomInt);
                videoTitle = (String) snippet.get("title");
                JSONObject resourceID = (JSONObject) snippet.get("resourceId");
                url = (String) resourceID.get("videoId");

            }
        }
        return url;
    }

    public boolean commentHandler(){
        if (getJSON().equalsIgnoreCase("error")){
            return false;
        }
        else {
            JSONParser parser = new JSONParser();
            try {
                JSONObject object = (JSONObject) parser.parse(getJSON());
                JSONArray array = (JSONArray) object.get("items");
                for (Object o : array){
                    JSONObject init = (JSONObject) o;
                    JSONObject snippet = (JSONObject) init.get("snippet");
                    videos.add(snippet);
                }
                try {
                    String pagetoken = (String) object.get("nextPageToken");
                    if (pagetoken == null){
                        pageToken = "stop";
                    }
                    else{
                        pageToken = pagetoken;
                    }
                } catch (NullPointerException e){
                    pageToken = "stop";
                }
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public String getJSON(){
        try {
            YouTube youtube = RandomComment.getBuilder();
            YouTube.PlaylistItems.List request = youtube.playlistItems().list(Collections.singletonList("snippet"));
            PlaylistItemListResponse videoInfo;
            if (pageToken.equalsIgnoreCase("null")){
                videoInfo = request.setKey(Constants.API_KEY)
                        .setPlaylistId(Constants.PLAYLIST_URL)
                        .setMaxResults(50L)
                        .execute();
            }
            else{
                videoInfo = request.setKey(Constants.API_KEY)
                        .setPlaylistId(Constants.PLAYLIST_URL)
                        .setPageToken(pageToken)
                        .setMaxResults(50L)
                        .execute();
            }
            return videoInfo.toPrettyString();
        }
        catch (Exception e){
            return "error";
        }
    }
}
