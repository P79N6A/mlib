/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysh.dev.regexp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.regex.Pattern.compile;

/**
 * @author Allen
 */
public class RegExpTest extends javax.swing.JPanel {

	/**
	 * Creates new form RegExpTest
	 */
	public RegExpTest() {
		initComponents();
		DocumentListener listener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				testReg();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				testReg();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		};
		this.textTest.getDocument().addDocumentListener(listener);
		this.textReg.getDocument().addDocumentListener(listener);
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
		jScrollPane1 = new javax.swing.JScrollPane();
		textTest = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		textReg = new javax.swing.JTextArea();
		panelResult = new javax.swing.JPanel();

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Content & Regular", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft YaHei", 0, 14))); // NOI18N
		jPanel1.setFont(new java.awt.Font("Microsoft YaHei", 0, 12)); // NOI18N

		textTest.setColumns(20);
		textTest.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
		textTest.setLineWrap(true);
		textTest.setRows(3);
		jScrollPane1.setViewportView(textTest);

		textReg.setColumns(20);
		textReg.setFont(new java.awt.Font("Microsoft YaHei", 0, 14)); // NOI18N
		textReg.setLineWrap(true);
		textReg.setRows(1);
		jScrollPane2.setViewportView(textReg);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
										.addComponent(jScrollPane2)
		);
		jPanel1Layout.setVerticalGroup(
						jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
		);

		panelResult.setBackground(new java.awt.Color(255, 255, 255));
		panelResult.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				panelResultMouseClicked(evt);
			}
		});

		javax.swing.GroupLayout panelResultLayout = new javax.swing.GroupLayout(panelResult);
		panelResult.setLayout(panelResultLayout);
		panelResultLayout.setHorizontalGroup(
						panelResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGap(0, 0, Short.MAX_VALUE)
		);
		panelResultLayout.setVerticalGroup(
						panelResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGap(0, 16, Short.MAX_VALUE)
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(panelResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
														.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(panelResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addContainerGap())
		);
	}// </editor-fold>//GEN-END:initComponents

	private void panelResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelResultMouseClicked
		this.testReg();
	}//GEN-LAST:event_panelResultMouseClicked

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JPanel panelResult;
	private javax.swing.JTextArea textReg;
	private javax.swing.JTextArea textTest;
	// End of variables declaration//GEN-END:variables

	private AtomicBoolean isTesting = new AtomicBoolean(false);
	private Timer timer = new Timer(true);

	private void testReg() {
		if (isTesting.compareAndSet(false, true)) {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Color result = Color.WHITE;
						String text = textTest.getText().trim();
						String reg = textReg.getText().trim();
						if (text.length() == 0 || reg.length() == 0) {
							panelResult.setBackground(result);
							return;
						}

						if (text.matches(reg)) {
							result = Color.GREEN;
						} else if (compile(reg).matcher(text).find()) {
							result = Color.BLUE;
						} else {
							result = Color.RED;
						}
						panelResult.setBackground(result);
					} catch (Exception e) {
						panelResult.setBackground(Color.BLACK);
					} catch (Error e) {
						JOptionPane.showMessageDialog(RegExpTest.this, e.toString(), "Error",
										JOptionPane.ERROR_MESSAGE);
					} finally {
						isTesting.set(false);
					}
				}
			};
			t.start();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (t.isAlive()) {
						t.stop();
					}
				}
			}, 5_000L);
		}
	}
}
