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
    private boolean isContextAware = false;

    // Initialization block to define console and setup engine
    {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("nashorn");
        if (this.engine == null) {
            throw new IllegalStateException("Nashorn JavaScript engine not found...");
        }

        // Bind your custom logger instance to the engine context
        //engine.put("javaConsole", new JavaConsole());
        // Define console in initialization block - COMMON TO ALL CONSTRUCTORS
//            engine.eval(
//                    "if (typeof console === 'undefined') { " +
//                            "   console = { " +
//                            "       log: function(msg) { javaConsole.log(String(msg)); }, " +
//                            "       warn: function(msg) { javaConsole.warn(String(msg)); }, " +
//                            "       error: function(msg) { javaConsole.error(String(msg)); } " +
//                            "   }; " +
//                            "}"
//            );
    }

    public JsExecutor() throws ScriptException {
        this(new IIEFPreprocessingExecutor());
    }

    public JsExecutor(ScriptPreprocessor preProcessor) {
        this.preProcessor = preProcessor;
    }


    public JsExecutor(JsContextAwareScriptPreprocessor<JsContext> preProcessor) throws ScriptException {
        this.preProcessor = preProcessor;
        isContextAware = true;
    }

    @Override
    public boolean isContextAware() {
        return isContextAware;
    }

    @Override
    public Object executeScript(JsScript script) throws ScriptLibExecutionException {
        return executeScript(script.getJsCode(), new JsContext());
    }

    @Override
    public Object executeScript(String scriptCode) throws ScriptLibExecutionException {
        return executeScript(scriptCode, new JsContext());
    }

    @Override
    public Object executeScript(JsScript script, JsContext context) throws ScriptLibExecutionException {
        return executeScript(script.getJsCode(), context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object executeScript(String script, JsContext context) throws ScriptLibExecutionException {
        try {
            // Create a fresh context for each execution
            SimpleScriptContext scriptContext = new SimpleScriptContext();
            if (context != null && context.getBindings() != null) {
                scriptContext.getBindings(javax.script.ScriptContext.ENGINE_SCOPE).putAll(context.getBindings());
            }
            String preprocessScript;
            // Check if preprocessor supports context-aware preprocessing
            if (isContextAware) {
                if (this.preProcessor instanceof JsContextAwareScriptPreprocessor) {
                    preprocessScript = ((JsContextAwareScriptPreprocessor<JsContext>) this.preProcessor).preprocess(script, context);
                } else {
                    throw new ScriptLibExecutionException("Preprocessor is not context-aware");
                }
            } else {
                preprocessScript = this.preProcessor.preprocess(script);
            }
            // Execute script with preprocessor
            return engine.eval(preprocessScript, scriptContext);
        } catch (ScriptException e) {
            throw new ScriptLibExecutionException("Error executing JavaScript: " + e.getMessage(), e);
        }
    }

}