package com.mymo.flexa.intent;

import com.amazon.speech.speechlet.SpeechletResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 1/23/17.
 */
public class IntentHandlerConfigurer {

    /**
     * This method will
     * @param handler
     * @return
     */
    public IntentHandler configure(Class<?> handler) {
        List<HandlerMethodConfiguration> handlerMethodConfigurations = Arrays
                .stream(handler.getMethods())
                .filter(m -> m.getReturnType() == SpeechletResponse.class)
                .map(hm -> new HandlerMethodConfiguration(hm))
                .collect(Collectors.toList());

        if (handlerMethodConfigurations.isEmpty()) {
            //TODO: throw execption
        }

        if (handlerMethodConfigurations.size() > 1) {
            //TODO: throw exception?
        }

        try {
            Object instance = handler.newInstance();
            return new IntentHandler(instance, handlerMethodConfigurations);

        } catch (InstantiationException | IllegalAccessException e) {
            //TODO: Throw exception
            e.printStackTrace();
        }

        return null;
    }

}
