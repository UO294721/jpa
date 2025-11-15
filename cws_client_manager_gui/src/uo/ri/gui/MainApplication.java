package uo.ri.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import uo.ri.conf.Factories;

/**
 * Main entry point for the Car Workshop Management GUI application
 */
public class MainApplication {

	public static void main(String[] args) {
		// Set Look and Feel to system default
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Launch GUI on Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			try {
				MainFrame frame = new MainFrame();
				frame.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Error initializing application: " + e.getMessage(),
						"Startup Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		});

		// Add shutdown hook to close resources
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Factories.close();
		}));
	}
}