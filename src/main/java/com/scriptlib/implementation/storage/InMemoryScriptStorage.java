package com.scriptlib.implementation.storage;

import com.scriptlib.core.JsScript;
import com.scriptlib.core.ScriptStorage;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import com.scriptlib.core.Script;
import java.util.HashMap;
import java.util.Map;

public class InMemoryScriptStorage implements ScriptStorage {

    private final Map<String, Script> scripts = new HashMap<>();

    public InMemoryScriptStorage() {
        //Some sample scripts for testing in memory
        scripts.put("script1", new Script("script1", "Sample Script 1", "console.log('Hello from script1!');"));
        scripts.put("script2", new Script("script2", "Sample Script 2", "function greet(name) { return 'Hello, ' + name + '!'; } greet('World');"));
        scripts.put("script3", new Script("script3", "Sample Script 3", "new Date().toString();"));
    }

    @Override
    public JsScript getScriptById(String scriptId) throws ScriptLibExecutionException {
        Script script = scripts.get(scriptId);
        if (script == null) {
            throw new ScriptLibExecutionException("Script with ID '" + scriptId + "' not found.");
        }
        return script; // Returning Script which implements JsScript
    }

    @Override
    public JsScript getScriptByName(String scriptName) throws ScriptLibExecutionException {
        // In this simple in-memory implementation, let's just search by name (not efficient for large storage)
        for (Script script : scripts.values()) {
            if (script.getScriptName().equals(scriptName)) {
                return script;
            }
        }
        throw new ScriptLibExecutionException("Script with name '" + scriptName + "' not found.");
    }
}
