package com.mymo.alexamino;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.mymo.alexamino.annotation.IntentHandler;
import com.mymo.alexamino.annotation.Slot;
import com.mymo.alexamino.annotation.Utterance;

import java.util.Date;
import static com.mymo.alexamino.annotation.Slot.DateFilter.*;

/**
 * Created by Phil on 1/22/17.
 */
@IntentHandler(TestIntentHandler.INTENT_NAME)
public class TestIntentHandler {
    protected static final String INTENT_NAME   = "test-intent";
    protected static final String SLOT_1        = "slot_1";
    protected static final String SLOT_2        = "slot-2";
    protected static final String DATE_SLOT     = "date";

    @Utterance
    public SpeechletResponse any(@Slot(SLOT_1) String string1,
                                 @Slot(SLOT_2) String string2,
                                 @Slot(DATE_SLOT) Date date,
                                 Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse date(@Slot(SLOT_1) String string1,
                                  @Slot(SLOT_2) String string2,
                                  @Slot(value =DATE_SLOT, dateFilter = DATE) Date date,
                                  Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse weekend(@Slot(SLOT_1) String string1,
                                     @Slot(SLOT_2) String string2,
                                     @Slot(value =DATE_SLOT, dateFilter = WEEKEND) Date date,
                                     Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse weekOfYear(@Slot(SLOT_1) String string1,
                                        @Slot(SLOT_2) String string2,
                                        @Slot(value =DATE_SLOT, dateFilter = WEEK_OF_YEAR) Date date,
                                        Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse month(@Slot(SLOT_1) String string1,
                                   @Slot(SLOT_2) String string2,
                                   @Slot(value =DATE_SLOT, dateFilter = MONTH) Date date,
                                   Session session) {
        return null;
    }


    @Utterance
    public SpeechletResponse year(@Slot(SLOT_1) String string1,
                                  @Slot(SLOT_2) String string2,
                                  @Slot(value =DATE_SLOT, dateFilter = YEAR) Date date,
                                  Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse season(@Slot(SLOT_1) String string1,
                                    @Slot(SLOT_2) String string2,
                                    @Slot(value =DATE_SLOT, dateFilter = SEASON) Date date,
                                    Session session) {
        return null;
    }

    @Utterance
    public SpeechletResponse decade(@Slot(SLOT_1) String string1,
                                    @Slot(SLOT_2) String string2,
                                    @Slot(value =DATE_SLOT, dateFilter = DECADE) Date date,
                                    Session session) {
        return null;
    }

}
