package gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import features.Cmd;
import features.Cmd.StreamWrapper;
import factory.DatabaseFactory;
import main.SplashScreen;
import utils.ConstantManager;

/**
 * User login class
 * @author Ahmed Ghanem.
 */
public final class UserLogin extends javax.swing.JFrame {
    //static variables reference

    private Image mainIcon = Toolkit.getDefaultToolkit().getImage(ConstantManager.OCTOPUS_ICON_PATH);
    private static String serverHostname;
    private static String portNumber = "1521";
    private static String userName;
    private static String passWord;
    private static String globalDbName = "orcl";
    private String storedUserName;
    private String storedPassWord;
    private static String databaseUrl;
    private SpinnerModel model;
    private File dbFile;
    private File fileFromCombo;
    private File delFile;
    private PrintWriter fileWrite; //to write into dbFile
    private Scanner readUrl;
    private MainScreen mainScreen;
    private DatabaseFactory orclInfo;
    private SplashScreen splash;
    Runtime rt = Runtime.getRuntime();
    Cmd rte = new Cmd();
    StreamWrapper error, output;

    /***
     * init GUi components
     * @throws ClassNotFoundException
     */
    public UserLogin() throws ClassNotFoundException {

        initComponents();
        init();
    }

    public void makeObj() {
        //default 1521 (from 1024 to 65536 ++1)
        model = new SpinnerNumberModel(1521, 1024, 56536, 1);
        //jSpinner1.setEnabled(false);

    }

    /***
     * init for objects, set model, files
     */
    public void init() {
        try {
            makeObj();
            makeNull();
            setLocationRelativeTo(null);
            jSpinner1.setModel(model);
            readFiles(); //used in combobox
            setIconImage(mainIcon);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "ok");
        }
    }

    /***
     * read connection file
     */
    public void readFiles() {
        File dir = new File("db");
        String[] files = dir.list(); //get list od files
        delExtension(files);


        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(files));
    }

    /***
     * search about connection file in db folder
     * @param connectionName db connection name
     * @param temp search in folder
     * @return true if the file found
     */
    public boolean search(String connectionName, String[] temp) {
        for (int i = 0; i < temp.length; i++) {
            if (connectionName.equals(temp[i].substring(0, temp[i].lastIndexOf(".")))) {
                return false;
            }
        }

        return true;
    }

//************ remove extensions of stored db files [using it ref] *************
    /***
     * remove the extension of stored file
     * @param temp stored files
     */
    public void delExtension(String[] temp) {
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].substring(0, temp[i].lastIndexOf("."));

        }
    }

    /***
     * make all fields null after operation
     */
    public void makeNull() {
        jTextField2.setText(null);
        jPasswordField1.setText(null);
        jSpinner1.setModel(model);
    }

    /***
     *
     * @return true if no fields is null
     */
    public boolean checkNullValues() {
        //check the input from the user >>>
        if (jTextField1.getText() == null || jTextField1.getText().trim().length() == 0) {
            return false;
        } else if (jTextField2.getText() == null || jTextField2.getText().trim().length() == 0) {
            return false;
        } else if (jPasswordField1.getText() == null || jPasswordField1.getText().trim().length() == 0) {
            return false;
        }else if (jTextField3.getText() == null || jTextField3.getText().trim().length() == 0) {
            return false;
        } else {
            return true;
        }

    }
    //------------ GET methods -----------------------

    /***
     *
     * @return server host name
     */
    static public String getServerHostname() {

        return serverHostname;
    }

    /***
     *
     * @return port number
     */
    static public String getPortNumber() {
        return portNumber;
    }

    /***
     *
     * @return db user
     */
    static public String getUserName() {
        return userName;
    }

    /***
     *
     * @return db password
     */
    static public String getPassWord() {
        return passWord;
    }

    /***
     *
     * @return stored user name
     */
    public String getStroedUserName() {
        return storedUserName;
    }
    public String getGlobalDbName(){
        return globalDbName;
    }

    /***
     *
     * @return stored password
     */
    public String getgetStroedPassWord() {
        return storedPassWord;
    }

    /***
     *
     * @return db url
     */
    static public String getDbUrl() { //used in another classes
        return databaseUrl;
    }


