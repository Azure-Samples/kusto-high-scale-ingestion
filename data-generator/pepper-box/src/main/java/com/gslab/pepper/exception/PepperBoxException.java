package com.gslab.pepper.exception;


public class PepperBoxException extends Exception {

    public PepperBoxException(String message) {
        super(message);
    }

    public PepperBoxException(Exception exc) {
        super(exc);
    }

}