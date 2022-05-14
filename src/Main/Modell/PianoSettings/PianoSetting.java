package Main.Modell.PianoSettings;

import Main.Modell.Enums.KeyboardCodes;
import Main.Modell.Enums.Notes;

import java.util.HashMap;

public class PianoSetting {
    public HashMap<Notes,KeyboardCodes> notesToKeyboardKey = new HashMap<Notes,KeyboardCodes>();

    public PianoSetting(){
        notesToKeyboardKey.put(Notes.C3,KeyboardCodes.A);
        notesToKeyboardKey.put(Notes.CS3,KeyboardCodes.W);
        notesToKeyboardKey.put(Notes.D3,KeyboardCodes.S);
        notesToKeyboardKey.put(Notes.DS3,KeyboardCodes.E);
        notesToKeyboardKey.put(Notes.E3,KeyboardCodes.D);
        notesToKeyboardKey.put(Notes.F3,KeyboardCodes.F);
        notesToKeyboardKey.put(Notes.FS3,KeyboardCodes.T);
        notesToKeyboardKey.put(Notes.G3,KeyboardCodes.G);
        notesToKeyboardKey.put(Notes.GS3,KeyboardCodes.Z);
        notesToKeyboardKey.put(Notes.A3,KeyboardCodes.H);
        notesToKeyboardKey.put(Notes.AS3,KeyboardCodes.U);
        notesToKeyboardKey.put(Notes.B3,KeyboardCodes.J);
    }

}
