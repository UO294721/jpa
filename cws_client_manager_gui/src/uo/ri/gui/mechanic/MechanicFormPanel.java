package uo.ri.gui.mechanic;

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
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.gui.MainFrame;
import uo.ri.gui.WelcomePanel;

/**
 * Panel for adding a new mechanic
 */
public class MechanicFormPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2674512975389579966L;
	private JTextField nifField;
	private JTextField nameField;
	private JTextField surnameField;
	private MechanicCrudService service;
	private MainFrame parentFrame;

	public MechanicFormPanel(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.service = Factories.service.forMechanicCrudService();
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		// Title
		JLabel titleLabel = new JLabel("Add New Mechanic");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		// Form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// NIF field
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("NIF:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		nifField = new JTextField(20);
		formPanel.add(nifField, gbc);

		// Name field
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Name:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		nameField = new JTextField(20);
		formPanel.add(nameField, gbc);

		// Surname field
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		formPanel.add(new JLabel("Surname:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		surnameField = new JTextField(20);
		formPanel.add(surnameField, gbc);

		add(formPanel, BorderLayout.CENTER);

		// Buttons panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> saveMechanic());
		buttonPanel.add(saveButton);

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> clearFields());
		buttonPanel.add(clearButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton
				.addActionListener(e -> parentFrame.showPanel(new WelcomePanel()));
		buttonPanel.add(cancelButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void saveMechanic() {
		try {
			// Validate input
			String nif = nifField.getText().trim();
			String name = nameField.getText().trim();
			String surname = surnameField.getText().trim();

			if (nif.isEmpty() || name.isEmpty() || surname.isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields are required!",
						"Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Create DTO
			MechanicDto dto = new MechanicDto();
			dto.nif = nif;
			dto.name = name;
			dto.surname = surname;

			// Save
			MechanicDto savedMechanic = service.create(dto);

			JOptionPane.showMessageDialog(this,
					"Mechanic created successfully!\nID: " + savedMechanic.id, "Success",
					JOptionPane.INFORMATION_MESSAGE);

			clearFields();
			parentFrame.setStatus("Mechanic created: " + savedMechanic.name);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error creating mechanic: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		nifField.setText("");
		nameField.setText("");
		surnameField.setText("");
		nifField.requestFocus();
	}
}