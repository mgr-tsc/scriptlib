package com.scriptlib.implementation.engine;

import com.scriptlib.core.JsContext;
import com.scriptlib.core.JsScript;
import com.scriptlib.core.JsScriptExecutor;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class SimpleNashornJsExecutor implements JsScriptExecutor {

    private final ScriptEngine engine;

    public SimpleNashornJsExecutor() {
        ScriptEngineManager factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("nashorn");
        if (this.engine == null) {
            throw new IllegalStateException("Nashorn JavaScript engine not found. Ensure you are using a Java version that includes Nashorn (Java 8-14, or include as module in later versions).");
        }
    }

    @Override
    public boolean isContextAware() {
        return false;
    }

    @Override
    public Object executeScript(JsScript script) throws ScriptLibExecutionException {
        return executeScript(script, new JsContext()); // Execute with a default empty context
    }

    @Override
    public Object executeScript(String scriptCode) throws ScriptLibExecutionException {
        return executeScript(scriptCode, new JsContext()); // Execute with a default empty context
    }

    @Override
    public Object executeScript(JsScript jsScript, JsContext context) throws ScriptLibExecutionException {
        return executeScript(jsScript.getJsCode(), context);
    }


    @Override
    public Object executeScript(String scriptCode, JsContext context) throws ScriptLibExecutionException {
        try {
            // Create a fresh context for each execution
            SimpleScriptContext scriptContext = new SimpleScriptContext();
            if (context != null && context.getBindings() != null) {
                scriptContext.getBindings(javax.script.ScriptContext.ENGINE_SCOPE).putAll(context.getBindings());
            }
            return engine.eval(scriptCode, scriptContext);

        } catch (ScriptException e) {
            throw new ScriptLibExecutionException("Error executing JavaScript: " + e.getMessage(), e);
        }
    }

}