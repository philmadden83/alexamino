package com.mymo.flexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.mymo.flexa.speechlet.SpeechletDispatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
/**
 * Created by Phil on 1/22/17.
 */
public class IntentDispatcherTest {
    private HandlerContext context;
    private SpeechletDispatcher speechletDispatcher;

    @Mock
    private IntentRequest mockIntentRequest;
    @Mock
    private Session mockSession;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        context = new HandlerContext();
        context.init();
        speechletDispatcher = new SpeechletDispatcher(context);
    }

    @Test
    public void testCallMethod1() throws SpeechletException {
        Map<String, Slot> slots = new HashMap<>();
        slots.put("test-slot", Slot.builder().withName("test-slot").withValue("test-value").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName("test-intent")
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }


    @Test
    public void testCallMethod2() throws SpeechletException {
        Map<String, Slot> slots = new HashMap<>();
        slots.put("test-slot", Slot.builder().withName("test-slot").withValue("test-value").build());
        slots.put("test-other-slot", Slot.builder().withName("test-other-slot").withValue("test-other-value").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName("test-intent")
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }


}
