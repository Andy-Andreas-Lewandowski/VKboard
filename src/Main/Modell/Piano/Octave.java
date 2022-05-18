package Main.Modell.Piano;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;

import javax.sound.midi.*;
import java.util.*;

public class Octave {
    InstrumentPreset preset;
    Synthesizer synthesizer;
    Receiver receiver;
    LinkedList<ShortMessage> playMessages = new LinkedList<>();
    LinkedList<ShortMessage> stopMessages = new LinkedList<>();
    HashMap<Notes,ShortMessage> noteToPlayMessage = new HashMap<Notes, ShortMessage>();
    HashMap<Notes,ShortMessage> noteToStopMessage = new HashMap<Notes, ShortMessage>();

    public Octave() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        receiver = synthesizer.getReceiver();
        for(int i = 0; i <= 13; i++){
            ShortMessage play = new ShortMessage();
            ShortMessage stop = new ShortMessage();
            playMessages.add(play);
            stopMessages.add(stop);
        }


    }

    public void loadInstrument(InstrumentPreset preset, Collection<Notes> notes) throws InvalidMidiDataException {
        //Initialize
        this.preset = preset;
        //Clear Hashmap
        noteToPlayMessage.clear();
        // Set messages
        int channel = 0;
        MidiChannel[] channels = synthesizer.getChannels();
        Iterator<ShortMessage> nxtPly = playMessages.iterator();
        Iterator<ShortMessage> nxtStp = stopMessages.iterator();
        for(Notes note : notes) {
            //Set Message per Channel
            ShortMessage playMsg = nxtPly.next();
            ShortMessage stopMsg = nxtStp.next();

            playMsg.setMessage(ShortMessage.NOTE_ON, channel, note.getCode(), preset.getVelocity());
            stopMsg.setMessage(ShortMessage.NOTE_OFF,channel,note.getCode(),preset.getVelocity());
            noteToPlayMessage.put(note, playMsg);
            noteToStopMessage.put(note,stopMsg);

            //Set instrument in Channel
            channels[channel].programChange(preset.getBank(), preset.getInstrument());

            channel++;
            // Skip drum channel
            if (channel == 9) channel++;



        }


    }

    public boolean play(Notes note){
        ShortMessage msg = noteToPlayMessage.get(note);
        if(msg != null){
            receiver.send(msg,-1);
            return true;
        }
        return false;
    }

    public boolean stop(Notes note) {
        ShortMessage msg = noteToStopMessage.get(note);
        if(msg != null){
            receiver.send(msg,-1);
            return true;
        }
        return false;
    }

}
