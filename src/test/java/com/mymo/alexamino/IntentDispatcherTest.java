package com.mymo.alexamino;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.mymo.alexamino.intent.IntentHandler;
import com.mymo.alexamino.speechlet.SpeechletDispatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

import static  com.mymo.alexamino.TestIntentHandler.*;
/**
 * Created by Phil on 1/22/17.
 */
public class IntentDispatcherTest {


    private SpeechletDispatcher speechletDispatcher;

    @Mock
    private IntentRequest mockIntentRequest;
    @Mock
    private Session mockSession;
    @Mock
    private IntentHandler mockIntentHandler;

    private IntentHandlerContext spiedIntentHandlerContext;
    private Map<String, Slot> slots;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        IntentHandlerContext context = new IntentHandlerContext();
        context.init();

        spiedIntentHandlerContext = Mockito.spy(context);
        speechletDispatcher = new SpeechletDispatcher(spiedIntentHandlerContext);

        slots = new HashMap<>();
        slots.put(SLOT_1, Slot.builder().withName(SLOT_1).withValue("one").build());
        slots.put(SLOT_2, Slot.builder().withName(SLOT_2).withValue("two").build());

        when(spiedIntentHandlerContext.getIntentHandler("INTENT_NAME")).thenReturn(mockIntentHandler);
    }

    @Test
    public void testDate() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017-01-01").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testShortMonth() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Jan").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testShortMonthPlural() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Jan's").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testLongMonth() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("January").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testShortDay() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Mon").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testShortDayPlural() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Mon's").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testLongDay() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Monday").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testLongDayPlural() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("Monday's").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testLongDayPlurals() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("tuesdays").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testWeekOfYear() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017-W12").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testWeekendOfYear() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017-W12-WE").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testMonth() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017-12").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testYear() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testSeason() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("2017-SU").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }

    @Test
    public void testDecade() throws SpeechletException {
        slots.put(DATE_SLOT, Slot.builder().withName(DATE_SLOT).withValue("201X").build());

        when(mockIntentRequest.getIntent()).thenReturn(
                Intent.builder()
                        .withName(INTENT_NAME)
                        .withSlots(slots)
                        .build());

        speechletDispatcher.onIntent(mockIntentRequest, mockSession);
    }


}
