package Main.Components.EnumsAndMaps;

import java.util.LinkedList;
import java.util.List;

public class KeyboardMapping {

    private KeyboardMapping(){}
    private static KeyboardMapping keyboardMapping = new KeyboardMapping();
    public static KeyboardMapping getInstance(){
        return keyboardMapping;
    }



    // -- Keyboard Settings
    public static final List<KeyToInputCode> leftMusicKeys = List.of(
            KeyToInputCode.Q,
            KeyToInputCode.TWO,
            KeyToInputCode.W,
            KeyToInputCode.THREE,
            KeyToInputCode.E,
            KeyToInputCode.R,
            KeyToInputCode.FIVE,
            KeyToInputCode.T,
            KeyToInputCode.SIX,
            KeyToInputCode.Y,
            KeyToInputCode.SEVEN,
            KeyToInputCode.U
    );

    public static final List<KeyToInputCode> rightMusicKeys = List.of(
            KeyToInputCode.V,
            KeyToInputCode.G,
            KeyToInputCode.B,
            KeyToInputCode.H,
            KeyToInputCode.N,
            KeyToInputCode.M,
            KeyToInputCode.K,
            KeyToInputCode.KOMMA,
            KeyToInputCode.L,
            KeyToInputCode.PERIOD,
            KeyToInputCode.SEMICOLON,
            KeyToInputCode.SLASH
    );

    public static List<Integer> getAllInputCodes(){
        List<Integer> inputCodes = new LinkedList<>();
        leftMusicKeys.forEach(key -> inputCodes.add(key.getCode()));
        rightMusicKeys.forEach(key -> inputCodes.add(key.getCode()));
        return inputCodes;
    }

}
