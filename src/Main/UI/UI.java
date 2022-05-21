package Main.UI;

import Main.Components.Instrument.Pianoroll;
import Main.Components.Preset.SynthesizerPreset;
import Main.Components.Sequencer.Metronome;
import Main.Components.Sequencer.Sequencer;
import Main.Service.RootService;
import Main.UI.Keys.BlackKey;
import Main.UI.Keys.WhiteKey;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


public class UI {
    final static int APPROX_WIDTH = 1200;
    final static int APPROX_HEIGHT = 700;
    // magic number 38 and i do not know why it works
    static int divider = APPROX_HEIGHT /2;

    public static final Font TITEL_FONT = new Font("Arial",Font.BOLD,12);
    public static final Border TITEL_BORDER = BorderFactory.createCompoundBorder();

    RootService rootService;
    MainFrame frame;
    Keyboardroll keyboardroll;
    KeyboardrollSettingPane splitPane;

    public UI(RootService rootService){
        this.rootService = rootService;
        buildUI();
    }

    public JFrame buildUI(){
        frame = new MainFrame(new KeyInputHandler(rootService));
        frame.repaint();

        frame.doLayout();


        return frame;
    }





    class MainFrame extends JFrame{
        Dimension dimension;
        public MainFrame(KeyListener keyListener){
            UI.divider -= getInsets().bottom;
            addKeyListener(keyListener);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            setLayout(new BorderLayout());
            // Build as Tree
            add(new KeyboardrollSettingPane());
            //add(new Keyboardroll());

            setSize(UI.APPROX_WIDTH,UI.APPROX_HEIGHT);
        }
    }

    class KeyboardrollSettingPane extends JPanel{
        public KeyboardrollSettingPane(){
            setLayout(new GridLayout(2,1));
            add(new SettingPanel());
            add(new Keyboardroll());
        }
    }



    // ############
    // # Settings #
    // ############
    class SettingPanel extends JPanel{
        public SettingPanel(){
            setLayout(new GridLayout(1,3));
            add(new InstrumentAndMetronomePanel());
        }

    }

    // ############################
    // # Instrument and Metronome #
    // ############################

    class InstrumentAndMetronomePanel extends JPanel{
        public InstrumentAndMetronomePanel(){
            setLayout(new GridLayout(2,1));

            // TOP
            JPanel instrumentBpmPanel = new JPanel();
            instrumentBpmPanel.setLayout(new GridLayout(1,2));
            instrumentBpmPanel.add(new PresetComboBox());
            instrumentBpmPanel.add(new BpmSpinner());
            add(instrumentBpmPanel);

            // BOTTOM
            add(new MetronomeButton());


        }

        public class PresetComboBox extends JComboBox implements Pianoroll.InstrumentObserver {
            int previousSelectedId = 0;

            public PresetComboBox(){
                // Fill
                for(SynthesizerPreset preset : Pianoroll.presets){
                    String name = preset.getName();
                    addItem(name);
                }

                // Appearance
                setBorder(getComponentTitleBorder("Instruments"));
                setSize(50,30);

                //Behavior
                setSelectedIndex(0);
                addActionListener(new OnInstrumentSelect());
                setFocusable(false);
                Pianoroll.subscribeToInstrument(this);
            }

            @Override
            public void onInstrumentChange(int index) {
                if(index != getSelectedIndex()) {
                    previousSelectedId = index;
                    setSelectedIndex(index);
                }

            }

            public class OnInstrumentSelect implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(previousSelectedId != getSelectedIndex()) {
                        Pianoroll.loadPreset(getSelectedIndex());
                    }
                }
            }

        }

        public class BpmSpinner extends JSpinner implements Sequencer.BpmObserver {
            int previousBpm = Sequencer.getBpm();

            public BpmSpinner(){
                // DATA
                SpinnerModel spinnerModel = new SpinnerNumberModel(Sequencer.getBpm(),Sequencer.MIN_BPM,Sequencer.MAX_BPM,10);
                setModel(spinnerModel);


                // APPEARANCE
                setBorder(getComponentTitleBorder("BPM"));
                setSize(200,50);

                //BEHAVIOR
                setFocusable(false);
                Sequencer.subscribeToBpm(this);
                addChangeListener(new OnBpmSelect());
            }

            public class OnBpmSelect implements ChangeListener {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if((Integer)getValue()!= previousBpm) {
                        Sequencer.setBpm((Integer)getValue());
                    }
                }
            }

            @Override
            public void onBpmChange(int bpm) {
                previousBpm = bpm;
                setValue(previousBpm);
            }
        }

        public class MetronomeButton extends JButton{
            boolean wasPressed = false;
            Metronome metronome = Metronome.getInstance();
            public MetronomeButton(){
                // APPEARANCE
                setFont(TITEL_FONT);
                setText("Metronome");
                // BEHAVIOR
                setFocusable(false);
                addActionListener(new OnMetronomeButtonClick());
            }

            public class OnMetronomeButtonClick implements ActionListener{


                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Metronome.getIsPlaying()) metronome.stopMetronome();
                    else metronome.startMetronome();
                }
            }
        }
    }


    // ################
    // # Keyboardroll #
    // ################
    class Keyboardroll extends JLayeredPane{


        public Keyboardroll(){


            // Layout

            //System.out.println(this.getAlignmentY());

            setBackground(Color.GRAY);
            for(int i = 0 ; i < 14 ; i++){
                add(new WhiteKey(UI.APPROX_WIDTH,i),JLayeredPane.DEFAULT_LAYER);
            }
            for(int i = 0 ; i < 10; i++)add(new BlackKey(UI.APPROX_WIDTH,i),JLayeredPane.PALETTE_LAYER);
            setSize(APPROX_WIDTH,APPROX_HEIGHT);
        }

    }

    public class ComponentLabel extends JLabel{

        public ComponentLabel(String text){
            setFont(TITEL_FONT);
            setText(text);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

    }



    public static Border getComponentTitleBorder(String title){
        return BorderFactory.createTitledBorder(TITEL_BORDER,title, TitledBorder.LEFT,TitledBorder.ABOVE_TOP, TITEL_FONT);
    }


}
