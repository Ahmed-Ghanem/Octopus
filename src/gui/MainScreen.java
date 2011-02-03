package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.RowSet;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import features.Autocomplete;
import features.Cmd;
import features.Cmd.StreamWrapper;
import database.DatabaseConnection;
import features.HighlightSearch;
import main.LookAndFeel;
import factory.NetworkFactory;
import database.InsertDialog;
import factory.DatabaseFactory;
import database.adminstration.Adminstrator;
import files.DumpFile;
import files.DumpOpenFile;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import javax.swing.JPopupMenu;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import files.SaveSqlFile;
import graphics.Uml;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneLayout;
import utils.ConstantManager;

public class MainScreen extends javax.swing.JFrame {

    private About about;
    private Image mainIcon = Toolkit.getDefaultToolkit().getImage(ConstantManager.OCTOPUS_ICON_PATH);
    private UserLogin login;
    private LineBorder lineBorder;
    private String[] editWords;
    private String delemter = " ";
    private DatabaseFactory orclInfo = new DatabaseFactory(); //get information about oracle and oracle
    private NetworkFactory network;
    private DatabaseConnection database;
    private String globalParent;
    private Color bColor;
    private Color fColor;
    private JFileChooser batchChooser;
    private File batchFile;
    private int globalError = 1;
    private HighlightSearch search = new HighlightSearch();
    private Autocomplete popupMenu = new Autocomplete();
    private JScrollPane scroll;
    Runtime rt = Runtime.getRuntime();
    Cmd rte = new Cmd();
    StreamWrapper error, output;
    //-------------------------------------
    //connection
    private DatabaseConnection showTableConnection;
    private Connection sqlConnection;
    private Statement sqlStatement;
    private ResultSet sqlResultSet;
    //table view
    private DefaultTableModel tableModel = new DefaultTableModel();
    private DefaultListSelectionModel listSelectionModel =
            new DefaultListSelectionModel();
    private InsertDialog record;
    //data
    private String sqlCommand;
    private Vector rowVectors = new Vector();
    private Vector columnHeaderVector = new Vector();
    private int colCount;
    //insert
    private Vector colHead = new Vector();
    private RowSet rowset;
    private LookAndFeel look = new LookAndFeel();
    private String part;
    private SaveSqlFile sqlFile; //save sql file
    private UserLogin getLogin;
    private DumpFile saveFile; //save dump file
    private DumpOpenFile dumpOpen; //open dump file
    private OracalError orclError;
    private Uml uml;
    private Process proc;
    private Adminstrator admin;
    private String userName; //create user
    private String passWord; //create user
    //----------
    private MigrationTool migTool;

    /** init GUI component,make objects and table init */
    public MainScreen() throws ClassNotFoundException, SQLException, UnknownHostException {

        initComponents();
        init();
        tableInit();
        sqlTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        jButton11.setVisible(false);

    }

    /***
     * init table view
     */
    public void tableInit() {
        showTableConnection = new DatabaseConnection();
        jTable1.setModel(tableModel);
        jTable1.setSelectionModel(listSelectionModel);
        record = new InsertDialog();

    }

    /***
     * to set table cursor on row
     * @throws Exception caused by using result set
     */
    private void setTableCursor() throws Exception {
        int row = sqlResultSet.getRow();
        listSelectionModel.setSelectionInterval(row - 1, row - 1);

    }

    /*public void setAdminTable(boolean state) {
    jTable2.setEnabled(state);

    }*/
    /***
     * table selection action
     * @param e get event
     */
    void listSelectionModel_valueChanged(ListSelectionEvent e) {
        int selectedRow = jTable1.getSelectedRow();

        try {
            sqlResultSet.absolute(selectedRow + 1);
            setTableCursor();
        } catch (Exception ex) {
        }
    }

    /***
     * to excute non sql statement
     * @param sqlCommand SQL
     * @throws SQLException error in SQL
     */
    public void excuteNonSelect(String nonSqlCommand) throws SQLException {
        sqlConnection = showTableConnection.getConnection();
        sqlStatement = sqlConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        sqlStatement.executeUpdate(nonSqlCommand);

    }

    /***
     *
     * @param sql SQL
     * @return number of rows
     * @throws SQLException error in SQL
     */
    public int showTable(String sql) throws SQLException {
        int numOfRecord = 0;
        // Clear vectors to store new data for the table
        rowVectors.clear();
        columnHeaderVector.clear();
        // clear table.
        tableModel.setDataVector(rowVectors, columnHeaderVector);

        sqlConnection = showTableConnection.getConnection();
        sqlStatement = sqlConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


        // Obtain table contents
        sqlResultSet = sqlStatement.executeQuery(sql);
        // Get column count
        colCount = sqlResultSet.getMetaData().getColumnCount();
        // Store rows to rowVectors
        while (sqlResultSet.next()) {
            Vector singleRow = new Vector();
            for (int i = 0; i < colCount; i++) { // Store cells to a row
                singleRow.addElement(sqlResultSet.getObject(i + 1));
            }
            rowVectors.addElement(singleRow);
            ++numOfRecord;
        }

        // Get column name and add to columnHeaderVector
        for (int i = 1; i <= colCount; i++) {
            columnHeaderVector.addElement(sqlResultSet.getMetaData().getColumnName(i));

        } // Set new data to the table model
        tableModel.setDataVector(rowVectors, columnHeaderVector);
        // number of records on select statment
        return numOfRecord;
    }

