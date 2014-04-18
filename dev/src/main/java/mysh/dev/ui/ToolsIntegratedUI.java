/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysh.dev.ui;

import mysh.dev.codegen.ui.BeanPropCopy;
import mysh.dev.codegen.ui.NameConvert;
import mysh.dev.encoding.Encoding;
import mysh.dev.filesearch.FileSearchFrame;
import mysh.dev.regexp.RegExpTestFrame;
import mysh.dev.tcpportscanner.TcpPortScannerUI;
import mysh.util.UIUtil;

/**
 *
 * @author Allen
 */
public class ToolsIntegratedUI extends javax.swing.JFrame {

    /**
     * Creates new form ToolsIntegratedUI
     */
    public ToolsIntegratedUI() {
        initComponents();
        this.tabPane.add("属性复制", new BeanPropCopy().getContentPane());
        this.tabPane.add("命名转换", new NameConvert().getContentPane());
        this.tabPane.add("编码转换", new Encoding().getContentPane());
        this.tabPane.add("文件搜索", new FileSearchFrame().getContentPane());
        this.tabPane.add("正则判断", new RegExpTestFrame().getContentPane());
        this.tabPane.add("TCP端口扫描", new TcpPortScannerUI().getContentPane());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("开发工具集");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ToolsIntegratedUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ToolsIntegratedUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ToolsIntegratedUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ToolsIntegratedUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        UIUtil.resetFont(null);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            ToolsIntegratedUI frame = new ToolsIntegratedUI();
            frame.setBounds(0, 0, 720, 550);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
}
