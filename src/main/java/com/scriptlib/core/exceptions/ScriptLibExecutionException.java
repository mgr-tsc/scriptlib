package com.scriptlib.core.exceptions;

public class ScriptLibExecutionException extends Exception {
    public ScriptLibExecutionException(String message) {
        super(message);
    }

    public ScriptLibExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
