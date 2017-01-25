package com.mymo.flexa.intent;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by Phil Madden on 1/24/17.
 */
public class HandlerMethodConfiguration {
    private final Method method;
    private final ArgumentConfiguration argumentConfiguration;

    protected HandlerMethodConfiguration(Method method) {
        this.method = method;
        this.argumentConfiguration = new ArgumentConfiguration(method.getParameters());
    }

    public Method getHandlerMethod() {
        return method;
    }

    public boolean hasArguments(Collection<Slot> slots) {
        return slots.stream().allMatch(s -> argumentConfiguration.hasSlotArgument(s.getName()));
    }

    public Object[] getHandlerMethodArguments(Collection<Slot> slots, Session session) {
        Object[] args = new Object[slots.size() + 1];

        if (argumentConfiguration.hasSessionArgument()) {
            args[argumentConfiguration.getSessionArgumentIndex()] = session;
        }

        for (Slot slot : slots) {
            //TODO: Conversion of complext types.
            args[argumentConfiguration.getSlotArgumentIndex(slot.getName())] = slot.getValue();
        }

        return args;
    }

    private class ArgumentConfiguration {
        private Integer sessionArgumentIndex;
        private Map<String, Integer> slotIndexMap = new HashMap<>();

        private ArgumentConfiguration(Parameter[] parameters) {
            for (int pIdx = 0; pIdx < parameters.length; pIdx++) {
                if (parameters[pIdx].isAnnotationPresent(com.mymo.flexa.annotation.Slot.class)) {
                    slotIndexMap.put(parameters[pIdx].getAnnotation(com.mymo.flexa.annotation.Slot.class).value(), pIdx);
                } else if (parameters[pIdx].getType() == Session.class) {
                    sessionArgumentIndex = pIdx;
                }
            }
        }


        public boolean hasSessionArgument() {
            return sessionArgumentIndex != null;
        }

        public int getSessionArgumentIndex() {
            return sessionArgumentIndex;
        }

        public boolean hasSlotArgument(String slotName) {
            return slotIndexMap.containsKey(slotName);
        }

        public int getSlotArgumentIndex(String slotName) {
            return slotIndexMap.get(slotName);
        }
    }
}
