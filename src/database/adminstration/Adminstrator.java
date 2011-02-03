/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.adminstration;

import database.DatabaseConnection;
import gui.MainScreen;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class play an admin role on octopus
 * @author ahmed
 */
public class Adminstrator {

    private DatabaseConnection connection;
    private Statement stmt;
    private JLabel userNameLabel;

    /**
     *
     * @throws SQLException
     */
    public Adminstrator() throws SQLException {
        connection = new DatabaseConnection();
        stmt = connection.getConnection().createStatement();


    }

    /**
     *
     * @param userName create user
     * @param passWord create user's password
     */
    public void createUser(String userName, String passWord) {
        try {
            stmt.executeUpdate("create user " + userName + " identified by " + passWord);
            JOptionPane.showMessageDialog(null, "User: " + userName + " created successfully\n with no privilegs\n"
                    + "add privileges from the table", "Info", JOptionPane.INFORMATION_MESSAGE);
            MainScreen.jLabel43.setText(userName);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());

        }

    }

    /**
     *
     * @param privileges privileges for user
     * @param userName
     */
    public void grantePrivileges(String privileges, String userName) {
        try {
            stmt.executeUpdate("grant " + privileges + " to " + userName);
            JOptionPane.showMessageDialog(null, "Privilege : " + privileges + " granted to the user..", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     *
     * @param privileges privileges for user
     * @param userName
     */
    public void revokePrivileges(String privileges, String userName) {
        try {
            stmt.executeUpdate("revoke " + privileges + " from " + userName);
            JOptionPane.showMessageDialog(null, "Privileges : " + privileges + " revoked from user the user..", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     *
     * @param userName
     */
    public void dropUser(String userName) {
        int choose = JOptionPane.showConfirmDialog(null, "Are you sure you want to permanently delete : "
                + userName,
                "Delete User", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {
            try {
                stmt.executeUpdate("drop user " + userName);
                JOptionPane.showMessageDialog(null, "User : " + userName + " deleted ..", "Info", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

            }
        } else {
        }

    }
}
