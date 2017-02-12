package generator.ui;

import generator.nmea.Compass;
import generator.nmea.GPS;
import generator.nmea.NmeaDispatcher;
import generator.nmea.Yacht;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;


public class SettingsPanel extends AbstractPanel {

    public static final String PROPERTY_FILENAME = "nmea-generator.properties";
    public static final String GPS_INTERVAL = "gps.interval";
    public static final String GPS_LONGITUDE = "gps.start_position.longitude";
    public static final String GPS_LATITUDE = "gps.start_position.latitude";
    public static final String GPS_ENABLED = "gps.enabled";
    public static final String COMPASS_INTERVAL = "compass.interval";
    public static final String COMPASS_ENABLED = "compass.enabled";
    public static final String CONNECTION_PORT = "connection.port";

    Properties prop;
    Compass compass;
    GPS gps;
    Yacht yacht;
    NmeaDispatcher dispatcher;

    public SettingsPanel(Compass compass, GPS gps, Yacht yacht, NmeaDispatcher dispatcher) {
        this.compass = compass;
        this.gps = gps;
        this.yacht = yacht;
        this.dispatcher = dispatcher;
        loadProperties();
        gps.setInterval(Integer.parseInt(prop.getProperty(GPS_INTERVAL)));
        gps.setEnabled(Boolean.parseBoolean(prop.getProperty(GPS_INTERVAL)));
        compass.setInterval(Integer.parseInt(prop.getProperty(COMPASS_INTERVAL)));
        compass.setEnabled(Boolean.parseBoolean(prop.getProperty(COMPASS_INTERVAL)));
        yacht.setLongitude(Double.parseDouble(prop.getProperty(GPS_LONGITUDE)));
        yacht.setLatitude(Double.parseDouble(prop.getProperty(GPS_LATITUDE)));
        dispatcher.setPort(Integer.parseInt(prop.getProperty(CONNECTION_PORT)));

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 1));
        p.add(createGPSPanel());
        p.add(createCompassPanel());
        p.add(createConnectionPanel());

        setLayout(new BorderLayout());
        add(createTablePanel(), BorderLayout.WEST);
        add(p, BorderLayout.CENTER);
    }

    private void loadProperties() {
        prop = new Properties();
        try (InputStream inputStream = getPropertyFileInputStream()) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + PROPERTY_FILENAME + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    private InputStream getPropertyFileInputStream() throws Exception {

        String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
        String absolutePath = cwd + File.separator + PROPERTY_FILENAME;
        File f = new File(absolutePath);
        if (f.exists()) {
            System.out.println("loading properties from " + absolutePath);
            return new FileInputStream(absolutePath);
        }

        String dir = System.getProperty("user.dir");
        absolutePath = dir + File.separator + PROPERTY_FILENAME;
        f = new File(absolutePath);
        if (f.exists()) {
            System.out.println("loading properties from " + absolutePath);
            return new FileInputStream(PROPERTY_FILENAME);
        }

        f = new File(PROPERTY_FILENAME);
        if (f.exists()) {
            System.out.println("loading properties from " + PROPERTY_FILENAME);
            return new FileInputStream(PROPERTY_FILENAME);
        }

        InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILENAME);
        return in;
    }

    private OutputStream getPropertyFileOutputStream() throws Exception {

        String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
        String absolutePath = cwd + File.separator + PROPERTY_FILENAME;
        File f = new File(absolutePath);
        if (f.exists()) {
            System.out.println("loading properties from " + absolutePath);
            return new FileOutputStream(absolutePath);
        }

        String dir = System.getProperty("user.dir");
        absolutePath = dir + File.separator + PROPERTY_FILENAME;
        f = new File(absolutePath);
        if (f.exists()) {
            System.out.println("loading properties from " + absolutePath);
            return new FileOutputStream(PROPERTY_FILENAME);
        }

        f = new File(PROPERTY_FILENAME);
        if (f.exists()) {
            System.out.println("loading properties from " + PROPERTY_FILENAME);
            return new FileOutputStream(PROPERTY_FILENAME);
        }

        return null;
    }

    private JPanel createGPSPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JTextField tf = addInputField(p, "Transmission Interval [sec]", 2);
        tf.setText(prop.getProperty(GPS_INTERVAL));
        JTextField tf1 = addInputField(p, "Startposition Latitude", 5);
        tf1.setText(prop.getProperty(GPS_LATITUDE));
        JTextField tf2 = addInputField(p, "Startposition Longitude", 5);
        tf2.setText(prop.getProperty(GPS_LONGITUDE));
        JCheckBox cb = addCheckboxField(p, "GPS is Sending Data");
        cb.setSelected(Boolean.parseBoolean(prop.getProperty(GPS_ENABLED)));
        p.setBorder(
                BorderFactory.createTitledBorder("GPS"));

        cb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                gps.setEnabled(cb.isSelected());
                savePropertiesToFile();
            }
        });

        tf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setGPSInterval();
            }

            public void removeUpdate(DocumentEvent e) {
                // setGPSInterval();
            }

            public void insertUpdate(DocumentEvent e) {
                setGPSInterval();
            }

            public void setGPSInterval() {

                try {
                    int interval = Integer.parseInt(tf.getText());
                    gps.setInterval(interval);
                    savePropertiesToFile();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter the number of seconds that the GPS is waiting after each transmission of NMEA sentences.", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        tf1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setGPSLatitude();
            }

            public void removeUpdate(DocumentEvent e) {
                // setGPSLatitude();
            }

            public void insertUpdate(DocumentEvent e) {
                setGPSLatitude();
            }

            public void setGPSLatitude() {

                try {
                    double latitude = Double.parseDouble(tf1.getText());
                    yacht.setLatitude(latitude);
                    savePropertiesToFile();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter the latitude of the start position (e.g. 12.34).", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        tf2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setGPSLongitude();
            }

            public void removeUpdate(DocumentEvent e) {
                // setGPSLongitude();
            }

            public void insertUpdate(DocumentEvent e) {
                setGPSLongitude();
            }

            public void setGPSLongitude() {

                try {
                    double longitude = Double.parseDouble(tf2.getText());
                    yacht.setLongitude(longitude);
                    savePropertiesToFile();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter the longitude of the start position (e.g. 12.34).", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        return p;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 1));
        p.add(new DeviationtablePanel());
        p.add(new DeclinationtablePanel());
        return p;
    }

    private JPanel createCompassPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JTextField tf = addInputField(p, "Transmission Interval [sec]", 2);
        tf.setText(prop.getProperty(COMPASS_INTERVAL));
        JCheckBox cb = addCheckboxField(p, "Compass is Sending Data");
        cb.setSelected(Boolean.parseBoolean(prop.getProperty(COMPASS_ENABLED)));
        p.setBorder(
                BorderFactory.createTitledBorder("Compass"));

        tf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setCompassInterval();
            }

            public void removeUpdate(DocumentEvent e) {
                //setCompassInterval();
            }

            public void insertUpdate(DocumentEvent e) {
                setCompassInterval();
            }

            public void setCompassInterval() {

                try {
                    int interval = Integer.parseInt(tf.getText());
                    compass.setInterval(interval);
                    savePropertiesToFile();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter the number of seconds that the compass is waiting after each transmission of NMEA sentences.", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        cb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compass.setEnabled(cb.isSelected());
                savePropertiesToFile();
            }
        });

        return p;
    }

    private void savePropertiesToFile() {

        if(true)return;

        try (OutputStream out = getPropertyFileOutputStream()) {
            prop.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createConnectionPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JTextField tf = addInputField(p, "Listen on IP Port", 7);
        tf.setText(prop.getProperty(CONNECTION_PORT));
        p.setBorder(
                BorderFactory.createTitledBorder("Connection"));
        return p;
    }

}
