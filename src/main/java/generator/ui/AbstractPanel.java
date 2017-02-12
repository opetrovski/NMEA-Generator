package generator.ui;

import javax.swing.*;
import java.awt.*;


abstract public class AbstractPanel extends JPanel {

    protected JTextField addInputField(JPanel p, String label, int inputSize, boolean readonly) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.NONE; //BOTH;//NONE HORIZONTAL
        c.weightx = 0;//1.0;
        c.weighty = 0;//1.0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 3, 3, 3);
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        p.add(new JLabel(label), c);

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        JTextField tf = new JTextField(inputSize);
        tf.setEnabled(!readonly);
        p.add(tf, c);
        return tf;
    }

    protected JTextField addInputField(JPanel p, String label, int inputSize) {
        return addInputField(p, label, inputSize, false);
    }

    protected JCheckBox addCheckboxField(JPanel p, String label) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.NONE; //BOTH;//NONE HORIZONTAL
        c.weightx = 0;//1.0;
        c.weighty = 0;//1.0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 3, 3, 3);
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        p.add(new JLabel(label), c);

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        JCheckBox cb = new JCheckBox();
        p.add(cb, c);
        return cb;
    }
}
