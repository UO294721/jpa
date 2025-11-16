package uo.ri.gui.contracttype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractTypesSubMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainFrame parentFrame;

	public ContractTypesSubMenu(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(20, 20));
		setBorder(new EmptyBorder(30, 30, 30, 30));

		// Header
		JPanel headerPanel = new JPanel(new BorderLayout());

		JLabel titleLabel = new JLabel("📑 Contract Types Management");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(new Color(46, 204, 113));
		headerPanel.add(titleLabel, BorderLayout.NORTH);

		JLabel subtitleLabel = new JLabel("Manage contract type configurations");
		subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subtitleLabel.setForeground(Color.GRAY);
		subtitleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

		add(headerPanel, BorderLayout.NORTH);

		// Options panel
		JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
		optionsPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

		// Add option cards
		optionsPanel.add(createOptionCard("➕ Add Contract Type",
				"Create a new contract type", new Color(46, 204, 113),
				e -> parentFrame.showPanel(new ContractTypeFormPanel(parentFrame))));

		optionsPanel.add(createOptionCard("✏️ Update Contract Type",
				"Modify existing contract type", new Color(52, 152, 219),
				e -> parentFrame.showPanel(new ContractTypeUpdatePanel(parentFrame))));

		optionsPanel.add(createOptionCard("🗑️ Delete Contract Type",
				"Remove a contract type", new Color(231, 76, 60),
				e -> parentFrame.showPanel(new ContractTypeDeletePanel(parentFrame))));

		optionsPanel.add(createOptionCard("📋 List All Types",
				"View all contract types", new Color(155, 89, 182),
				e -> parentFrame.showPanel(new ContractTypesListPanel())));

		add(optionsPanel, BorderLayout.CENTER);

		// Back button
		JPanel buttonPanel = new JPanel();
		JButton backButton = new JButton("← Back to Home");
		backButton.setFont(new Font("Arial", Font.PLAIN, 14));
		backButton.addActionListener(
				e -> parentFrame.showPanel(new WelcomePanel(parentFrame)));
		buttonPanel.add(backButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private JPanel createOptionCard(String title, String description, Color color,
			java.awt.event.ActionListener action) {
		JPanel card = new JPanel(new BorderLayout(10, 10));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(color, 2),
				new EmptyBorder(20, 20, 20, 20)));
		card.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(color);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel descLabel = new JLabel(description);
		descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		descLabel.setForeground(Color.GRAY);
		descLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton actionButton = new JButton("Open");
		actionButton.setFont(new Font("Arial", Font.BOLD, 14));
		actionButton.setBackground(color);
		actionButton.setForeground(Color.WHITE);
		actionButton.setFocusPainted(false);
		actionButton.addActionListener(action);

		JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		textPanel.setOpaque(false);
		textPanel.add(titleLabel);
		textPanel.add(descLabel);

		card.add(textPanel, BorderLayout.CENTER);
		card.add(actionButton, BorderLayout.SOUTH);

		// Hover effect
		card.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				card.setBackground(new Color(245, 245, 245));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				card.setBackground(Color.WHITE);
			}
		});

		return card;
	}
}