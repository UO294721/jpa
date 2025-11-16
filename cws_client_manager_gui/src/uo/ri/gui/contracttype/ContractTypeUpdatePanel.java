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

public class ContractTypeUpdatePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField compensationDaysField;
	private ContractTypeCrudService service;
	private MainFrame parentFrame;
	private ContractTypeDto currentContractType;

	public ContractTypeUpdatePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractTypeCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Update Contract Type");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Name field for searching
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

		// Compensation Days field
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Compensation Days/Year:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		compensationDaysField = new JTextField(20);
		compensationDaysField.setEnabled(false);
		formPanel.add(compensationDaysField, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateContractType());
		buttonPanel.add(updateButton);

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
				compensationDaysField
						.setText(String.valueOf(currentContractType.compensationDays));
				compensationDaysField.setEnabled(true);
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

	private void updateContractType() {
		if (currentContractType == null) {
			JOptionPane.showMessageDialog(this,
					"Please search for a contract type first", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			String compensationText = compensationDaysField.getText().trim();
			if (compensationText.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Compensation days is required!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			double compensationDays = Double.parseDouble(compensationText);
			if (compensationDays <= 0) {
				JOptionPane.showMessageDialog(this,
						"Compensation days must be positive!", "Validation Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			currentContractType.compensationDays = compensationDays;
			service.update(currentContractType);

			JOptionPane.showMessageDialog(this, "Contract type updated successfully!",
					"Success", JOptionPane.INFORMATION_MESSAGE);
			parentFrame
					.setStatus("Contract type updated: " + currentContractType.name);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid compensation days format!",
					"Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}