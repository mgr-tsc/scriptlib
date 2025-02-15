package com.scriptlib.core;

public class JavaConsole {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }
}