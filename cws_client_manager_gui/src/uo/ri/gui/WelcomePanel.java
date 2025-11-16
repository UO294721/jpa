package uo.ri.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import uo.ri.gui.contract.ContractDeletePanel;
import uo.ri.gui.contract.ContractDetailsPanel;
import uo.ri.gui.contract.ContractFormPanel;
import uo.ri.gui.contract.ContractTerminatePanel;
import uo.ri.gui.contract.ContractUpdatePanel;
import uo.ri.gui.contract.ContractsListPanel;
import uo.ri.gui.contracttype.ContractTypesSubMenu;

/**
 * Enhanced welcome panel with functional navigation buttons
 */
public class WelcomePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainFrame parentFrame;

	public WelcomePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(20, 20));
		setBorder(new EmptyBorder(20, 20, 20, 20));

		// Header
		add(createHeaderPanel(), BorderLayout.NORTH);

		// Main content with module buttons
		add(createModulesPanel(), BorderLayout.CENTER);

		// Footer
		add(createFooterPanel(), BorderLayout.SOUTH);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());

		JLabel titleLabel = new JLabel("Car Workshop Management System");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(titleLabel, BorderLayout.NORTH);

		JLabel subtitleLabel = new JLabel("Select a module to begin");
		subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subtitleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

		return headerPanel;
	}

	private JPanel createModulesPanel() {
		JPanel modulesPanel = new JPanel(new GridLayout(2, 2, 20, 20));

		// Add module cards
		modulesPanel.add(createMechanicsModule());
		modulesPanel.add(createContractsModule());
		modulesPanel.add(createPayrollsModule());
		modulesPanel.add(createInvoicesModule());

		return modulesPanel;
	}

	private JPanel createMechanicsModule() {
		JPanel modulePanel = createModuleCard("👤 Mechanics",
				"Manage mechanic information", new Color(52, 152, 219));

		JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		buttonsPanel.add(
				createActionButton("➕ Add Mechanic", e -> navigateToMechanicForm()));
		buttonsPanel.add(createActionButton("✏️ Update Mechanic",
				e -> navigateToMechanicUpdate()));
		buttonsPanel.add(createActionButton("🗑️ Delete Mechanic",
				e -> navigateToMechanicDelete()));
		buttonsPanel.add(createActionButton("📋 List All Mechanics",
				e -> navigateToMechanicsList()));

		modulePanel.add(buttonsPanel, BorderLayout.CENTER);
		return modulePanel;
	}

	private JPanel createContractsModule() {
		JPanel modulePanel = createModuleCard("📄 Contracts",
				"Manage contracts and employment", new Color(46, 204, 113));

		JPanel buttonsPanel = new JPanel(new GridLayout(8, 1, 5, 5));
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		buttonsPanel.add(
				createActionButton("➕ Add Contract", e -> navigateToContractForm()));
		buttonsPanel.add(createActionButton("✏️ Update Contract",
				e -> navigateToContractUpdate()));
		buttonsPanel.add(createActionButton("🗑️ Delete Contract",
				e -> navigateToContractDelete()));
		buttonsPanel.add(createActionButton("⏹️ Terminate Contract",
				e -> navigateToContractTerminate()));
		buttonsPanel.add(createActionButton("🔍 Contract Details",
				e -> navigateToContractDetails()));
		buttonsPanel.add(createActionButton("📋 List All Contracts",
				e -> navigateToContractsList()));
		buttonsPanel.add(createActionButton("📑 Contract Types",
				e -> navigateToContractTypes()));

		modulePanel.add(buttonsPanel, BorderLayout.CENTER);
		return modulePanel;
	}

	private JPanel createPayrollsModule() {
		JPanel modulePanel = createModuleCard("💰 Payrolls",
				"Generate and manage payrolls", new Color(155, 89, 182));

		JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		buttonsPanel.add(createActionButton("🔄 Generate Today",
				e -> showComingSoon("Generate Payrolls Today")));
		buttonsPanel.add(createActionButton("📅 Generate for Date",
				e -> showComingSoon("Generate Payrolls for Date")));
		buttonsPanel.add(createActionButton("🔍 View Details",
				e -> showComingSoon("View Payroll Details")));
		buttonsPanel.add(createActionButton("📊 By Mechanic",
				e -> showComingSoon("Payrolls by Mechanic")));
		buttonsPanel.add(createActionButton("📈 By Group",
				e -> showComingSoon("Payrolls by Professional Group")));

		modulePanel.add(buttonsPanel, BorderLayout.CENTER);
		return modulePanel;
	}

	private JPanel createInvoicesModule() {
		JPanel modulePanel = createModuleCard("🧾 Invoices",
				"Create and manage invoices", new Color(230, 126, 34));

		JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		buttonsPanel.setOpaque(false);
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		buttonsPanel.add(createActionButton("➕ Create Invoice",
				e -> showComingSoon("Create Invoice")));
		buttonsPanel.add(createActionButton("🔍 View Invoice",
				e -> showComingSoon("View Invoice")));
		buttonsPanel.add(createActionButton("📋 Work Orders",
				e -> showComingSoon("List Work Orders")));

		modulePanel.add(buttonsPanel, BorderLayout.CENTER);
		return modulePanel;
	}

	private JPanel createModuleCard(String title, String description,
			Color color) {
		JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
		cardPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(color, 2),
				new EmptyBorder(15, 15, 15, 15)));
		cardPanel.setBackground(Color.WHITE);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(color);
		headerPanel.add(titleLabel, BorderLayout.NORTH);

		JLabel descLabel = new JLabel(description);
		descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		descLabel.setForeground(Color.GRAY);
		headerPanel.add(descLabel, BorderLayout.SOUTH);

		cardPanel.add(headerPanel, BorderLayout.NORTH);

		return cardPanel;
	}

	private JButton createActionButton(String text,
			java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.PLAIN, 12));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(200, 30));
		button.addActionListener(action);

		// Hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(240, 240, 240));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(null);
			}
		});

		return button;
	}

	private JPanel createFooterPanel() {
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JLabel footerLabel = new JLabel(
				"University of Oviedo - Software Engineering | Version 1.1");
		footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		footerLabel.setForeground(Color.GRAY);
		footerPanel.add(footerLabel);

		return footerPanel;
	}

	// Navigation methods - Mechanics
	private void navigateToMechanicForm() {
		if (parentFrame != null) {
			parentFrame
					.showPanel(new uo.ri.gui.mechanic.MechanicFormPanel(parentFrame));
		}
	}

	private void navigateToMechanicUpdate() {
		if (parentFrame != null) {
			parentFrame
					.showPanel(new uo.ri.gui.mechanic.MechanicUpdatePanel(parentFrame));
		}
	}

	private void navigateToMechanicDelete() {
		if (parentFrame != null) {
			parentFrame
					.showPanel(new uo.ri.gui.mechanic.MechanicDeletePanel(parentFrame));
		}
	}

	private void navigateToMechanicsList() {
		if (parentFrame != null) {
			parentFrame.showPanel(new uo.ri.gui.mechanic.MechanicsListPanel());
		}
	}

	// Navigation methods - Contracts
	private void navigateToContractForm() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractFormPanel(parentFrame));
		}
	}

	private void navigateToContractUpdate() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractUpdatePanel(parentFrame));
		}
	}

	private void navigateToContractDelete() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractDeletePanel(parentFrame));
		}
	}

	private void navigateToContractTerminate() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractTerminatePanel(parentFrame));
		}
	}

	private void navigateToContractDetails() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractDetailsPanel(parentFrame));
		}
	}

	private void navigateToContractsList() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractsListPanel());
		}
	}

	private void navigateToContractTypes() {
		if (parentFrame != null) {
			parentFrame.showPanel(new ContractTypesSubMenu(parentFrame));
		}
	}

	private void showComingSoon(String feature) {
		javax.swing.JOptionPane.showMessageDialog(this,
				feature + " will be available in the next version.\n\nStay tuned!",
				"Coming Soon", javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}
}