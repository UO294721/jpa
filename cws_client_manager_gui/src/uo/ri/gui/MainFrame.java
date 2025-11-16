package uo.ri.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uo.ri.gui.contract.ContractDeletePanel;
import uo.ri.gui.contract.ContractDetailsPanel;
import uo.ri.gui.contract.ContractFormPanel;
import uo.ri.gui.contract.ContractTerminatePanel;
import uo.ri.gui.contract.ContractUpdatePanel;
import uo.ri.gui.contract.ContractsListPanel;
import uo.ri.gui.contracttype.ContractTypeDeletePanel;
import uo.ri.gui.contracttype.ContractTypeFormPanel;
import uo.ri.gui.contracttype.ContractTypeUpdatePanel;
import uo.ri.gui.contracttype.ContractTypesListPanel;
import uo.ri.gui.mechanic.MechanicDeletePanel;
import uo.ri.gui.mechanic.MechanicFormPanel;
import uo.ri.gui.mechanic.MechanicUpdatePanel;
import uo.ri.gui.mechanic.MechanicsListPanel;

/**
 * Main application frame with menu bar and content area
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783529826535220913L;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 800;

	private JPanel contentPanel;
	private JLabel statusLabel;

	public MainFrame() {
		initializeFrame();
		createMenuBar();
		createContentArea();
		createStatusBar();
	}

	private void initializeFrame() {
		setTitle("Car Workshop Management System");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Set application icon (optional)
		try {
			setIconImage(Toolkit.getDefaultToolkit()
					.getImage(getClass().getResource("/icons/app-icon.png")));
		} catch (Exception e) {
			// Icon not found, continue without it
		}
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Mechanics Menu
		JMenu mechanicsMenu = new JMenu("Mechanics");
		mechanicsMenu.setMnemonic('M');

		addMenuItem(mechanicsMenu, "List All Mechanics", 'L',
				e -> showPanel(new MechanicsListPanel()));
		addMenuItem(mechanicsMenu, "Add Mechanic", 'A',
				e -> showPanel(new MechanicFormPanel(this)));
		addMenuItem(mechanicsMenu, "Update Mechanic", 'U',
				e -> showPanel(new MechanicUpdatePanel(this)));
		addMenuItem(mechanicsMenu, "Delete Mechanic", 'D',
				e -> showPanel(new MechanicDeletePanel(this)));

		menuBar.add(mechanicsMenu);

		// Contracts Menu
		JMenu contractsMenu = new JMenu("Contracts");
		contractsMenu.setMnemonic('C');

		JMenu contractSubMenu = new JMenu("Contract Management");
		addMenuItem(contractSubMenu, "List All Contracts", 'L',
				e -> showPanel(new ContractsListPanel()));
		addMenuItem(contractSubMenu, "Add Contract", 'A',
				e -> showPanel(new ContractFormPanel(this)));
		addMenuItem(contractSubMenu, "Update Contract", 'U',
				e -> showPanel(new ContractUpdatePanel(this)));
		addMenuItem(contractSubMenu, "Delete Contract", 'D',
				e -> showPanel(new ContractDeletePanel(this)));
		addMenuItem(contractSubMenu, "Terminate Contract", 'T',
				e -> showPanel(new ContractTerminatePanel(this)));
		addMenuItem(contractSubMenu, "View Contract Details", 'V',
				e -> showPanel(new ContractDetailsPanel(this)));
		contractsMenu.add(contractSubMenu);

		JMenu contractTypesMenu = new JMenu("Contract Types");
		addMenuItem(contractTypesMenu, "List Contract Types", 'L',
				e -> showPanel(new ContractTypesListPanel()));
		addMenuItem(contractTypesMenu, "Add Contract Type", 'A',
				e -> showPanel(new ContractTypeFormPanel(this)));
		addMenuItem(contractTypesMenu, "Update Contract Type", 'U',
				e -> showPanel(new ContractTypeUpdatePanel(this)));
		addMenuItem(contractTypesMenu, "Delete Contract Type", 'D',
				e -> showPanel(new ContractTypeDeletePanel(this)));
		contractsMenu.add(contractTypesMenu);

		menuBar.add(contractsMenu);

		// Help Menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		addMenuItem(helpMenu, "About", 'A', e -> showAboutDialog());
		addMenuItem(helpMenu, "User Guide", 'U', e -> showUserGuide());

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	}

	private void addMenuItem(JMenu menu, String text, char mnemonic,
			java.awt.event.ActionListener action) {
		JMenuItem item = new JMenuItem(text);
		item.setMnemonic(mnemonic);
		item.addActionListener(action);
		menu.add(item);
	}

	private void createContentArea() {
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Show welcome panel by default
		showPanel(new WelcomePanel(this));

		add(contentPanel, BorderLayout.CENTER);
	}

	private void createStatusBar() {
		JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusBar.setBorder(BorderFactory.createEtchedBorder());

		statusLabel = new JLabel("Ready");
		statusBar.add(statusLabel);

		add(statusBar, BorderLayout.SOUTH);
	}

	public void showPanel(JPanel panel) {
		contentPanel.removeAll();
		contentPanel.add(panel, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	public void setStatus(String message) {
		statusLabel.setText(message);
	}

	private void showAboutDialog() {
		String message = "Car Workshop Management System\n\n" + "Version 1.0\n"
				+ "University of Oviedo\n\n"
				+ "A comprehensive system for managing mechanics, "
				+ "contracts, payrolls, and invoices.";
		JOptionPane.showMessageDialog(this, message, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void showUserGuide() {
		String guide = "User Guide\n\n"
				+ "1. Use the menu bar to navigate between different modules\n"
				+ "2. Mechanics: Manage mechanic information\n"
				+ "3. Contracts: Handle contracts, types, and professional groups\n"
				+ "4. Payrolls: Generate and view payroll information\n"
				+ "5. Invoices: Create and manage invoices\n\n"
				+ "For more information, consult the system documentation.";
		JOptionPane.showMessageDialog(this, guide, "User Guide",
				JOptionPane.INFORMATION_MESSAGE);
	}
}