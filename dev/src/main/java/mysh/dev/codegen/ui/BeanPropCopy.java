/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysh.dev.codegen.ui;

import mysh.dev.codegen.CodeUtil;
import mysh.util.UIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Allen
 */
public class BeanPropCopy extends javax.swing.JFrame {
	private static final Logger log = LoggerFactory.getLogger(BeanPropCopy.class);

	public BeanPropCopy() {
		initComponents();

		this.decoList.setModel(new DefaultComboBoxModel<>(new String[]{
						"",
						"StringUtils.getNotNullString"
		}));

		DocumentListener genTrigger = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				genCode();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				genCode();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				genCode();
			}
		};
		this.destBean.getDocument().addDocumentListener(genTrigger);
		this.decoList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				genCode();
			}
		});
		this.srcBean.getDocument().addDocumentListener(genTrigger);
		this.interfaceDefine.getDocument().addDocumentListener(genTrigger);

	}

	private static String lineSep = System.getProperty("line.separator");

	private void genCode() {
		try {
			String defineText = this.interfaceDefine.getText().trim();
			if (defineText.length() == 0) return;

			String[] lines = defineText.split("[\\r\\n]+");

			String dest = this.destBean.getText();
			String src = this.srcBean.getText();
			String decorator = (String) this.decoList.getSelectedItem();

			StringBuilder codeResult = new StringBuilder();
			for (String line : lines) {
				String[] cells = line.split("[\\s]+");
				String propName = cells[0].contains("_") ? CodeUtil.underline2hump(cells[0]) : cells[0];

				codeResult.append("//");
				codeResult.append(cells[cells.length - 1]);
				codeResult.append(lineSep);
				codeResult.append(dest);
				codeResult.append(".set");
				codeResult.append(propName);
				codeResult.append('(');
				if (decorator.length() > 0) {
					codeResult.append(decorator);
					codeResult.append('(');
				}
				codeResult.append(src);
				codeResult.append(".get");
				codeResult.append(propName);
				codeResult.append("()");
				if (decorator.length() > 0) {
					codeResult.append(')');
				}
				codeResult.append(");");
				codeResult.append(lineSep);
			}
			this.code.setText(codeResult.toString());
		} catch (Exception e) {
			log.error("gen code error.", e);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		destBean = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		decoList = new javax.swing.JComboBox();
		srcBean = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		interfaceDefine = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		code = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("属性复制代码生成");

		jLabel1.setText(".setProp(");

		jLabel2.setText(".getProp());");

		jSplitPane1.setDividerLocation(150);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		interfaceDefine.setColumns(20);
		interfaceDefine.setRows(5);
		interfaceDefine.setText("RegionId\tRequest\t?\tString\tV1\t地市\nCountyId\tRequest\t?\tString\tV3\t县市\nORDER_ID           NUMBER(16)                     订单标识，主键，一个订单一个邮寄地址 \nADDRESS_ID         NUMBER(16)    Y                地址编号，关联我的地址信息\n");
		jScrollPane1.setViewportView(interfaceDefine);

		jSplitPane1.setTopComponent(jScrollPane1);

		code.setEditable(false);
		code.setColumns(20);
		code.setRows(5);
		code.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				codeMouseClicked(evt);
			}
		});
		jScrollPane2.setViewportView(code);

		jSplitPane1.setRightComponent(jScrollPane2);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup()
														.addContainerGap()
														.addComponent(destBean, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jLabel1)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(decoList, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(srcBean, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jLabel2)
														.addContainerGap())
										.addComponent(jSplitPane1)
		);
		jPanel1Layout.setVerticalGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup()
														.addContainerGap()
														.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(destBean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel1)
																		.addComponent(decoList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(srcBean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel2))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
		);

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void codeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_codeMouseClicked
		UIUtil.copyToSystemClipboard(code.getText());
	}//GEN-LAST:event_codeMouseClicked

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
			java.util.logging.Logger.getLogger(BeanPropCopy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(BeanPropCopy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(BeanPropCopy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(BeanPropCopy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		UIUtil.resetFont(null);

        /* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				BeanPropCopy frame = new BeanPropCopy();
				frame.setSize(750, 550);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTextArea code;
	private javax.swing.JComboBox decoList;
	private javax.swing.JTextField destBean;
	private javax.swing.JTextArea interfaceDefine;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JTextField srcBean;
	// End of variables declaration//GEN-END:variables
}
