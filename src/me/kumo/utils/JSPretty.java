package me.kumo.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.Objects;

public class JSPretty {
    private static final JSPretty instance = new JSPretty();
    private static final String BEAUTIFY_JS_RESOURCE = "/scripts/beautify.min.js";
    private static final String BEAUTIFY_METHOD_NAME = "js_beautify";
    private final ScriptEngine engine;

    JSPretty() {
        try {
            engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval("var global = this;");
            engine.eval(new InputStreamReader(Objects.requireNonNull(JSPretty.class.getResourceAsStream(BEAUTIFY_JS_RESOURCE))));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public static String beautify(String javascriptCode) {
        try {
            return (String) ((Invocable) instance.engine).invokeFunction(BEAUTIFY_METHOD_NAME, javascriptCode);
        } catch (ScriptException | NoSuchMethodException e) {
            return javascriptCode;
        }
    }
}