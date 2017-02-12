package generator.ui;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.EventObject;

public class DeviationtablePanel extends AbstractPanel {

    public DeviationtablePanel() {
        DevTableModel tablemodel = new DevTableModel();
        JTable table = new JTable(tablemodel);
        //table.setShowGrid(false);
        // table.setBackground(Color.lightGray);
        //table.setFont(font);

//        SearchListRenderer renderer = new SearchListRenderer();
//        table.setDefaultRenderer(Object.class, renderer);
//
        table.getColumn( "Ship's Heading Magnetic" ).setMaxWidth( 150 );
        table.getColumn( "Ship's Heading Magnetic" ).setMinWidth( 150 );
        table.getColumn( "Deviation" ).setMaxWidth( 80 );
        table.getColumn( "Deviation" ).setMinWidth( 80 );

        TableColumn col = table.getColumnModel().getColumn(1);
        col.setCellEditor(new DevTableCellEditor());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //SINGLE_SELECTION MULTIPLE_INTERVAL_SELECTION
        //table.setRowHeight(16);
        //table.setIntercellSpacing( new Dimension( 5, 5 ) );

        JScrollPane scrollpane = new JScrollPane(table);
//        scrollpane.setMinimumSize( new Dimension( 220,200 ) );
//        scrollpane.setMaximumSize( new Dimension( 220,200 ) );
//        scrollpane.setPreferredSize(new Dimension(220,200));
        //scrollpane.setBorder(BorderFactory.createTitledBorder("Deviationtable"));
        setLayout(new GridLayout());
        add(scrollpane);
        setMaximumSize(new Dimension( 230,200 ));
        setMinimumSize(new Dimension( 230,200 ));
        setPreferredSize(new Dimension( 230,200 ));
        setBorder(BorderFactory.createTitledBorder("Deviation"));
    }

    class DevTableModel extends AbstractTableModel {
        final String[] columnNames = {"Ship's Heading Magnetic", "Deviation"};
        Integer[][] data = new Integer[][]{{0, 2}, {10, 2}, {20, 3}, {30, -2}, {40, 0}, {50, 0}, {60, 1}, {70, 2}, {80, 1}, {90, -1}, {100, -2}, {110, -3}, {120, 0}, {130, 5}, {140, 6}, {150, 7}, {160, 8}, {170, 6}, {180, 4}, {190, 3}, {200, 0}, {210, -2}, {220, -5}, {230, -5}, {240, -4}, {250, -3}, {260, 0}, {270, -2}, {280, 0}, {290, -1}, {300, 0}, {310, 0}, {320, 1}, {330, 0}, {340, 3}, {350, 0}, {360, 2}};

        public void setData(Integer[][] data) {
            this.data = data;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public void setValueAt(int value, int row, int col) {
            data[row][col] = value;
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
