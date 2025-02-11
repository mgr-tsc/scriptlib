package com.scriptlib.core;

import com.scriptlib.core.exceptions.ScriptLibExecutionException;

public interface ScriptPreprocessor {
    String preprocess(String scriptContent) throws ScriptLibExecutionException;
}
