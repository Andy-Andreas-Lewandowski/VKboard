package Main.Modell.Enums;

import java.util.ArrayList;

public enum Notes {

    // 3rd Octave
    C3(60),CS3(61),D3(62),DS3(63),E3(64),F3(65),FS3(66),G3(67),
    GS3(68),A3(69),AS3(70),B3(71);

    private final int code;

    Notes(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }


    public static ArrayList<Notes> getThirdOctave(){
        ArrayList<Notes> thirdOctave = new ArrayList<Notes>();
        for(Notes note : Notes.values()){
            if(note.getCode()>= 60 && note.getCode()<= 71){
                thirdOctave.add(note);
            }
        }
        return thirdOctave;
    }


}
