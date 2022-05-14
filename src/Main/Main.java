package Main;
import Main.Service.RootService;
import Main.UI.UI;

import javax.sound.midi.*;
import javax.swing.*;
import java.util.List;


public class Main {
    static Synthesizer synth;
    static Synthesizer synth2;

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
/*
        // Init Synth
        synth = MidiSystem.getSynthesizer();
        synth.open();
        synth2 = MidiSystem.getSynthesizer();
        synth2.open();
        while(!synth.isOpen()||!synth2.isOpen()){}

        // Init Receiver
        Receiver receiver = synth.getReceiver();
        Receiver receiver2 = synth2.getReceiver();
        for(Instrument elem : synth.getAvailableInstruments()){
            System.out.println(elem.toString());
        }

        // Init Midi Message
        ShortMessage tuneOn = new ShortMessage();
        tuneOn.setMessage(ShortMessage.NOTE_ON,60,100);
        ShortMessage tuneOnTwo = new ShortMessage();
        tuneOnTwo.setMessage(ShortMessage.NOTE_ON,72,120);


        // Music
        receiver.send(tuneOn,-1);
        receiver2.send(tuneOnTwo,-1);
        Thread.sleep(4000);


        // Close all
        synth.close();
        synth2.close();*/

        RootService rootService = new RootService();
        UI ui = new UI(rootService);

    }

}
