package generator.ui;

import generator.nmea.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JTextArea monitorTextArea;
    PipedInputStream pisMonitor = new PipedInputStream();

    public SettingsPanel(Compass compass, GPS gps, Yacht yacht, NmeaDispatcher dispatcher) throws Exception{
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

        PipedOutputStream pos = new PipedOutputStream(pisMonitor);
        dispatcher.setMonitorPipe(pos);

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("GPS", null, createGPSPanel(), null);
        tabbedPane.addTab("Compass", null, createCompassPanel(), null);
        tabbedPane.addTab("Connection", null, createConnectionPanel(), null);
        add(tabbedPane, BorderLayout.CENTER);
        startMonitoring();
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

    private JPanel createGPSPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBorder(new TitledBorder("NMEA Sentence Transmission"));
        JTextField tf = addInputField(p, "Transmission Interval [sec]", 2);
        tf.setText(prop.getProperty(GPS_INTERVAL));
        JCheckBox cb = addCheckboxField(p, "GPS is Sending Data");
        cb.setSelected(Boolean.parseBoolean(prop.getProperty(GPS_ENABLED)));

        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        JTextField tf1 = addInputField(p2, "Latitude", 5);
        tf1.setText(prop.getProperty(GPS_LATITUDE));
        JTextField tf2 = addInputField(p2, "Longitude", 5);
        tf2.setText(prop.getProperty(GPS_LONGITUDE));

        JPanel p4 = new JPanel();
        p4.setBorder(new TitledBorder("Startposition"));
        p4.setLayout(new GridLayout(2,1));
        p4.add(p2);
        JButton posBtn = addButton(p4, "Change Current Position");

        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(p, BorderLayout.NORTH);
        p3.add(p4, BorderLayout.CENTER);

        cb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                gps.setEnabled(cb.isSelected());
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
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter the longitude of the start position (e.g. 12.34).", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        return p3;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(new DeviationtablePanel());
        p.add(new DeclinationtablePanel());
        return p;
    }

    private JPanel createCompassPanel() {
        JPanel p = new JPanel();
        p.setBorder(new TitledBorder("NMEA Sentence Transmission"));
        p.setLayout(new GridBagLayout());
        JTextField tf = addInputField(p, "Transmission Interval [sec]", 2);
        tf.setText(prop.getProperty(COMPASS_INTERVAL));
        JCheckBox cb = addCheckboxField(p, "Compass is Sending Data");
        cb.setSelected(Boolean.parseBoolean(prop.getProperty(COMPASS_ENABLED)));

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
            }
        });


        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(p, BorderLayout.NORTH);
        p2.add(createTablePanel(), BorderLayout.CENTER);
        return p2;
    }

    private JPanel createConnectionPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JTextField tf = addInputField(p, "Listen on IP Port", 7);
        tf.setText(prop.getProperty(CONNECTION_PORT));

        JPanel p3 = new JPanel();
        p3.setBorder(new TitledBorder("Server Settings"));
        p3.setLayout(new GridLayout(2,1));
        p3.add(p);
        JButton resetBtn = addButton(p3, "Reset");
        resetBtn.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent ae){
                dispatcher.setPort(Integer.parseInt(tf.getText()));
                ResetThread thread = new ResetThread(dispatcher.getSocket());
                thread.start();
            }
        });

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.setBorder(new TitledBorder("Server Monitor"));
        monitorTextArea = new JTextArea();
        p2.add(monitorTextArea);

        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(p3, BorderLayout.NORTH);
        p1.add(p2, BorderLayout.CENTER);

        return p1;
    }

    private void startMonitoring(){
        Runnable r = new Runnable(){
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(pisMonitor));
                    String s;
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }

                        while (in.ready()) {
                            s = in.readLine();
                            monitorTextArea.append(s);
                            monitorTextArea.append("\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(r).start();

    }
}