    /***
     *
     * @throws ClassNotFoundException
     * @throws SQLException error in SQL
     * @throws UnknownHostException error in host
     */
    public void init() throws ClassNotFoundException, SQLException, UnknownHostException {
        makeObj();
        setLocationRelativeTo(null);

        //show onely the firt item on the list
        hideAllPanels();
        jPanel1.setVisible(true);
        oracleInfo.setVisible(true);

        jPanel4.setVisible(true);

        serverInfo();
        jPanel1.setBorder(new TitledBorder("Connected to ORACLE Server Instance"));
        jPanel4.setBorder(new TitledBorder("Server Information"));
        jPanel3.setBorder(new TitledBorder("Oracle Information"));
        batchProcessing.setBorder(new TitledBorder("Batch Update"));
        exportImport.setBorder(new TitledBorder("Oracle export and import"));

        jPanel6.setBorder(new TitledBorder("SQL Editor"));
        serviceControl.setBorder(new TitledBorder("Start/Stop Oracle Services"));
        jPanel9.setBorder(new TitledBorder("Source File"));
        userAdminstrator.setBorder(new TitledBorder("User Admistration"));
        jPanel7.setBorder(new TitledBorder("New User"));
        jPanel11.setBorder(new TitledBorder("Users"));
        jPanel10.setBorder(new TitledBorder("Oracle System Privileges"));

        setIconImage(mainIcon);

        popupMenu.show(sqlTextArea);
    }