//------------------------------------- ACTIONS --------------------------
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(ConstantManager.OCTOPUS_VERSION);
        setResizable(false);

        jLabel2.setText("Stored Connection :");

        jLabel1.setText("Server Hostname :");

        jTextField1.setText("localhost");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel3.setText("Port : ");

        jLabel4.setText("User Name : ");

        jLabel5.setText("Password : ");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/details.png"))); // NOI18N
        jButton5.setText("Details >>");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/cancel.png"))); // NOI18N
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/clear.png"))); // NOI18N
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/connect.png"))); // NOI18N
        jButton3.setText("Connect");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Save This Connection.");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/connect.png"))); // NOI18N
        jButton4.setText("Connect");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/logo2.png"))); // NOI18N

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/cancel.png"))); // NOI18N
        jButton6.setText("Delete");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Arrow Right.png"))); // NOI18N
        jButton7.setText("Enable oracle services");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("please be patient this action will take few minutes .");

        jLabel8.setText("Global DB Name : ");

        jTextField3.setText("orcl");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jPasswordField1)
                                            .addComponent(jTextField2)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                                    .addComponent(jLabel7))))
                        .addGap(20, 20, 20)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(jButton6))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(1, 1, 1))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        makeNull(); //clear
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        System.out.println("pass in user" + getPassWord());





        if (checkNullValues() == true) {
            serverHostname = jTextField1.getText();
            portNumber = "" + model.getValue();
            userName = jTextField2.getText();
            passWord = jPasswordField1.getText();
            globalDbName = jTextField3.getText();
            databaseUrl = "jdbc:oracle:thin:@" + serverHostname + ":" + portNumber + ":" +globalDbName;
            try {
                orclInfo = new DatabaseFactory();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "OK");
            }
            if (orclInfo.getCheckConnection() == true) {



                if (jCheckBox1.isSelected()) {
                    if (search(userName, new File("db").list()) == true) {
                        // for each login make a file with username contain dbUrl
                        dbFile = new File("db/" + userName + ".txt");
                        try {
                            fileWrite = new PrintWriter(dbFile);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        /**write URL
                         * username
                         * password
                         */
                        fileWrite.println(databaseUrl);
                        fileWrite.println(userName);
                        fileWrite.println(serverHostname);
                        fileWrite.println(passWord);
                        fileWrite.close();

                    } else {
                        JOptionPane.showMessageDialog(this, "There is a connection with the same name \n[Connection will not saved] ", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                makeNull(); //make null after each connect
                readFiles();//read files id dir
                this.dispose();
                try {
                    try {
                        mainScreen = new MainScreen();
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                mainScreen.show();
                System.out.println("pass in user" + getPassWord());
            } else {
                this.dispose();
                System.out.println("error");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Complete Fields", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed
//---------------------------------- BUG ------------------------
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String fileName = (String) jComboBox1.getSelectedItem();
        if (fileName != null) {
            fileFromCombo = new File("db/" + fileName + ".txt"); //for stored connection
            try {
                readUrl = new Scanner(fileFromCombo);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Unable To Make Connection",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            databaseUrl = readUrl.nextLine();
            userName = readUrl.nextLine();
            serverHostname = readUrl.nextLine();
            passWord = readUrl.nextLine();
            readUrl.close();
            this.dispose();
            try {
                try {
                    mainScreen = new MainScreen();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            mainScreen.show();
        } else {
            JOptionPane.showMessageDialog(this, "There Is No Stored Connections",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //details of connections no write (URL)
        if (checkNullValues() == false) {
            JOptionPane.showMessageDialog(this, "This Connection Has No Deatails", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "jdbc:oracle:thin:@"
                    + jTextField2.getText() + ":" + "" + model.getValue() + ":"
                    + "orcl", "Database URL", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //delete Stored file

        String delFileName = (String) jComboBox1.getSelectedItem();
        if (delFileName != null) {
            int choose = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete : "
                    + delFileName,
                    "Delete Connection", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (choose == JOptionPane.YES_OPTION) {

                delFile = new File("db/" + delFileName + ".txt");
                if (delFile.delete() == true) {
                    JOptionPane.showMessageDialog(this, " Connection: " + delFileName + " Deleted",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    readFiles();
                } else {
                    JOptionPane.showMessageDialog(this, delFileName + "Problem on deleting Connection",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
            }
        } else { //delete empty stored
            JOptionPane.showMessageDialog(this, "There Is No Stored Connections",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (jTextField1.getText().startsWith("lo")) {
            jTextField1.setText("localhost");
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String[] commands = {"net start OracleDBConsoleORCL",
                "net start OracleJobSchedulerORCL",
                "net start OracleOraDb10g_home1iSQL*Plus",
                "net start OracleOraDb10g_home1TNSListener",
                "net start OracleServiceORCL"
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
                exitVal = proc.waitFor();

            }
            JOptionPane.showMessageDialog(null, "Oracle Services Started", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new UserLogin().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
