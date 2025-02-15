package com.scriptlib.core;

import com.scriptlib.core.exceptions.ScriptLibExecutionException;

public interface JsContextAwareScriptPreprocessor<T extends JsContext> extends ScriptPreprocessor{
    String preprocess(String scriptContent, T context) throws ScriptLibExecutionException;
}
