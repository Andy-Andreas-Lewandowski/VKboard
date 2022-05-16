package Main.Modell.Piano;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;

import javax.sound.midi.*;
import java.util.*;

public class Octave {
    InstrumentPreset preset;
    Synthesizer synthesizer;
    Receiver receiver;
    LinkedList<ShortMessage> messages = new LinkedList<>();
    HashMap<Notes,ShortMessage> noteToMessage = new HashMap<Notes, ShortMessage>();


    public Octave() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        receiver = synthesizer.getReceiver();
        for(int i = 0; i <= 13; i++){
            ShortMessage tone = new ShortMessage();
            messages.add(tone);
        }
    }

    public void loadInstrument(InstrumentPreset preset, Collection<Notes> notes){
        this.preset = preset;
        int channel = 0;
        Iterator nxtMsg = messages.iterator();
        for(Notes note : notes){

        }



    }


}
