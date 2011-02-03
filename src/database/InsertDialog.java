package database;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Next version ...
 * @author Ahmed Ghanem.
 */
public class InsertDialog extends JDialog {

    private JButton jbtOK = new JButton("OK");
    private JButton jbtCancel = new JButton("Cancel");
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable jTable1 = new JTable(tableModel);
    private Vector newRecord;
    private Vector data = new Vector();

    /** Creates new form InsertDialog */
    public InsertDialog(Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Insert a New Record");
        setModal(true);

        JPanel jPanel1 = new JPanel();
        jPanel1.add(jbtOK);
        jPanel1.add(jbtCancel);

        jbtOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
                data = (Vector) tableModel.getDataVector().get(0);
                JOptionPane.showMessageDialog(null,data);
       
            }
        });
        jbtCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                newRecord = null;
                setVisible(false);
            }
        });

        add(jPanel1, BorderLayout.SOUTH);
        add(new JScrollPane(jTable1), BorderLayout.CENTER);
    }

    public InsertDialog() {
        this(null, true);
    }

    public Vector getNewRecord() {
        return newRecord;
    }

    /** Display the table */
    public void displayTable(Vector columnHeaderVector) {
        tableModel.setRowCount(0);
        this.setSize(new Dimension(700, 200));
        this.setLocationRelativeTo(null);

        tableModel.setColumnIdentifiers(columnHeaderVector);

//     Must create a new vector for a new record
        tableModel.addRow(newRecord = new Vector());
        setVisible(true);
    }
}
