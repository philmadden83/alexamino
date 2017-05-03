package com.mymo.alexamino.intent;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.util.CollectionUtils;
import com.mymo.alexamino.exception.IntentHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil Madden on 1/23/17.
 */
public class IntentHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentHandler.class);

    private final Object intentHandler;
    private final List<UtteranceHandlerContext> handlerMethods;

    protected IntentHandler(Object intentHandler, List<UtteranceHandlerContext> handlerMethods) {
        this.intentHandler = intentHandler;
        this.handlerMethods = handlerMethods;
    }

    public SpeechletResponse handleIntent(Collection<Slot> slots, Session session) {
        UtteranceHandlerContext utteranceHandler = getUtteranceHandler(slots);

        if (utteranceHandler == null) {
            throw new IntentHandlingException("No method defined to handle utterance.");
        }

        return handleUtterance(utteranceHandler, slots, session);
    }

    private SpeechletResponse handleUtterance(UtteranceHandlerContext utteranceHandlerContext, Collection<Slot> slots, Session session) {
        try {
            Method handlerMethod = utteranceHandlerContext.getHandlerMethod();
            return (SpeechletResponse) handlerMethod.invoke(intentHandler, utteranceHandlerContext.getParameterValueArray(slots, session));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UtteranceHandlerContext getUtteranceHandler(Collection<Slot> slots) {
        List<UtteranceHandlerContext> utteranceHandlerContexts =  handlerMethods.stream().filter(hm -> hm.canHandle(slots)).collect(Collectors.toList());
        UtteranceHandlerContext utteranceHandlerContext = null;

        if (!CollectionUtils.isNullOrEmpty(utteranceHandlerContexts) && utteranceHandlerContexts.size() == 1) {
            utteranceHandlerContext = utteranceHandlerContexts.get(0);
        } else if (!CollectionUtils.isNullOrEmpty(utteranceHandlerContexts) && utteranceHandlerContexts.size() > 1) {
            utteranceHandlerContext = refineToClosest(utteranceHandlerContexts);
        }

        return utteranceHandlerContext;
    }

    /**
    Multiple handler methods occur when filters have been applied to @Slots, one being a generic form of the other.

    i.e.
    public void method1(@Slot(dateFilter = DateFilter.ANY) Date d1);
     public void method2(@Slot(dateFilter = DateFilter.WEEK_OF_YEAR) Date d1);

    The refineToClosest method therefore tries to find the method defining the more specific filter.
    */
    private static UtteranceHandlerContext refineToClosest(List<UtteranceHandlerContext> utteranceHandlerContexts) {
        for (UtteranceHandlerContext configuration : utteranceHandlerContexts) {
            for (UtteranceHandlerContext.SlotContext slotContext : configuration.getDefinedSlots()) {
                if (slotContext.isDateTypeDefined() && slotContext.getDateFilter() != com.mymo.alexamino.annotation.Slot.DateFilter.ANY) {
                    return configuration;
                }
            }
        }
        return utteranceHandlerContexts.get(0);
    }

}
