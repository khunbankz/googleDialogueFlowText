package com.example.googledialogflow;
import th.co.ais.genesis.blueprint.dialogue.DialogueInputText;
import th.co.ais.genesis.blueprint.factory.AbstractFactory;
public class Dialogflowfactory implements AbstractFactory<DialogueInputText> {

    public DialogueInputText create(){
        return new Dialogflow() ;
    }

}
