package com.mymo.alexamino.speechlet;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazonaws.util.StringUtils;

import com.mymo.alexamino.IntentHandlerContext;
import com.mymo.alexamino.intent.IntentHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 1/23/17.
 */
public class SpeechletDispatcher implements Speechlet {

    //TODO: Look at context to forward request, setup sessions etc
    private final IntentHandlerContext intentHandlerContext;

    public SpeechletDispatcher(IntentHandlerContext handlerContext) {
        this.intentHandlerContext = handlerContext;
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return null;
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        IntentHandler handler = intentHandlerContext.getIntentHandler(request.getIntent().getName());

        if (handler == null) {
            throw new SpeechletException("No handler specified defined.");
        }

        Map<String, Slot> slotMap = request.getIntent().getSlots();
        removeUndefinedSlots(slotMap);

        return handler.handleIntent(removeUndefinedSlots(request.getIntent().getSlots()).values(), session);
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }

    private static Map<String, Slot> removeUndefinedSlots(Map<String, Slot> slotMap) {
        return slotMap
                .entrySet().stream()
                .filter(e -> !StringUtils.isNullOrEmpty(e.getValue().getValue()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
}
