package com.Shynee;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class AlexaSkillStreamHandler extends SkillStreamHandler {

    public AlexaSkillStreamHandler() {
        super(Skills.standard()
                .addRequestHandler(new CommentIntentHandler())
                .addRequestHandler(new LaunchRequestHandler())
                .build());
    }
}