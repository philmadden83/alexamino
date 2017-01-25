package com.mymo.flexa.speechlet;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazonaws.util.StringUtils;

import com.mymo.flexa.HandlerContext;
import com.mymo.flexa.intent.IntentHandler;

import java.util.Map;
/**
 * Created by Phil on 1/23/17.
 */
public class SpeechletDispatcher implements Speechlet {

    //TODO: Look at context to forward request, setup sessions etc
    private final HandlerContext handlerContext;

    public SpeechletDispatcher(HandlerContext handlerContext) {
        this.handlerContext = handlerContext;
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
        IntentHandler handler = handlerContext.getIntentHandler(request.getIntent().getName());

        if (handler == null) {
            throw new SpeechletException("No handler specified defined.");
        }

        Map<String, Slot> slotMap = request.getIntent().getSlots();
        removeUndefinedSlots(slotMap);

        return handler.handleIntent(slotMap.values(), session);
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }

    private static void removeUndefinedSlots(Map<String, Slot> slotMap) {
        slotMap.keySet().stream()
                .filter(k -> StringUtils.isNullOrEmpty(slotMap.get(k).getValue()))
                .forEach(s -> slotMap.remove(s));
    }
}
