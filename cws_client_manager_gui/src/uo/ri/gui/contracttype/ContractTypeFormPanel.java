package uo.ri.gui.contracttype;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

public class ContractTypeFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField compensationDaysField;
	private ContractTypeCrudService service;
	private MainFrame parentFrame;

	public ContractTypeFormPanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractTypeCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Add New Contract Type");
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
		nameField = new JTextField(20);
		formPanel.add(nameField, gbc);

		// Help text for name
		gbc.gridx = 1;
		gbc.gridy = 1;
		JLabel helpLabel = new JLabel(
				"<html><i>Examples: PERMANENT, SEASONAL, FIXED_TERM</i></html>");
		helpLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		formPanel.add(helpLabel, gbc);

		// Compensation Days field
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Compensation Days/Year:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		compensationDaysField = new JTextField(20);
		formPanel.add(compensationDaysField, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> saveContractType());
		buttonPanel.add(saveButton);

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> clearFields());
		buttonPanel.add(clearButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(
				e -> parentFrame.showPanel(new WelcomePanel(parentFrame)));
		buttonPanel.add(cancelButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void saveContractType() {
		try {
			String name = nameField.getText().trim();
			String compensationText = compensationDaysField.getText().trim();

			if (name.isEmpty() || compensationText.isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields are required!",
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

			ContractTypeDto dto = new ContractTypeDto();
			dto.name = name.toUpperCase(); // Normalize to uppercase
			dto.compensationDays = compensationDays;

			service.create(dto);

			JOptionPane.showMessageDialog(this, "Contract type created successfully!",
					"Success", JOptionPane.INFORMATION_MESSAGE);

			clearFields();
			parentFrame.setStatus("Contract type created: " + dto.name);

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid compensation days format!",
					"Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error creating contract type: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearFields() {
		nameField.setText("");
		compensationDaysField.setText("");
		nameField.requestFocus();
	}
}