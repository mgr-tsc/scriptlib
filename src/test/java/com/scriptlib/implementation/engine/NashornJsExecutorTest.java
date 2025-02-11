package com.scriptlib.implementation.engine;

import com.scriptlib.core.JsContext;
import com.scriptlib.core.JsScript;
import com.scriptlib.core.Script;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NashornJsExecutorTest {

    private SimpleNashornJsExecutor jsExecutor;

    @BeforeEach
    void setUp() {
        jsExecutor = new SimpleNashornJsExecutor();
    }

    @Test
    void executeScript_StringCode_NoContext() throws ScriptLibExecutionException {
        Object result = jsExecutor.executeScript("1 + 1");
        assertEquals(2, result);
    }

    @Test
    void executeScript_JsScript_NoContext() throws ScriptLibExecutionException {
        JsScript script = new Script("testScript", "Test Script", "(function() { return 'hello'; })();");
        Object result = jsExecutor.executeScript(script);
        assertEquals("hello", result);
    }

    @Test
    void executeScript_StringCode_WithContext() throws ScriptLibExecutionException {
        JsContext context = new JsContext();
        context.addBinding("greeting", "Hello from Java");
        Object result = jsExecutor.executeScript("greeting + ' in script';", context);
        assertEquals("Hello from Java in script", result);
    }

    @Test
    void executeScript_JsScript_WithContext() throws ScriptLibExecutionException {
        JsScript script = new Script("contextScript", "Context Script", "scriptContextValue + ' - from script';");
        JsContext context = new JsContext();
        context.addBinding("scriptContextValue", "Data passed");
        Object result = jsExecutor.executeScript(script, context);
        assertEquals("Data passed - from script", result);
    }

    @Test
    void executeScript_ErrorScript() {
        JsScript errorScript = new Script("errorScript", "Error Script", "syntax error here");
        assertThrows(ScriptLibExecutionException.class, () -> {
            jsExecutor.executeScript(errorScript);
        });
    }

    @Test
    void bindingsArePassedToScript() throws ScriptLibExecutionException {
        JsScript script = new Script("bindingTestScript", "Binding Test Script",
                "(function () {\n" +
                        "    if (javaBinding === 'Java Value') {\n" +
                        "        return 'Binding Success: ' + javaBinding;\n" +
                        "    } else {\n" +
                        "        return 'Binding Failed';\n" +
                        "    }\n" +
                        "})();");
        JsContext context = new JsContext();
        context.addBinding("javaBinding", "Java Value");
        Object result = jsExecutor.executeScript(script, context);
        assertEquals("Binding Success: Java Value", result);
    }

    @Test
    void bindingsWithJsonObjects() throws ScriptLibExecutionException {
        String jsonObjectInvoice = "{\n" +
                "    \"invoiceNumber\": \"INV-123\",\n" +
                "    \"totalAmount\": 100.0\n" +
                "}";
        JsScript script = new Script("complexBindingTestScript", "Complex Binding Test Script",
                "(function () {\n" +
                        "    if (complexObject.totalAmount > 99.00) {\n" +
                        "        var newAmount = complexObject.totalAmount - 25;\n" +
                        "        return 'modification success, new amount is: ' + newAmount \n" +
                        "    } else {\n" +
                        "        return 'Binding Failed';\n" +
                        "    }\n" +
                        "})();");
        JsContext context = new JsContext();
        context.addBinding("complexObject", jsonObjectInvoice);
        Object result = jsExecutor.executeScript(script, context);
        assertTrue(result.toString().contains("modification success, new amount is"));
    }

}