package com.kbetkowski.uptimemonitor.service.exception;

public class SiteNotFoundException extends RuntimeException {
    public SiteNotFoundException(String message) {
        super(message);
    }
}
