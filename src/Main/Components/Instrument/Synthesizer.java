package Main.Components.Instrument;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Preset.SynthesizerPreset;

import javax.sound.midi.*;
import java.util.*;

public class Synthesizer implements Cloneable{
    // Soundobjects for quick building
    ArrayList<javax.sound.midi.Synthesizer>      synthesizer            = new ArrayList<>();
    ArrayList<ShortMessage>                      messages               = new ArrayList<>();
    // Loaded Synth preset
    SynthesizerPreset preset;
    // Maps for quick access
    boolean isLowerOctave = true;
    HashMap<Notes,Receiver>                      noteToReceiver         = new HashMap<>();
    HashMap<Notes,ShortMessage>                  noteToPlayMessage      = new HashMap<>();
    HashMap<Notes,ShortMessage>                  noteToStopMessage      = new HashMap<>();

    public Synthesizer(){
        for(int i = 0 ; i < 2 ; i++){
            try {
                // Build Midi.Synths
                javax.sound.midi.Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                synthesizer.add(synth);
                // Build Midi.ShortMessages
                for(int j = 0; j < 24; j++){
                    ShortMessage message = new ShortMessage();
                    messages.add(message);
                }
            } catch (MidiUnavailableException e) {throw new RuntimeException(e);}
        }
    }

    public void loadPreset(SynthesizerPreset preset){
        if(preset.getIsDrumset()){

        }else{
            loadInstrumentPreset(preset);
        }

    }

    private void loadInstrumentPreset(SynthesizerPreset preset){
        this.preset = preset;

        // Clear old data
        noteToReceiver.clear();
        noteToPlayMessage.clear();
        noteToStopMessage.clear();

        // Initialize necessary variables
        int velocity                   =  preset.getVelocity();
        int bank                       =  preset.getBank();
        int instrument                 =  preset.getInstrument();

        Iterator<ShortMessage> nxtMsg  =  messages.iterator();
        Iterator<Notes>        nxtNote =  preset.getNotes().iterator();

        for(javax.sound.midi.Synthesizer synth : synthesizer){
            try {
                // Get Synth specific variables
                MidiChannel[] channel = synth.getChannels();
                Receiver receiver = synth.getReceiver();
                // Distribute each Note on 1 Channel and 12 Channel per MidiSystem.Synthesizer (1 Octave : 1 Synth)
                for (int i = 0; i < 13; i++) {
                    if (i == 9) continue; // Channel 9 is only for Drumkit in MidiProtocol.

                    // Get Next Note and Messages
                    Notes note       = nxtNote.next();
                    ShortMessage on  = nxtMsg.next();
                    ShortMessage off = nxtMsg.next();

                    // Setup channels and Messages
                    channel[i].programChange(bank, instrument);
                    on.setMessage(ShortMessage.NOTE_ON, i, note.getCode(), velocity);
                    off.setMessage(ShortMessage.NOTE_OFF, i, note.getCode(), velocity);

                    // Setup Access
                    noteToReceiver.put(note, receiver);
                    noteToPlayMessage.put(note,on);
                    noteToStopMessage.put(note,off);
                }
            }catch (InvalidMidiDataException | MidiUnavailableException e){
                System.out.println("ERROR: Couldn't load preset into Synthesizer!");
                this.preset = null;
                throw new RuntimeException(e);
            }
        }
    }

    public Collection<Notes> getNotes(){
        return noteToPlayMessage.keySet();
    }


    public void playNote(Notes note){
        Receiver receiver = noteToReceiver.get(note);
        if(receiver == null) return;
        ShortMessage on   = noteToPlayMessage.get(note);
        receiver.send(on,-1);
    }

    public void stopNote(Notes note){
        Receiver receiver  = noteToReceiver.get(note);
        if(receiver == null) return;
        ShortMessage off   = noteToStopMessage.get(note);
        receiver.send(off,-1);
    }

    @Override
    public Object clone(){
        Synthesizer clone = new Synthesizer();
        clone.loadPreset(preset);
        return clone;
    }
}
