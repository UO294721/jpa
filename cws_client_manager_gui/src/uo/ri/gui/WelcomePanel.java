package uo.ri.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Welcome panel displayed when the application starts
 */
public class WelcomePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4016049799816661538L;

	public WelcomePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);

		// Title
		JLabel titleLabel = new JLabel("Car Workshop Management System");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(titleLabel, gbc);

		// Subtitle
		JLabel subtitleLabel = new JLabel(
				"University of Oviedo - Software Engineering");
		subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridy = 1;
		add(subtitleLabel, gbc);

		// Welcome message
		JTextArea welcomeText = new JTextArea(
				"\nWelcome to the Car Workshop Management System!\n\n"
						+ "This application allows you to:\n\n"
						+ "• Manage mechanics and their information\n"
						+ "• Handle contracts and employment types\n"
						+ "• Generate and view payrolls\n"
						+ "• Create and manage invoices\n"
						+ "• Organize professional groups\n\n"
						+ "Use the menu bar above to navigate to different modules.\n\n"
						+ "Get started by selecting an option from the menu!");
		welcomeText.setEditable(false);
		welcomeText.setFont(new Font("Arial", Font.PLAIN, 16));
		welcomeText.setBackground(getBackground());
		welcomeText.setLineWrap(true);
		welcomeText.setWrapStyleWord(true);

		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(welcomeText, gbc);

		// Icon panel
		JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
		iconPanel.setOpaque(false);

		iconPanel.add(createModuleButton("👤", "Mechanics"));
		iconPanel.add(createModuleButton("📄", "Contracts"));
		iconPanel.add(createModuleButton("💰", "Payrolls"));
		iconPanel.add(createModuleButton("🧾", "Invoices"));

		gbc.gridy = 3;
		add(iconPanel, gbc);
	}

	private JButton createModuleButton(String emoji, String text) {
		JButton button = new JButton(
				"<html><center>" + emoji + "<br/>" + text + "</center></html>");
		button.setPreferredSize(new Dimension(120, 100));
		button.setFont(new Font("Arial", Font.PLAIN, 14));
		button.setFocusPainted(false);
		return button;
	}
}