package com.mymo.flexa;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.mymo.flexa.annotation.IntentHandler;
import com.mymo.flexa.annotation.Slot;

/**
 * Created by Phil on 1/22/17.
 */
@IntentHandler("test-intent")
public class MyIntentHandler {


    public SpeechletResponse method1(@Slot("test-slot") String string1,
                                     Session session) {
        return null;
    }

    public SpeechletResponse method2(@Slot("test-slot") String string2,
                                     @Slot("test-other-slot") String string1,
                                     Session session) {
        return null;
    }

}
