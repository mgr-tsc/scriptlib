package com.scriptlib.implementation.storage;

import com.scriptlib.core.JsScript;
import com.scriptlib.core.exceptions.ScriptLibExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryScriptStorageTest {

    private InMemoryScriptStorage scriptStorage;

    @BeforeEach
    void setUp() {
        scriptStorage = new InMemoryScriptStorage();
    }

    @Test
    void getScriptById_ExistingScript() throws ScriptLibExecutionException {
        JsScript script = scriptStorage.getScriptById("script1");
        assertNotNull(script);
        assertEquals("script1", script.getJsId());
        assertEquals("Sample Script 1", script.getScriptName());
        assertTrue(script.getJsCode().contains("console.log('Hello from script1!');"));
    }

    @Test
    void getScriptById_NonExistingScript() {
        assertThrows(ScriptLibExecutionException.class, () -> {
            scriptStorage.getScriptById("nonExistentScriptId");
        });
    }

    @Test
    void getScriptByName_ExistingScript() throws ScriptLibExecutionException {
        JsScript script = scriptStorage.getScriptByName("Sample Script 2");
        assertNotNull(script);
        assertEquals("script2", script.getJsId());
        assertEquals("Sample Script 2", script.getScriptName());
        assertTrue(script.getJsCode().contains("function greet(name) { return 'Hello, ' + name + '!'; }"));
    }

    @Test
    void getScriptByName_NonExistingScript() {
        assertThrows(ScriptLibExecutionException.class, () -> {
            scriptStorage.getScriptByName("NonExistentScriptName");
        });
    }

    @Test
    void getScriptById_Script3() throws ScriptLibExecutionException {
        JsScript script = scriptStorage.getScriptById("script3");
        assertNotNull(script);
        assertEquals("script3", script.getJsId());
        assertEquals("Sample Script 3", script.getScriptName());
        assertTrue(script.getJsCode().contains("new Date().toString();"));
    }
}