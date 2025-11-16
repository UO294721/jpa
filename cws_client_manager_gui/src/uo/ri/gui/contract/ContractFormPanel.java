package uo.ri.gui.contract;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField nifField;
	private JComboBox<String> contractTypeCombo;
	private JComboBox<String> professionalGroupCombo;
	private JTextField annualSalaryField;
	private JTextField endDateField;
	private ContractCrudService service;
	private MainFrame parentFrame;

	public ContractFormPanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Add New Contract");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Mechanic NIF
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Mechanic NIF:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		nifField = new JTextField(20);
		formPanel.add(nifField, gbc);

		// Contract Type
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Contract Type:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		contractTypeCombo = new JComboBox<>(
				new String[] { "PERMANENT", "SEASONAL", "FIXED_TERM" });
		contractTypeCombo.addActionListener(e -> updateEndDateField());
		formPanel.add(contractTypeCombo, gbc);

		// Professional Group
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Professional Group:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		professionalGroupCombo = new JComboBox<>(
				new String[] { "I", "II", "III", "IV", "V", "VI", "VII" });
		formPanel.add(professionalGroupCombo, gbc);

		// Annual Base Salary
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Annual Base Salary:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		annualSalaryField = new JTextField(20);
		formPanel.add(annualSalaryField, gbc);

		// End Date
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		endDateField = new JTextField(20);
		endDateField.setEnabled(false);
		formPanel.add(endDateField, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> saveContract());
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

	private void updateEndDateField() {
		String contractType = (String) contractTypeCombo.getSelectedItem();
		endDateField.setEnabled("FIXED_TERM".equals(contractType));
		if (!"FIXED_TERM".equals(contractType)) {
			endDateField.setText("");
		}
	}

	private void saveContract() {
		try {
			String nif = nifField.getText().trim();
			String contractType = (String) contractTypeCombo.getSelectedItem();
			String professionalGroup = (String) professionalGroupCombo
					.getSelectedItem();
			String salaryText = annualSalaryField.getText().trim();

			if (nif.isEmpty() || salaryText.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"NIF and Annual Salary are required!", "Validation Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			double annualSalary = Double.parseDouble(salaryText);

			ContractDto dto = new ContractDto();
			dto.mechanic.nif = nif;
			dto.contractType.name = contractType;
			dto.professionalGroup.name = professionalGroup;
			dto.annualBaseSalary = annualSalary;
			dto.startDate = LocalDate.now();

			if ("FIXED_TERM".equals(contractType)) {
				String endDateText = endDateField.getText().trim();
				if (endDateText.isEmpty()) {
					JOptionPane.showMessageDialog(this,
							"End Date is required for FIXED_TERM contracts!",
							"Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				dto.endDate = LocalDate.parse(endDateText);
			}

			service.create(dto);

			JOptionPane.showMessageDialog(this, "Contract created successfully!",
					"Success", JOptionPane.INFORMATION_MESSAGE);

			clearFields();
			parentFrame.setStatus("Contract created");

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid salary format!",
					"Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error creating contract: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearFields() {
		nifField.setText("");
		contractTypeCombo.setSelectedIndex(0);
		professionalGroupCombo.setSelectedIndex(0);
		annualSalaryField.setText("");
		endDateField.setText("");
		updateEndDateField();
		nifField.requestFocus();
	}
}