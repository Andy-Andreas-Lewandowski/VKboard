package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.KeyboardCodes;
import Main.Modell.Enums.Notes;

import java.util.HashMap;

public class KeysToScaleBindings {


    public static HashMap<Integer,Notes> getPianoBindings(){
        HashMap<Integer,Notes> notesToKeyboardKey = new HashMap<Integer, Notes>();

        notesToKeyboardKey.put(KeyboardCodes.Q.getCode(),Notes.C3);
        notesToKeyboardKey.put(KeyboardCodes.TWO.getCode(),Notes.CS3);
        notesToKeyboardKey.put(KeyboardCodes.W.getCode(),Notes.D3);
        notesToKeyboardKey.put(KeyboardCodes.THREE.getCode(),Notes.DS3);
        notesToKeyboardKey.put(KeyboardCodes.E.getCode(),Notes.E3);
        notesToKeyboardKey.put(KeyboardCodes.R.getCode(),Notes.F3);
        notesToKeyboardKey.put(KeyboardCodes.FIVE.getCode(),Notes.FS3);
        notesToKeyboardKey.put(KeyboardCodes.T.getCode(),Notes.G3);
        notesToKeyboardKey.put(KeyboardCodes.SIX.getCode(),Notes.GS3);
        notesToKeyboardKey.put(KeyboardCodes.Y.getCode(),Notes.A3);
        notesToKeyboardKey.put(KeyboardCodes.SEVEN.getCode(),Notes.AS3);
        notesToKeyboardKey.put(KeyboardCodes.U.getCode(),Notes.B3);

        notesToKeyboardKey.put(KeyboardCodes.V.getCode(),Notes.C4);
        notesToKeyboardKey.put(KeyboardCodes.G.getCode(),Notes.CS4);
        notesToKeyboardKey.put(KeyboardCodes.B.getCode(),Notes.D4);
        notesToKeyboardKey.put(KeyboardCodes.H.getCode(),Notes.DS4);
        notesToKeyboardKey.put(KeyboardCodes.N.getCode(),Notes.E4);
        notesToKeyboardKey.put(KeyboardCodes.M.getCode(),Notes.F4);
        notesToKeyboardKey.put(KeyboardCodes.K.getCode(),Notes.FS4);
        notesToKeyboardKey.put(KeyboardCodes.KOMMA.getCode(),Notes.G4);
        notesToKeyboardKey.put(KeyboardCodes.L.getCode(),Notes.GS4);
        notesToKeyboardKey.put(KeyboardCodes.PERIOD.getCode(),Notes.A4);
        notesToKeyboardKey.put(KeyboardCodes.SEMICOLON.getCode(),Notes.AS4);
        notesToKeyboardKey.put(KeyboardCodes.SLASH.getCode(),Notes.B4);

        return notesToKeyboardKey;
    }

    public static HashMap<Integer,Notes> getGuitarBindings(){
        HashMap<Integer,Notes> notesToKeyboardKey = new HashMap<Integer, Notes>();

        notesToKeyboardKey.put(KeyboardCodes.Q.getCode(),Notes.E2);
        notesToKeyboardKey.put(KeyboardCodes.TWO.getCode(),Notes.G1);
        notesToKeyboardKey.put(KeyboardCodes.W.getCode(),Notes.F2);
        notesToKeyboardKey.put(KeyboardCodes.THREE.getCode(),Notes.A1);
        notesToKeyboardKey.put(KeyboardCodes.E.getCode(),Notes.G2);
        notesToKeyboardKey.put(KeyboardCodes.R.getCode(),Notes.A2);
        notesToKeyboardKey.put(KeyboardCodes.FIVE.getCode(),Notes.B1);
        notesToKeyboardKey.put(KeyboardCodes.T.getCode(),Notes.B2);
        notesToKeyboardKey.put(KeyboardCodes.SIX.getCode(),Notes.C1);
        notesToKeyboardKey.put(KeyboardCodes.Y.getCode(),Notes.C2);
        notesToKeyboardKey.put(KeyboardCodes.SEVEN.getCode(),Notes.D1);
        notesToKeyboardKey.put(KeyboardCodes.U.getCode(),Notes.D2);

        notesToKeyboardKey.put(KeyboardCodes.V.getCode(),Notes.E3);
        notesToKeyboardKey.put(KeyboardCodes.G.getCode(),Notes.E4);
        notesToKeyboardKey.put(KeyboardCodes.B.getCode(),Notes.F3);
        notesToKeyboardKey.put(KeyboardCodes.H.getCode(),Notes.F4);
        notesToKeyboardKey.put(KeyboardCodes.N.getCode(),Notes.G3);
        notesToKeyboardKey.put(KeyboardCodes.M.getCode(),Notes.A3);
        notesToKeyboardKey.put(KeyboardCodes.K.getCode(),Notes.G4);
        notesToKeyboardKey.put(KeyboardCodes.KOMMA.getCode(),Notes.B3);
        notesToKeyboardKey.put(KeyboardCodes.L.getCode(),Notes.A4);
        notesToKeyboardKey.put(KeyboardCodes.PERIOD.getCode(),Notes.C3);
        notesToKeyboardKey.put(KeyboardCodes.SEMICOLON.getCode(),Notes.B4);
        notesToKeyboardKey.put(KeyboardCodes.SLASH.getCode(),Notes.D3);

        return notesToKeyboardKey;
    }

}
