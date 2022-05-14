package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;
import Main.Modell.PianoSettings.PianoSetting;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.util.HashMap;

public class Piano {
    PianoSetting setting = new PianoSetting();
    HashMap<Integer, Key> keys;


    public void loadInstrument(InstrumentPreset preset){
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
    }

    public void unloadInstrument(){
        for(Key key : keys.values()){key.terminate();}
        keys.clear();
    }

    public void play(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            keys.get(keyboardCode).play();
        }
    }

    public void stop(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            keys.get(keyboardCode).stop();
        }
    }


}
