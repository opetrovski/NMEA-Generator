package generator.ui;

import generator.nmea.Yacht;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class SteeringPanel extends AbstractPanel {

    Yacht yacht;
    JTextField latitude, longitude, compassCourse, deviation, magneticCourse, declination, trueCourse;

    public SteeringPanel(Yacht yacht) {
        this.yacht = yacht;
        setLayout(new GridLayout(2, 1, 5, 5));
        add(new SteeringWheelPanel(yacht));
        add(createCompassPanel());
        updateGUI();
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
                    yacht.setSpeedInKnots(value);
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

    private JPanel createCurrentPositionPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        latitude = addInputField(p, "Latitude", 8, true);
        longitude = addInputField(p, "Longitude", 8, true);
        p.setBorder(
                BorderFactory.createTitledBorder("Current Position"));
        return p;
    }


    private JPanel createCompassPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        compassCourse = addInputField(p, "Compass course", 8, true);
        deviation = addInputField(p, "Deviation", 8, true);
        magneticCourse = addInputField(p, "Magnetic course", 8, true);
        declination = addInputField(p, "Declination", 8, true);
        trueCourse = addInputField(p, "True course", 8, true);
        p.setBorder(BorderFactory.createTitledBorder("Course"));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(p, BorderLayout.WEST);
        panel.add(createCurrentPositionPanel(), BorderLayout.CENTER);
        panel.add(createSpeedPanel(), BorderLayout.EAST);
        return panel;
    }

    private void updateGUI(){
        Runnable r = new Runnable(){
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    latitude.setText(format(yacht.getLatitude()));
                    longitude.setText(format(yacht.getLongitude()));
                    compassCourse.setText(formatCourse(yacht.getHeading_compass()));
                    deviation.setText(formatCourse(yacht.getCompass_deviation()));
                    magneticCourse.setText(formatCourse(yacht.getHeading_magnetic()));
                    declination.setText(formatCourse(yacht.getCompass_declination()));
                    trueCourse.setText(formatCourse(yacht.getHeading_true()));
                }
            }

            private String format(double value){
                return String.format( "%.2f", value);
            }

            private String formatCourse(double value){
                return String.format( "%.0f", value);
            }

        };

        new Thread(r).start();
    }
}
