package com.mymo.alexamino;

import com.mymo.alexamino.intent.IntentHandlerConfigurer;
import com.mymo.alexamino.intent.IntentHandler;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phil Madden on 1/22/17.
 */
public class IntentHandlerContext {
    private static final Map<String, IntentHandler> INTENT_HANDLERS = new HashMap<>();
    private static final IntentHandlerConfigurer INTENT_HANDLER_CONFIGURER = new IntentHandlerConfigurer();

    public static void init() {
        new Reflections("").getTypesAnnotatedWith(com.mymo.alexamino.annotation.IntentHandler.class)
                .stream()
                .forEach(ih ->
                        INTENT_HANDLERS.put(ih.getDeclaredAnnotation(com.mymo.alexamino.annotation.IntentHandler.class).value(), INTENT_HANDLER_CONFIGURER.configure(ih)));
    }

    public Set<String> getSupportedApplicationIds() {
        //TODO: Configure
        return null;
    }

    public IntentHandler getIntentHandler(String intentName) {
        return INTENT_HANDLERS.get(intentName);
    }

}
