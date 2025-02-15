package com.scriptlib.core;

public class IIEFPreprocessingExecutor implements ScriptPreprocessor {
    @Override
    public String preprocess(String scriptContent) {
        return "(function(){" + scriptContent + "})();";
    }
}
