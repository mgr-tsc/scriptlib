package com.scriptlib.core;

public class PreprocessingExecutor implements ScriptPreprocessor {
    @Override
    public String preprocess(String scriptContent) {
        return "(function(){" + scriptContent + "})();";
    }
}
