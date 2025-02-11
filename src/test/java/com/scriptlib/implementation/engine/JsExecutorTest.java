package com.scriptlib.implementation.engine;


import com.scriptlib.core.*;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;

public class JsExecutorTest {
    private JsExecutor jsExecutor;

    @BeforeEach
    void setUp() {
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
    void executeScript_WithCustomPreprocessor_NoIIFEWrapping() throws ScriptLibExecutionException {
        // Create a No-Op preprocessor
        ScriptPreprocessor noOpPreprocessor = scriptCode -> scriptCode;

        // Create SimpleIIFEExecutor with the No-Op preprocessor
        JsExecutor executorWithNoOp = new JsExecutor(noOpPreprocessor);

        JsScript script = new Script("noIIFEPreprocessScript", "No IIFE Preprocess Script", " 'no iife test'; "); // Script is NOT wrapped by No-Op

        Object result = executorWithNoOp.executeScript(script);
        Assertions.assertEquals("no iife test", result); // Should execute without IIFE wrapping because of custom preprocessor
    }

    @Test
    void executeScript_WithCustomPreprocessor_ModifiedScript() throws ScriptLibExecutionException {
        // Create a preprocessor that adds a comment to the script
        ScriptPreprocessor commentPreprocessor = scriptCode -> "// Script was preprocessed!\n" + scriptCode;

        // Create SimpleIIFEExecutor with the comment preprocessor
        JsScriptExecutor executorWithCommentPreprocessor = new JsExecutor(commentPreprocessor);

        JsScript script = new Script("commentPreprocessScript", "Comment Preprocess Script", " 'comment test'; ");

        Object result = executorWithCommentPreprocessor.executeScript(script);
        Assertions.assertEquals("comment test", result); // Should still execute, comment is added but doesn't affect result
    }
}
