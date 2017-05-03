package com.mymo.alexamino.exception;

/**
 * Created by Phil Madden on 3/30/17.
 */
public class SpeechletConfigurationException extends RuntimeException {

    public SpeechletConfigurationException(String m) {
        super(m);
    }

    public SpeechletConfigurationException(String m, Throwable t) {
        super(m, t);
    }

}
