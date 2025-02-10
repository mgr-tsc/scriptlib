package com.scriptlib.core;


import java.util.HashMap;
import java.util.Map;

public class JsContext {
    private final String guid;
    private final Map<String, Object> bindings;

    public JsContext() {
        this.bindings = new HashMap<>();
        this.guid = java.util.UUID.randomUUID().toString();
    }

    public JsContext(Map<String, Object> initialBindings) {
        this.bindings = new HashMap<>(initialBindings);
        this.guid = java.util.UUID.randomUUID().toString();
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void addBinding(String name, Object value) {
        this.bindings.put(name, value);
    }

    public Object getBinding(String name) {
        return this.bindings.get(name);
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public int hashCode() {
        int result = guid.hashCode();
        result = 31 * result + bindings.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JsContext{" +
                "guid='" + guid + '\'' +
                ", bindings=" + bindings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JsContext jsContext = (JsContext)o;
        if (!guid.equals(jsContext.guid))
            return false;
        return bindings.equals(jsContext.bindings);
    }

}