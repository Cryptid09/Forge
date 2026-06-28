package com.forge.devopstoolbox.application.service;

public class ToolNotFoundException extends RuntimeException {

    public ToolNotFoundException(String message) {
        super(message);
    }
}
