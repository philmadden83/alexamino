package com.mymo.flexa.intent;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Phil on 1/23/17.
 */
public class IntentHandler {
    private final Object handler;
    private final List<HandlerMethodConfiguration> handlerMethods;

    protected IntentHandler(Object handler, List<HandlerMethodConfiguration> handlerMethods) {
        this.handler = handler;
        this.handlerMethods = handlerMethods;
    }

    public SpeechletResponse handleIntent(Collection<Slot> slots, Session session) {
        Optional<HandlerMethodConfiguration> methodConfiguration = getHandlerMethod(slots);
        if (methodConfiguration.isPresent()) {
            return  invokeHandlerMethod(methodConfiguration.get(), slots, session);
        }
        return null;
    }

    private SpeechletResponse invokeHandlerMethod(HandlerMethodConfiguration handlerMethodConfiguration, Collection<Slot> slots, Session session) {
        try {
            Method handlerMethod = handlerMethodConfiguration.getHandlerMethod();
            return (SpeechletResponse) handlerMethod.invoke(handler, handlerMethodConfiguration.getHandlerMethodArguments(slots, session));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Optional<HandlerMethodConfiguration> getHandlerMethod(Collection<Slot> slots) {
        return handlerMethods.stream().filter(hm -> hm.hasArguments(slots)).findFirst();
    }

}
