package com.scriptlib.implementation.preprocessors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.scriptlib.core.JsContext;
import com.scriptlib.core.JsContextAwareScriptPreprocessor;

/**
 * The JsonPayloadPreprocessor class implements the ScriptPreprocessor interface.
 * It preprocesses script content by wrapping it in an Immediately Invoked Function Expression (IIFE)
 * and optionally includes a 'payload' variable if provided in the context.
 * The 'payload' variable is expected to be a JSON string and is parsed into a JavaScript object.
 */
public class JsonPayloadPreprocessor implements JsContextAwareScriptPreprocessor<JsContext> {
    // Gson instance for JSON parsing
    private final Gson gson = new Gson();

    /**
     * Preprocesses the script content without any context.
     *
     * @param scriptContent The script content to preprocess.
     * @return The preprocessed script content.
     */
    @Override
    public String preprocess(String scriptContent) {
        return scriptContent;
    }

    /**
     * Preprocesses the script content with the provided context.
     * Wraps the script content in an IIFE and includes a 'payload' variable if provided in the context.
     *
     * @param scriptContent The script content to preprocess.
     * @param context The context containing bindings for the script.
     * @return The preprocessed script content.
     */
    @Override
    public String preprocess(String scriptContent, JsContext context) {
        StringBuilder preprocessedScript = new StringBuilder();
        preprocessedScript.append("(function() {\n"); // Start with IIFE wrapping

        // Check if context and 'payload' binding are provided and valid
        if (context != null && context.getBindings() != null && context.getBinding("payload") instanceof String) {
            String payloadJsonString = (String) context.getBinding("payload");
            try {
                // Try to parse 'payload' binding as JSON
                Object parsedPayload = gson.fromJson(payloadJsonString, Object.class);
                // Make the parsed 'payload' object available in the script scope
                preprocessedScript.append("  var payload = ");
                preprocessedScript.append(gson.toJson(parsedPayload)); // Convert back to JSON string for JS
                preprocessedScript.append(";\n");
            } catch (JsonParseException e) {
                // Handle invalid JSON in 'payload' binding
                System.err.println("Warning: Invalid JSON in 'payload' binding. Binding will not be parsed: " + e.getMessage());
                preprocessedScript.append("  var payload = null;\n"); // Define payload as null if parsing fails
            }
        } else {
            // Define payload as null if not provided or not a string
            preprocessedScript.append("  var payload = null;\n");
        }
        preprocessedScript.append(scriptContent).append("\n})();"); // Close IIFE wrapping
        return preprocessedScript.toString();
    }

}