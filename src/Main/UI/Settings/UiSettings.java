package Main.UI.Settings;

import Main.Components.Instrument.Pianoroll;
import Main.Components.Preset.SynthesizerPreset;
import Main.Components.Sequencer.Metronome;
import Main.Components.Sequencer.Sequencer;
import Main.UI.UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UiSettings extends JPanel {
    public UiSettings() {
        // - APPEARANCE
        setBorder(UI.getGroupTitleBorder("Synthesizer Settings"));


        // - LAYOUT
        setLayout(new GridLayout(2, 1));

        // -- TOP
        JPanel instrumentBpmPanel = new JPanel();
        instrumentBpmPanel.setLayout(new GridLayout(1, 2));
        instrumentBpmPanel.add(new PresetComboBox());
        instrumentBpmPanel.add(new BpmSpinner());
        add(instrumentBpmPanel);

        // -- BOTTOM
        add(new MetronomeButton());


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
            setSize(50, 30);

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
            setSize(200, 50);

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
                setVisible(false);
                setVisible(true);
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
}
