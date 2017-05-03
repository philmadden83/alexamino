package com.mymo.alexamino.intent;

import com.mymo.alexamino.annotation.Utterance;
import com.mymo.alexamino.exception.SpeechletConfigurationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil Madden on 1/23/17.
 */
public class IntentHandlerConfigurer {

    public IntentHandler configure(Class<?> handler) {
        List<UtteranceHandlerContext> utteranceHandlerContexts = Arrays
                .stream(handler.getMethods())
                .filter(m -> m.isAnnotationPresent(Utterance.class))
                .map(hm -> new UtteranceHandlerContext(hm))
                .collect(Collectors.toList());

        if (utteranceHandlerContexts.isEmpty()) {
            throw new SpeechletConfigurationException("No Utterances defined within intent handler: " + handler.getCanonicalName());
        }

//        if (utteranceHandlerContexts.size() > 1) {
//            throw new SpeechletConfigurationException("Ambiguous Utterance method within intent handler: " + handler.getCanonicalName());
//        }

        try {
            Object instance = handler.newInstance();
            return new IntentHandler(instance, utteranceHandlerContexts);

        } catch (InstantiationException | IllegalAccessException e) {
            throw new SpeechletConfigurationException(e.getMessage(), e);
        }
    }

}
