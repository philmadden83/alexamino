package com.mymo.flexa;

import com.mymo.flexa.intent.IntentHandlerConfigurer;
import com.mymo.flexa.intent.IntentHandler;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phil on 1/22/17.
 */
public class HandlerContext {
    private static final Map<String, IntentHandler> INTENT_HANDLERS = new HashMap<>();
    private static final IntentHandlerConfigurer INTENT_HANDLER_CONFIGURER = new IntentHandlerConfigurer();

    public static void init() {
        new Reflections("").getTypesAnnotatedWith(com.mymo.flexa.annotation.IntentHandler.class)
                .stream()
                .forEach(ih ->
                        INTENT_HANDLERS.put(ih.getDeclaredAnnotation(com.mymo.flexa.annotation.IntentHandler.class).value(), INTENT_HANDLER_CONFIGURER.configure(ih)));
    }

    public Set<String> getSupportedApplicationIds() {
        return null;
    }

    public IntentHandler getIntentHandler(String intentName) {
        return INTENT_HANDLERS.get(intentName);
    }

}
