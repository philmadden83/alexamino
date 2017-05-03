package com.mymo.alexamino.intent;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazonaws.util.StringUtils;
import com.mymo.alexamino.intent.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Phil Madden on 1/24/17.
 */
public class UtteranceHandlerContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtteranceHandlerContext.class);

    private final Method method;
    private final UtteranceMethodParameterContext utteranceMethodParameterContext;

    protected UtteranceHandlerContext(Method method) {
        this.method = method;

        utteranceMethodParameterContext = new UtteranceMethodParameterContext();
        utteranceMethodParameterContext.init(method.getParameters());
    }

    public Method getHandlerMethod() {
        return method;
    }

    public boolean canHandle(Collection<Slot> slots) {
        if (getDefinedSlots().size() != slots.size()) {
            return false;
        }
        return slots.stream().allMatch(s -> utteranceMethodParameterContext.hasParameterDefinedForSlot(s));
    }

    public Object[] getParameterValueArray(Collection<Slot> slots, Session session) {
        Object[] args = new Object[utteranceMethodParameterContext.getParameterCount()];

        if (utteranceMethodParameterContext.hasSessionArgument()) {
            args[utteranceMethodParameterContext.getSessionArgumentIndex()] = session;
        }

        SlotContext slotContext;
        for (Slot slot : slots) {
            slotContext = utteranceMethodParameterContext.getArgumentDetailsBySlotName(slot.getName());
            if (slotContext.getType().isAssignableFrom(Date.class)) {
                args[slotContext.getOrdinal()] = DateUtil.parseUtteranceDate(slot.getValue());
            } else {
                args[slotContext.getOrdinal()] = slot.getValue();
            }
        }
        return args;
    }

    public List<SlotContext> getDefinedSlots() {
        return new ArrayList<SlotContext>() {{ addAll(utteranceMethodParameterContext.slotContextMap.values()); }};
    }

    private class UtteranceMethodParameterContext {
        private Integer sessionArgumentIndex;
        private Map<String, SlotContext> slotContextMap = new TreeMap<>();

        private UtteranceMethodParameterContext() { }

        public void init(Parameter[] parameters) {
            Parameter parameter;
            com.mymo.alexamino.annotation.Slot slot;
            for (int pIdx = 0; pIdx < parameters.length; pIdx++) {
                parameter = parameters[pIdx];
                if (parameter.isAnnotationPresent(com.mymo.alexamino.annotation.Slot.class)) {
                    slot = parameter.getAnnotation(com.mymo.alexamino.annotation.Slot.class);
                    slotContextMap.put(slot.value(), new SlotContext(pIdx, parameter.getType(),  parameter.getName(), slot.pattern(), slot.dateFilter()));
                } else if (parameter.getType() == Session.class) {
                    sessionArgumentIndex = pIdx;
                }
            }
        }

        public boolean hasSessionArgument() {
            return sessionArgumentIndex != null;
        }

        public int getParameterCount() {
            return slotContextMap.size() + (hasSessionArgument() ? 1 : 0);
        }

        public int getSessionArgumentIndex() {
            return sessionArgumentIndex;
        }

        public boolean hasParameterDefinedForSlot(Slot slot) {
            if (slotContextMap.containsKey(slot.getName())) {
                return matchesFiltersAndPatterns(slotContextMap.get(slot.getName()), slot.getValue());
            }
            return false;
        }

        private boolean matchesFiltersAndPatterns(SlotContext slotContext, String slotValue) {
            if (hasDateTypeFilter(slotContext)) {
                return slotContext.getDateFilter().matches(slotValue);
            } else if (slotContext.isPatternDefined()) {
                return Pattern.matches(slotContext.getPattern(), slotValue);
            } else {
                return true;
            }
        }

        private boolean hasDateTypeFilter(SlotContext slotContext) {
            return slotContext.isDateTypeDefined() && slotContext.getType() == Date.class && slotContext.getDateFilter() != slotContext.getDateFilter().ANY;
        }

        public SlotContext getArgumentDetailsBySlotName(String name) {
            return slotContextMap.get(name);
        }
    }

    protected class SlotContext {
        private final int ordinal;
        private final Class type;
        private final String name;
        private final String pattern;
        private final com.mymo.alexamino.annotation.Slot.DateFilter dateFilter;

        public SlotContext(int ordinal, Class type, String name, String pattern,
                           com.mymo.alexamino.annotation.Slot.DateFilter dateFilter) {
            this.ordinal = ordinal;
            this.type = type;
            this.name = name;
            this.pattern = pattern;
            this.dateFilter = dateFilter;

            if (isDateTypeDefined() && type != Date.class) {
                LOGGER.warn(String.format("Ignoring Date Filter on field %s. Field is not a Date.", name));
            }
        }

        public int getOrdinal() {
            return ordinal;
        }

        public Class getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public boolean isPatternDefined() {
            return !StringUtils.isNullOrEmpty(pattern);
        }

        public boolean isDateTypeDefined() {
            return dateFilter != null;
        }

        public String getPattern() {
            return pattern;
        }

        public com.mymo.alexamino.annotation.Slot.DateFilter getDateFilter() {
            return dateFilter;
        }
    }
}
