package com.mymo.flexa.speechlet;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.mymo.flexa.HandlerContext;

/**
 * Created by Phil on 1/23/17.
 */
//TODO: Wire in classpath scanning via spring. Aliases for annotations.
public class DefaultSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final HandlerContext SPEECHLET_CONTEXT = new HandlerContext();

    static {
        SPEECHLET_CONTEXT.init();
    }

    public DefaultSpeechletRequestStreamHandler() {
        super(new SpeechletDispatcher(SPEECHLET_CONTEXT), SPEECHLET_CONTEXT.getSupportedApplicationIds());
    }

}
