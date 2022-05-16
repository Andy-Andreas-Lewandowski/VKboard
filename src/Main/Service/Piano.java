package Main.Service;

import Main.Modell.Enums.KeyboardCodes;
import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Piano {
    HashMap<Integer, Key> keyboardCodeToKeys = new HashMap<>();

    ArrayList<InstrumentPreset> instruments = new ArrayList<>();
    int insSelectIndex = 0;
    InstrumentPreset preset;


    public ArrayList<SequenceChannel> sequenceChannels = new ArrayList<>();
    public int seqChannelSelectIndex = 0;
    public SequenceChannel selectedSequenceChannel = new SequenceChannel();
    public Piano() throws MidiUnavailableException, InvalidMidiDataException {
        for(int i = 0 ; i < 26 ; i++){
            try {
                Key key = new Key();
                keyboardCodeToKeys.put(0, key);
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void nextSequenceChannel(){
        if(selectedSequenceChannel.getIsRecording()){
            System.out.println("Can't change Channel while it is recording!");
            return;
        }
        selectedSequenceChannel = sequenceChannels.get(seqChannelSelectIndex);
        System.out.println("Sequencer " + seqChannelSelectIndex + " selected.");
        seqChannelSelectIndex = (seqChannelSelectIndex + 1) % sequenceChannels.size();
    }

    public void nextInstrument() throws MidiUnavailableException, InvalidMidiDataException {
        loadInstrument(instruments.get(insSelectIndex));
        System.out.println("Instrument " + instruments.get(insSelectIndex).toString() + " selected.");
        insSelectIndex = (insSelectIndex + 1) % instruments.size();

    }

    public void loadInstrument(InstrumentPreset preset) throws MidiUnavailableException, InvalidMidiDataException {
       this.preset = preset;
       LinkedList<Key> keys = new LinkedList<>();
       for(Key key : keyboardCodeToKeys.values() ) keys.add(key);
       keyboardCodeToKeys.clear();
       for(Integer keyboardCode : preset.keyboardCodesToNotes.keySet()){
           Key key = keys.pop();
           key.setKey(preset.keyboardCodesToNotes.get(keyboardCode), preset.getVelocity(), preset.getBank(), preset.getInstrument());
           keyboardCodeToKeys.put(keyboardCode,key);


        }

    }

    public void connectInstrumentToSequencer() throws MidiUnavailableException, InvalidMidiDataException {
        selectedSequenceChannel.loadInstrument(preset);
    }


    public void unloadInstrument(){
        for(Key key : keyboardCodeToKeys.values()){key.terminate();}
        keyboardCodeToKeys.clear();
    }

    public void play(int keyboardCode){
        if(keyboardCodeToKeys.get(keyboardCode)!=null){
            Notes note = keyboardCodeToKeys.get(keyboardCode).play();
            if (selectedSequenceChannel.getIsRecording()){
                selectedSequenceChannel.addNote(note,true);
            }
        }
    }

    public void stop(int keyboardCode){
        if(keyboardCodeToKeys.get(keyboardCode)!=null){
            Notes note = keyboardCodeToKeys.get(keyboardCode).stop();
            if (selectedSequenceChannel.getIsRecording()){
                selectedSequenceChannel.addNote(note,false);
            }
        }
    }



}
