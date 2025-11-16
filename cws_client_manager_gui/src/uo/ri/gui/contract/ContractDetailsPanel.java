package uo.ri.gui.contract;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractDetailsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField idField;
	private JTextArea detailsArea;
	private ContractCrudService service;
	private MainFrame parentFrame;

	public ContractDetailsPanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		// Title
		JLabel titleLabel = new JLabel("Contract Details");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		// Search panel
		JPanel searchPanel = new JPanel(new GridBagLayout());
		searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		searchPanel.add(new JLabel("Contract ID:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		idField = new JTextField(15);
		idPanel.add(idField);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchContract());
		idPanel.add(searchButton);
		searchPanel.add(idPanel, gbc);

		add(searchPanel, BorderLayout.NORTH);

		// Details area
		JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
		detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

		JLabel detailsLabel = new JLabel("Contract Information:");
		detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
		detailsPanel.add(detailsLabel, BorderLayout.NORTH);

		detailsArea = new JTextArea(20, 60);
		detailsArea.setEditable(false);
		detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		detailsArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(java.awt.Color.GRAY),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JScrollPane scrollPane = new JScrollPane(detailsArea);
		detailsPanel.add(scrollPane, BorderLayout.CENTER);

		add(detailsPanel, BorderLayout.CENTER);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> clearDetails());
		buttonPanel.add(clearButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(
				e -> parentFrame.showPanel(new WelcomePanel(parentFrame)));
		buttonPanel.add(backButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void searchContract() {
		try {
			String id = idField.getText().trim();
			if (id.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a contract ID",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Optional<ContractDto> result = service.findById(id);
			if (result.isPresent()) {
				displayContractDetails(result.get());
				parentFrame.setStatus("Contract loaded: " + id);
			} else {
				JOptionPane.showMessageDialog(this, "Contract not found with ID: " + id,
						"Not Found", JOptionPane.WARNING_MESSAGE);
				clearDetails();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void displayContractDetails(ContractDto contract) {
		StringBuilder sb = new StringBuilder();

		sb.append(
				"═══════════════════════════════════════════════════════════════\n");
		sb.append("                     CONTRACT DETAILS\n");
		sb.append(
				"═══════════════════════════════════════════════════════════════\n\n");

		sb.append("CONTRACT INFORMATION\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(String.format("  Contract ID:          %s\n", contract.id));
		sb.append(String.format("  State:                %s\n", contract.state));
		sb.append(String.format("  Contract Type:        %s\n",
				contract.contractType.name));
		sb.append(String.format("  Professional Group:   %s\n",
				contract.professionalGroup.name));
		sb.append("\n");

		sb.append("MECHANIC INFORMATION\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(
				String.format("  NIF:                  %s\n", contract.mechanic.nif));
		sb.append(String.format("  Name:                 %s %s\n",
				contract.mechanic.name, contract.mechanic.surname));
		sb.append("\n");

		sb.append("DATES\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(
				String.format("  Start Date:           %s\n", contract.startDate));
		sb.append(String.format("  End Date:             %s\n",
				contract.endDate != null ? contract.endDate : "N/A (Permanent)"));
		sb.append("\n");

		sb.append("FINANCIAL INFORMATION\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(String.format("  Annual Base Salary:   %.2f €\n",
				contract.annualBaseSalary));
		sb.append(String.format("  Monthly Base Salary:  %.2f €\n",
				contract.annualBaseSalary / 14));
		sb.append(String.format("  Tax Rate:             %.2f%%\n",
				contract.taxRate * 100));
		sb.append(
				String.format("  Settlement Amount:    %.2f €\n", contract.settlement));
		sb.append("\n");

		sb.append("CONTRACT TYPE DETAILS\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(String.format("  Compensation Days:    %.2f days/year\n",
				contract.contractType.compensationDaysPerYear));
		sb.append("\n");

		sb.append("PROFESSIONAL GROUP DETAILS\n");
		sb.append(
				"───────────────────────────────────────────────────────────────\n");
		sb.append(String.format("  Triennium Payment:    %.2f €\n",
				contract.professionalGroup.trieniumPayment));
		sb.append(String.format("  Productivity Rate:    %.4f (%.2f%%)\n",
				contract.professionalGroup.productivityRate,
				contract.professionalGroup.productivityRate * 100));
		sb.append("\n");

		sb.append(
				"═══════════════════════════════════════════════════════════════\n");

		detailsArea.setText(sb.toString());
		detailsArea.setCaretPosition(0);
	}

	private void clearDetails() {
		idField.setText("");
		detailsArea.setText("");
		idField.requestFocus();
	}
}