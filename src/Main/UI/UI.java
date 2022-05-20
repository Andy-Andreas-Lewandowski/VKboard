package Main.UI;

import Main.Components.EnumsAndMaps.KeyToInputCode;
import Main.Service.RootService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class UI {
    final static int WIDTH = 812;
    final static int HEIGHT = 500;

    final static int DIVIDER = Key.WHITE_KEY_HEIGHT;

    RootService rootService;
    KeyboardFrame frame;
    Keyboardroll keyboardroll;
    KeyboardrollSettingPane splitPane;

    public UI(RootService rootService){
        this.rootService = rootService;
        buildUI();
    }

    public JFrame buildUI(){
        frame = new KeyboardFrame(new KeyInputHandler(rootService));
        splitPane = new KeyboardrollSettingPane();
        frame.add(splitPane);
        keyboardroll = new Keyboardroll();

        // Add for layout works
        JPanel one = new JPanel();
        JPanel two = new JPanel();
        one.setBackground(Color.CYAN);
        two.setBackground(Color.GREEN);
        one.setOpaque(true);

        splitPane.add(two);
        splitPane.add(keyboardroll);

        return frame;
    }





    class KeyboardFrame extends JFrame{
        Dimension dimension;
        public KeyboardFrame(KeyListener keyListener){


            addKeyListener(keyListener);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBackground(Color.GRAY);

            // Size
            dimension = new Dimension(UI.WIDTH,UI.HEIGHT);
            setSize(dimension);
            setMaximumSize(dimension);
            setMaximumSize(dimension);
            setResizable(false);

        }
    }

    class KeyboardrollSettingPane extends JSplitPane{
        Rectangle size;
        final int splitPosition = 239;
        public KeyboardrollSettingPane(){
            // Initialize JSplitPane
            setOrientation(JSplitPane.VERTICAL_SPLIT);
            setLeftComponent(null);
            setRightComponent(null);

            // Set Sizes
            size = new Rectangle(0,0,UI.WIDTH,UI.HEIGHT);
            setBounds(size);

            setDividerLocation(splitPosition);
            setDividerSize(0);
        }

        @Override
        public int getDividerLocation(){
            return  splitPosition;
        }
        @Override
        public int getLastDividerLocation(){
            return splitPosition;
        }


    }
    class Keyboardroll extends JLayeredPane{
        public Keyboardroll(){
            setBackground(Color.GRAY);
            for(int i = 0 ; i < 14 ; i++)add(new WhiteKey(i),JLayeredPane.DEFAULT_LAYER);
            for(int i = 0 ; i < 10; i++)add(new BlackKey(i),JLayeredPane.PALETTE_LAYER);
        }

    }
    abstract class Key extends JButton{
        static final int WHITE_KEY_WIDTH = (UI.WIDTH-14) / 14;
        static final int WHITE_KEY_HEIGHT = (int) (WHITE_KEY_WIDTH * 4.5);
        static final int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH/3*2;
        static final int BLACK_KEY_HEIGHT = (int) (BLACK_KEY_WIDTH * 3.0);



    }

    class WhiteKey extends Key{
        KeyToInputCode key;
        public WhiteKey(int keyNumber){
            setBackground(Color.WHITE);

            Dimension dimension = new Dimension(WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
            setMaximumSize(dimension);
            setMinimumSize(dimension);

            setBounds(keyNumber* WHITE_KEY_WIDTH,0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);


        }
        //@Override
        //public Dimension get
    }


    class BlackKey extends Key{

        KeyToInputCode key;

        public BlackKey(int number){
            setBackground(Color.BLACK);
            Dimension dimension = new Dimension(BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            setMaximumSize(dimension);
            setMinimumSize(dimension);
            setBounds(getKeyPosition(number),0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);


        }

        int getKeyPosition(int keyNumber){
            int[] whiteKeysBeforeBlackKey = {1,2,4,5,6,8,9,11,12,13};

            return WHITE_KEY_WIDTH * whiteKeysBeforeBlackKey[keyNumber] - (BLACK_KEY_WIDTH/2);
        }

        //@Override
        //public Dimension get
    }

}
