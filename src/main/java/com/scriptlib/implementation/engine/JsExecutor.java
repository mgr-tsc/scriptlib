package com.scriptlib.implementation.engine;

import com.scriptlib.core.*;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class JsExecutor implements JsScriptExecutor {

    private final ScriptPreprocessor preProcessor;
    private final ScriptEngine engine;

    public JsExecutor() {
        this(new PreprocessingExecutor()); // Corrected typo: IifeScriptPreprocessor
    }

    public JsExecutor(ScriptPreprocessor preProcessor) {
        this.preProcessor = preProcessor;
        ScriptEngineManager factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("nashorn");
        if (this.engine == null) {
            throw new IllegalStateException("Nashorn JavaScript engine not found. Ensure you are using a Java version that includes Nashorn (Java 8-14, or include as module in later versions).");
        }
    }

    @Override
    public Object executeScript(JsScript script) throws ScriptLibExecutionException {
        return executeScript(script.getJsCode(), new JsContext()); // Delegate to String, Context version
    }

    @Override
    public Object executeScript(String scriptCode) throws ScriptLibExecutionException {
        return executeScript(scriptCode, new JsContext()); // Delegate to String, Context version
    }

    @Override
    public Object executeScript(JsScript script, JsContext context) throws ScriptLibExecutionException {
        return executeScript(script.getJsCode(), context); // Delegate to String, Context version
    }

    @Override
    public Object executeScript(String script, JsContext context) throws ScriptLibExecutionException { // Main implementation
        try {
            // Create a fresh context for each execution
            SimpleScriptContext scriptContext = new SimpleScriptContext();
            if (context != null && context.getBindings() != null) {
                scriptContext.getBindings(javax.script.ScriptContext.ENGINE_SCOPE).putAll(context.getBindings());
            }
            return engine.eval(this.preProcessor.preprocess(script), scriptContext); // Corrected to preprocessScript
        } catch (ScriptException e) {
            throw new ScriptLibExecutionException("Error executing JavaScript: " + e.getMessage(), e);
        }
    }
}