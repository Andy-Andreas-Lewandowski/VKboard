package Main.Modell.Enums;

import java.util.ArrayList;

public enum Notes {

    // 3rd Octave
    C1(36),CS1(37),D1(38),DS1(39),E1(40),F1(41),FS1(42),G1(43),
    GS1(44),A1(45),AS1(46),B1(47),
    C2(48),CS2(49),D2(50),DS2(51),E2(52),F2(53),FS2(54),G2(55),
    GS2(56),A2(57),AS2(58),B2(59),

    C3(60),CS3(61),D3(62),DS3(63),E3(64),F3(65),FS3(66),G3(67),
    GS3(68),A3(69),AS3(70),B3(71),
    // 4th Octave
    C4(72),CS4(73),D4(74),DS4(75),E4(76),F4(77),FS4(78),G4(79),
    GS4(80),A4(81),AS4(82),B4(83),
    C5(84),CS5(85),D5(86),DS5(87),E5(89),F5(90),FS5(91),G5(92),
    GS5(93),A5(94),AS5(95),B5(96)


    ;

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
    public static ArrayList<Notes> getFourthOctave(){
        ArrayList<Notes> thirdOctave = new ArrayList<Notes>();
        for(Notes note : Notes.values()){
            if(note.getCode()>= 72 && note.getCode()<= 83){
                thirdOctave.add(note);
            }
        }
        return thirdOctave;
    }


}
