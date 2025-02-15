package com.scriptlib.implementation.engine;


import com.scriptlib.core.*;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import com.scriptlib.implementation.preprocessors.JsonPayloadPreprocessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;

import static org.junit.Assert.*;

public class JsExecutorTest {
    private JsExecutor jsExecutor;

    @BeforeEach
    void setUp() throws ScriptException {
        jsExecutor = new JsExecutor(); // Using default constructor (with IIFE Preprocessor)
    }

    @Test
    void executeScript_StringCode_NoContext_IIFEWrapped() throws ScriptLibExecutionException {
        Object result = jsExecutor.executeScript("return 'iife test';"); // Script without explicit IIFE, should be wrapped
        Assertions.assertEquals("iife test", result);
    }

    @Test
    void executeScript_JsScript_NoContext_IIFEWrapped() throws ScriptLibExecutionException {
        JsScript script = new Script("testScriptIIFE", "Test Script IIFE", "return 'iife script test';"); // Script without explicit IIFE
        Object result = jsExecutor.executeScript(script);
        Assertions.assertEquals("iife script test", result);
    }

    @Test
    void executeScript_StringCode_WithContext_IIFEWrapped() throws ScriptLibExecutionException {
        JsContext context = new JsContext();
        context.addBinding("greeting", "Hello from Java Context");
        Object result = jsExecutor.executeScript("return greeting + ' in IIFE wrapped script';", context);
        Assertions.assertEquals("Hello from Java Context in IIFE wrapped script", result);
    }

    @Test
    void executeScript_JsScript_WithContext_IIFEWrapped() throws ScriptLibExecutionException {
        JsScript script = new Script("contextScriptIIFE", "Context Script IIFE", "return contextValue + ' - in IIFE script';");
        JsContext context = new JsContext();
        context.addBinding("contextValue", "Context Data IIFE");
        Object result = jsExecutor.executeScript(script, context);
        Assertions.assertEquals("Context Data IIFE - in IIFE script", result);
    }

    @Test
    void executeScript_ErrorScript_IIFEWrapped() {
        JsScript errorScript = new Script("errorScriptIIFE", "Error Script IIFE", "syntax error in iife script");
        assertThrows(ScriptLibExecutionException.class, () -> {
            jsExecutor.executeScript(errorScript);
        });
    }

    @Test
    void bindingsArePassedToScript_IIFEWrapped() throws ScriptLibExecutionException {
        JsScript script = new Script("bindingTestScriptIIFE", "Binding Test Script IIFE",
                "if (bindingValue === 'Binding Value IIFE') { return 'IIFE Binding Success: ' + bindingValue; } else { return 'IIFE Binding Failed'; }");
        JsContext context = new JsContext();
        context.addBinding("bindingValue", "Binding Value IIFE");
        Object result = jsExecutor.executeScript(script, context);
        Assertions.assertEquals("IIFE Binding Success: Binding Value IIFE", result);
    }

    @Test
    void executeScript_WithCustomPreprocessor_NoIIFEWrapping() throws ScriptLibExecutionException, ScriptException {
        // Create a No-Op preprocessor
        ScriptPreprocessor noOpPreprocessor = scriptCode -> scriptCode;

        // Create SimpleIIFEExecutor with the No-Op preprocessor
        JsExecutor executorWithNoOp = new JsExecutor(noOpPreprocessor);

        JsScript script = new Script("noIIFEPreprocessScript", "No IIFE Preprocess Script", " 'no iife test'; "); // Script is NOT wrapped by No-Op

        Object result = executorWithNoOp.executeScript(script);
        Assertions.assertEquals("no iife test", result); // Should execute without IIFE wrapping because of custom preprocessor
    }

    @Test
    void executeScript_WithCustomPreprocessor_ModifiedScript() throws ScriptLibExecutionException, ScriptException {
        // Create a preprocessor that adds a comment to the script
        ScriptPreprocessor commentPreprocessor = scriptCode -> "// Script was preprocessed!\n" + scriptCode;

        // Create SimpleIIFEExecutor with the comment preprocessor
        JsScriptExecutor executorWithCommentPreprocessor = new JsExecutor(commentPreprocessor);

        JsScript script = new Script("commentPreprocessScript", "Comment Preprocess Script", " 'comment test'; ");

        Object result = executorWithCommentPreprocessor.executeScript(script);
        Assertions.assertEquals("comment test", result); // Should still execute, comment is added but doesn't affect result
    }

    @Test
    void executeScriptFunction_WithPreprocessor() throws ScriptLibExecutionException, ScriptException {
        // Create SimpleIIFEExecutor with the comment preprocessor
        JsScriptExecutor executor = new JsExecutor();
        JsScript script = new Script("test", "Script with function call", "function testFunction() { return new Date().toString(); } return testFunction();");
        Object result = executor.executeScript(script);
        System.out.println("Result: " + result);
        Assertions.assertTrue(result != null); // Should still execute, comment is added but doesn't affect result
    }

    @Test
    void executeScriptFunction_WithPayloadBinding() throws ScriptLibExecutionException, ScriptException {
        // Create SimpleIIFEExecutor with the comment preprocessor
        JsonPayloadPreprocessor preprocessor = new JsonPayloadPreprocessor();
        JsScriptExecutor executor = new JsExecutor(preprocessor);
        JsContext context = new JsContext();
        context.addBinding("payload", "{\"finalAmount\": 149.67, \"note\": \"credit\"}");
        JsScript script = new Script("test", "Script with function call", "function testFunction() { " +
                "if (payload.finalAmount > 0 && payload.note === 'credit')" +
                " { print('testFunction returning true'); return true;}" + // Log before return true
                " print('testFunction returning false'); return false;" +  // Log before return false
                "} " +
                "return testFunction();");
        String preprocessedCode; // Capture preprocessed code

        // Check if preprocessor supports context-aware preprocessing
        preprocessedCode = ((JsonPayloadPreprocessor) preprocessor).preprocess(script.getJsCode(), context); // Call preprocess with context
        System.out.println("\nPreprocessed Script Code:\n--------------------\n" + preprocessedCode + "\n--------------------\n"); // Print preprocessed code
        Object result = executor.executeScript(script, context);
        System.out.println("Result: " + result);
        Assertions.assertTrue((Boolean) result);
    }
}
