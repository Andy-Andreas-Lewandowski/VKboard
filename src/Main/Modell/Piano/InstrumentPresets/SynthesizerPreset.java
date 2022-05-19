package Main.Modell.Piano.InstrumentPresets;

import Main.Modell.Enums.Notes;

import java.util.List;

public abstract class SynthesizerPreset {
    int velocity;
    int bank;
    int instrument;
    boolean isDrumset = false;

    String name;

    List<List<Notes>> octavesList;

    public List<List<Notes>> getOctavesList() {return octavesList;}

    public int getVelocity(){return velocity;}
    public int getBank(){return bank;}
    public int getInstrument(){return instrument;}
    public boolean getIsDrumset(){return isDrumset;}

    public String toString(){
        return name;
    }

}
