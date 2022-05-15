package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;

import java.util.HashMap;

public class Piano {
    HashMap<Integer, Key> keys;
    InstrumentPreset preset;

   public SequenceChannel sequenceChannel = new SequenceChannel();
   Synthesizer metronomSynth;

   public Piano() throws MidiUnavailableException {
       metronomSynth = MidiSystem.getSynthesizer();
       for(Instrument instrument : metronomSynth.getAvailableInstruments()){System.out.println(instrument.toString());}
   }

    public void loadInstrument(InstrumentPreset preset) throws MidiUnavailableException, InvalidMidiDataException {
       this.preset = preset;
       keys = new HashMap<Integer, Key>();
        for(Integer keyboardCode : preset.keyboardCodesToNotes.keySet()){
            try {
                Key key = new Key(preset.keyboardCodesToNotes.get(keyboardCode),
                                    preset.getVelocity(), preset.getBank(), preset.getInstrument());
                keys.put(keyboardCode,key);
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        }

        sequenceChannel.loadInstrument(preset);

    }

    public void unloadInstrument(){
        for(Key key : keys.values()){key.terminate();}
        keys.clear();
    }

    public void play(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            Notes note = keys.get(keyboardCode).play();
            if (sequenceChannel.getIsRecording()){
                sequenceChannel.addNote(note,true);
            }
        }
    }

    public void stop(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            Notes note = keys.get(keyboardCode).stop();
            if (sequenceChannel.getIsRecording()){
                sequenceChannel.addNote(note,false);
            }
        }
    }



}
