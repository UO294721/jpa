package uo.ri.gui.contracttype;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;

public class ContractTypesListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable contractTypesTable;
	private DefaultTableModel tableModel;
	private ContractTypeCrudService service;

	public ContractTypesListPanel() {
		this.service = Factories.service.forContractTypeCrudService();
		initializeUI();
		loadContractTypes();
	}

	private void initializeUI() {
		setLayout(new BorderLayout(10, 10));

		JLabel titleLabel = new JLabel("Contract Types List");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		String[] columnNames = { "ID", "Name", "Compensation Days/Year" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		contractTypesTable = new JTable(tableModel);
		contractTypesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contractTypesTable.setRowHeight(25);
		contractTypesTable.getTableHeader()
				.setFont(new Font("Arial", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(contractTypesTable);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> loadContractTypes());
		buttonPanel.add(refreshButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void loadContractTypes() {
		try {
			tableModel.setRowCount(0);
			List<ContractTypeDto> contractTypes = service.findAll();

			for (ContractTypeDto type : contractTypes) {
				Object[] row = { type.id, type.name,
						String.format("%.2f", type.compensationDays) };
				tableModel.addRow(row);
			}

			JOptionPane.showMessageDialog(this,
					"Loaded " + contractTypes.size() + " contract types", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error loading contract types: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}