package uo.ri.gui.contract;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractUpdatePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField idField;
	private JTextField annualSalaryField;
	private JTextField endDateField;
	private JLabel nifLabel;
	private JLabel contractTypeLabel;
	private JLabel professionalGroupLabel;
	private ContractCrudService service;
	private MainFrame parentFrame;
	private ContractDto currentContract;

	public ContractUpdatePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Update Contract");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ID field
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Contract ID:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		idField = new JTextField(15);
		idPanel.add(idField);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchContract());
		idPanel.add(searchButton);
		formPanel.add(idPanel, gbc);

		// NIF (read-only)
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Mechanic NIF:"), gbc);
		gbc.gridx = 1;
		nifLabel = new JLabel("-");
		formPanel.add(nifLabel, gbc);

		// Contract Type (read-only)
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(new JLabel("Contract Type:"), gbc);
		gbc.gridx = 1;
		contractTypeLabel = new JLabel("-");
		formPanel.add(contractTypeLabel, gbc);

		// Professional Group (read-only)
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(new JLabel("Professional Group:"), gbc);
		gbc.gridx = 1;
		professionalGroupLabel = new JLabel("-");
		formPanel.add(professionalGroupLabel, gbc);

		// Annual Base Salary
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(new JLabel("Annual Base Salary:"), gbc);
		gbc.gridx = 1;
		annualSalaryField = new JTextField(20);
		annualSalaryField.setEnabled(false);
		formPanel.add(annualSalaryField, gbc);

		// End Date
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
		gbc.gridx = 1;
		endDateField = new JTextField(20);
		endDateField.setEnabled(false);
		formPanel.add(endDateField, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateContract());
		buttonPanel.add(updateButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(
				e -> parentFrame.showPanel(new WelcomePanel(parentFrame)));
		buttonPanel.add(cancelButton);

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
				currentContract = result.get();
				nifLabel.setText(currentContract.mechanic.nif);
				contractTypeLabel.setText(currentContract.contractType.name);
				professionalGroupLabel.setText(currentContract.professionalGroup.name);
				annualSalaryField
						.setText(String.valueOf(currentContract.annualBaseSalary));
				endDateField.setText(
						currentContract.endDate != null ? currentContract.endDate.toString()
								: "");

				annualSalaryField.setEnabled(true);
				endDateField.setEnabled(true);

				parentFrame.setStatus("Contract loaded");
			} else {
				JOptionPane.showMessageDialog(this, "Contract not found with ID: " + id,
						"Not Found", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateContract() {
		if (currentContract == null) {
			JOptionPane.showMessageDialog(this, "Please search for a contract first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			String salaryText = annualSalaryField.getText().trim();
			if (salaryText.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Annual Salary is required!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			currentContract.annualBaseSalary = Double.parseDouble(salaryText);
			String endDateText = endDateField.getText().trim();
			currentContract.endDate = endDateText.isEmpty() ? null
					: LocalDate.parse(endDateText);

			service.update(currentContract);

			JOptionPane.showMessageDialog(this, "Contract updated successfully!",
					"Success", JOptionPane.INFORMATION_MESSAGE);
			parentFrame.setStatus("Contract updated");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid salary format!",
					"Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}