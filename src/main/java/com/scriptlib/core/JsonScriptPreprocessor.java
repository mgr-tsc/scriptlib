package com.scriptlib.core;

import com.scriptlib.core.JsContext;
import com.scriptlib.core.ScriptPreprocessor;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import com.scriptlib.core.JsScript; // Import JsScript to potentially access context info if needed

import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonScriptPreprocessor implements ScriptPreprocessor {

    private final Gson gson = new Gson(); // Or create once and reuse

    @Override
    public String preprocess(String scriptCode) throws ScriptLibExecutionException {
        // For this simple version, we'll just assume ALL string bindings should be parsed if they are valid JSON.
        // In a more advanced version, you might want to configure which bindings to parse (e.g., by name, or a flag in JsContext)

        // For now, the JSON parsing happens inside the script itself (as we did in the test fix).
        // We are not PRE-processing bindings *outside* the script in Java in this version.
        // The parsing logic will be part of the *script* that gets executed.

        // No code transformation needed in this preprocessor itself for *this* approach.
        // The parsing is handled *within* the script using JSON.parse()
        return scriptCode;

    }


    // Alternative approach (more complex preprocessor - not doing this now, but for future consideration):
    // We *could* pre-parse JSON in Java and pass Java objects as bindings directly.
    // But for now, let's stick with the simpler approach of parsing JSON strings *inside* the script using JSON.parse().

    // Example of a more complex approach (NOT IMPLEMENTING NOW):
    // @Override
    // public String preprocessScript(String scriptCode, JsContext context) throws ScriptLibExecutionException {
    //     if (context != null && context.getBindings() != null) {
    //         Map<String, Object> bindings = context.getBindings();
    //         StringBuilder preprocessedScript = new StringBuilder();
    //         preprocessedScript.append("(function() {\n"); // Still wrap in IIFE

    //         for (Map.Entry<String, Object> entry : bindings.entrySet()) {
    //             String bindingName = entry.getKey();
    //             Object bindingValue = entry.getValue();

    //             if (bindingValue instanceof String) {
    //                 String stringValue = (String) bindingValue;
    //                 try {
    //                     // Try to parse as JSON in Java preprocessor
    //                     Object parsedJson = gson.fromJson(stringValue, Object.class);
    //                     // Generate JavaScript code to make the parsed object available
    //                     preprocessedScript.append("  var ").append(bindingName).append(" = JSON.parse('").append(escapeJsonString(stringValue)).append("');\n"); // Or stringify parsedJson back to JSON string?
    //                 } catch (JsonParseException e) {
    //                     // If not valid JSON, treat as a regular string binding (no parsing in preprocessor)
    //                     preprocessedScript.append("  var ").append(bindingName).append(" = '").append(escapeJsString(stringValue)).append("';\n"); // Escape for JS string
    //                 }
    //             } else {
    //                 // For non-string bindings, just make them available as is (using existing binding mechanism in executor)
    //             }
    //         }
    //         preprocessedScript.append(scriptCode).append("\n})();");
    //         return preprocessedScript.toString();


    //     } else { // No context or bindings, just wrap in IIFE
    //         return "(function() { \n" + scriptCode + "\n })();";
    //     }
    // }

    // // Helper method for escaping JSON string for embedding in JavaScript string (basic example, might need more robust escaping)
    // Basic escaping - needs thorough review for security
    private String escapeJsonString(String jsonString) {
         return jsonString.replace("\\", "\\\\").replace("'", "\\'");
    }

    // Helper method for escaping general string for embedding in JavaScript string
    // Basic escaping - needs thorough review for security
    private String escapeJsString(String str) {
         return str.replace("\\", "\\\\").replace("'", "\\'");
    }
}