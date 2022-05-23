package Main.UI.Settings;

import Main.Components.Instrument.Pianoroll;
import Main.Components.Preset.SynthesizerPreset;
import Main.Components.Sequencer.Metronome;
import Main.Components.Sequencer.Sequencer;
import Main.Components.Sequencer.SequencerChannel;
import Main.UI.Settings.SequenceChannelController.SequenceChannelController;
import Main.UI.UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UiSettings extends JPanel {
    public UiSettings() {
        Dimension size = new Dimension(UI.APPROX_WIDTH*2/12,UI.APPROX_TOP_COMPONENT_HEIGHT);

        // - APPEARANCE
        setBorder(UI.getGroupTitleBorder("Control"));
        setPreferredSize(size);
        setMinimumSize(size);

        // - LAYOUT
        GridBagLayout       gbl = new GridBagLayout();
        GridBagConstraints  gbc = new GridBagConstraints();
        setLayout(gbl);

        // -- CONSTRAINTS

        gbc.insets = new Insets(0,0,0,0);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = this.getWidth();
        //setLayout(new GridLayout(2, 1));

        // -- PRESET
        // --- CONSTRAINTS

        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- CONNECT
        PresetComboBox presetComboBox = new PresetComboBox();
        //gbl.addLayoutComponent(presetComboBox,gbc);
        add(presetComboBox,gbc);

        // --BPM SPINNER
        // --- CONSTRAINTS
        gbc.gridx = 0;
        gbc.gridy = 1;
        // --- CONNECT
        BpmSpinner bpmSpinner = new BpmSpinner();
        add(bpmSpinner,gbc);


        // -- Metronome
        // --- CONSTRAINTS
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //--- CONNECT
        MetronomeButton metronome = new MetronomeButton();
        add(metronome,gbc);

        // -- BeatsSelectSpinner
        // --- Constraints
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // --- CONNECT
        BeatsInUseSpinner beatsInUseSpinner = new BeatsInUseSpinner();
        add(beatsInUseSpinner,gbc);

        // -- ON ALL CHANNEL ACTIONS
        // --- CONSTRAINTS
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //--- CONNECT
        OnAllChannelPanel onAllChannelPanel = new OnAllChannelPanel();
        add(onAllChannelPanel,gbc);



    }

    public class PresetComboBox extends JComboBox implements Pianoroll.InstrumentObserver {
        int previousSelectedId = 0;

        public PresetComboBox() {
            // Fill
            for (SynthesizerPreset preset : Pianoroll.presets) {
                String name = preset.getName();
                addItem(name);
            }

            // Appearance
            setBorder(UI.getComponentTitleBorder("Instruments"));
           // setSize(50, 30);

            //Behavior
            setSelectedIndex(0);
            addActionListener(new PresetComboBox.OnInstrumentSelect());
            setFocusable(false);
            Pianoroll.subscribeToInstrument(this);
        }

        @Override
        public void onInstrumentChange(int index) {
            if (index != getSelectedIndex()) {
                previousSelectedId = index;
                setSelectedIndex(index);
            }

        }

        public class OnInstrumentSelect implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (previousSelectedId != getSelectedIndex()) {
                    Pianoroll.loadPreset(getSelectedIndex());
                    previousSelectedId = getSelectedIndex();
                }
            }
        }

    }

    public class BpmSpinner extends JSpinner implements Sequencer.BpmObserver {
        int previousBpm = Sequencer.getBpm();

        public BpmSpinner() {
            // DATA
            SpinnerModel spinnerModel = new SpinnerNumberModel(Sequencer.getBpm(), Sequencer.MIN_BPM, Sequencer.MAX_BPM, 10);
            setModel(spinnerModel);


            // APPEARANCE
            setBorder(UI.getComponentTitleBorder("BPM"));
            //setSize(200, 50);

            //BEHAVIOR
            setFocusable(false);

            Sequencer.subscribeToBpm(this);
            addChangeListener(new BpmSpinner.OnBpmSelect());
        }

        public class OnBpmSelect implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) getValue() != previousBpm) {
                    Sequencer.setBpm((Integer) getValue());
                }
                UI.setFocusToFrame();
            }
        }


        @Override
        public void onBpmChange(int bpm) {
            previousBpm = bpm;
            setValue(previousBpm);

        }
    }

    public class MetronomeButton extends JButton {
        boolean wasPressed = false;
        Metronome metronome = Metronome.getInstance();

        public MetronomeButton() {
            // APPEARANCE
            setFont(UI.TITEL_FONT);
            setText("Metronome");
            // BEHAVIOR
            setFocusable(false);
            addActionListener(new MetronomeButton.OnMetronomeButtonClick());
        }

        public class OnMetronomeButtonClick implements ActionListener {


            @Override
            public void actionPerformed(ActionEvent e) {
                if (Metronome.getIsPlaying()) metronome.stopMetronome();
                else metronome.startMetronome();
            }
        }
    }

    public class BeatsInUseSpinner extends JSpinner implements Sequencer.BpmObserver{
        public BeatsInUseSpinner(){
            SpinnerModel spinnerModel = new SpinnerNumberModel(5,1,Sequencer.MAX_BEATS,1);
            setModel(spinnerModel);

            setBorder(UI.getComponentTitleBorder("Beats used bei Sequencer"));

            setFocusable(false);
            Sequencer.subscribeToBpm(this);
            addChangeListener(new OnBeatsSelect());

        }

        @Override
        public void onBpmChange(int bpm) {
            if(Sequencer.beatsInUse != (int)getValue())setValue(Sequencer.beatsInUse);
        }

        public class OnBeatsSelect implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) getValue() != Sequencer.beatsInUse) {
                    Sequencer.setBeatsInUse((Integer) getValue());
                }
                UI.setFocusToFrame();
            }
        }
    }

    public class OnAllChannelPanel extends JPanel {


        public  OnAllChannelPanel(){
            // - Layout
            GridLayout layout = new GridLayout(2,1);
            layout.setVgap(5);
            setLayout(layout);
            setBorder(UI.getSubgroupTitleBorder("Recorder"));

            // Channel Selector
            add(new RadioButtonContainer());

            // Buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1,3));
            buttonPanel.setBackground(Color.GREEN);



            add(buttonPanel);



            buttonPanel.add(new StartRecordingButton());
            buttonPanel.add(new StopRecordingButton());
            buttonPanel.add(new SyncButton());

        }

        public class StartRecordingButton extends JButton implements Sequencer.SelectedChannelObserver{

            public StartRecordingButton(){
                // - APPEARANCE
                setText("Rec");

                // - BEHAVIOR
                setFocusable(false);
                setEnabled(false);
                Sequencer.subscribeToSelectedChannel(this);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Sequencer.getInstance().startRecording();
                    }
                });
            }

            @Override
            public void onSelectedChannelChange(int channelNo) {
                if(Sequencer.isRecording ||Sequencer.selectedChannel.synthesizer == null ) setEnabled(false);
                else if(!Sequencer.isRecording && Sequencer.selectedChannel.synthesizer != null) {
                    setEnabled(true);
                    System.out.println(Sequencer.selectedChannel + " " + Sequencer.selectedChannel.synthesizer.preset);
                }
            }

            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording) setEnabled(false);
                else if(!Sequencer.isRecording && !Sequencer.selectedChannel.noteToSequences.isEmpty()) setEnabled(true);
            }
        }
        public class StopRecordingButton extends JButton implements Sequencer.SelectedChannelObserver{
            public StopRecordingButton() {
                //- APPEARANCE
                setText("Stop");
                setEnabled(false);
                // BEHAVIOR
                setFocusable(false);
                Sequencer.subscribeToSelectedChannel(this);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Sequencer.getInstance().stopRecording();
                    }
                });

            }
            @Override
            public void onSelectedChannelChange(int channelNo) {}

            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording) setEnabled(true);
                else setEnabled(false);
            }
        }

        public class SyncButton extends JButton{
            public SyncButton() {
                //- APPEARANCE
                setText("Sync");
                // BEHAVIOR
                setFocusable(false);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(int i = 0; i < Sequencer.channels.size();i++){
                            Sequencer.synchronizeSequencer(i);
                        }
                    }
                });
            }
        }


        public class RadioButtonContainer extends JPanel{
            ButtonGroup buttonGroup = new ButtonGroup();

            public RadioButtonContainer(){
                setLayout(new GridLayout(4,4));
                setBorder(UI.getComponentTitleBorder("Toggle Channel"));

                for(int i = 0; i < Sequencer.channels.size();i++){
                    ChannelSelectButton button = new ChannelSelectButton(i);
                    buttonGroup.add(button);

                    add(button);

                    JLabel label = new JLabel();
                    label.setFont(UI.TITEL_FONT);
                    label.setText("Channel " + i);
                    add(label);
                }
            }



            public class ChannelSelectButton extends JRadioButton implements Sequencer.SelectedChannelObserver {
                int channelNo = 0;
                SequencerChannel channel;

                public ChannelSelectButton(int channelNo) {
                    this.channelNo = channelNo;
                    channel = Sequencer.channels.get(channelNo);
                    if(Sequencer.selectedChannel == channel) setSelected(true);
                    addActionListener(new ChannelSelectAction());
                    Sequencer.subscribeToSelectedChannel(this);
                }

                @Override
                public void onSelectedChannelChange(int channelNo) {
                    if (this.channelNo == channelNo && !isSelected()) {
                        setSelected(true);
                    } else if (this.channelNo != channelNo && isSelected()) {
                        setSelected(false);
                    }
                    UI.setFocusToFrame();
                }

                @Override
                public void onIsRecordingChange() {
                    if(Sequencer.isRecording) setEnabled(false);
                    else setEnabled(true);
                }


                public class ChannelSelectAction implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isSelected()) {
                            Sequencer.loadChannel(channelNo);
                        }
                    }
                }

            }
        }
    }
}
