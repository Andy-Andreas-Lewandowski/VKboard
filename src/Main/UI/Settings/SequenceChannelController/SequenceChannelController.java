package Main.UI.Settings.SequenceChannelController;

import Main.Components.Instrument.Synthesizer;
import Main.Components.Sequencer.Sequencer;
import Main.Components.Sequencer.SequencerChannel;
import Main.UI.UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SequenceChannelController extends JPanel {
    private final UI ui;
    Dimension rowSize = new Dimension(UI.APPROX_WIDTH * 8 / 11, (UI.APPROX_TOP_COMPONENT_HEIGHT - 10) / (Sequencer.channels.size() + 1));

    public SequenceChannelController(UI ui) {
        this.ui = ui;
        // - APPEARANCE
        setBorder(UI.getGroupTitleBorder("Channels"));
        Dimension dim = new Dimension((UI.APPROX_WIDTH * 6) / 8, UI.APPROX_TOP_COMPONENT_HEIGHT);
        setPreferredSize(dim);
        setMinimumSize(dim);

        // - LAYOUT
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);

        // -- GENERAL CONSTRAINTS
        gbc.gridwidth = 1;
        gbc.gridheight = 4;

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // -- SEQUENCER CHANNELS
        for(int i = 0; i < Sequencer.channels.size(); i++) {
            ChannelPanel channelPanel = new ChannelPanel(i);
            gbl.addLayoutComponent(channelPanel, gbc);
            add(channelPanel);
        }

        //CommandPanelForAllSequencers multiSequenceController = new CommandPanelForAllSequencers();
        //gbl.addLayoutComponent(multiSequenceController,gbc);
        //add(multiSequenceController);
        //multiSequenceController.setSizing(rowSize);
    }

    public void setSizing(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
    }



    public class ChannelPanel extends JPanel{
        Dimension   size         = new Dimension(UI.APPROX_WIDTH *8/11,(UI.APPROX_TOP_COMPONENT_HEIGHT-30)/(Sequencer.channels.size()));
        ButtonGroup radioButtons = new ButtonGroup();
        int         channelNo    = 0;

        public ChannelPanel(int channelNo){
            // - DATA
            this.channelNo = channelNo;

            // - APPEARANCE
            setBorder(UI.getSubgroupTitleBorder("Channel " + channelNo + ":"));
            setPreferredSize(size);
            setMinimumSize(size);
            // - LAYOUT
            GridBagLayout      gbl  = new GridBagLayout();
            GridBagConstraints gbc  = new GridBagConstraints();
            setLayout(gbl);
            // -- CONSTRAINTS
            gbc.insets = new Insets(0,0,0,0);
            gbc.gridwidth  = 1;
            gbc.gridheight = 2;
            gbc.gridx   = 0;
            gbc.gridy   = GridBagConstraints.RELATIVE;
            gbc.weighty = 0;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;



            // -- BUTTON PANEL
            JPanel buttonPanel = new JPanel();
            gbl.addLayoutComponent(buttonPanel,gbc);
            add(buttonPanel);
            // --- APPEARANCE

            // --- LAYOUT
            GridBagLayout      buttonsGbl  = new GridBagLayout();
            GridBagConstraints buttonGbc  = new GridBagConstraints();
            buttonPanel.setLayout(buttonsGbl);
            // ---- GENERAL CONSTRAINTS
            buttonGbc.insets = new Insets(0,10,0,10);
            buttonGbc.gridwidth  = 6;
            buttonGbc.gridheight = 2;
            buttonGbc.gridx   = GridBagConstraints.RELATIVE;
            buttonGbc.gridy   = 0;
            buttonGbc.weightx = 0;
            //gbc.fill = GridBagConstraints.HORIZONTAL;

            // ----  PLAY BUTTON
            // ----- CONNECT
            PlayStepButton playButton = new PlayStepButton(channelNo);
            buttonsGbl.addLayoutComponent(playButton,buttonGbc);
            buttonPanel.add(playButton);

            // ----  STOP BUTTON
            // ----- CONNECT
            StopPlayButton stopButton = new StopPlayButton(channelNo);
            buttonsGbl.addLayoutComponent(stopButton,buttonGbc);
            buttonPanel.add(stopButton);

            // ---- LOAD SYNTH
            // ----- CONNECT
            LoadSynthButton loadSynthButton = new LoadSynthButton(channelNo);
            buttonsGbl.addLayoutComponent(loadSynthButton,buttonGbc);
            buttonPanel.add(loadSynthButton);

            // ---- CLEAR
            // ----- CONNECT
            ClearStepButton clearButton = new ClearStepButton(channelNo);
            buttonsGbl.addLayoutComponent(clearButton,buttonGbc);
            buttonPanel.add(clearButton);
            // ---- BEATS
            // ----- CONNECT
            BeatControllSpinner beatCrtl = new BeatControllSpinner(channelNo);
            buttonsGbl.addLayoutComponent(beatCrtl,buttonGbc);
            buttonPanel.add(beatCrtl);


            // ---- LABEL
            // ----- CONSTRAINTS
            buttonGbc.weightx = 1;
            // ----- CONNECT
            ChannelPresetLabel presetLabel = new ChannelPresetLabel(channelNo);
            buttonsGbl.addLayoutComponent(presetLabel,buttonGbc);
            buttonPanel.add(presetLabel);



            // -- STEPS
            // --- CONSTRAINTS
            // --- CONNECT
            StepControl stepControl = new StepControl(channelNo);
            gbl.addLayoutComponent(stepControl,gbc);
            add(stepControl);




        }

        public class PlayStepButton extends JButton implements SequencerChannel.PlayObserver {
            boolean isPlaying = false;
            int channelNo = 0;

            public PlayStepButton(int channelNo){
                this.channelNo = channelNo;
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Play");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);
                Sequencer.channels.get(channelNo).subscribeToChannelPlay(this);

                addActionListener(new PlayChannelAction());
            }

            @Override
            public void onPlayChange(boolean isPlaying) {
                if(this.isPlaying != isPlaying){
                    this.isPlaying = isPlaying;
                    setEnabled(!isPlaying);
                }
            }
            public class PlayChannelAction implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Sequencer.getInstance().playSequence(channelNo);
                }
            }
        }

        public class StopPlayButton extends JButton implements SequencerChannel.PlayObserver{
            boolean isPlaying = false;
            int channelNo = 0;

            public StopPlayButton(int channelNo){
                this.channelNo = channelNo;
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Stop");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);
                setEnabled(false);

                Sequencer.channels.get(channelNo).subscribeToChannelPlay(this);
                addActionListener(new StopPlayAction());
            }


            @Override
            public void onPlayChange(boolean isPlaying) {
                if(this.isPlaying != isPlaying){
                    setEnabled(isPlaying);
                    this.isPlaying = isPlaying;
                }
            }


            public class StopPlayAction implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Sequencer.getInstance().stopSequencer(channelNo);
                }
            }
        }

        public class LoadSynthButton extends JButton implements Sequencer.SelectedChannelObserver, SequencerChannel.PlayObserver {
            int channelNo = 0;
            SequencerChannel channel;

            public LoadSynthButton(int channelNo){
                this.channelNo = channelNo;
                channel = Sequencer.channels.get(channelNo);
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Load Synth");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);

                Sequencer.subscribeToSelectedChannel(this);
                channel.subscribeToChannelPlay(this);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Sequencer.loadSynthesizerToSequencer(Sequencer.channels.indexOf(channel));
                    }
                });
            }
            @Override
            public void onSelectedChannelChange(int channelNo) {}
            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording){
                    setEnabled(false);
                }else{
                    setEnabled(true);
                }
            }
            @Override
            public void onPlayChange(boolean isPlaying) {
                if(isPlaying){
                    setEnabled(false);
                }else{
                    setEnabled(true);
                }
            }
        }

        public class ClearStepButton extends JButton implements Sequencer.SelectedChannelObserver, SequencerChannel.PlayObserver {
            int channelNo = 0;
            SequencerChannel channel;

            public ClearStepButton(int channelNo){
                this.channelNo = channelNo;
                channel = Sequencer.channels.get(channelNo);
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Clear");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);

                Sequencer.subscribeToSelectedChannel(this);
                channel.subscribeToChannelPlay(this);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        channel.clearSequences();
                    }
                });
            System.out.println(channelNo);
            }




            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording || channel.isPlaying)   setEnabled(false);
                else                                             setEnabled(true);
            }
            @Override
            public void onPlayChange(boolean isPlaying) {
                if(Sequencer.isRecording || channel.isPlaying)   setEnabled(false);
                else                                             setEnabled(true);
            }
            @Override
            public void onSelectedChannelChange(int channelNo) {}

        }

        public class BeatControllSpinner extends JSpinner implements Sequencer.SelectedChannelObserver, SequencerChannel.StepObserver, SequencerChannel.PlayObserver {
            int channelNo = 0;
            int beat = 0;
            Dimension size = new Dimension(35,30);
            SequencerChannel channel;

            public BeatControllSpinner(int channelNo){
                // DATA
                this.channelNo = channelNo;
                channel = Sequencer.channels.get(channelNo);
                SpinnerModel spinnerModel = new SpinnerNumberModel(channel.beat,0,Sequencer.MAX_BEATS,1);

                // APPEARANCE
                setPreferredSize(size);
                setMinimumSize(size);

                // BEHAVIOR
                setFocusable(false);
                Sequencer.subscribeToSelectedChannel(this);
                channel.subscribeToChannelStep(this);
                channel.subscribeToChannelPlay(this);
                addChangeListener(new OnBeatSelect());

            }

            public class OnBeatSelect implements ChangeListener{

                @Override
                public void stateChanged(ChangeEvent e) {
                    if((Integer) getValue() != channel.beat){
                        beat = (int)getValue();

                        int newSteps = (beat * Sequencer.STEPS_PER_BEAT) + channel.stepInBeat;
                        channel.setStep(newSteps);
                    }
                    UI.setFocusToFrame();
                }
            }

            @Override
            public void onSelectedChannelChange(int channelNo) {}

            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording || channel.isPlaying)   setEnabled(false);
                else                                             setEnabled(true);
            }
            @Override
            public void onPlayChange(boolean isPlaying) {
                if(Sequencer.isRecording || channel.isPlaying)   setEnabled(false);
                else                                             setEnabled(true);
            }

            @Override
            public void onStepChange(int absoluteStep, int stepInBeat, int beat) {
                if(this.beat != beat){
                    this.beat = beat;
                    setValue((Integer)beat);
                }
            }

        }




        public class ChannelPresetLabel extends JLabel implements SequencerChannel.ChannelSynthObserver{
            int channelNo = 0;
            SequencerChannel channel;

            public ChannelPresetLabel(int channelNo){
                // - DATA
                this.channelNo = channelNo;
                channel = Sequencer.channels.get(channelNo);
                // - APPEARANCE
                setFont(UI.PRESET_FONT);

                // - BEHAVIOR
                channel.subscribeToChannelSynth(this);
            }


            @Override
            public void onSynthLoad(Synthesizer synthesizer) {
                setText(synthesizer.preset.getName());
            }
        }



        public class StepControl extends JPanel implements SequencerChannel.StepObserver {
            int channelNo = 0;
            int step = 0;
            SequencerChannel channel;
            ArrayList<StepButton> stepButtons = new ArrayList<>();

            public StepControl(int channelNo){
                // - DATA
                this.channelNo = channelNo;
                channel = Sequencer.channels.get(channelNo);
                // - BEHAVIOR
                channel.subscribeToChannelStep(this);
                // - BUTTONS
                for (int i = 0; i < Sequencer.STEPS_PER_BEAT;i++){
                    StepButton stepButton = new StepButton(channelNo,i);
                    stepButtons.add(stepButton);
                    add(stepButton);
                }
                stepButtons.get(channel.stepInBeat).setOn();
            }

            public void setStep(int step){
                if(channelNo != 0){
                    System.out.print("heee");
                }
                if(!Sequencer.isRecording && !channel.isPlaying){
                    stepButtons.get(this.step).setOff();
                    this.step = step;
                    stepButtons.get(step).setOn();


                    channel.setStep((channel.beat*Sequencer.STEPS_PER_BEAT)+step);
                    stepButtons.get(channel.stepInBeat).setOn();

                    System.out.println(step);
                    System.out.println(channel.absoluteStep);
                }
            }

            @Override
            public void onStepChange(int absoluteStep, int stepInBeat, int beat) {
                if(this.step != stepInBeat){
                    stepButtons.get(this.step).setOff();
                    stepButtons.get(stepInBeat).setOn();
                    this.step = stepInBeat;
                }
            }


            public class StepButton extends JButton {
                int step = 0;
                Dimension size  = new Dimension(23, 8);
                Color onColor   = new Color(250,20,20);
                Color offColor  = new Color(50,50,50);

                public StepButton(int channelNo, int step) {
                    this.step = step;
                    // - APPEARANCE
                    setPreferredSize(size);
                    setMinimumSize(size);
                    setMaximumSize(size);
                    setBackground(offColor);

                    // - Behavior
                    setFocusable(false);
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setStep(step);
                        }
                    });

                }
                public void setOn(){setBackground(onColor);}
                public void setOff(){setBackground(offColor);}

            }

        }



    }

}
