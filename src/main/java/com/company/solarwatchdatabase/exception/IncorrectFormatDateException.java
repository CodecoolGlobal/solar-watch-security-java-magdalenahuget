package com.company.solarwatchdatabase.exception;

public class IncorrectFormatDateException extends RuntimeException {
    public IncorrectFormatDateException() {
        super("Incorrect date format.");
    }
}
