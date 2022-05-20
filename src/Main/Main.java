package Main;
import Main.Components.Instrument.Pianoroll;
import Main.Service.RootService;
import Main.UI.UI;

import javax.sound.midi.*;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        //Arrays.asList(synthesizer.getDefaultSoundbank().getInstruments()).forEach(ins -> System.out.println(ins.toString()));

        RootService rootService = new RootService();


    }

}
