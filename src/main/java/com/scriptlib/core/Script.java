package com.scriptlib.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Script implements JsScript {

    private final String scriptId;
    private final String scriptName;
    private final String scriptContent;
    private final Map<String, Object> metadata; // For future use

    public Script(String scriptId, String scriptName, String scriptContent, Map<String, Object> metadata) {
        this.scriptId = scriptId;
        this.scriptName = scriptName;
        this.scriptContent = scriptContent;
        this.metadata = metadata;
    }

    public Script(String scriptId, String scriptName, String scriptContent) {
        this.scriptId = scriptId;
        this.scriptName = scriptName;
        this.scriptContent = scriptContent;
        this.metadata = new HashMap<String, Object>();
    }


    @Override
    public String getJsCode() {
        return scriptContent;
    }

    @Override
    public String getScriptName() {
        return scriptName;
    }

    @Override
    public String getJsId() {
        return scriptId;
    }


    public String getScriptId() {
        return scriptId;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "Script{" +
                "scriptId='" + scriptId + '\'' +
                ", scriptName='" + scriptName + '\'' +
                ", scriptContent='" + scriptContent + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Script script = (Script) o;
        return scriptId.equals(script.scriptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scriptId, scriptName); // Use Objects.hash
    }
}
