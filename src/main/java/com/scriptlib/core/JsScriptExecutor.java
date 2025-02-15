package com.scriptlib.core;

import com.scriptlib.core.exceptions.ScriptLibExecutionException;

import javax.script.ScriptEngine;

public interface JsScriptExecutor {
    boolean isContextAware();
    Object executeScript(JsScript script) throws ScriptLibExecutionException;
    Object executeScript(String script) throws ScriptLibExecutionException;
    Object executeScript(JsScript script, JsContext context) throws ScriptLibExecutionException;
    Object executeScript(String script, JsContext context) throws ScriptLibExecutionException;
}