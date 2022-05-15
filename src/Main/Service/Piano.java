package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;
import Main.Modell.PianoSettings.PianoSetting;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;

import java.util.HashMap;

public class Piano {
    PianoSetting setting = new PianoSetting();
    HashMap<Integer, Key> keys;

   public SequenceChannel sequenceChannel = new SequenceChannel();
   Synthesizer metronomSynth;

   public Piano() throws MidiUnavailableException {
       metronomSynth = MidiSystem.getSynthesizer();
       for(Instrument instrument : metronomSynth.getAvailableInstruments()){System.out.println(instrument.toString());}

   }

    public void loadInstrument(InstrumentPreset preset) throws MidiUnavailableException, InvalidMidiDataException {
        keys = new HashMap<Integer, Key>();
        for(Notes note : preset.getNotes()){
            int keyboardCode = setting.notesToKeyboardKey.get(note).getCode();
            try {
                Key key = new Key(note, preset.getVelocity(), preset.getBank(), preset.getInstrument());
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
