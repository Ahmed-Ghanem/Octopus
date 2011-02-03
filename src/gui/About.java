package gui;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import utils.ConstantManager;

/**
 * This Class to get information about the program like credit and license.
 * @author Ahmed Ghanem.
 */
public class About extends javax.swing.JFrame {

    private Credits credits;
    private License license;
    private Image mainIcon = Toolkit.getDefaultToolkit().getImage(ConstantManager.OCTOPUS_ICON_PATH);

    /** About constructor show and center a new About frame  */
    public About() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(mainIcon);
    }
    public void init(){
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        octopusLabel = new javax.swing.JLabel();
        creditButton = new javax.swing.JButton();
        licenseButton = new javax.swing.JButton();
        aboutCloseButton = new javax.swing.JButton();
        octopusLogo = new javax.swing.JLabel();
        octopusUrl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(ConstantManager.OCTOPUS_VERSION);
        setResizable(false);

        octopusLabel.setFont(new java.awt.Font("Liberation Sans", 1, 36));
        octopusLabel.setForeground(new java.awt.Color(255, 0, 0));
        octopusLabel.setText("Octopus 1.0");

        creditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/credits_logo.png"))); // NOI18N
        creditButton.setText("Credits");
        creditButton.setDoubleBuffered(true);
        creditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditButtonActionPerformed(evt);
            }
        });

        licenseButton.setText("License");
        licenseButton.setMaximumSize(new java.awt.Dimension(68, 30));
        licenseButton.setMinimumSize(new java.awt.Dimension(68, 30));
        licenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licenseButtonActionPerformed(evt);
            }
        });

        aboutCloseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/close_logo.png"))); // NOI18N
        aboutCloseButton.setText("Close");
        aboutCloseButton.setMaximumSize(new java.awt.Dimension(68, 30));
        aboutCloseButton.setMinimumSize(new java.awt.Dimension(68, 30));
        aboutCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutCloseButtonActionPerformed(evt);
            }
        });

        octopusLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/Octopus_logo.png"))); // NOI18N

        octopusUrl.setForeground(new java.awt.Color(0, 0, 204));
        octopusUrl.setText("Octopus page");
        octopusUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                octopusUrlMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(licenseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(aboutCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(octopusLogo)
                .addContainerGap(39, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(octopusLabel)
                .addGap(109, 109, 109))
            .addGroup(layout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addComponent(octopusUrl)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(octopusLogo)
                .addGap(1, 1, 1)
                .addComponent(octopusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(octopusUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditButton)
                    .addComponent(aboutCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(licenseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void creditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditButtonActionPerformed
        credits = new Credits();
        credits.show();
    }//GEN-LAST:event_creditButtonActionPerformed

    private void aboutCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutCloseButtonActionPerformed

        this.dispose();
    }//GEN-LAST:event_aboutCloseButtonActionPerformed

    private void licenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licenseButtonActionPerformed
        license = new License();
        license.show();
    }//GEN-LAST:event_licenseButtonActionPerformed

    private void octopusUrlMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_octopusUrlMouseReleased
        try {
            URI uri = new URI(ConstantManager.OCTOPUS_URL);
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


    }//GEN-LAST:event_octopusUrlMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutCloseButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton creditButton;
    private javax.swing.JButton licenseButton;
    private javax.swing.JLabel octopusLabel;
    private javax.swing.JLabel octopusLogo;
    private javax.swing.JLabel octopusUrl;
    // End of variables declaration//GEN-END:variables
}
