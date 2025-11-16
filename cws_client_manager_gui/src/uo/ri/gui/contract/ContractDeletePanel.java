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
import javax.swing.JTextField;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class ContractDeletePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField idField;
	private JLabel nifLabel;
	private JLabel contractTypeLabel;
	private JLabel stateLabel;
	private ContractCrudService service;
	private MainFrame parentFrame;
	private ContractDto currentContract;

	public ContractDeletePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forContractCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Delete Contract");
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

		// State (read-only)
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(new JLabel("State:"), gbc);
		gbc.gridx = 1;
		stateLabel = new JLabel("-");
		formPanel.add(stateLabel, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> deleteContract());
		buttonPanel.add(deleteButton);

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
				stateLabel.setText(currentContract.state);
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

	private void deleteContract() {
		if (currentContract == null) {
			JOptionPane.showMessageDialog(this, "Please search for a contract first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete contract: " + currentContract.id + "?",
				"Confirm Deletion", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				service.delete(currentContract.id);
				JOptionPane.showMessageDialog(this, "Contract deleted successfully!",
						"Success", JOptionPane.INFORMATION_MESSAGE);
				parentFrame.setStatus("Contract deleted");
				currentContract = null;
				clearFields();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Error deleting contract: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void clearFields() {
		idField.setText("");
		nifLabel.setText("-");
		contractTypeLabel.setText("-");
		stateLabel.setText("-");
		idField.requestFocus();
	}
}