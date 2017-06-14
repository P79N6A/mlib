/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysh.dev.ui;

import mysh.dev.codegen.ui.BeanPropCopy2;
import mysh.dev.codegen.ui.FieldGetSet;
import mysh.dev.codegen.ui.NameConvert;
import mysh.dev.encoding.Encoding;
import mysh.dev.filesearch.FileSearchFrame;
import mysh.dev.regexp.RegExpTestFrame;
import mysh.dev.tcpportscanner.TcpPortScannerUI;
import mysh.dev.video.FFmpegUI;
import mysh.util.UIs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 */
public class ToolsIntegratedUI extends javax.swing.JFrame {

	private static final Logger log = LoggerFactory.getLogger(ToolsIntegratedUI.class);
	private Image icon;

	private static final List<JFrame> frames = new ArrayList<>();

	/**
	 * Creates new form ToolsIntegratedUI
	 */
	public ToolsIntegratedUI() {
		try {
			icon = ImageIO.read(getClass().getClassLoader().getResource("mysh/dev/ui/devtools.png"));
		} catch (IOException ex) {
		}
		initComponents();


//		BeanPropCopy beanPropCopy = new BeanPropCopy();
//		this.tabPane.add("属性复制", beanPropCopy.getContentPane());
		BeanPropCopy2 beanPropCopy2 = new BeanPropCopy2();
		this.tabPane.add("属性复制2", beanPropCopy2.getContentPane());
		frames.add(beanPropCopy2);

		FieldGetSet fieldGetSet = new FieldGetSet();
		this.tabPane.add("GetSet生成", fieldGetSet);

		NameConvert nameConvert = new NameConvert();
		this.tabPane.add("命名转换", nameConvert.getContentPane());
		frames.add(nameConvert);

		Encoding encoding = new Encoding();
		this.tabPane.add("编码转换", encoding.getContentPane());
		frames.add(encoding);

		FileSearchFrame fileSearchFrame = new FileSearchFrame();
		this.tabPane.add("文件搜索", fileSearchFrame.getContentPane());
		frames.add(fileSearchFrame);

		RegExpTestFrame regExpTestFrame = new RegExpTestFrame();
		this.tabPane.add("正则判断", regExpTestFrame.getContentPane());
		frames.add(regExpTestFrame);

		TcpPortScannerUI tcpPortScannerUI = new TcpPortScannerUI();
		this.tabPane.add("TCP端口扫描", tcpPortScannerUI.getContentPane());
		frames.add(tcpPortScannerUI);

		FFmpegUI ffmpegUI = new FFmpegUI();
		this.tabPane.add("FFmpeg", ffmpegUI.getContentPane());
		frames.add(ffmpegUI);

//        CLJInterpreterFrame cljIptFrame = new CLJInterpreterFrame(this);
//        Component cljItpPane = this.tabPane.add("Clj 解释器", cljIptFrame.getContentPane());
//
//        this.tabPane.addChangeListener(new ChangeListener() {
//            private AtomicBoolean cljStartFlag = new AtomicBoolean(false);
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                if (tabPane.getSelectedComponent() == cljItpPane
//                        && cljStartFlag.compareAndSet(false, true)) {
//                    cljIptFrame.startCljThread();
//                    System.out.println(cljIptFrame.getTitle());
//                }
//            }
//        });
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

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Dev Tools");
		setIconImage(icon);

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
		UIs.resetFont(null);
        /* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			frame = new ToolsIntegratedUI();
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					exitApp();
				}
			});
			frame.setBounds(0, 0, 720, 600);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

	private static ToolsIntegratedUI frame;

	public static void shutdown() throws IOException {
		try {
			WindowEvent we = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			frame.dispatchEvent(we);
		} finally {
			exitApp();
		}
	}

	private static void exitApp()   {
		for (JFrame jFrame : frames) {
			jFrame.dispose();
		}

		ClassLoader cl = ToolsIntegratedUI.class.getClassLoader();
		if (cl instanceof Closeable) try {
			((Closeable) cl).close();
		} catch (IOException e) {
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JTabbedPane tabPane;
	// End of variables declaration//GEN-END:variables
}
