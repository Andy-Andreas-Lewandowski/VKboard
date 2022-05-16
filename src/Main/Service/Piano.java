package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.GrandPiano;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Piano {
    HashMap<Integer, Key> keys;

    ArrayList<InstrumentPreset> instruments = new ArrayList<>();
    int insSelectIndex = 0;
    InstrumentPreset preset;


    public ArrayList<SequenceChannel> sequenceChannels = new ArrayList<>();
    public int seqChannelSelectIndex = 0;
    public SequenceChannel selectedSequenceChannel = new SequenceChannel();

    public Piano(){
        System.out.println(selectedSequenceChannel);
        System.out.println(instruments);

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

    }

    public void connectInstrumentToSequencer() throws MidiUnavailableException, InvalidMidiDataException {
        selectedSequenceChannel.loadInstrument(preset);
    }


    public void unloadInstrument(){
        for(Key key : keys.values()){key.terminate();}
        keys.clear();
    }

    public void play(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            Notes note = keys.get(keyboardCode).play();
            if (selectedSequenceChannel.getIsRecording()){
                selectedSequenceChannel.addNote(note,true);
            }
        }
    }

    public void stop(int keyboardCode){
        if(keys.get(keyboardCode)!=null){
            Notes note = keys.get(keyboardCode).stop();
            if (selectedSequenceChannel.getIsRecording()){
                selectedSequenceChannel.addNote(note,false);
            }
        }
    }



}
