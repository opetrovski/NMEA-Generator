package generator.ui;

import generator.nmea.Compass;
import generator.nmea.GPS;
import generator.nmea.NmeaDispatcher;
import generator.nmea.Yacht;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Main extends JFrame {


    public Main() throws Exception {
        setTitle("NMEA Generator");
        setSize(600, 450);
        setMinimumSize(new Dimension(600, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis);

        NmeaDispatcher dispatcher = new NmeaDispatcher(pis);
        Yacht yacht = new Yacht();
        GPS gps = new GPS(yacht, pos);
        Compass compass = new Compass(yacht, pos);
        SettingsPanel settingsPanel = new SettingsPanel(compass, gps, yacht, dispatcher);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Steering", null, new SteeringPanel(yacht),
                "Set speed and direction");
//        tabbedPane.addTab("AIS", null, new AISRadarPanel(),
//                "Set number and position of other vessels");
//        tabbedPane.addTab("Auto Pilot", null, new JPanel(), "");
        tabbedPane.addTab("Settings", null, settingsPanel,
                "Enable Components, define ports and intervalls");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        getContentPane().add(tabbedPane);

//        dispatcher.start();
        yacht.start();
//        gps.start();
//        compass.start();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main f = new Main();
                    f.setVisible(true);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace(System.err);
                }
            }
        });
    }

}
