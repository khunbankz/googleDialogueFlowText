package com.example.dialogflow;

import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.factory.AbstractFactory;

public class DialogFactory implements AbstractFactory<DialogueInputText> {
    @Override
    public DialogueInputText create() {
        return new DialogFlow();
    }
}
