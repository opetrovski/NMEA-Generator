package generator.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class SteeringPanel extends AbstractPanel {

    public SteeringPanel() {
        setLayout(new GridLayout(2, 1, 5, 5));
        add(new SteeringWheelPanel());
        add(createCompassPanel());
    }

    private JPanel createSpeedPanel() {

        JSlider throttle = new JSlider(JSlider.VERTICAL, 0, 40, 0);
        throttle.setMajorTickSpacing(10);
        throttle.setMinorTickSpacing(5);
        throttle.setPaintTicks(true);
        throttle.setPaintLabels(true);
        throttle.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int value = source.getValue();
                    System.out.println(Integer.toString(value));
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMinimumSize(new Dimension(150, 200));
        panel.setMaximumSize(new Dimension(150, 200));
        panel.setPreferredSize(new Dimension(150, 200));
        panel.add(throttle);
        panel.setBorder(BorderFactory.createTitledBorder("Speed in Knots"));
        return panel;

    }

    private void addSlider(JPanel p, JSlider slider) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.NONE; //BOTH;//NONE HORIZONTAL
        c.weightx = 0;//1.0;
        c.weighty = 0;//1.0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 3, 3, 3);
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        p.add(slider, c);

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        final JTextField tf = new JTextField(3);
        tf.setText("0");
        tf.setEnabled(false);
        p.add(tf, c);

    }

    private JPanel createCurrentPositionPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        addInputField(p, "Latitude", 8, true);
        addInputField(p, "Longitude", 8, true);
        p.setBorder(
                BorderFactory.createTitledBorder("Current Position"));
        return p;
    }

    private JPanel createCompassPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        addInputField(p, "Compass course", 8, true);
        addInputField(p, "Deviation", 8, true);
        addInputField(p, "Magnetic course", 8, true);
        addInputField(p, "Declination", 8, true);
        addInputField(p, "True course", 8, true);
        p.setBorder(BorderFactory.createTitledBorder("Course"));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(p, BorderLayout.WEST);
        panel.add(createCurrentPositionPanel(), BorderLayout.CENTER);
        panel.add(createSpeedPanel(), BorderLayout.EAST);
        return panel;
    }

}
