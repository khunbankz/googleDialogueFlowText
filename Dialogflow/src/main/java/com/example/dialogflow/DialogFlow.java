package com.example.dialogflow;

import th.co.ais.genesis.blueprint.configuration.Config;
import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.dialogue.ResultListener;
import th.co.ais.genesis.blueprint.log.Log;

public class DialogFlow implements DialogueInputText {
    private ResultListener listener;

    @Override
    public void open(ResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void speech(String input) {
        android.util.Log.d("Hello", "request speech: " + input);
        this.listener.OnResult("b", 1.0, "");

    }

    @Override
    public void close() {

    }

    @Override
    public void init(Config config, Log log, Object extended_arg) throws IllegalArgumentException {

    }
}
