package com.company.solarwatch.exception;

public class IncorrectFormatDateException extends RuntimeException {
    public IncorrectFormatDateException() {
        super("Incorrect date format.");
    }
}
