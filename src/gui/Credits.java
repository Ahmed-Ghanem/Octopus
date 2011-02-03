package gui;

import java.awt.Image;
import java.awt.Toolkit;
import utils.ConstantManager;

/**
 * This Class to get information about the author rights.
 * @author Ahmed Ghanem.
 */
public class Credits extends javax.swing.JFrame {

    private Image mainIcon = Toolkit.getDefaultToolkit().getImage(ConstantManager.OCTOPUS_ICON_PATH);

    /** Credits constructor show and center a new Credits frame  */
    public Credits() {
        initComponents();
        setLocationRelativeTo(null);
        setIconImage(mainIcon);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        creditsTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        ccreditsCloseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(ConstantManager.OCTOPUS_VERSION);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("Ahmed Ghanem <ah.ghanem2@gmail.com>\n");
        jScrollPane2.setViewportView(jTextArea2);

        creditsTabbedPane.addTab("Written by", jScrollPane2);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Designs have been obtained from searching on the internet,\nall rights reserved to their respective owners");
        jScrollPane1.setViewportView(jTextArea1);

        creditsTabbedPane.addTab("Art work by", jScrollPane1);

        ccreditsCloseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/orcl/images/close_logo.png"))); // NOI18N
        ccreditsCloseButton.setText("Close");
        ccreditsCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ccreditsCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ccreditsCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(creditsTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(creditsTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ccreditsCloseButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ccreditsCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ccreditsCloseButtonActionPerformed
        this.hide();
    }//GEN-LAST:event_ccreditsCloseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ccreditsCloseButton;
    private javax.swing.JTabbedPane creditsTabbedPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
