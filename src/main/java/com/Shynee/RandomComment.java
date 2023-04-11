package com.Shynee;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomComment {

    private String videoID = "";
    private String pageToken = "null";
    private List<JSONObject> comments = new ArrayList<>();
    Random rand = new Random();

    public String[] getRandomComment(String videoId) {
        videoID = videoId;
        boolean hasError = false;
        commentHandler();
        while (!pageToken.equalsIgnoreCase("stop")){
            if (!commentHandler()){
                hasError = true;
                break;
            }
        }
        if (!hasError) {
            if (!(comments.size() == 0)) {
                int randomInt = rand.nextInt(comments.size());
                JSONObject snippet = comments.get(randomInt);
                JSONObject topLevelComment = (JSONObject) snippet.get("topLevelComment");
                JSONObject snippet2 = (JSONObject) topLevelComment.get("snippet");
                String comment = (String) snippet2.get("textOriginal");
                String author = (String) snippet2.get("authorDisplayName");
                long likes = (long) snippet2.get("likeCount");
                long replies = (long) snippet.get("totalReplyCount");
                return new String[]{comment,author,String.valueOf(likes),String.valueOf(replies)};
            }
        }
        return null;
    }

    public boolean commentHandler(){
        if (getJSON().equalsIgnoreCase("error")) return false;

        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(getJSON());
            JSONArray array = (JSONArray) object.get("items");
            for (Object o : array){
                JSONObject init = (JSONObject) o;
                JSONObject snippet = (JSONObject) init.get("snippet");
                comments.add(snippet);
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
    public String getJSON(){
        try {
            YouTube youtube = getBuilder();
            YouTube.CommentThreads.List request = youtube.commentThreads().list(Collections.singletonList("snippet"));
            CommentThreadListResponse videoInfo = null;
            if (pageToken.equalsIgnoreCase("null")){
                videoInfo = request.setKey(Constants.API_KEY)
                        .setMaxResults(100L)
                        .setVideoId(videoID)
                        .execute();
            }
            else{
                videoInfo = request.setKey(Constants.API_KEY)
                        .setMaxResults(100L)
                        .setPageToken(pageToken)
                        .setVideoId(videoID)
                        .execute();
            }
            return videoInfo.toPrettyString();
        }
        catch (Exception e){
            return "error";
        }
    }
    public static YouTube getBuilder() throws GeneralSecurityException, IOException {
        NetHttpTransport http = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory json = JacksonFactory.getDefaultInstance();
        return new YouTube.Builder(http, json, null).setApplicationName("idk1").build();
    }

}
