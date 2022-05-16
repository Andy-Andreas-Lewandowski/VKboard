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

    public void loadInstrument(InstrumentPreset preset, Collection<Notes> notes) throws InvalidMidiDataException {
        //Initialize
        this.preset = preset;
        //Clear Hashmap
        noteToMessage.clear();
        // Set messages
        int channel = 0;
        MidiChannel[] channels = synthesizer.getChannels();
        Iterator<ShortMessage> nxtMsg = messages.iterator();
        for(Notes note : notes) {
            //Set Message per Channel
            ShortMessage msg = nxtMsg.next();
            msg.setMessage(ShortMessage.NOTE_ON, channel, note.getCode(), preset.getVelocity());
            noteToMessage.put(note, msg);
            //Set instrument in Channel
            channels[channel].programChange(preset.getBank(), preset.getInstrument());

            channel++;
            // Skip drum channel
            if (channel == 9) channel++;



        }


    }

    public boolean play(Notes note){
        ShortMessage msg = noteToMessage.get(note);
        if(msg != null){
            receiver.send(msg,-1);
            return true;
        }
        return false;
    }

    public boolean stop(Notes note) throws InvalidMidiDataException {
        ShortMessage msg = noteToMessage.get(note);
        if(msg != null){
            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_ON, msg.getChannel(),msg.getData1(),0);
            receiver.send(off,-1);
            return true;
        }
        return false;
    }

}
