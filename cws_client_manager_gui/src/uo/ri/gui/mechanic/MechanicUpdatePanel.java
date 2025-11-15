package uo.ri.gui.mechanic;

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
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

public class MechanicUpdatePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField idField;
	private JTextField nameField;
	private JTextField surnameField;
	private JLabel nifLabel;
	private MechanicCrudService service;
	private MainFrame parentFrame;
	private MechanicDto currentMechanic;

	public MechanicUpdatePanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forMechanicCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Update Mechanic");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ID field for searching
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Mechanic ID:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		idField = new JTextField(15);
		idPanel.add(idField);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchMechanic());
		idPanel.add(searchButton);
		formPanel.add(idPanel, gbc);

		// NIF (read-only)
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("NIF:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		nifLabel = new JLabel("-");
		formPanel.add(nifLabel, gbc);

		// Name field
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Name:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		nameField = new JTextField(20);
		nameField.setEnabled(false);
		formPanel.add(nameField, gbc);

		// Surname field
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Surname:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		surnameField = new JTextField(20);
		surnameField.setEnabled(false);
		formPanel.add(surnameField, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateMechanic());
		buttonPanel.add(updateButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton
				.addActionListener(e -> parentFrame.showPanel(new WelcomePanel()));
		buttonPanel.add(cancelButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void searchMechanic() {
		try {
			String id = idField.getText().trim();
			if (id.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a mechanic ID",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Optional<MechanicDto> result = service.findById(id);
			if (result.isPresent()) {
				currentMechanic = result.get();
				nifLabel.setText(currentMechanic.nif);
				nameField.setText(currentMechanic.name);
				surnameField.setText(currentMechanic.surname);
				nameField.setEnabled(true);
				surnameField.setEnabled(true);
				parentFrame.setStatus("Mechanic loaded: " + currentMechanic.name);
			} else {
				JOptionPane.showMessageDialog(this, "Mechanic not found with ID: " + id,
						"Not Found", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateMechanic() {
		if (currentMechanic == null) {
			JOptionPane.showMessageDialog(this, "Please search for a mechanic first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			currentMechanic.name = nameField.getText().trim();
			currentMechanic.surname = surnameField.getText().trim();

			service.update(currentMechanic);

			JOptionPane.showMessageDialog(this, "Mechanic updated successfully!",
					"Success", JOptionPane.INFORMATION_MESSAGE);
			parentFrame.setStatus("Mechanic updated: " + currentMechanic.name);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}