package uo.ri.gui.contracttype;

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
import javax.swing.JTextField;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractTypeDeletePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JLabel compensationLabel;
	private ContractTypeCrudService service;
	private MainFrame parentFrame;
	private ContractTypeDto currentContractType;

	public ContractTypeDeletePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractTypeCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Delete Contract Type");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Name field
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Contract Type Name:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameField = new JTextField(15);
		namePanel.add(nameField);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchContractType());
		namePanel.add(searchButton);
		formPanel.add(namePanel, gbc);

		// Compensation Days (read-only)
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Compensation Days/Year:"), gbc);
		gbc.gridx = 1;
		compensationLabel = new JLabel("-");
		formPanel.add(compensationLabel, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> deleteContractType());
		buttonPanel.add(deleteButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(
				e -> parentFrame.showPanel(new WelcomePanel(parentFrame)));
		buttonPanel.add(cancelButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void searchContractType() {
		try {
			String name = nameField.getText().trim();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a contract type name",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Optional<ContractTypeDto> result = service.findByName(name.toUpperCase());
			if (result.isPresent()) {
				currentContractType = result.get();
				compensationLabel.setText(
						String.format("%.2f", currentContractType.compensationDays));
				parentFrame
						.setStatus("Contract type loaded: " + currentContractType.name);
			} else {
				JOptionPane.showMessageDialog(this, "Contract type not found: " + name,
						"Not Found", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteContractType() {
		if (currentContractType == null) {
			JOptionPane.showMessageDialog(this,
					"Please search for a contract type first", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete contract type: "
						+ currentContractType.name + "?",
				"Confirm Deletion", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				service.delete(currentContractType.name);
				JOptionPane.showMessageDialog(this,
						"Contract type deleted successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				parentFrame
						.setStatus("Contract type deleted: " + currentContractType.name);
				currentContractType = null;
				clearFields();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Error deleting contract type: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void clearFields() {
		nameField.setText("");
		compensationLabel.setText("-");
		nameField.requestFocus();
	}
}