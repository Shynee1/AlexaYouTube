package com.Shynee;

import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import static com.amazon.ask.request.Predicates.intentName;
public class CommentIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("CommentIntent"));
    }
    public Optional<Response> handle(HandlerInput input) {
        RandomShyneeVideo shyneeVideo = new RandomShyneeVideo();
        String videoID = shyneeVideo.getShyneeVideo();

        if (videoID != null){
            RandomComment randomComment = new RandomComment();
            String[] details = randomComment.getRandomComment(videoID);

            if (details != null){
                return input.getResponseBuilder()
                        .withSpeech(String.format("Here is a random comment from %s. %s. It has %s likes and %s replies", details[1], details[0], details[2], details[3]))
                        .build();
            }
        }
        return input.getResponseBuilder()
                .withSpeech("Something went wrong, please try again.")
                .build();
    }
}
