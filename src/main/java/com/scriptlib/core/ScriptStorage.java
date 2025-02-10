package com.scriptlib.core;

import com.scriptlib.core.exceptions.ScriptLibExecutionException;

public interface ScriptStorage {
    JsScript getScriptById(String scriptId) throws ScriptLibExecutionException;
    JsScript getScriptByName(String scriptName) throws ScriptLibExecutionException;
}
