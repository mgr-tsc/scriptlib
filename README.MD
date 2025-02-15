# Java ScriptLib: Embeddable JavaScript Script Execution Engine

ScriptLib is a lightweight Java library designed to easily embed and execute JavaScript scripts within Java applications.  It provides a flexible and extensible framework for:

*   **Executing JavaScript code dynamically:** Run JavaScript scripts from strings, files, or databases within your Java application.
*   **Contextual Script Execution:** Pass data from Java to JavaScript scripts using bindings.
*   **Script Preprocessing:** Modify script code before execution using preprocessors (e.g., IIFE wrapping, JSON payload injection).
*   **Extensibility:** Easily extend the library with custom script preprocessors and executors.
*   **Lightweight and Dependency-Minimal:**  Designed to be a small and self-contained library with minimal external dependencies.

## Getting Started

**1. Basic Script Execution Example:**

```java
import com.scriptlib.core.JsScript;
import com.scriptlib.core.JsScriptExecutor;
import com.scriptlib.implementation.engine.JsExecutor; // Or your chosen executor
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import javax.script.ScriptException;

public class Example {
    public static void main(String[] args) throws ScriptException, ScriptLibExecutionException {
        JsScriptExecutor executor = new JsExecutor(); // Or your chosen executor
        JsScript script = new JsScript("hello", "Simple Hello Script", "'Hello from ScriptLib!'");
        Object result = executor.executeScript(script);
        System.out.println("Script Result: " + result); // Output: Script Result: Hello from ScriptLib!
    }
}

```

## Working with JSON Payloads
**2.Script Execution Example:**

ScriptLib provides the `JsonPayloadPreprocessor` to simplify working with JSON data in your JavaScript scripts.  This preprocessor automatically parses a "payload" binding (if provided as a JSON string in the `JsContext`) and makes it available as a `payload` JavaScript object within your script's scope.

```java
import com.scriptlib.core.*;
import com.scriptlib.implementation.engine.JsExecutor;
import com.scriptlib.implementation.engine.JsonPayloadPreprocessor; // Import JsonPayloadPreprocessor
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import javax.script.ScriptException;

public class JsonPayloadExample {
    public static void main(String[] args) throws ScriptException, ScriptLibExecutionException {
        JsonPayloadPreprocessor preprocessor = new JsonPayloadPreprocessor(); // Use JsonPayloadPreprocessor
        JsScriptExecutor executor = new JsExecutor(preprocessor);
        JsContext context = new JsContext();
        context.addBinding("payload", "{\"finalAmount\": 149.67, \"note\": \"credit\"}"); // Add JSON payload binding
        JsScript script = new JsScript("jsonTest", "Script with JSON payload",
                "function processPayload() { " +
                "  if (payload.finalAmount > 100 && payload.note === 'credit') { " +
                "    return 'Approved'; " +
                "  } else { " +
                "    return 'Rejected'; " +
                "  } " +
                "} " +
                "return processPayload();");

        Object result = executor.executeScript(script, context);
        System.out.println("Script Result: " + result); // Output: Script Result: Approved
    }
}
```