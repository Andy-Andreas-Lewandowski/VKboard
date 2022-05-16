package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.KeyboardCodes;
import Main.Modell.Enums.Notes;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class InstrumentPreset {
    int velocity;
    int bank;
    int instrument;

    String name = "";

    public HashMap<Integer,Notes> keyboardCodesToNotes;
    ArrayList<Notes> notes = new ArrayList<Notes>();

    public int getVelocity(){return velocity;}
    public int getBank(){return bank;}
    public int getInstrument(){return instrument;}
    public ArrayList<Notes> getNotes(){return notes;}

    public String toString(){
        return name;
    }

}
