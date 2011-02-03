package database;

import gui.UserLogin;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.ConstantManager;

/**
 * This Class to connect and get database columns.
 * @author Ahmed Ghanem.
 */
public class DatabaseConnection {

    private DatabaseMetaData dbmd;
    private ResultSet res;
    private Vector tablesName;
    private Statement stmt;
    private Connection connection;

    /** Make connection and get meta data.  */
    public DatabaseConnection() {
        try {
            Class.forName(ConstantManager.ORACLE_DRIVER);
            connection = DriverManager.getConnection(UserLogin.getDbUrl(), UserLogin.getUserName(), UserLogin.getPassWord());
            dbmd = connection.getMetaData();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) { //oracle service not started correctly ...

            JOptionPane.showMessageDialog(null, "Please Enable Oracle Services First", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * @return   connection to database.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return   Vector of selected database tables name
     * @throws   SQLException if Connection failed.
     */
    public Vector getTablesNames() throws SQLException {
        tablesName = new Vector();
        stmt = connection.createStatement();
        res = stmt.executeQuery(ConstantManager.USER_TABLES);
        while (res.next()) {
            tablesName.add(res.getString(1));
        }
        return tablesName;
    }
    public Vector getQueryRes(String query)  {
        try {
            tablesName = new Vector();
            stmt = connection.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                tablesName.add(res.getString(1));
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
         return tablesName;
    }
}
