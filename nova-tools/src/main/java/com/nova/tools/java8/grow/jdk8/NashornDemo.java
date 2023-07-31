package com.nova.tools.java8.grow.jdk8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Nashorn JavaScript引擎
 *
 * @author wzh
 * @date 2018/2/8
 */
class NashornDemo {

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        System.err.println(engine.getClass().getName());
        System.err.println("Result:" + engine.eval("function f() { return 1; }; f() + 1;"));
    }
}