    /***
     * make objects that octopus needed to start
     * @throws ClassNotFoundException
     * @throws SQLException error in SQL
     * @throws UnknownHostException
     */
    public void makeObj() throws ClassNotFoundException, SQLException, UnknownHostException {
        login = new UserLogin();
        lineBorder = new LineBorder(Color.black, 2);
        orclInfo = new DatabaseFactory();
        network = new NetworkFactory();
        database = new DatabaseConnection();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(database.getTablesNames()));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(database.getTablesNames()));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(database.getQueryRes("select username from all_users")));
        scroll = new JScrollPane();
        tableInit();

        // doc = jTextPane1.getStyledDocument();
        //style = jTextPane1.addStyle("Red", null);
        //StyleConstants.setForeground(style, Color.red);


    }

    /***
     * hide all panels to show one per one in list
     */
    public void hideAllPanels() {
        //1
        jPanel1.setVisible(false);
        oracleInfo.setVisible(false);
        jPanel4.setVisible(false);
        //2
        sqlEditor.setVisible(false);
        jPanel6.setVisible(false);
        //3
        serviceControl.setVisible(false);
        jPanel8.setVisible(false);
        //4
        batchProcessing.setVisible(false); // for batch processing
        jTextArea1.setText("");
        //5
        exportImport.setVisible(false);

        saveMenuItem.setVisible(false); //save sql text
        printMenuItem.setVisible(false);//print sql text

        //6
        userAdminstrator.setVisible(false);


    }

    /***
     * get server information at 0
     * @throws SQLException error in SQL
     */
    public void serverInfo() throws SQLException {
        //------------------- Host Informations ----------------
        jLabel1.setText(UserLogin.getUserName() + ".");
        jLabel5.setText(UserLogin.getServerHostname() + ".");
        jLabel6.setText(UserLogin.getPortNumber() + ".");
        //------------------- Network informations -------------
        System.out.println("=============" + orclInfo.getProductVersion() + ".");
        jLabel7.setText(orclInfo.getProductVersion() + ".");
        jLabel9.setText(network.getHostName() + ".");
        jLabel10.setText(network.getIp() + ".");
        //------------------- Database informations -------------
        jLabel15.setText(orclInfo.getOracleDriverName() + ".");
        jLabel21.setText(orclInfo.getOracleDriverVersion() + ".");
        jLabel23.setText(orclInfo.getConnectionUrl() + ".");


    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        sqlEditor = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        rTextScrollPane1 = new org.fife.ui.rtextarea.RTextScrollPane();
        sqlTextArea = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        serviceControl = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel24 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        batchProcessing = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton11 = new javax.swing.JButton();
        oracleInfo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        exportImport = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        userAdminstrator = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton18 = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel42 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        fileMenusItemSeparator = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        toolMenu = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(ConstantManager.OCTOPUS_VERSION);

        jInternalFrame1.setVisible(true);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Server Information", "Service Control", "SQL Editor", "Batch Processing", "Oracle Export", "User Admistration" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setDragEnabled(true);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
        );

        jInternalFrame2.setVisible(true);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/oclogo.png"))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jLabel19.setText("SQL Execution Result");

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jLabel30.setText(" Total Rows: ");
        jToolBar2.add(jLabel30);

        jLabel31.setText("0   ");
        jToolBar2.add(jLabel31);

        sqlTextArea.setColumns(20);
        sqlTextArea.setRows(5);
        sqlTextArea.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                sqlTextAreaCaretUpdate(evt);
            }
        });
        sqlTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sqlTextAreaKeyReleased(evt);
            }
        });
        rTextScrollPane1.setViewportView(sqlTextArea);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(853, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(rTextScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
                        .addGap(112, 112, 112))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(rTextScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/crystal_clear_action_run.png"))); // NOI18N
        jButton1.setMnemonic('r');
        jButton1.setToolTipText("Excute SQL Command(alt+r)");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/clear.png"))); // NOI18N
        jButton2.setToolTipText("Clear");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Font-icon.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/background-icon.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/help.png"))); // NOI18N
        jButton5.setToolTipText("Oracle Help");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/gtk_find_and_replace.png"))); // NOI18N
        jButton6.setToolTipText("Replace");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/error.png"))); // NOI18N
        jButton17.setToolTipText("Oracle error search");
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton17);

        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("Search : ");
        jToolBar1.add(jLabel16);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jToolBar1.add(jTextField1);

        javax.swing.GroupLayout sqlEditorLayout = new javax.swing.GroupLayout(sqlEditor);
        sqlEditor.setLayout(sqlEditorLayout);
        sqlEditorLayout.setHorizontalGroup(
            sqlEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
            .addGroup(sqlEditorLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        sqlEditorLayout.setVerticalGroup(
            sqlEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqlEditorLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Arrow Right.png"))); // NOI18N
        jButton7.setText("Enable ");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/stop.png"))); // NOI18N
        jButton12.setText("Disable");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jScrollPane4.setEnabled(false);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel24.setForeground(new java.awt.Color(255, 0, 0));
        jLabel24.setText("please be patient this action will take few minutes .");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton12))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/database_1.png"))); // NOI18N

        javax.swing.GroupLayout serviceControlLayout = new javax.swing.GroupLayout(serviceControl);
        serviceControl.setLayout(serviceControlLayout);
        serviceControlLayout.setHorizontalGroup(
            serviceControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceControlLayout.createSequentialGroup()
                .addGroup(serviceControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serviceControlLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(serviceControlLayout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel18)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        serviceControlLayout.setVerticalGroup(
            serviceControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceControlLayout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGroup(serviceControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serviceControlLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/batch.png"))); // NOI18N

        jLabel26.setText("Table Name:");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel27.setText("File:");

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jScrollPane5.setViewportView(jTextArea2);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/New File.png"))); // NOI18N
        jButton9.setText("Browse");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/copy.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel28.setText("copy to Database");

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/show.png"))); // NOI18N
        jButton10.setText("View File");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel29.setText("FilePath: No File Selected ..");

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/add_record_32.gif"))); // NOI18N
        jButton11.setText("Insert");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jProgressBar1, 0, 0, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton11))
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout batchProcessingLayout = new javax.swing.GroupLayout(batchProcessing);
        batchProcessing.setLayout(batchProcessingLayout);
        batchProcessingLayout.setHorizontalGroup(
            batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, batchProcessingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, batchProcessingLayout.createSequentialGroup()
                        .addGroup(batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(batchProcessingLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jLabel25)
                .addContainerGap())
        );
        batchProcessingLayout.setVerticalGroup(
            batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(batchProcessingLayout.createSequentialGroup()
                .addGroup(batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(batchProcessingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(batchProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel2.setText("Username:  ");

        jLabel3.setText("Hostname: ");

        jLabel4.setText("Port: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(267, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setText("ORACLE Version:");

        jLabel12.setText("Network Name:");

        jLabel13.setText("IP:");

        jLabel7.setText("jLabel7");

        jLabel9.setText("jLabel9");

        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jLabel14.setText("Driver Name: ");

        jLabel15.setText("jLabel15");

        jLabel20.setText("Driver Version:");

        jLabel21.setText("jLabel21");

        jLabel22.setText("DBMS Url:");

        jLabel23.setText("jLabel23");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel21))))
                .addContainerGap(677, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addContainerGap(109, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout oracleInfoLayout = new javax.swing.GroupLayout(oracleInfo);
        oracleInfo.setLayout(oracleInfoLayout);
        oracleInfoLayout.setHorizontalGroup(
            oracleInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oracleInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(oracleInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        oracleInfoLayout.setVerticalGroup(
            oracleInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oracleInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/database-7.png"))); // NOI18N

        jLabel34.setText("To export the entire database to a single file ");

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Crystal_Clear_app_database.png"))); // NOI18N
        jButton13.setText("Export");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel35.setText("To dump a single schema (Current Schema)");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Database 3.png"))); // NOI18N
        jButton14.setText("Export");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel36.setText("To export specific table : ");

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Database 3.png"))); // NOI18N
        jButton15.setText("Export");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel37.setText("To export specific table : ");

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Database 3.png"))); // NOI18N
        jButton16.setText("Export");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jTextField2.setText("example : dept,emp,salGrade");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel38.setText("The Export and Import utilities provide a simple way for you to transfer data objects between Oracle databases");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextField2))
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addComponent(jButton13)
                .addGap(45, 45, 45)
                .addComponent(jLabel35)
                .addGap(18, 18, 18)
                .addComponent(jButton14)
                .addGap(54, 54, 54)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton15)
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel37)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jButton16)
                .addGap(91, 91, 91))
        );

        jTabbedPane1.addTab("Export", jPanel2);

        javax.swing.GroupLayout exportImportLayout = new javax.swing.GroupLayout(exportImport);
        exportImport.setLayout(exportImportLayout);
        exportImportLayout.setHorizontalGroup(
            exportImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exportImportLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        exportImportLayout.setVerticalGroup(
            exportImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/man-key.png"))); // NOI18N

        jLabel40.setText("User Name : ");

        jLabel41.setText("Password : ");

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Arrow Right.png"))); // NOI18N
        jButton18.setText("OK");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel43.setForeground(new java.awt.Color(0, 0, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(404, 404, 404)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton18)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Administer", "dba"},
                {"Administer", "Administer Any SQL Tuning Set"},
                {"Administer", "Administer Database Trigger"},
                {"Administer", "Administer Resource Manager"},
                {"Administer", "Administer SQL Management Object"},
                {"Administer", "Administer SQL Tuning Set"},
                {"Administer", "Flashback Archive Administrator"},
                {"Administer", "Grant Any Object Privilege"},
                {"Administer", "Grant Any Privilege"},
                {"Administer", "Grant Any Role"},
                {"Administer", "Manage Scheduler"},
                {"Administer", "Manage Tablespace"},
                {" Alter Any Privileges", "Alter Any Cluster"},
                {" Alter Any Privileges", "Alter Any Cube"},
                {" Alter Any Privileges", "Alter Any Cube Dimension"},
                {" Alter Any Privileges", "Alter Any Dimension"},
                {" Alter Any Privileges", "Alter Any Evaluation Context"},
                {" Alter Any Privileges", "Alter Any Index"},
                {" Alter Any Privileges", "Alter Any Indextype"},
                {" Alter Any Privileges", "Alter Any Library"},
                {" Alter Any Privileges", "Alter Any Materialized View"},
                {" Alter Any Privileges", "Alter Any Mining Model"},
                {" Alter Any Privileges", "Alter Any Operator"},
                {" Alter Any Privileges", "Alter Any Outline"},
                {" Alter Any Privileges", "Alter Any Procedure"},
                {" Alter Any Privileges", "Alter Any Role"},
                {" Alter Any Privileges", "Alter Any Rule"},
                {" Alter Any Privileges", "Alter Any Rule Set"},
                {" Alter Any Privileges", "Alter Any Sequence"},
                {" Alter Any Privileges", "Alter Any SQL Profile"},
                {" Alter Any Privileges", "Alter Any Table"},
                {" Alter Any Privileges", "Alter Any Trigger"},
                {" Alter Any Privileges", "Alter Any Type"},
                {"Alter Privileges", "Alter Database"},
                {"Alter Privileges", "Alter Profile"},
                {"Alter Privileges", "Alter Resource Cost"},
                {"Alter Privileges", "Alter Rollback Segment"},
                {"Alter Privileges", "Alter Session"},
                {"Alter Privileges", "Alter System"},
                {"Alter Privileges", "Alter Tablespace"},
                {"Alter Privileges", "Alter User"},
                {"Audit Privileges", "Audit Any"},
                {"Audit Privileges", "Audit System"},
                {"Backup Privileges", "Backup Any Table"},
                {"Create Privileges", "Create Cluster"},
                {"Create Privileges", "Create Database Link"},
                {"Create Privileges", "Create Mining Model"},
                {"Create Privileges", "Create Operator"},
                {"Create Privileges", "Create Procedure"},
                {"Create Privileges", "Create Profile"},
                {"Create Privileges", "Create Role"},
                {"Create Privileges", "Create Rollback Segment"},
                {"Create Privileges", "Create Rule"},
                {"Create Privileges", "Create Rule Set"},
                {"Create Privileges", "Create Sequence"},
                {"Create Privileges", "Create Session"},
                {"Create Privileges", "Create Synonym"},
                {"Create Privileges", "Create Table"},
                {"Create Privileges", "Create Tablespace"},
                {"Create Privileges", "Create Trigger"},
                {"Create Privileges", "Create Type"},
                {"Create Privileges", "Create User"},
                {"Create Privileges", "Create View"},
                {"Database", "Alter Database"},
                {"Database", "Alter System"},
                {"Database", "Audit System"},
                {"Delete", "Delete Any Cube Dimension"},
                {"Delete", "Delete Any Measure Folder"},
                {"Delete", "Delete Any Table"},
                {"Export & Import", "Export Full Database"},
                {"Export & Import", "Import Full Database"},
                {"Force", "Force Any Transaction"},
                {"Force", "Force Transaction"},
                {"Indexes", "Alter Any Index"},
                {"Indexes", "Create Any Index"},
                {"Indexes", "Drop Any Index"},
                {"Locks", "Lock Any Table"},
                {"Procedures", "Alter Any Procedure"},
                {"Procedures", "Create Any Procedure"},
                {"Procedures", "Create Procedure"},
                {"Procedures", "Drop Any Procedure"},
                {"Procedures", "Execute Any Procedure"},
                {"Select", "Select Any Sequence"},
                {"Select", "Select Any Table"},
                {"Select", "Select Any Transaction"},
                {"Sequence", "Alter Any Sequence"},
                {"Sequence", "Create Any Sequence"},
                {"Sequence", "Create Sequence"},
                {"Sequence", "Drop Any Sequence"},
                {"Sequence", "Select Any Sequence"},
                {"Sys Privileges", "SYSDBA"},
                {"Sys Privileges", "SYSOPER"},
                {"Table", "Alter Any Table"},
                {"Table", "Backup Any Table"},
                {"Table", "Comment Any Table"},
                {"Table", "Create Any Table"},
                {"Table", "Create Table"},
                {"Table", "Delete Any Table"},
                {"Table", "Drop Any Table"},
                {"Table", "Flashback Any Table"},
                {"Table", "Insert Any Table"},
                {"Table", "Lock Any Table"},
                {"Table", "Select Any Table"},
                {"Table", "Update Any Table"},
                {"User", "Alter User"},
                {"User", "Become User"},
                {"User", "Create User"},
                {"User", "Drop User"},
                {"View", "Create Any View"},
                {"View", "Create View"},
                {"View", "Drop Any View"},
                {"View", " Flashback Any Table"},
                {"View", "Merge Any View"},
                {"View", "Under Any View"}
            },
            new String [] {
                "Sort as", "Privileges"
            }
        ));
        jScrollPane1.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel42.setText("All user in oracle database :");

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Key (3).png"))); // NOI18N
        jButton19.setText("Add Privileges");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/revoke.png"))); // NOI18N
        jButton22.setText("Revoke Privileges");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/cancel.png"))); // NOI18N
        jButton25.setText("Drop User");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton22)
                        .addGap(18, 18, 18)
                        .addComponent(jButton19))
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton22)
                    .addComponent(jButton19)
                    .addComponent(jButton25))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout userAdminstratorLayout = new javax.swing.GroupLayout(userAdminstrator);
        userAdminstrator.setLayout(userAdminstratorLayout);
        userAdminstratorLayout.setHorizontalGroup(
            userAdminstratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userAdminstratorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userAdminstratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userAdminstratorLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(userAdminstratorLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel39)
                        .addGap(18, 18, 18))))
        );
        userAdminstratorLayout.setVerticalGroup(
            userAdminstratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userAdminstratorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userAdminstratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jInternalFrame2.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oracleInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addComponent(sqlEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(54, Short.MAX_VALUE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addComponent(serviceControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(68, Short.MAX_VALUE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addComponent(batchProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(118, Short.MAX_VALUE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(exportImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(userAdminstrator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(28, Short.MAX_VALUE)))
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oracleInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addComponent(sqlEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame2Layout.createSequentialGroup()
                    .addContainerGap(74, Short.MAX_VALUE)
                    .addComponent(serviceControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame2Layout.createSequentialGroup()
                    .addContainerGap(100, Short.MAX_VALUE)
                    .addComponent(batchProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame2Layout.createSequentialGroup()
                    .addContainerGap(69, Short.MAX_VALUE)
                    .addComponent(exportImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jInternalFrame2Layout.createSequentialGroup()
                    .addGap(85, 85, 85)
                    .addComponent(userAdminstrator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(96, Short.MAX_VALUE)))
        );

        fileMenu.setText("File");
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(jSeparator3);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/save.png"))); // NOI18N
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        printMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/print.png"))); // NOI18N
        printMenuItem.setText("Print");
        printMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(printMenuItem);
        fileMenu.add(fileMenusItemSeparator);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/1588889018.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        viewMenu.setText("View");

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Theme 1");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        viewMenu.add(jMenuItem9);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Theme 2");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        viewMenu.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Theme 3");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        viewMenu.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Theme 4");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        viewMenu.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Theme 5");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        viewMenu.add(jMenuItem8);

        jMenuBar1.add(viewMenu);

        toolMenu.setText("Tools");
        toolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolMenuActionPerformed(evt);
            }
        });

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/cmd.png"))); // NOI18N
        jMenuItem11.setText("Windows Command Line");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        toolMenu.add(jMenuItem11);
        toolMenu.add(jSeparator5);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/logo.png"))); // NOI18N
        jMenuItem10.setText("Generate Schema UML");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        toolMenu.add(jMenuItem10);

        jMenuBar1.add(toolMenu);

        windowMenu.setText("Window");
        jMenuBar1.add(windowMenu);

        jMenu6.setText("Help");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Bug_logo.png"))); // NOI18N
        jMenuItem2.setText("Report a Bug");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Suggestion_logo.jpg"))); // NOI18N
        jMenuItem3.setText("Send Suggetion");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/about_icon.png"))); // NOI18N
        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jInternalFrame2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jInternalFrame1)
            .addComponent(jInternalFrame2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        System.out.println(jList1.getSelectedIndex());


        if (jList1.getSelectedIndex() == 0) { // informations
            hideAllPanels();
            jPanel1.setVisible(true);
            oracleInfo.setVisible(true);
            jPanel4.setVisible(true);
            saveMenuItem.setVisible(false);
            printMenuItem.setVisible(false);



        } else if (jList1.getSelectedIndex() == 1) { //service control
            hideAllPanels();
            serviceControl.setVisible(true);
            jPanel8.setVisible(true);
            saveMenuItem.setVisible(false);
            printMenuItem.setVisible(false);




        } else if (jList1.getSelectedIndex() == 2) { // sql editor
            hideAllPanels();
            sqlEditor.setVisible(true);
            jPanel6.setVisible(true);
            saveMenuItem.setVisible(true);
            printMenuItem.setVisible(true);



        } else if (jList1.getSelectedIndex() == 3) { // batch processing
            hideAllPanels();
            batchProcessing.setVisible(true);
            saveMenuItem.setVisible(false);
            printMenuItem.setVisible(false);



        } else if (jList1.getSelectedIndex() == 4) { // export emport
            hideAllPanels();
            saveMenuItem.setVisible(false);
            printMenuItem.setVisible(false);
            exportImport.setVisible(true);

        } else if (jList1.getSelectedIndex() == 5) { // user adminstration
            hideAllPanels();
            saveMenuItem.setVisible(false);
            printMenuItem.setVisible(false);
            userAdminstrator.setVisible(true);
            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(database.getQueryRes("select username from all_users")));


        }


    }//GEN-LAST:event_jList1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (sqlTextArea.getText().trim().length() != 0) {
            jLabel31.setText("" + 0);
            sqlCommand = sqlTextArea.getText();
            rowVectors.clear();
            columnHeaderVector.clear();
            tableModel.setDataVector(rowVectors, columnHeaderVector);

            if (sqlCommand.trim().toUpperCase().startsWith("SELECT")) {
                try {
                    jLabel31.setText("" + showTable(sqlCommand));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {
                    excuteNonSelect(sqlCommand);
                    JOptionPane.showMessageDialog(null, "Execution finished 0 error(s) occurred.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid : write sql command first ", "Info", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sqlTextArea.setText(null);//clear the JTextPane
    }//GEN-LAST:event_jButton2ActionPerformed
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jLabel24.setVisible(true);
        jTextArea1.setText("");
        jTextArea1.setText("Oracle Services Started...\n\n");


        try {
            String[] commands = {"net start OracleDBConsoleORCL",
                "net start OracleJobSchedulerORCL",
                "net start OracleOraDb10g_home1iSQL*Plus",
                "net start OracleOraDb10g_home1TNSListener",
                "net start OracleServiceORCL"
            };


            for (int i = 0; i
                    <= 4; i++) {
                Process proc = rt.exec(commands[i]);
                error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
                output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");


                int exitVal = 0;
                error.start();
                output.start();
                error.join(3000);
                output.join(3000);
                //exitVal = proc.waitFor();
                jTextArea1.append(output.message + error.message + "\n\n");



            }

        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //change the sql text color
        fColor = JColorChooser.showDialog(MainScreen.this, "Choose Color", bColor);
        sqlTextArea.setForeground(fColor);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        bColor = JColorChooser.showDialog(MainScreen.this, "Choose Color", bColor);
        sqlTextArea.setBackground(bColor);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        jTextArea1.setText("");
        jTextArea1.setText("Oracle Services Stopped...\n\n");


        try {
            String[] commands = {"net stop OracleDBConsoleORCL",
                "net stop OracleJobSchedulerORCL",
                "net stop OracleOraDb10g_home1iSQL*Plus",
                "net stop OracleOraDb10g_home1TNSListener",
                "net stop OracleServiceORCL"
            };


            for (int i = 0; i <= 4; i++) {
                Process proc = rt.exec(commands[i]);
                error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
                output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");


                int exitVal = 0;
                error.start();
                output.start();
                error.join(3000);
                output.join(3000);
                //exitVal = proc.waitFor();
                jTextArea1.append(output.message + error.message + "\n\n");

            }

        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //get table names in combo box
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // batch file action .
        jTextArea2.setText(null);
        jProgressBar1.setStringPainted(true);
        jProgressBar1.setValue(0);
        batchFile = null;
        batchChooser = new JFileChooser();


        int returnValue = batchChooser.showOpenDialog(null);


        if (returnValue == JFileChooser.APPROVE_OPTION) {
            batchFile = batchChooser.getSelectedFile(); //get file
            jLabel29.setText("FilePath: " + batchFile.getAbsolutePath());


            int dotPos = batchFile.getAbsolutePath().lastIndexOf(".");
            String extension = batchFile.getAbsolutePath().substring(dotPos);
            globalParent = batchFile.getParent();


            if (!extension.equals(".txt")) {
                jLabel29.setText("FilePath: No File Selected ..");
                batchFile = null;
                JOptionPane.showMessageDialog(null, "Not Supported File", "Error", JOptionPane.ERROR_MESSAGE);


            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTextArea2.setText(null);
        showFile(
                batchFile);


    }//GEN-LAST:event_jButton10ActionPerformed
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (batchFile == null) {
            JOptionPane.showMessageDialog(null, "Please choose batch first.", "Info", JOptionPane.INFORMATION_MESSAGE);



        } else {
            insertRows((String) jComboBox1.getSelectedItem());


        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            URI uri = new URI("http://www.oracle.com/pls/db102/help");
            Desktop desktop = null;


            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();


            }

            if (desktop != null) {
                desktop.browse(uri);


            }
        } catch (IOException ioe) {
            ioe.printStackTrace();


        } catch (URISyntaxException use) {
            use.printStackTrace();


        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        about = new About();
        about.show();

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String replace = JOptionPane.showInputDialog(null, "Replace word:", "Replace", JOptionPane.QUESTION_MESSAGE);
        String replacedWith = JOptionPane.showInputDialog(null, "With", "Replace", JOptionPane.QUESTION_MESSAGE);
        String sqlCommand = sqlTextArea.getText();
        String replaced = sqlCommand.replaceAll(replace, replacedWith);
        sqlTextArea.setText(replaced);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", "mailto:ah.ghanem2@gmail.com"});
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", "mailto:ah.ghanem2@gmail.com"});
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        try {
            look.setSelectedLookAndFeel(1);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            look.setSelectedLookAndFeel(2);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            look.setSelectedLookAndFeel(3);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        try {
            look.setSelectedLookAndFeel(4);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try {
            look.setSelectedLookAndFeel(0);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (jTextField1.getText().equals("")) {
            search.removeHighlights(sqlTextArea);
        } else {
            search.highlight(sqlTextArea, jTextField1.getText());
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuItemActionPerformed
        try {
            sqlTextArea.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error while priniting SQl commands ", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_printMenuItemActionPerformed

    private void sqlTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sqlTextAreaKeyReleased

        JPopupMenu menu_temp = null;
        menu_temp = popupMenu.getMenu(getPattern(evt));
        /*if (evt.getKeyCode() == KeyEvent.VK_UP) {
        sqlTextArea.append("ahmed");
        }*/
        if (getPattern(evt).length() != 0) {
            // if ((evt.getKeyCode() == KeyEvent.VK_SPACE) && (evt.getModifiers() & InputEvent.CTRL_MASK) != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_CONTROL) && (Autocomplete.items != 0)) {
                menu_temp.show(this, 210, 200);
            }

        }
        Autocomplete.items = 0;

    }//GEN-LAST:event_sqlTextAreaKeyReleased

    private void sqlTextAreaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_sqlTextAreaCaretUpdate
    }//GEN-LAST:event_sqlTextAreaCaretUpdate

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        try {
            sqlFile = new SaveSqlFile(sqlTextArea.getText());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        /*try {
            record.displayTable(getCols());
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_jButton11ActionPerformed
//To dump a single schema (Current Schema)//To export the entire database to a single file
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        orclError = new OracalError();
        orclError.show();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        try {
            uml = new Uml();
            uml.show();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
       


    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            admin = new Adminstrator();
            userName = jTextField3.getText();
            passWord = jPasswordField1.getText();

        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        admin.createUser(userName, passWord);
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(database.getQueryRes("select username from all_users")));
        jTextField3.setText("");
        jPasswordField1.setText("");
        if (jLabel43.getText().trim().length() != 0) {
        } else {
            JOptionPane.showMessageDialog(null, "Create new user first",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton18ActionPerformed
    public void setDefaultAdmin() { //important for privileges
        jLabel43.setText("");
        userName = "";
        passWord = "";
    }
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            proc = rt.exec("cmd.exe /c start");
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        try {
            admin = new Adminstrator();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        int[] x = jTable2.getSelectedRows();
        if (x.length != 0) {
            for (int i = 0; i < x.length; i++) {
                admin.grantePrivileges((String) jTable2.getModel().getValueAt(x[i], 1), (String) jComboBox3.getSelectedItem());
            }


        } else {
            JOptionPane.showMessageDialog(null, "Choose privileges then press add privileges", "Error", JOptionPane.ERROR_MESSAGE);

        }



    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        try {
            admin = new Adminstrator();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        int[] x = jTable2.getSelectedRows();
        if (x.length != 0) {
            for (int i = 0; i < x.length; i++) {
                admin.revokePrivileges((String) jTable2.getModel().getValueAt(x[i], 1), (String) jComboBox3.getSelectedItem());
            }


        } else {
            JOptionPane.showMessageDialog(null, "Choose privileges then press revoke privileges", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_jButton22ActionPerformed
//To export specific table ://To export specific tables :
    private void toolMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toolMenuActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        try {
            admin = new Adminstrator();
            admin.dropUser((String) (jComboBox3.getSelectedItem()));
            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(database.getQueryRes("select username from all_users")));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        saveFile = new DumpFile();
        if (saveFile.getFilePath() != null) {
            try {

                proc = rt.exec("cmd.exe /c start " + "exp " + UserLogin.getUserName() + "/" + UserLogin.getPassWord() + " FIlE= "
                        + saveFile.getFilePath() + " LOG="
                        + saveFile.getLogPath()
                        + " TABLES=(" + jTextField2.getText() + ")");


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
}//GEN-LAST:event_jButton16ActionPerformed
    }
        private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
            saveFile = new DumpFile();
            if (saveFile.getFilePath() != null) {
                try {

                    proc = rt.exec("cmd.exe /c start " + "exp " + UserLogin.getUserName() + "/" + UserLogin.getPassWord() + " FIlE= "
                            + saveFile.getFilePath() + " LOG="
                            + saveFile.getLogPath()
                            + " TABLES=(" + jComboBox2.getSelectedItem() + ")");


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
            }
}//GEN-LAST:event_jButton15ActionPerformed

        private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
            saveFile = new DumpFile();
            if (saveFile.getFilePath() != null) {
                try {

                    proc = rt.exec("cmd.exe /c start " + "exp " + UserLogin.getUserName() + "/" + UserLogin.getPassWord() + " FIlE= "
                            + saveFile.getFilePath() + " LOG="
                            + saveFile.getLogPath()
                            + " OWNER=" + UserLogin.getUserName());


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
            }
}//GEN-LAST:event_jButton14ActionPerformed

        private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
            saveFile = new DumpFile();
            if (saveFile.getFilePath() != null) {
                try {

                    proc = rt.exec("cmd.exe /c start " + "exp " + UserLogin.getUserName() + "/" + UserLogin.getPassWord() + " full = y FIlE= "
                            + saveFile.getFilePath() + " LOG="
                            + saveFile.getLogPath()
                            + " CONSISTENT=y");


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
            }
}//GEN-LAST:event_jButton13ActionPerformed
    public String getPattern(java.awt.event.KeyEvent evt) {
        int sqlCommandLength = sqlTextArea.getText().length();
        String sqlAll = sqlTextArea.getText();
        int spacePos = sqlAll.lastIndexOf(" ");
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            part = " ";
            return " ";
        } else if (spacePos > 0) {
            part = sqlAll.substring(spacePos + 1);
            return part;
        } else {
            return sqlAll;
        }

    }

    /***
     * get all columns
     * @return all columns of selected table
     * @throws SQLException
     */
    public Vector getCols() throws SQLException {
        colHead.clear();
        String tableName = (String) jComboBox1.getSelectedItem();
        showTableConnection = new DatabaseConnection();
        sqlConnection = showTableConnection.getConnection();

        try {
            sqlStatement = sqlConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error in insert");
        }
        try {
            sqlResultSet = sqlStatement.executeQuery("select * from " + tableName);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Get column count
        colCount = sqlResultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            colHead.addElement(
                    sqlResultSet.getMetaData().getColumnName(i));


        }

        return colHead;
    }

    /***
     * show the content of file (used in batch)
     * @param file batch file
     */
    public void showFile(File file) {
        //StringBuilder data = new StringBuilder();
        Scanner input = null;


        try {
            input = new Scanner(file);


        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File not found");


        }
        while (input.hasNext()) {
            jTextArea2.append(input.nextLine() + "\n");


        }
    }

    /***
     * insert row by row using batch
     * @param tableName insert into
     */
    public void insertRows(String tableName) {
        int lineCount = 1;
        jProgressBar1.setValue(0);



        try {
            // copy data to database ...
            DatabaseConnection connection = new DatabaseConnection();
            Statement insertStament = connection.getConnection().createStatement();
            String insertCommand = "insert into " + tableName + " values(";
            Scanner input = null;


            try {
                input = new Scanner(batchFile);


            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "File not found");


            }
            int choose = JOptionPane.showConfirmDialog(this, "Are you sure you want to copy batch file to " + tableName + "?", "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);


            if (choose == JOptionPane.YES_OPTION) {
                while (input.hasNext()) {
                    String dataS = input.nextLine();


                    if (globalError == lineCount) {
                        insertStament.addBatch(insertCommand + dataS + ")");
                        insertStament.executeBatch();


                        ++globalError;


                    }
                    ++lineCount;


                }


                jProgressBar1.setStringPainted(true);
                jProgressBar1.setValue(100);
                JOptionPane.showMessageDialog(null, "Batch updates Completed ..", "Info", JOptionPane.INFORMATION_MESSAGE);
                globalError = 1;


            } else {
            }
            input.close();
            insertStament.close();


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error during copying file\n at line:" + lineCount + "\n" + ex.getMessage()
                    + "fix your patch and try again to copy (Don't close the program ..)",
                    "Error", JOptionPane.ERROR_MESSAGE);


        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                //new MainScreen().setVisible(true);
            }
        });


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel batchProcessing;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JPanel exportImport;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileMenusItemSeparator;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    public static javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel oracleInfo;
    private javax.swing.JMenuItem printMenuItem;
    private org.fife.ui.rtextarea.RTextScrollPane rTextScrollPane1;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel serviceControl;
    private javax.swing.JPanel sqlEditor;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea sqlTextArea;
    private javax.swing.JMenu toolMenu;
    private javax.swing.JPanel userAdminstrator;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenu windowMenu;
    // End of variables declaration//GEN-END:variables
}
