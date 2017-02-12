package generator.ui;


import generator.compass.DeclinationTable2013;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.EventObject;

public class DeclinationtablePanel extends AbstractPanel {

    public DeclinationtablePanel() {
        DevTableModel tablemodel = new DevTableModel();
        JTable table = new JTable(tablemodel);
        table.getColumn( "Latitude" ).setMaxWidth( 70 );
        table.getColumn( "Latitude" ).setMinWidth( 70 );
        table.getColumn( "Longitude" ).setMaxWidth( 80 );
        table.getColumn( "Longitude" ).setMinWidth( 80 );
        table.getColumn( "Declination" ).setMaxWidth( 80 );
        table.getColumn( "Declination" ).setMinWidth( 80 );

        TableColumn col = table.getColumnModel().getColumn(1);
        col.setCellEditor(new DevTableCellEditor());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //SINGLE_SELECTION MULTIPLE_INTERVAL_SELECTION

        JScrollPane scrollpane = new JScrollPane(table);
        setLayout(new GridLayout());
        add(scrollpane);
        setMaximumSize(new Dimension( 260,200 ));
        setMinimumSize(new Dimension( 260,200 ));
        setPreferredSize(new Dimension( 260,200 ));
        setBorder(BorderFactory.createTitledBorder("Declination"));
    }

    class DevTableModel extends AbstractTableModel {
        final String[] columnNames = {"Latitude", "Longitude", "Declination"};

        //public void setData(Integer[][] data){ this.data = data; }
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return DeclinationTable2013.declination_table.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return DeclinationTable2013.declination_table[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public void setValueAt(int value, int row, int col) {
            DeclinationTable2013.declination_table[row][col] = value;
            fireTableCellUpdated(row, col);
        }

    }

    class DevTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        JComponent component = new JTextField();

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int rowIndex, int vColIndex) {

            ((JTextField) component).setText((String) value);

            return component;
        }

        public boolean isCellEditable(EventObject evt) {
//            if (evt instanceof MouseEvent) {
//                return ((MouseEvent) evt).getClickCount() >= 2;
//            }
            return true;
        }

        public Object getCellEditorValue() {
            return ((JTextField) component).getText();
        }
    }
}
