package com.example.myapp2;

import java.util.Map;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.log.Log;

public class MyLog implements Log {
    @Override
    public void debug(String tag, String log) {

    }

    @Override
    public void debug(String tag, Exception exception) {


    }

    @Override
    public void debug(String tag, String log, Exception exception) {

    }

    @Override
    public void info(String tag, String log) {
        android.util.Log.d(tag,log);
        return ;

    }

    @Override
    public void info(String tag, Exception exception) {

    }

    @Override
    public void info(String tag, String log, Exception exception) {

    }

    @Override
    public void warn(String tag, String log) {

    }

    @Override
    public void warn(String tag, Exception exception) {

    }

    @Override
    public void warn(String tag, String log, Exception exception) {

    }

    @Override
    public void error(String tag, String log) {
        android.util.Log.e(tag,log);
        return ;
    }
    @Override
    public void error(String tag, Exception exception) {

    }

    @Override
    public void error(String tag, String log, Exception exception) {

    }

    @Override
    public void notify(String topic, String message) {

    }


    @Override
    public void diagnose(Map<String, String> information, String message, Exception exception) {

    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {

    }
}
